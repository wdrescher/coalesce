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

import java.util.UUID;
import java.util.concurrent.Callable;

import com.incadencecorp.coalesce.framework.CoalesceComponentImpl;

/**
 * Abstract base for jobs in Coalesce.
 * 
 * @author Derek
 * @param <T> input type
 * @param <Y> output type
 */
public abstract class AbstractCoalesceJob<T, Y> extends CoalesceComponentImpl implements Callable<Y> {

    /*--------------------------------------------------------------------------
    Member Variables
    --------------------------------------------------------------------------*/

    private T _params;
    private UUID id;

    /*--------------------------------------------------------------------------
    Constructors
    --------------------------------------------------------------------------*/

    /**
     * @param params parameters to pass to the task.
     */
    public AbstractCoalesceJob(T params)
    {
        _params = params;
    }

    /*--------------------------------------------------------------------------
    Override Methods
    --------------------------------------------------------------------------*/

    @Override
    public final Y call() throws Exception
    {
        return doWork(_params);
    }

    /*--------------------------------------------------------------------------
    Abstract Methods
    --------------------------------------------------------------------------*/

    /**
     * Performs the work of the job.
     */
    protected abstract Y doWork(T params) throws Exception;

}
