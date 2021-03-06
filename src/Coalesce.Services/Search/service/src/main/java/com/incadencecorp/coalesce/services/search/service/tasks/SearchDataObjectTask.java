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

package com.incadencecorp.coalesce.services.search.service.tasks;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import javax.xml.parsers.ParserConfigurationException;

import com.incadencecorp.coalesce.search.api.SearchResults;
import org.geotools.data.Query;
import org.geotools.filter.SortByImpl;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.xml.sax.SAXException;

import com.incadencecorp.coalesce.api.EResultStatus;
import com.incadencecorp.coalesce.common.exceptions.CoalesceException;
import com.incadencecorp.coalesce.framework.tasks.AbstractTask;
import com.incadencecorp.coalesce.framework.tasks.TaskParameters;
import com.incadencecorp.coalesce.search.api.ICoalesceSearchPersistor;
import com.incadencecorp.coalesce.search.factory.CoalescePropertyFactory;
import com.incadencecorp.coalesce.search.filter.FilterUtil;
import com.incadencecorp.coalesce.services.api.search.HitType;
import com.incadencecorp.coalesce.services.api.search.QueryResultType;
import com.incadencecorp.coalesce.services.api.search.QueryResultsType;
import com.incadencecorp.coalesce.services.api.search.QueryType;
import com.incadencecorp.coalesce.services.api.search.SortByType;

public class SearchDataObjectTask extends AbstractTask<QueryType, QueryResultsType, ICoalesceSearchPersistor> {

    @Override
    protected QueryResultsType doWork(TaskParameters<ICoalesceSearchPersistor, QueryType> parameters)
            throws CoalesceException
    {
        QueryResultsType result;

        try
        {
            List<String> properties = new ArrayList<>();
            properties.add(CoalescePropertyFactory.getName().getPropertyName());
            properties.add(CoalescePropertyFactory.getSource().getPropertyName());
            properties.add(CoalescePropertyFactory.getEntityTitle().getPropertyName());
            properties.addAll(parameters.getParams().getPropertyNames());

            SortBy[] sortby = new SortBy[parameters.getParams().getSortBy().size()];

            for (int ii = 0; ii < parameters.getParams().getSortBy().size(); ii++)
            {
                SortByType sort = parameters.getParams().getSortBy().get(ii);
                PropertyName property = CoalescePropertyFactory.getFilterFactory().property(sort.getPropertyName());

                switch (sort.getSortOrder()) {
                case ASC:
                    sortby[ii] = new SortByImpl(property, SortOrder.ASCENDING);
                    break;
                case DESC:
                    sortby[ii] = new SortByImpl(property, SortOrder.DESCENDING);
                    break;
                }
            }

            Query query = new Query("coalesce", FilterUtil.fromXml(parameters.getParams().getFilter()));
            query.setPropertyNames(properties);
            query.setStartIndex(parameters.getParams().getPageNumber());
            query.setMaxFeatures(parameters.getParams().getPageSize());
            query.setSortBy(sortby);

            SearchResults searchResults = parameters.getTarget().search(query);
            CachedRowSet rowset = searchResults.getResults();

            QueryResultType results = new QueryResultType();
            results.setTotal(BigInteger.valueOf(searchResults.getTotal()));

            if (rowset.first())
            {
                // Obtain list of keys
                do
                {
                    int idx = 1;

                    HitType hit = new HitType();
                    hit.setEntityKey(rowset.getString(idx++));

                    if (parameters.getParams().getPropertyNames() != null)
                    {
                        for (int ii = idx; ii < parameters.getParams().getPropertyNames().size() + idx; ii++)
                        {
                            hit.getValues().add(rowset.getString(ii));
                        }
                    }

                    results.getHits().add(hit);
                }
                while (rowset.next());
            }

            result = new QueryResultsType();
            result.setStatus(EResultStatus.SUCCESS);
            result.setResult(results);
        }
        catch (SAXException | IOException | ParserConfigurationException | SQLException e)
        {
            throw new CoalesceException(e);
        }

        return result;
    }

    @Override
    protected Map<String, String> getParameters(QueryType params, boolean isTrace)
    {
        // TODO Not Implemented
        return null;
    }

    @Override
    protected QueryResultsType createResult()
    {
        return new QueryResultsType();
    }
}
