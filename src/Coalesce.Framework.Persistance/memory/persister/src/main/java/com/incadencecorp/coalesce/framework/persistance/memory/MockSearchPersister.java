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

package com.incadencecorp.coalesce.framework.persistance.memory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.geotools.data.Query;
import org.opengis.filter.expression.PropertyName;

import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.persistance.CoalesceParameter;
import com.incadencecorp.coalesce.search.api.ICoalesceSearchPersistor;
import com.incadencecorp.coalesce.search.factory.CoalescePropertyFactory;
import com.incadencecorp.coalesce.search.resultset.CoalesceResultSet;

public class MockSearchPersister extends MockPersister implements ICoalesceSearchPersistor {

    @Override
    public CachedRowSet search(Query query, CoalesceParameter... parameters) throws CoalescePersistorException
    {
        CachedRowSet results;

        List<String> columns = new ArrayList<String>();

        if (query != null && query.getProperties() != null)
        {
            for (PropertyName property : query.getProperties())
            {
                columns.add(property.getPropertyName());
            }
        }

        List<Object[]> rows = new ArrayList<Object[]>();

        int idxKey = columns.indexOf(CoalescePropertyFactory.getEntityKey().getPropertyName());
        int idxName = columns.indexOf(CoalescePropertyFactory.getName().getPropertyName());
        int idxSource = columns.indexOf(CoalescePropertyFactory.getSource().getPropertyName());
        int idxTitle = columns.indexOf(CoalescePropertyFactory.getEntityTitle().getPropertyName());

        for (CoalesceEntity entity : getEntity(keys.toArray(new String[keys.size()])))
        {
            Object[] data = new Object[columns.size()];

            if (idxKey != -1)
            {
                data[idxKey] = entity.getKey();
            }

            if (idxName != -1)
            {
                data[idxName] = entity.getName();
            }

            if (idxSource != -1)
            {
                data[idxSource] = entity.getSource();
            }
            
            if (idxTitle != -1)
            {
                data[idxTitle] = entity.getTitle();
            }

            rows.add(data);
        }

        try
        {
            results = RowSetProvider.newFactory().createCachedRowSet();
            results.populate(new CoalesceResultSet(rows.iterator(), columns.toArray(new String[columns.size()])));
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("Failed", e);
        }

        return results;
    }
}