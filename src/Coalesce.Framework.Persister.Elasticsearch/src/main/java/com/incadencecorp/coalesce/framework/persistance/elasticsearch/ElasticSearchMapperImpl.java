/*-----------------------------------------------------------------------------'
 Copyright 2018 - InCadence Strategic Solutions Inc., All Rights Reserved

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

package com.incadencecorp.coalesce.framework.persistance.elasticsearch;

import com.incadencecorp.coalesce.api.ICoalesceMapper;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceFieldDataTypes;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import java.util.Date;

/**
 * This implementation maps Coalesce types to Java classes that are acceptable by ElasticSearch.
 *
 * @author Chris Krentz
 */
public class ElasticSearchMapperImpl implements ICoalesceMapper<Class<?>> {

    /**
     * Gets the ElasticSearch String name for compatible types to put
     * in ElasticSearch type maps
     *
     * @param type Coalesce type
     * @return String of type
     */
    public String mapToString(ECoalesceFieldDataTypes type)
    {
        switch (type)
        {
        case BOOLEAN_LIST_TYPE:
        case BOOLEAN_TYPE:
            return "boolean";

        case DOUBLE_LIST_TYPE:
        case DOUBLE_TYPE:
            return "double";

        case FLOAT_LIST_TYPE:
        case FLOAT_TYPE:
            return "float";

        case GEOCOORDINATE_LIST_TYPE:
        case GEOCOORDINATE_TYPE:
            return "geo_point";

        case LINE_STRING_TYPE:
        case POLYGON_TYPE:
        case CIRCLE_TYPE:
            return "geo_shape";

        case ENUMERATION_LIST_TYPE:
        case ENUMERATION_TYPE:
        case INTEGER_LIST_TYPE:
        case INTEGER_TYPE:
            return "integer";

        case GUID_LIST_TYPE:
        case GUID_TYPE:
        case STRING_LIST_TYPE:
        case STRING_TYPE:
        case URI_TYPE:
            return "string";

        case DATE_TIME_TYPE:
            return "date";

        case LONG_LIST_TYPE:
        case LONG_TYPE:
            return "long";

        case FILE_TYPE:
        case BINARY_TYPE:
        default:
            return null;
        }
    }

    @Override
    public Class<?> map(ECoalesceFieldDataTypes type)
    {
        switch (type)
        {

        case BOOLEAN_TYPE:
            return Boolean.class;

        case DOUBLE_TYPE:
            return Double.class;

        case FLOAT_TYPE:
            return Float.class;

        case GEOCOORDINATE_LIST_TYPE:
            return MultiPoint.class;

        case GEOCOORDINATE_TYPE:
            return Point.class;

        case LINE_STRING_TYPE:
            return LineString.class;

        case POLYGON_TYPE:
            return Polygon.class;
        // Circles will be converted to polygons
        case CIRCLE_TYPE:
            return Polygon.class;

        case INTEGER_TYPE:
            return Integer.class;

        case STRING_TYPE:
        case URI_TYPE:
        case STRING_LIST_TYPE:
        case DOUBLE_LIST_TYPE:
        case INTEGER_LIST_TYPE:
        case LONG_LIST_TYPE:
        case FLOAT_LIST_TYPE:
        case GUID_LIST_TYPE:
        case BOOLEAN_LIST_TYPE:
        case ENUMERATION_LIST_TYPE:
            return String.class;

        case GUID_TYPE:
            return String.class;

        case DATE_TIME_TYPE:
            return Date.class;

        case LONG_TYPE:
            return Long.class;

        case ENUMERATION_TYPE:
            return Integer.class;

        case FILE_TYPE:
        case BINARY_TYPE:
        default:
            return null;
        }
    }

}
