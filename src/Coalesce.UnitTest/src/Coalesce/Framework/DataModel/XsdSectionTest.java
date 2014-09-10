package Coalesce.Framework.DataModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Test;

import Coalesce.Common.Helpers.GUIDHelper;
import Coalesce.Common.Helpers.JodaDateTimeHelper;
import Coalesce.Common.UnitTest.CoalesceTypeInstances;
import Coalesce.Framework.GeneratedJAXB.Entity.Section;

public class XsdSectionTest {

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
    public void createSectionTest()
    {
        // Create Entity
        XsdEntity entity = XsdEntity.create("TREXOperation",
                                            "TREX Portal",
                                            "1.0.0.0",
                                            "",
                                            "",
                                            "TREXOperation/Operation Information Section/Operation Information Recordset/Operation Information Recordset Record/OperationName");

        // Create Live Status Section
        assertNull(entity.getSection("TREXOperation/Live Status Section"));

        XsdSection liveSection = XsdSection.create(entity, "Live Status Section");
        assertNotNull(GUIDHelper.IsValid(liveSection.getKey()));
        assertEquals(liveSection, entity.getSection("TREXOperation/Live Status Section"));
        assertFalse(liveSection.getNoIndex());
    }

    @Test
    public void createSectionFromXmlTest()
    {

        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        assertNull(entity.getSection("TREXMission/A New Section"));

        XsdSection newSection = XsdSection.create(entity, "A New Section");

        assertNotNull(newSection);
        assertNotNull(GUIDHelper.IsValid(newSection.getKey()));
        assertEquals(newSection, entity.getSection("TREXMission/A New Section"));
        assertFalse(newSection.getNoIndex());

    }

    @Test
    public void createSectionFromXmlExistingTest()
    {

        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        XsdSection liveSection = entity.getSection("TREXMission/Live Status Section");
        XsdSection informationSection = entity.getSection("TREXMission/Mission Information Section");

        assertNotNull(liveSection);
        assertNotNull(informationSection);
        
        XsdSection createdLiveSection = XsdSection.create(entity, "Live Status Section");
        XsdSection createdInformationSection = XsdSection.create(entity, "Mission Information Section");

        assertEquals(liveSection, createdLiveSection);
        assertEquals(informationSection, createdInformationSection);

    }

    @Test
    public void createSectionNullParentTest()
    {
        XsdSection liveSection = XsdSection.create(null, "Live Status Section");
        
        assertNull(liveSection);
        
    }
    
    @Test
    public void createSectionNullNameTest()
    {
        // Create Entity
        XsdEntity entity = XsdEntity.create("TREXOperation",
                                            "TREX Portal",
                                            "1.0.0.0",
                                            "",
                                            "",
                                            "TREXOperation/Operation Information Section/Operation Information Recordset/Operation Information Recordset Record/OperationName");

        XsdSection section = XsdSection.create(entity, null);
        
        assertNull(section);
        
    }
    
    @Test
    public void createSectionNullBothTest()
    {

        XsdSection section = XsdSection.create(null, null);
        
        assertNull(section);

    }
    
    @Test
    public void createSectionExistingNoIndexTrueTest()
    {
        // Create Entity
        XsdEntity entity = XsdEntity.create("TREXOperation",
                                            "TREX Portal",
                                            "1.0.0.0",
                                            "",
                                            "",
                                            "TREXOperation/Operation Information Section/Operation Information Recordset/Operation Information Recordset Record/OperationName");

        XsdSection liveSection = XsdSection.create(entity, "Live Status Section", true);
        assertEquals(liveSection, entity.getSection("TREXOperation/Live Status Section"));
        assertTrue(liveSection.getNoIndex());

        XsdSection liveSection2 = XsdSection.create(entity, "Live Status Section");
        assertEquals(liveSection2, entity.getSection("TREXOperation/Live Status Section"));
        assertEquals(liveSection, liveSection2);
        assertFalse(liveSection.getNoIndex());
        assertFalse(liveSection2.getNoIndex());

    }

    @Test
    public void createSectionWithNoIndexFalseTest()
    {
        // Create Entity
        XsdEntity entity = XsdEntity.create("TREXOperation",
                                            "TREX Portal",
                                            "1.0.0.0",
                                            "",
                                            "",
                                            "TREXOperation/Operation Information Section/Operation Information Recordset/Operation Information Recordset Record/OperationName");

        XsdSection liveSection = XsdSection.create(entity, "Live Status Section", false);
        assertEquals(liveSection, entity.getSection("TREXOperation/Live Status Section"));
        assertFalse(liveSection.getNoIndex());
    }

    @Test
    public void createSectionWithNoIndexTrueTest()
    {
        // Create Entity
        XsdEntity entity = XsdEntity.create("TREXOperation",
                                            "TREX Portal",
                                            "1.0.0.0",
                                            "",
                                            "",
                                            "TREXOperation/Operation Information Section/Operation Information Recordset/Operation Information Recordset Record/OperationName");

        XsdSection liveSection = XsdSection.create(entity, "Live Status Section", true);
        assertEquals(liveSection, entity.getSection("TREXOperation/Live Status Section"));
        assertTrue(liveSection.getNoIndex());

    }

    @Test
    public void createSectionWithNoIndexTrueForExistingNoIndexFalseTest()
    {
        // Create Entity
        XsdEntity entity = XsdEntity.create("TREXOperation",
                                            "TREX Portal",
                                            "1.0.0.0",
                                            "",
                                            "",
                                            "TREXOperation/Operation Information Section/Operation Information Recordset/Operation Information Recordset Record/OperationName");

        XsdSection liveSection = XsdSection.create(entity, "Live Status Section", false);
        assertEquals(liveSection, entity.getSection("TREXOperation/Live Status Section"));
        assertFalse(liveSection.getNoIndex());

        XsdSection liveSection2 = XsdSection.create(entity, "Live Status Section", true);
        assertEquals(liveSection2, entity.getSection("TREXOperation/Live Status Section"));
        assertEquals(liveSection, liveSection2);
        assertTrue(liveSection.getNoIndex());
        assertTrue(liveSection2.getNoIndex());

    }

    @Test
    public void createSectionWithNoIndexTrueFromXmlTest()
    {

        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        assertNull(entity.getSection("TREXMission/A New Section"));

        XsdSection newSection = XsdSection.create(entity, "A New Section", true);

        assertNotNull(newSection);
        assertEquals(newSection, entity.getSection("TREXMission/A New Section"));
        assertTrue(newSection.getNoIndex());

        String entityXml = entity.toXml();

        XsdEntity desEntity = XsdEntity.create(entityXml);

        XsdSection desSection = desEntity.getSection("TREXMission/A New Section");
        assertEquals(newSection.getKey(), desSection.getKey());
        assertEquals(newSection.getName(), desSection.getName());
        assertEquals(newSection.getNoIndex(), desSection.getNoIndex());
    }

    @Test
    public void createSectionNoIndexTrueNullParentTest()
    {

        XsdSection liveSection = XsdSection.create(null, "Live Status Section", false);

        assertNull(liveSection);
        
        liveSection = XsdSection.create(null, "Live Status Section", true);
        
        assertNull(liveSection);
    }
    
    @Test
    public void createSectionIndexTrueNullNameTest()
    {
        // Create Entity
        XsdEntity entity = XsdEntity.create("TREXOperation",
                                            "TREX Portal",
                                            "1.0.0.0",
                                            "",
                                            "",
                                            "TREXOperation/Operation Information Section/Operation Information Recordset/Operation Information Recordset Record/OperationName");

        XsdSection section = XsdSection.create(entity, null, false);
        
        assertNull(section);
        
        section = XsdSection.create(entity, null, true);
        
        assertNull(section);

    }
    
    @Test
    public void createSectionIndexTrueNullBothTest()
    {

        XsdSection section = XsdSection.create(null, null, false);

        assertNull(section);
        
        section = XsdSection.create(null, null, true);
        
        assertNull(section);
        
    }
    

    @Test
    public void initializeNullParent()
    {

        XsdSection section = new XsdSection();
        
        assertFalse(section.initialize(null, new Section()));
        
    }

    @Test
    public void initializeNullSection()
    {
        // Create Entity
        XsdEntity entity = XsdEntity.create("TREXOperation",
                                            "TREX Portal",
                                            "1.0.0.0",
                                            "",
                                            "",
                                            "TREXOperation/Operation Information Section/Operation Information Recordset/Operation Information Recordset Record/OperationName");

        XsdSection section = new XsdSection();
        
        assertFalse(section.initialize(entity, null));
   
    }
    
    @Test
    public void initializeNullBoth()
    {
        XsdSection section = new XsdSection();
        
        assertFalse(section.initialize(null, null));
        
    }
    
    @Test
    public void keyTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        XsdSection liveSection = entity.getSection("TREXMission/Live Status Section");

        assertEquals("85CB4256-4CC2-4F96-A03D-5EF880989822", liveSection.getKey());
        
        UUID guid = UUID.randomUUID();
        
        liveSection.setKey(guid);
        
        assertEquals(guid.toString(), liveSection.getKey());
        
        UUID guid2 = UUID.randomUUID();
        
        liveSection.setKey(guid2.toString());
        
        assertEquals(guid2.toString(), liveSection.getKey());
        
    }
    
    @Test
    public void nameTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        XsdSection liveSection = entity.getSection("TREXMission/Live Status Section");

        assertEquals("Live Status Section", liveSection.getName());
        
        liveSection.setName("New Section Name");
        
        assertEquals("New Section Name", liveSection.getName());
        
    }
    
    @Test
    public void typeTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);
        
        XsdSection liveSection = entity.getSection("TREXMission/Live Status Section");
        
        assertEquals("section", liveSection.getType());
        
        XsdEntity newEntity = XsdEntity.create("Operation", "Portal", "1.2.3.4", "ID", "Type");
        XsdSection newSection = XsdSection.create(newEntity, "Operation/New Section");
                
        assertEquals("section", newSection.getType());
        
    }
 
    @Test
    public void createRecordSetTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        XsdSection informationSection = entity.getSection("TREXMission/Mission Information Section");

        XsdRecordset recordset = informationSection.getRecordset("Mission New Recordset");
        
        assertNull(recordset);
        
        XsdRecordset newRecordset = informationSection.createRecordset("Mission New Recordset");
        
        assertNotNull(newRecordset);
        assertEquals(informationSection, newRecordset.getParent());
        assertEquals("Mission New Recordset", newRecordset.getName());
        
    }
    
    @Test
    public void createRecordSetAlreadyExists()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        XsdSection informationSection = entity.getSection("TREXMission/Mission Information Section");

        XsdRecordset recordSet = informationSection.getRecordset("Mission Information Section/Mission Information Recordset");

        assertNotNull(recordSet);
        
        XsdRecordset dublicateRecordset = informationSection.createRecordset("Mission Information Recordset");
        
        assertEquals(recordSet, dublicateRecordset);
        
    }
    
    @Test
    public void createRecordsetNullNameTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);
        
        XsdSection informationSection = entity.getSection("TREXMission/Mission Information Section");

        XsdRecordset recordset = informationSection.createRecordset(null);
        
        assertNull(recordset);
        
    }
    
    @Test
    public void createRecordsetEmptyNameTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);
        
        XsdSection informationSection = entity.getSection("TREXMission/Mission Information Section");

        XsdRecordset recordset = informationSection.createRecordset("");
        
        assertNull(recordset);
        
    }
    
    @Test
    public void createRecordsetWhiteSpaceNameTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);
        
        XsdSection informationSection = entity.getSection("TREXMission/Mission Information Section");

        XsdRecordset recordset = informationSection.createRecordset("   ");
        
        assertNull(recordset);

    }
    
    @Test
    public void getRecordsetNullTest()
    {
        XsdEntity entity = XsdEntity.create("Operation", "Test Portal", "1.2.3.5", "ID", "Type");
        XsdSection section = XsdSection.create(entity, "New Section");
        section.createRecordset("Recordset 1");
        section.createRecordset("Recordset 2");

        XsdRecordset recordsection = section.getRecordset(null);
        
        assertNull(recordsection);
        
    }
    
    @Test
    public void getRecordsetEmptyTest()
    {
        XsdEntity entity = XsdEntity.create("Operation", "Test Portal", "1.2.3.5", "ID", "Type");
        XsdSection section = XsdSection.create(entity, "New Section");
        section.createRecordset("Recordset 1");
        section.createRecordset("Recordset 2");

        XsdRecordset recordsection = section.getRecordset("");
        
        assertNull(recordsection);
        
    }
    
    @Test
    public void getRecordsetTest()
    {
        XsdEntity entity = XsdEntity.create("Operation", "Test Portal", "1.2.3.5", "ID", "Type");
        XsdSection section = XsdSection.create(entity, "New Section");
        XsdRecordset recordset1 = section.createRecordset("Recordset 1");
        XsdRecordset recordset2 = section.createRecordset("Recordset 2");

        assertEquals(recordset1, section.getRecordset("New Section/Recordset 1"));
        assertEquals(recordset2, section.getRecordset("New Section/Recordset 2"));
        
    }
    
    @Test
    public void getRecordsetsOneFromXmlTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        XsdSection informationSection = entity.getSection("TREXMission/Mission Information Section");

        Map<String, XsdRecordset> recordsets = informationSection.getRecordsets();
        
        assertEquals(1, recordsets.size());
        assertNotNull(recordsets.get("7A158E39-B6C4-4912-A712-DF296375A368"));
        assertEquals("Mission Information Recordset", recordsets.get("7A158E39-B6C4-4912-A712-DF296375A368").getName());
        
    }
    
    @Test
    public void getRecordsetsMultipleTest()
    {
        XsdEntity entity = XsdEntity.create("Operation", "Test Portal", "1.2.3.5", "ID", "Type");
        XsdSection section = XsdSection.create(entity, "New Section");
        XsdRecordset recordset1 = section.createRecordset("Recordset 1");
        XsdRecordset recordset2 = section.createRecordset("Recordset 2");

        Map<String, XsdRecordset> recordsets = section.getRecordsets();
        
        assertEquals(2, recordsets.size());
        assertEquals(recordset1, recordsets.get(recordset1.getKey()));
        assertEquals(recordset2, recordsets.get(recordset2.getKey()));
        
    }

    @Test
    public void noIndexTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);
        
        XsdSection liveSection = entity.getSection("TREXMission/Live Status Section");
        
        assertEquals(true, liveSection.getNoIndex());

        XsdSection informationSection = entity.getSection("TREXMission/Mission Information Section");
        
        assertEquals(false, informationSection.getNoIndex());
        
        XsdEntity newEntity = XsdEntity.create("Operation", "Portal", "1.2.3.4", "ID", "Type");
        XsdSection newSection = XsdSection.create(newEntity, "Operation/New Section");
                
        assertEquals(false, newSection.getNoIndex());
          
    }

    @Test
    public void DateCreatedTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);
        
        XsdSection liveSection = entity.getSection("TREXMission/Live Status Section");
        
        assertEquals(JodaDateTimeHelper.FromXmlDateTimeUTC("2014-05-02T14:33:51.851575Z"), liveSection.getDateCreated());

        DateTime now = JodaDateTimeHelper.NowInUtc();
        liveSection.setDateCreated(now);

        assertEquals(now, liveSection.getDateCreated());

    }

    @Test
    public void LastModifiedTest()
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);
        
        XsdSection liveSection = entity.getSection("TREXMission/Live Status Section");
        
        assertEquals(JodaDateTimeHelper.FromXmlDateTimeUTC("2014-05-02T14:33:59.1309914Z"), liveSection.getLastModified());

        DateTime now = JodaDateTimeHelper.NowInUtc();
        liveSection.setLastModified(now);

        assertEquals(now, liveSection.getLastModified());

    }


}
