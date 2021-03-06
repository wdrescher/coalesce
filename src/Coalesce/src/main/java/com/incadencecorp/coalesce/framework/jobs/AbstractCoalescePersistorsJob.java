/*-----------------------------------------------------------------------------'
 Copyright 2016 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

package com.incadencecorp.coalesce.framework.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.incadencecorp.coalesce.api.EResultStatus;
import com.incadencecorp.coalesce.api.ICoalescePersistorJob;
import com.incadencecorp.coalesce.api.ICoalescePrincipal;
import com.incadencecorp.coalesce.api.ICoalesceResponseType;
import com.incadencecorp.coalesce.api.IExceptionHandler;
import com.incadencecorp.coalesce.common.exceptions.CoalesceException;
import com.incadencecorp.coalesce.framework.jobs.responses.CoalesceResponseType;
import com.incadencecorp.coalesce.framework.jobs.responses.CoalesceStringResponseType;
import com.incadencecorp.coalesce.framework.persistance.ICoalescePersistor;
import com.incadencecorp.coalesce.framework.tasks.AbstractPersistorTask;
import com.incadencecorp.coalesce.framework.tasks.MetricResults;

/**
 * Abstract base for jobs that perform the same task with the same parameters
 * for each persister configured.
 * 
 * @author Derek
 * @param <INPUT> input type
 */
public abstract class AbstractCoalescePersistorsJob<INPUT> extends
        AbstractCoalesceJob<INPUT, ICoalesceResponseType<List<CoalesceStringResponseType>>, CoalesceStringResponseType>
        implements ICoalescePersistorJob {

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractCoalescePersistorsJob.class);

    /*--------------------------------------------------------------------------
    Private Member Variables
    --------------------------------------------------------------------------*/

    private IExceptionHandler _handler;
    private ICoalescePersistor _persistors[];

    /*--------------------------------------------------------------------------
    Constructors
    --------------------------------------------------------------------------*/

    /**
     * Sets the persistors that the task will be performed on.
     * 
     * @param params task parameters.
     */
    public AbstractCoalescePersistorsJob(INPUT params)
    {
        super(params);
    }

    /*--------------------------------------------------------------------------
    Getters / Setters
    --------------------------------------------------------------------------*/

    @Override
    public void setHandler(IExceptionHandler handler)
    {
        _handler = handler;
    }

    @Override
    public void setTarget(ICoalescePersistor... targets)
    {
        _persistors = targets;
    }

    /*--------------------------------------------------------------------------
    Override Methods
    --------------------------------------------------------------------------*/

    @Override
    public final ICoalesceResponseType<List<CoalesceStringResponseType>> doWork(ICoalescePrincipal principal, INPUT params)
            throws CoalesceException
    {
        List<CoalesceStringResponseType> results = new ArrayList<CoalesceStringResponseType>();

        try
        {
            List<AbstractPersistorTask<INPUT>> tasks = new ArrayList<AbstractPersistorTask<INPUT>>();

            // Create Tasks
            for (int ii = 0; ii < _persistors.length; ii++)
            {
                AbstractPersistorTask<INPUT> task = createTask();
                task.setParams(params);
                task.setTarget(_persistors[ii]);
                task.setPrincipal(principal);

                tasks.add(task);
            }

            int ii = 0;

            // Execute Tasks
            for (Future<MetricResults<CoalesceStringResponseType>> future : getService().invokeAll(tasks))
            {
                MetricResults<CoalesceStringResponseType> metrics;

                try
                {
                    metrics = future.get();
                }
                catch (InterruptedException | ExecutionException e)
                {
                    LOGGER.error("Interrupted Task", e);

                    // Create Failed Result
                    CoalesceStringResponseType task = createResults();
                    task.setError(e.getMessage());
                    task.setStatus(EResultStatus.FAILED);

                    metrics = new MetricResults<>(this.getName());
                    metrics.setResults(task);
                    metrics.getResults().setStatus(EResultStatus.FAILED);
                }

                if (!metrics.isSuccessful() && _handler != null)
                {
                    if (LOGGER.isTraceEnabled())
                    {
                        LOGGER.trace("Handling ({})", _handler.getName());
                    }

                    _handler.handle(getKeys(tasks.get(ii)), this, metrics.getResults().getException());
                }

                addResult(metrics);

                results.add(metrics.getResults());

                ii++;
            }
        }
        catch (InterruptedException e)
        {
            throw new CoalesceException("Job Interrupted", e);
        }

        CoalesceResponseType<List<CoalesceStringResponseType>> result = new CoalesceResponseType<List<CoalesceStringResponseType>>();
        result.setResult(results);
        result.setStatus(EResultStatus.SUCCESS);

        return result;
    }

    /*--------------------------------------------------------------------------
    Abstract Methods
    --------------------------------------------------------------------------*/

    abstract protected AbstractPersistorTask<INPUT> createTask();

    abstract protected String[] getKeys(AbstractPersistorTask<INPUT> task);
}
