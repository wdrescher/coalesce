package com.incadencecorp.coalesce.framework.persistance;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import com.incadencecorp.coalesce.api.persistance.EPersistorCapabilities;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecord;
import com.incadencecorp.coalesce.framework.datamodel.TestEntity;
import com.incadencecorp.coalesce.framework.datamodel.TestRecord;

/*-----------------------------------------------------------------------------'
 Copyright 2014 - InCadence Strategic Solutions Inc., All Rights Reserved

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

public abstract class AbstractCoalescePersistorTest<T extends ICoalescePersistor> {

    protected abstract T createPersister();
    
    @Test
    public void testCreation() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.CREATE));

        TestEntity entity = new TestEntity();
        entity.initialize();

        Assert.assertTrue(persister.saveEntity(false, entity));
        
        // Cleanup
        entity.markAsDeleted();
        
        persister.saveEntity(true, entity);

    }

    @Test
    public void testUpdates() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.CREATE));
        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.READ));
        
        TestEntity entity = new TestEntity();
        entity.initialize();

        Assert.assertTrue(persister.saveEntity(false, entity));
        
        TestRecord record1 = entity.addRecord1();
        record1.getBooleanField().setValue(true);

        Assert.assertTrue(persister.saveEntity(false, entity));

        CoalesceEntity updated = persister.getEntity(entity.getKey())[0];
        
        CoalesceRecord updatedRecord = (CoalesceRecord) updated.getCoalesceObjectForKey(record1.getKey());
        
        Assert.assertNotNull(updatedRecord);
        Assert.assertEquals(record1.getBooleanField().getBaseValue(), updatedRecord.getFieldByName(record1.getBooleanField().getName()).getBaseValue());

        // Cleanup
        entity.markAsDeleted();
        
        persister.saveEntity(true, entity);

    }

    @Test
    public void testDeletion() throws Exception
    {
        T persister = createPersister();

        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.CREATE));
        Assume.assumeTrue(persister.getCapabilities().contains(EPersistorCapabilities.DELETE));
        
        TestEntity entity = new TestEntity();
        entity.initialize();

        Assert.assertTrue(persister.saveEntity(false, entity));
        Assert.assertNotNull(persister.getEntityXml(entity.getKey())[0]);
        
        entity.markAsDeleted();
        
        Assert.assertTrue(persister.saveEntity(true, entity));
        Assert.assertEquals(0, persister.getEntityXml(entity.getKey()).length);
        
    }

}