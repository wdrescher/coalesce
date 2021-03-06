package com.incadencecorp.coalesce.framework.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.NullArgumentException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.incadencecorp.coalesce.common.CoalesceTypeInstances;
import com.incadencecorp.coalesce.common.helpers.ArrayHelper;
import com.incadencecorp.coalesce.common.helpers.JodaDateTimeHelper;
import com.incadencecorp.coalesce.common.helpers.XmlHelper;

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

public class CoalesceRecordSetTest {

    /*
     * @BeforeClass public static void setUpBeforeClass() throws Exception { }
     * 
     * @AfterClass public static void tearDownAfterClass() throws Exception { }
     * 
     * @Before public void setUp() throws Exception { }
     * 
     * @After public void tearDown() throws Exception { }
     */
    @Test
    public void createRecordsetEmptyTest()
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        CoalesceSection section = entity.createSection("New Section");

        CoalesceRecordset recordset = CoalesceRecordset.create(section, "New Recordset");

        assertNotNull(recordset);

        List<CoalesceRecordset> recordsets = section.getRecordsetsAsList();

        assertEquals(1, recordsets.size());
        assertNotNull(ArrayHelper.getItem(recordsets, recordset.getKey()));

    }

    @Test
    public void createRecordsetExistsTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceSection section = entity.getSection(CoalesceTypeInstances.TEST_MISSION_INFO_SECTION_PATH);

        CoalesceRecordset recordset = CoalesceRecordset.create(section, "New Section");

        assertNotNull(recordset);

        List<CoalesceRecordset> recordsets = section.getRecordsetsAsList();

        assertEquals(2, recordsets.size());

        CoalesceRecordset existingRecordset = ArrayHelper.getItem(recordsets, "7A158E39-B6C4-4912-A712-DF296375A368");
        assertNotNull(existingRecordset);

        assertNotNull(ArrayHelper.getItem(recordsets, recordset.getKey()));

        List<CoalesceFieldDefinition> fieldDefinitions = existingRecordset.getFieldDefinitions();

        assertEquals("93C6A209-AD86-4474-9FFB-D6801B2548AA", fieldDefinitions.get(0).getKey());
        assertEquals("DBBB6CEC-DD98-4B31-9995-8AF0A5E184EC", fieldDefinitions.get(1).getKey());
        assertEquals("7D45F5BD-14A0-4890-B8C5-D502806A4607", fieldDefinitions.get(2).getKey());
        assertEquals("1A7DA2CD-8A83-4E86-ADE8-15FDECE0564E", fieldDefinitions.get(3).getKey());
        assertEquals("5F2D150A-CEDB-4BF9-9A66-DD3212721E2B", fieldDefinitions.get(4).getKey());
        assertEquals("00D19206-5246-45EE-BA15-07507767040A", fieldDefinitions.get(5).getKey());
        assertEquals("FADEBDFD-4477-4D2E-8C33-186717209F16", fieldDefinitions.get(6).getKey());
        assertEquals("3B28419A-6744-4B10-B4E7-01B3A04620ED", fieldDefinitions.get(7).getKey());
        assertEquals("F862B507-8523-4BB2-8A49-EB45FBEB88AD", fieldDefinitions.get(8).getKey());
        assertEquals("6C2296DD-205E-432B-B381-2236E7A43162", fieldDefinitions.get(9).getKey());
        assertEquals("5150BB68-0156-469D-9C93-C62CF3F7FA19", fieldDefinitions.get(10).getKey());
        assertEquals("12E16C1A-2D48-4228-90AE-596591E66536", fieldDefinitions.get(11).getKey());
        assertEquals("EEA36E54-601E-4254-84D3-2B387CF4192A", fieldDefinitions.get(12).getKey());
        assertEquals("CD00930C-1659-415A-BE64-67A57FD8A1E9", fieldDefinitions.get(13).getKey());
        assertEquals("1019A528-481A-4E20-BF32-868C36B19ED0", fieldDefinitions.get(14).getKey());
        assertEquals("1EF2E901-DDD8-4C38-A5BF-858CB13F9562", fieldDefinitions.get(16).getKey());

        assertEquals("9A03833C-AC15-47C8-A037-1FFFD13A26E9", existingRecordset.getRecords().get(0).getKey());

    }

    @Test(expected = NullArgumentException.class)
    public void createNullParentTest()
    {
        @SuppressWarnings("unused")
        CoalesceRecordset recordset = CoalesceRecordset.create(null, "New Section");
    }

    @Test(expected = NullArgumentException.class)
    public void createNullNameTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceSection section = entity.getSection(CoalesceTypeInstances.TEST_MISSION_INFO_SECTION_PATH);

        @SuppressWarnings("unused")
        CoalesceRecordset recordset = CoalesceRecordset.create(section, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void createEmptyNameTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceSection section = entity.getSection(CoalesceTypeInstances.TEST_MISSION_INFO_SECTION_PATH);

        @SuppressWarnings("unused")
        CoalesceRecordset recordset = CoalesceRecordset.create(section, "");

    }

    @Test(expected = IllegalArgumentException.class)
    public void createWhiteSpaceTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceSection section = entity.getSection(CoalesceTypeInstances.TEST_MISSION_INFO_SECTION_PATH);

        @SuppressWarnings("unused")
        CoalesceRecordset recordset = CoalesceRecordset.create(section, "   ");

    }

    @Test(expected = NullArgumentException.class)
    public void createNullBothTest()
    {

        @SuppressWarnings("unused")
        CoalesceRecordset recordset = CoalesceRecordset.create(null, null);

    }

    @Test
    public void createMinMaxRecordsTest()
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        CoalesceSection section = entity.createSection("New Section");

        CoalesceRecordset recordset = CoalesceRecordset.create(section, "New Recordset", 0, 5);

        assertNotNull(recordset);
        assertEquals(0, recordset.getMinRecords());
        assertEquals(5, recordset.getMaxRecords());

    }

    @Test
    public void createMinMaxRecordsRequiredRecordsTest()
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        CoalesceSection section = entity.createSection("New Section");

        CoalesceRecordset recordset = CoalesceRecordset.create(section, "New Recordset", 3, 5);

        assertNotNull(recordset);
        assertEquals(3, recordset.getMinRecords());
        assertEquals(5, recordset.getMaxRecords());

    }

    @Test
    public void createMinMaxRecordsZeroTest()
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        CoalesceSection section = entity.createSection("New Section");

        CoalesceRecordset recordset = CoalesceRecordset.create(section, "New Recordset", 0, 0);

        assertNotNull(recordset);
        assertEquals(0, recordset.getMinRecords());
        assertEquals(0, recordset.getMaxRecords());

    }

    @Test
    public void createMinMaxRecordsMinNegativeTest()
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        CoalesceSection section = entity.createSection("New Section");

        CoalesceRecordset recordset = CoalesceRecordset.create(section, "New Recordset", -1, 5);

        assertNull(recordset);

    }

    @Test
    public void createMinMaxRecordsMaxNegativeTest()
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        CoalesceSection section = entity.createSection("New Section");

        CoalesceRecordset recordset = CoalesceRecordset.create(section, "New Recordset", 0, -1);

        assertNull(recordset);

    }

    @Test
    public void createMinMaxRecordsMinGtMaxTest()
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        CoalesceSection section = entity.createSection("New Section");

        CoalesceRecordset recordset = CoalesceRecordset.create(section, "New Recordset", 10, 9);

        assertNull(recordset);

    }

    @Test(expected = NullArgumentException.class)
    public void initializeNullParentTest()
    {

        CoalesceRecordset recordset = new CoalesceRecordset();
        recordset.initialize(null, new Recordset());

    }

    @Test(expected = NullArgumentException.class)
    public void initialzeNullRecordsetTest()
    {
        CoalesceEntity entity = new CoalesceEntity();
        entity.initialize();
        CoalesceSection section = entity.createSection("New Section");

        CoalesceRecordset recordset = new CoalesceRecordset();
        recordset.initialize(section, null);

    }

    @Test(expected = NullArgumentException.class)
    public void initializeNullBothTest()
    {

        CoalesceRecordset recordset = new CoalesceRecordset();
        recordset.initialize(null, null);

    }

    @Test
    public void keyTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertEquals("7A158E39-B6C4-4912-A712-DF296375A368", recordset.getKey());

        UUID guid2 = UUID.randomUUID();

        recordset.setKey(guid2.toString());

        assertEquals(guid2.toString(), recordset.getKey());

    }

    @Test
    public void nameTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertEquals("Mission Information Recordset", recordset.getName());

        recordset.setName("New Information Recordset");

        assertEquals("New Information Recordset", recordset.getName());

    }

    @Test
    public void typeTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertEquals("recordset", recordset.getType());

        CoalesceEntity newEntity = CoalesceEntity.create("Operation", "Portal", "1.2.3.4", "ID", "Type");
        CoalesceSection newSection = CoalesceSection.create(newEntity, "Operation/New Section");
        CoalesceRecordset newRecordset = CoalesceRecordset.create(newSection, "New Recordset");

        assertEquals("recordset", newRecordset.getType());

    }

    @Test
    public void hasActiveRecordsNoRecoresTest()
    {
        CoalesceEntity entity = CoalesceEntity.create("");
        CoalesceSection section = CoalesceSection.create(entity, "Section");
        CoalesceRecordset recordset = CoalesceRecordset.create(section, "Recordset");

        assertFalse(recordset.getHasActiveRecords());

    }

    @Test
    public void hasActiveRecordsNoActiveTest()
    {
        CoalesceEntity entity = CoalesceEntity.create("");
        CoalesceSection section = CoalesceSection.create(entity, "Section");
        CoalesceRecordset recordset = CoalesceRecordset.create(section, "Recordset");

        CoalesceRecord record = recordset.addNew();
        record.setStatus(ECoalesceObjectStatus.DELETED);

        record = recordset.addNew();
        record.setStatus(ECoalesceObjectStatus.UNKNOWN);

        assertFalse(recordset.getHasActiveRecords());

        record.setStatus(ECoalesceObjectStatus.ACTIVE);

        assertTrue(recordset.getHasActiveRecords());

    }

    @Test
    public void hasActiveRecordsSomeActiveTest()
    {
        CoalesceEntity entity = CoalesceEntity.create("");
        CoalesceSection section = CoalesceSection.create(entity, "Section");
        CoalesceRecordset recordset = CoalesceRecordset.create(section, "Recordset");

        CoalesceRecord record = recordset.addNew();
        record.setStatus(ECoalesceObjectStatus.DELETED);

        record = recordset.addNew();
        record.setStatus(ECoalesceObjectStatus.UNKNOWN);

        record = recordset.addNew();
        record.setStatus(ECoalesceObjectStatus.ACTIVE);

        assertTrue(recordset.getHasActiveRecords());

        record.setStatus(ECoalesceObjectStatus.DELETED);

        assertFalse(recordset.getHasActiveRecords());

    }

    @Test
    public void hasRecordsTest()
    {
        CoalesceEntity entity = CoalesceEntity.create("");
        CoalesceSection section = CoalesceSection.create(entity, "Section");
        CoalesceRecordset recordset = CoalesceRecordset.create(section, "Recordset");

        assertFalse(recordset.getHasRecords());

        CoalesceRecord record = recordset.addNew();
        record.setStatus(ECoalesceObjectStatus.DELETED);
        record.setStatus(ECoalesceObjectStatus.UNKNOWN);
        assertTrue(recordset.getHasRecords());

    }

    @Test
    public void noIndexTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        Assert.assertEquals(CoalesceObject.ATTRIBUTE_NOINDEX_DEFAULT, recordset.isNoIndex());

        recordset.setNoIndex(true);

        String entityXml = entity.toXml();

        CoalesceEntity desEntity = CoalesceEntity.create(entityXml);
        CoalesceRecordset desRecordset = (CoalesceRecordset) desEntity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertTrue(desRecordset.isNoIndex());

        CoalesceEntity newEntity = CoalesceEntity.create("Operation", "Portal", "1.2.3.4", "ID", "Type");
        CoalesceSection newSection = CoalesceSection.create(newEntity, "Operation/New Section");
        CoalesceRecordset newRecordset = CoalesceRecordset.create(newSection, "New Recordset");

        Assert.assertEquals(CoalesceObject.ATTRIBUTE_NOINDEX_DEFAULT, newRecordset.isNoIndex());

    }

    @Test
    public void dateCreatedTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertEquals(JodaDateTimeHelper.fromXmlDateTimeUTC("2014-05-02T14:33:51.8525751Z"), recordset.getDateCreated());

        DateTime now = JodaDateTimeHelper.nowInUtc();
        recordset.setDateCreated(now);

        assertEquals(now, recordset.getDateCreated());

    }

    @Test
    public void lastModifiedTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertEquals(JodaDateTimeHelper.fromXmlDateTimeUTC("2014-05-02T14:33:59.193995Z"), recordset.getLastModified());

        DateTime now = JodaDateTimeHelper.nowInUtc();
        recordset.setLastModified(now);

        assertEquals(now, recordset.getLastModified());

    }

    @Test
    public void createFieldDefinitionFullNullNameTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNull(recordset.createFieldDefinition(null, ECoalesceFieldDataTypes.STRING_TYPE, "Label", "(U)", "Default"));

    }

    @Test
    public void createFieldDefinitionFullEmptyNameTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNull(recordset.createFieldDefinition("", ECoalesceFieldDataTypes.STRING_TYPE, "Label", "(U)", "Default"));

    }

    @Test
    public void createFieldDefinitionFullWhiteSpaceNameTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNull(recordset.createFieldDefinition("   ", ECoalesceFieldDataTypes.STRING_TYPE, "Label", "(U)", "Default"));

    }

    @Test
    public void createFieldDefinitionFullNullDataTypeTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNull(recordset.createFieldDefinition("Field def", null, "Label", "(U)", "Default"));

    }

    @Test
    public void createFieldDefinitionFullNullLabelTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def",
                                                      ECoalesceFieldDataTypes.STRING_TYPE,
                                                      null,
                                                      "(U)",
                                                      "Default"));

    }

    @Test
    public void createFieldDefinitionFullEmptyLabelTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def", ECoalesceFieldDataTypes.STRING_TYPE, "", "(U)", "Default"));

    }

    @Test
    public void createFieldDefinitionFullWhiteSpaceLabelTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def",
                                                      ECoalesceFieldDataTypes.STRING_TYPE,
                                                      "   ",
                                                      "(U)",
                                                      "Default"));

    }

    @Test
    public void createFieldDefinitionFullNullDefaultClassTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def",
                                                      ECoalesceFieldDataTypes.STRING_TYPE,
                                                      "Label",
                                                      null,
                                                      "Default"));

    }

    @Test
    public void createFieldDefinitionFullEmptyDefaultClassTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def",
                                                      ECoalesceFieldDataTypes.STRING_TYPE,
                                                      "Label",
                                                      "",
                                                      "Default"));

    }

    @Test
    public void createFieldDefinitionFullWhiteSpaceDefaultClassTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def",
                                                      ECoalesceFieldDataTypes.STRING_TYPE,
                                                      "Label",
                                                      "   ",
                                                      "Default"));

    }

    @Test
    public void createFieldDefinitionFullNullDefaultValueTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def", ECoalesceFieldDataTypes.STRING_TYPE, "Label", "(U)", null));

    }

    @Test
    public void createFieldDefinitionFullEmptyDefaultValueTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def", ECoalesceFieldDataTypes.STRING_TYPE, "Label", "(U)", ""));

    }

    @Test
    public void createFieldDefinitionFullWhiteSpaceDefaultValueTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertNotNull(recordset.createFieldDefinition("Field def",
                                                      ECoalesceFieldDataTypes.STRING_TYPE,
                                                      "Label",
                                                      "(U)",
                                                      "   "));

    }

    @Test
    public void getFieldDefinitionTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertEquals("93C6A209-AD86-4474-9FFB-D6801B2548AA", recordset.getFieldDefinition("ActionNumber").getKey());
        assertEquals("1EF2E901-DDD8-4C38-A5BF-858CB13F9562", recordset.getFieldDefinition("MissionAddress").getKey());

        assertNull(recordset.getFieldDefinition("Something"));

    }

    @Test
    public void getAllowTests()
    {
        CoalesceEntity missionEntity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset missionRecordset = (CoalesceRecordset) missionEntity.getCoalesceObjectForNamePath("TREXMission/Mission Information Section/Mission Information Recordset");
 
        assertTrue(missionRecordset.getAllowEdit());
        assertTrue(missionRecordset.getAllowNew());
        assertTrue(missionRecordset.getAllowRemove());
    }
    
    
    @Test
    public void getCountTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertEquals(1, recordset.getCount());

        recordset.addNew();

        assertEquals(2, recordset.getCount());

    }

    @Test
    public void containsTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        CoalesceRecord existingRecord = (CoalesceRecord) recordset.getCoalesceObjectForNamePath("Mission Information Recordset/Mission Information Recordset Record");

        CoalesceRecord newRecord = recordset.addNew();

        assertTrue(recordset.contains(existingRecord));
        assertTrue(recordset.contains(newRecord));
        assertFalse(recordset.contains(new CoalesceRecord()));

    }

    @Test
    public void indexOfTest()
    {
        CoalesceEntity missionEntity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecord missionRecord = (CoalesceRecord) missionEntity.getCoalesceObjectForNamePath("TREXMission/Mission Information Section/Mission Information Recordset/Mission Information Recordset Record");
        CoalesceRecordset missionRecordset = (CoalesceRecordset) missionRecord.getParent();
        
        CoalesceRecord newRecord = missionRecordset.addNew();
        
        CoalesceRecord record = new CoalesceRecord();
        
        assertEquals(0, missionRecordset.indexOf(missionRecord));
        assertEquals(1, missionRecordset.indexOf(newRecord));
        assertEquals(-1, missionRecordset.indexOf(record));
        assertEquals(-1, missionRecordset.indexOf(null));
        assertEquals(-1, missionRecordset.indexOf("Testing"));
        
        
    }
        
    @Test
    public void addNewTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        assertEquals(1, recordset.getRecords().size());

        CoalesceRecord newRecord = recordset.addNew();

        assertEquals(2, recordset.getRecords().size());
        assertEquals(recordset, newRecord.getParent());
        assertEquals(recordset.getName() + " Record", newRecord.getName());

    }

    @Test
    public void getItemTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        CoalesceRecord existingRecord = (CoalesceRecord) recordset.getCoalesceObjectForNamePath("Mission Information Recordset/Mission Information Recordset Record");

        CoalesceRecord newRecord = recordset.addNew();

        assertEquals(existingRecord, recordset.getItem(0));
        assertEquals(newRecord, recordset.getItem(1));

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getItemNegativeTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        recordset.getItem(-1);

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getItemGreaterTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        @SuppressWarnings("unused")
        CoalesceRecord newRecord = recordset.addNew();

        recordset.getItem(2);

    }

    @Test
    public void removeAtTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        CoalesceRecord existingRecord = (CoalesceRecord) recordset.getCoalesceObjectForNamePath("Mission Information Recordset/Mission Information Recordset Record");

        CoalesceRecord newRecord = recordset.addNew();

        assertTrue(recordset.contains(existingRecord));
        assertTrue(recordset.contains(newRecord));
        assertEquals(2, recordset.getCount());
        assertTrue(recordset.getHasActiveRecords());
        assertTrue(recordset.getHasRecords());

        recordset.removeAt(2);
        assertEquals(2, recordset.getCount());
        
        recordset.removeAt(-1);
        assertEquals(2, recordset.getCount());
        
        recordset.removeAt(1);

        assertFalse(recordset.contains(newRecord));
        assertEquals(1, recordset.getCount());
        assertTrue(recordset.getHasActiveRecords());
        assertTrue(recordset.getHasRecords());

        recordset.removeAt(0);

        assertFalse(recordset.contains(existingRecord));
        assertEquals(0, recordset.getCount());
        assertFalse(recordset.getHasActiveRecords());
        assertTrue(recordset.getHasRecords());

    }
    
    @Test
    public void removeAllTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        CoalesceRecord existingRecord = (CoalesceRecord) recordset.getCoalesceObjectForNamePath("Mission Information Recordset/Mission Information Recordset Record");

        CoalesceRecord newRecord = recordset.addNew();

        assertTrue(recordset.contains(existingRecord));
        assertTrue(recordset.contains(newRecord));
        assertEquals(2, recordset.getCount());
        assertTrue(recordset.getHasActiveRecords());
        assertTrue(recordset.getHasRecords());

        System.out.println(recordset.toXml());

        recordset.removeAll();

        assertFalse(recordset.contains(existingRecord));
        assertEquals(0, recordset.getCount());
        assertFalse(recordset.getHasActiveRecords());
        assertTrue(recordset.getHasRecords());
        
        System.out.println(recordset.toXml());

    }

    @Test
    public void removeTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath(CoalesceTypeInstances.TEST_MISSION_RECORDSET_PATH);

        CoalesceRecord existingRecord = (CoalesceRecord) recordset.getCoalesceObjectForNamePath("Mission Information Recordset/Mission Information Recordset Record");

        CoalesceRecord newRecord = recordset.addNew();

        assertTrue(recordset.contains(existingRecord));
        assertTrue(recordset.contains(newRecord));
        assertEquals(2, recordset.getCount());
        assertTrue(recordset.getHasActiveRecords());
        assertTrue(recordset.getHasRecords());

        recordset.remove(UUID.randomUUID().toString());
        assertEquals(2, recordset.getCount());
        
        recordset.remove(newRecord.getKey());

        assertFalse(recordset.contains(newRecord));
        assertEquals(1, recordset.getCount());
        assertTrue(recordset.getHasActiveRecords());
        assertTrue(recordset.getHasRecords());

        recordset.remove(existingRecord.getKey());

        assertFalse(recordset.contains(existingRecord));
        assertEquals(0, recordset.getCount());
        assertFalse(recordset.getHasActiveRecords());
        assertTrue(recordset.getHasRecords());

    }

    @Test
    public void changeRecordStatusTest()
    {
        TestEntity entity = new TestEntity();
        entity.initialize();

        TestRecord record1 = entity.addRecord1();
        TestRecord record2 = entity.addRecord1();

        assertTrue(entity.getRecordset1().getHasActiveRecords());
        assertTrue(entity.getRecordset1().getHasRecords());
        assertEquals(2, entity.getRecordset1().getRecords().size());

        record1.setStatus(ECoalesceObjectStatus.DELETED);

        assertTrue(entity.getRecordset1().getHasActiveRecords());
        assertTrue(entity.getRecordset1().getHasRecords());
        assertEquals(1, entity.getRecordset1().getRecords().size());

        record2.setStatus(ECoalesceObjectStatus.UNKNOWN);

        assertFalse(entity.getRecordset1().getHasActiveRecords());
        assertTrue(entity.getRecordset1().getHasRecords());
        assertEquals(0, entity.getRecordset1().getRecords().size());
        assertEquals(2, entity.getRecordset1().getAllRecords().size());

        record1.setStatus(ECoalesceObjectStatus.ACTIVE);

        assertTrue(entity.getRecordset1().getHasActiveRecords());
        assertTrue(entity.getRecordset1().getHasRecords());
        assertEquals(1, entity.getRecordset1().getRecords().size());
    }

    @Test
    public void toXmlTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath("TREXMission/Mission Information Section/Mission Information Recordset");
        String recordsetXml = recordset.toXml();

        Recordset desRecordset = (Recordset) XmlHelper.deserialize(recordsetXml, Recordset.class);

        assertEquals(recordset.getRecords().size(), desRecordset.getRecord().size());
        assertEquals(recordset.getKey(), desRecordset.getKey());
        assertEquals(recordset.getName(), desRecordset.getName());
        assertEquals(recordset.getDateCreated(), desRecordset.getDatecreated());
        assertEquals(recordset.getLastModified(), desRecordset.getLastmodified());
        assertEquals(recordset.getMinRecords(), desRecordset.getMinrecords());
        assertEquals(recordset.getMaxRecords(), desRecordset.getMaxrecords());
        assertEquals(recordset.getStatus(), desRecordset.getStatus());
        assertEquals(recordset.getFieldDefinitions().size(), desRecordset.getFielddefinition().size());

    }

    @Test
    public void setStatusTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);
        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath("TREXMission/Mission Information Section/Mission Information Recordset");

        assertEquals(ECoalesceObjectStatus.ACTIVE, recordset.getStatus());

        recordset.setStatus(ECoalesceObjectStatus.UNKNOWN);
        String recordsetXml = recordset.toXml();

        Recordset desRecordset = (Recordset) XmlHelper.deserialize(recordsetXml, Recordset.class);

        assertEquals(ECoalesceObjectStatus.UNKNOWN, desRecordset.getStatus());

    }

    @Test
    public void attributeTest()
    {
        CoalesceEntity entity = CoalesceEntity.create(CoalesceTypeInstances.TEST_MISSION);

        CoalesceRecordset recordset = (CoalesceRecordset) entity.getCoalesceObjectForNamePath("TREXMission/Mission Information Section/Mission Information Recordset");
        
        int before = recordset.getAttributes().size();
        
        recordset.setAttribute("TestAttribute", "TestingValue");

        assertEquals(before + 1, recordset.getAttributes().size());

        assertEquals("TestingValue", recordset.getAttribute("TestAttribute"));

        assertEquals("Mission Information Recordset", recordset.getName());
        assertEquals(CoalesceObject.ATTRIBUTE_NOINDEX_DEFAULT, recordset.isNoIndex());

        recordset.setAttribute("Name", "TestingName");
        assertEquals("TestingName", recordset.getName());
        assertEquals("TestingName", recordset.getAttribute("Name"));

        UUID guid = UUID.randomUUID();
        recordset.setAttribute("Key", guid.toString());
        assertEquals(guid.toString(), recordset.getKey());
        assertEquals(guid.toString(), recordset.getAttribute("Key"));

        DateTime now = JodaDateTimeHelper.nowInUtc();
        DateTime future = now.plusDays(2);

        recordset.setAttribute("DateCreated", JodaDateTimeHelper.toXmlDateTimeUTC(now));
        assertEquals(now, recordset.getDateCreated());

        recordset.setAttribute("MinRecords", "123");
        assertEquals(123, recordset.getMinRecords());

        recordset.setAttribute("MaxRecords", "234");
        assertEquals(234, recordset.getMaxRecords());

        recordset.setAttribute("NoIndex", "True");
        assertEquals(true, recordset.isNoIndex());

        recordset.setAttribute("Status", ECoalesceObjectStatus.UNKNOWN.toString());
        assertEquals(ECoalesceObjectStatus.UNKNOWN, recordset.getStatus());

        recordset.setAttribute("LastModified", JodaDateTimeHelper.toXmlDateTimeUTC(future));
        assertEquals(future, recordset.getLastModified());

        String entityXml = entity.toXml();
        CoalesceEntity desEntity = CoalesceEntity.create(entityXml);
        CoalesceRecordset desRecordset = (CoalesceRecordset) desEntity.getCoalesceObjectForNamePath("TREXMission/Mission Information Section/TestingName");

        assertEquals("TestingValue", desRecordset.getAttribute("TestAttribute"));
        assertEquals("TestingName", desRecordset.getName());
        assertEquals(guid.toString(), desRecordset.getKey());
        assertEquals(now, desRecordset.getDateCreated());
        assertEquals(future, desRecordset.getLastModified());
        assertEquals(123, desRecordset.getMinRecords());
        assertEquals(234, desRecordset.getMaxRecords());
        assertEquals(true, desRecordset.isNoIndex());
        assertEquals(ECoalesceObjectStatus.UNKNOWN, desRecordset.getStatus());

    }
}
