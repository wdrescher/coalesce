/*
 *  Copyright 2017 - InCadence Strategic Solutions Inc., All Rights Reserved
 *
 *  Notwithstanding any contractor copyright notice, the Government has Unlimited
 *  Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 *  of this work other than as specifically authorized by these DFARS Clauses may
 *  violate Government rights in this work.
 *
 *  DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 *  Unlimited Rights. The Government has the right to use, modify, reproduce,
 *  perform, display, release or disclose this computer software and to have or
 *  authorize others to do so.
 *
 *  Distribution Statement D. Distribution authorized to the Department of
 *  Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 *
 */

package com.incadencecorp.coalesce.framework.persistance.elasticsearch;

import com.incadencecorp.coalesce.api.ICoalesceNormalizer;
import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceFieldDataTypes;
import com.incadencecorp.coalesce.framework.persistance.ObjectMetaData;
import com.incadencecorp.coalesce.framework.util.CoalesceTemplateUtil;
import com.incadencecorp.coalesce.search.factory.CoalescePropertyFactory;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

/**
 * Walks through a Filter, re-writing any property names removing the tablename from the property along with the /
 */
class ElasticSearchQueryRewriter extends DuplicatingFilterVisitor {

    private final Set<String> features = new HashSet<>();
    private final ICoalesceNormalizer normalizer;

    /**
     * Standard java logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchQueryRewriter.class);

    ElasticSearchQueryRewriter()
    {
        this(new ElasticSearchNormalizer());
    }

    ElasticSearchQueryRewriter(ICoalesceNormalizer normalizer)
    {
        super(CommonFactoryFinder.getFilterFactory2());

        this.normalizer = normalizer;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData)
    {
        PropertyName name;

        if (extraData instanceof Boolean)
        {
            try
            {
                if (isStringField(expression))
                {
                    name = ff.property(getNormalizedPropertyName(expression.getPropertyName()) + ".keyword");

                    LOGGER.info("Rewriting ({}) => ({})", expression.getPropertyName(), name.getPropertyName());
                }
                else
                {
                    name = ff.property(getNormalizedPropertyName(expression.getPropertyName()));
                }
            }
            catch (IllegalArgumentException e)
            {
                // TODO Need to address, for now do nothing
                name = expression;
            }
        }
        else
        {
            name = ff.property(getNormalizedPropertyName(expression.getPropertyName()));
        }

        return super.visit(name, extraData);
    }

    private boolean isStringField(PropertyName name)
    {
        String property = name.getPropertyName().toLowerCase();

        ECoalesceFieldDataTypes type = CoalesceTemplateUtil.getDataType(property);

        if (type == null)
        {
            throw new RuntimeException("Unknown Parameter Specified: " + property);
        }

        LOGGER.trace("({}): ({})", property, type);

        return (type == ECoalesceFieldDataTypes.STRING_TYPE) && !(property.startsWith("coalesceentity")
                || property.startsWith("coalescelinkage"));
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData)
    {
        return super.visit(filter, Boolean.TRUE);
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData)
    {
        return super.visit(filter, Boolean.TRUE);
    }

    public Query rewrite(Query original) throws CoalescePersistorException
    {
        features.clear();

        //create a new filter with the rewritten property names
        Query newQuery = new Query(original);

        // Convert to 0 Indexed Paging
        if (original.getStartIndex() != null && original.getStartIndex() > 0)
        {
            newQuery.setStartIndex(original.getStartIndex() - 1);
        }

        // See if a valid feature name was set in the query.  If
        // so make sure it is the first in the list
        if ((newQuery.getTypeName() != null) && (!newQuery.getTypeName().equalsIgnoreCase("coalesce")))
        {
            addFeature(newQuery.getTypeName());
        }

        // Clear the type name from the query
        newQuery.setTypeName(null);

        Filter f = original.getFilter();

        // Rewrite the filter
        newQuery.setFilter((Filter) f.accept(this, null));

        List<String> properties = new ArrayList<>();

        // Normalize properties and remove any duplicates
        for (String property : newQuery.getPropertyNames())
        {
            String normalized = getNormalizedPropertyName(property);

            if (!CoalescePropertyFactory.isRecordPropertyName(property))
            {
                if (!properties.contains(normalized))
                {
                    properties.add(normalized);
                }
            }
        }

        newQuery.setPropertyNames(properties);

        // Rewrite the any sorts also
        SortBy[] sorts = newQuery.getSortBy();
        if (sorts != null && sorts.length > 0)
        {
            for (int i = 0; i < sorts.length; i++)
            {
                String name = getNormalizedPropertyName(sorts[i].getPropertyName().getPropertyName());

                if (isStringField(sorts[i].getPropertyName()))
                {
                    name = name + ".keyword";
                }

                sorts[i] = ff.sort(name, sorts[i].getSortOrder());
            }

            newQuery.setSortBy(sorts);
        }

        Hints hints = new Hints();
        hints.put(Hints.VIRTUAL_TABLE_PARAMETERS, Collections.singletonMap("q", Collections.EMPTY_MAP.toString()));

        newQuery.setHints(hints);

        // Now go through the features used and figure
        // out what is the key feature

        // If there are none use coalesceentity
        if (features.isEmpty())
        {
            newQuery.setTypeName(ElasticSearchPersistor.COALESCE_ENTITY_INDEX);
        }
        else if (features.size() == 1)
        {
            newQuery.setTypeName(getTypeName(features.iterator().next()));
        }
        else
        {
            // Remove ENTITY_FEATURE_NAME if it is also used.
            Predicate<String> namePredicate = s -> s.equalsIgnoreCase("coalesceentity");
            features.removeIf(namePredicate);

            // Now if there is more than one feature throw an exception
            if (features.size() > 1)
            {
                LOGGER.debug("Features:");
                for (String feature : features)
                {
                    LOGGER.debug("\t{}", feature);
                }
                throw new CoalescePersistorException("Multiple featuretypes in query is not supported");
            }

            newQuery.setTypeName(getTypeName(features.iterator().next()));
        }

        features.clear();

        return newQuery;
    }

    private String getTypeName(String feature)
    {
        if (feature.equalsIgnoreCase("coalesceentity"))
        {
            return ElasticSearchPersistor.COALESCE_ENTITY_INDEX;
        }
        else if (feature.equalsIgnoreCase("coalescelinkage"))
        {
            return ElasticSearchPersistor.COALESCE_LINKAGE_INDEX;
        }
        else
        {
            return ElasticSearchPersistor.COALESCE_ENTITY_INDEX + "-" + normalizer.normalize(feature);
        }
    }

    private String getNormalizedPropertyName(String name)
    {
        String[] parts = name.split("[/.]");
        String normalized;

        if (parts.length == 1)
        {
            normalized = normalizer.normalize(parts[0]);
        }
        else if (parts.length == 2)
        {
            String feature = normalizer.normalize(parts[0]);

            if (!features.contains(feature))
            {
                addFeature(feature);
            }

            normalized = normalizer.normalize(parts[0], parts[1]);
        }
        else
        {
            normalized = name;
        }

        return normalized;
    }

    private void addFeature(String feature)
    {
        if (feature.equalsIgnoreCase("coalesceentity") || feature.equalsIgnoreCase("coalescelinkage"))
        {
            features.add(feature);
        }
        else
        {
            Set<ObjectMetaData> templates = CoalesceTemplateUtil.getTemplatesContainingRecordset(feature);

            if (templates.size() == 1)
            {
                features.add(templates.iterator().next().getName());
            }
            else if (templates.size() == 0)
            {
                LOGGER.error("(ERROR) Could not determine index; recordset ({}) does not exists in templates", feature);
            }
            else
            {
                LOGGER.error("(ERROR) Could not determine index; recordset ({}) exists in multiple templates", feature);
                templates.iterator().forEachRemaining(meta -> LOGGER.error("\t{} : {}", meta.getKey(), meta.getName()));
            }
        }
    }

}
