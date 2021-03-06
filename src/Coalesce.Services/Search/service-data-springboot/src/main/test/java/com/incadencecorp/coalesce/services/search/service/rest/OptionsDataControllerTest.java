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

package com.incadencecorp.coalesce.services.search.service.rest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.incadencecorp.coalesce.framework.datamodel.TestEntity;
import com.incadencecorp.coalesce.framework.persistance.memory.MockSearchPersister;
import com.incadencecorp.coalesce.services.api.search.SearchDataObjectResponse;
import com.incadencecorp.coalesce.services.search.service.client.SearchFrameworkClientImpl;
import com.incadencecorp.coalesce.services.search.service.rest.controllers.OptionsDataControllerSpring;
import com.incadencecorp.coalesce.services.search.service.rest.model.Option;

public class OptionsDataControllerTest {

    @Test
    public void testFilterCreation() throws Exception {
        
        DerbyPersistor persister = new DerbyPersistor();
        
        OptionsDataControllerSpring controller = new OptionsDataControllerSpring(new SearchFrameworkClientImpl(persister));
        
        TestEntity entity = new TestEntity(); 
        entity.initialize();
        
        persister.saveEntity(false, entity);
        
        List<Option> options = new ArrayList<Option>();
        
        Option option = new Option(); 
        option.setRecordset(TestEntity.RECORDSET1);
        option.setField("boolean");
        option.setValue("false");
        option.setComparer("=");
        option.setMatchCase(false);
        
        options.add(option);
        
        SearchDataObjectResponse results = controller.search(options);
        
        Assert.assertEquals(1, results.getResult().size());
        Assert.assertEquals(1, results.getResult().get(0).getResult().getHits().size());
        Assert.assertEquals(entity.getKey(), results.getResult().get(0).getResult().getHits().get(0).getEntityKey());
        

    }
    
}
