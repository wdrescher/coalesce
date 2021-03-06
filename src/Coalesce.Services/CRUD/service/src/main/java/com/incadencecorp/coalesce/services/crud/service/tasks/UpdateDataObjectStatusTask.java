/*-----------------------------------------------------------------------------'
 Copyright 2017 - InCadence Strategic Solutions Inc., All Rights Reserved

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

package com.incadencecorp.coalesce.services.crud.service.tasks;

import java.util.HashMap;
import java.util.Map;

import com.incadencecorp.coalesce.api.CoalesceErrors;
import com.incadencecorp.coalesce.api.EResultStatus;
import com.incadencecorp.coalesce.common.exceptions.CoalesceException;
import com.incadencecorp.coalesce.enums.ECrudOperations;
import com.incadencecorp.coalesce.framework.CoalesceFramework;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceObjectStatus;
import com.incadencecorp.coalesce.framework.tasks.AbstractFrameworkTask;
import com.incadencecorp.coalesce.framework.tasks.TaskParameters;
import com.incadencecorp.coalesce.framework.util.CoalesceNotifierUtil;
import com.incadencecorp.coalesce.services.api.common.ResultsType;
import com.incadencecorp.coalesce.services.api.crud.DataObjectStatusType;

public class UpdateDataObjectStatusTask extends AbstractFrameworkTask<DataObjectStatusType[], ResultsType> {

    @Override
    protected ResultsType doWork(TaskParameters<CoalesceFramework, DataObjectStatusType[]> parameters)
            throws CoalesceException
    {
        ResultsType result = new ResultsType();
        CoalesceFramework framework = parameters.getTarget();
        DataObjectStatusType[] params = parameters.getParams();

        CoalesceEntity[] entities = new CoalesceEntity[params.length];

        for (int ii = 0; ii < params.length; ii++)
        {
            DataObjectStatusType task = params[ii];

            if (task.getAction() == null)
            {
                throw new CoalesceException(String.format(CoalesceErrors.NOT_SPECIFIED, "Update Action"));
            }

            CoalesceEntity entity = framework.getCoalesceEntity(task.getKey());

            switch (task.getAction()) {
            case MARK_AS_ACTIVE:
                entity.setStatus(ECoalesceObjectStatus.ACTIVE);
                break;
            case MARK_AS_DELETED:
                entity.setStatus(ECoalesceObjectStatus.DELETED);
                break;
            case MARK_AS_READONLY:
                entity.setStatus(ECoalesceObjectStatus.READONLY);
                break;
            }
            
            entities[ii] = entity;
        }

        if (framework.saveCoalesceEntity(entities))
        {
            result.setStatus(EResultStatus.SUCCESS);
            
            CoalesceNotifierUtil.sendCrud(getName(), ECrudOperations.UPDATE, entities);
        }
        else
        {
            result.setStatus(EResultStatus.FAILED);
        }

        return result;
    }

    @Override
    protected Map<String, String> getParameters(DataObjectStatusType[] params, boolean isTrace)
    {
        Map<String, String> results = new HashMap<String, String>();

        for (DataObjectStatusType type : params)
        {
            // TODO Not Implemented
        }

        return results;
    }

    @Override
    protected ResultsType createResult()
    {
        return new ResultsType();
    }

}
