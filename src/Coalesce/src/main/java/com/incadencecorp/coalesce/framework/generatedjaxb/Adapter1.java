//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.15 at 12:29:37 PM EDT 
//


package com.incadencecorp.coalesce.framework.generatedjaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.DateTime;

public class Adapter1
    extends XmlAdapter<String, DateTime>
{


    public DateTime unmarshal(String value) {
        return (com.incadencecorp.coalesce.common.helpers.DateTimeConverter.parseDate(value));
    }

    public String marshal(DateTime value) {
        return (com.incadencecorp.coalesce.common.helpers.DateTimeConverter.printDate(value));
    }

}
