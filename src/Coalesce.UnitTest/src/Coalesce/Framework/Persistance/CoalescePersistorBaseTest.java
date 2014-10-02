package Coalesce.Framework.Persistance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.junit.After;
import org.junit.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import unity.connector.local.LocalConfigurationsConnector;
import Coalesce.Common.Exceptions.CoalesceException;
import Coalesce.Common.Exceptions.CoalescePersistorException;
import Coalesce.Common.Helpers.GUIDHelper;
import Coalesce.Common.Helpers.StringHelper;
import Coalesce.Common.Runtime.CoalesceSettings;
import Coalesce.Common.UnitTest.CoalesceAssert;
import Coalesce.Common.UnitTest.CoalesceTypeInstances;
import Coalesce.Framework.CoalesceFramework;
import Coalesce.Framework.DataModel.CoalesceEntityTemplate;
import Coalesce.Framework.DataModel.ECoalesceFieldDataTypes;
import Coalesce.Framework.DataModel.XsdDataObject;
import Coalesce.Framework.DataModel.XsdEntity;
import Coalesce.Framework.DataModel.XsdField;
import Coalesce.Framework.DataModel.XsdFieldDefinition;
import Coalesce.Framework.DataModel.XsdLinkageSection;
import Coalesce.Framework.DataModel.XsdRecord;
import Coalesce.Framework.DataModel.XsdRecordset;
import Coalesce.Framework.DataModel.XsdSection;
import Coalesce.Framework.Persistance.ICoalescePersistor.EntityMetaData;

/**
 * Provides an extendable JUnit base class for testing the interface logic defined in {@link Coalesce.Framework.Persistance.ICoalescePersistor}. All
 * classes that extend this JUnit base class must provide {@link Coalesce.Framework.Persistance.ServerConn} and {@link Coalesce.Framework.Persistance.ICoalescePersistor} instances that
 * the base class will use to run the interface tests against. The following methods must be implemented by the subclass in
 * addition to all abstract methods.
 * 
 * <pre>
 * 
 * 
 * &#064;BeforeClass
 * public static void setUpBeforeClass() throws CoalesceException
 * {
 * 
 *     &lt;SubclassPersistor&gt; tester = new &lt;SubclassPersistor&gt;();
 * 
 *     CoalescePersisterBaseTest.setupBeforeClassBase(tester);
 * 
 * }
 * 
 * &#064;AfterClass
 * public static void tearDownAfterClass()
 * {
 *     &lt;SubclassPersistor&gt; tester = new &lt;SubclassPersistor&gt;();
 * 
 *     CoalescePersisterBaseTest.tearDownAfterClassBase(tester);
 * 
 * }
 * </pre>
 * 
 * @author InCadence
 *
 */
public abstract class CoalescePersistorBaseTest {

    /*--------------------------------------------------------------------------
    Private Variables
    --------------------------------------------------------------------------*/

    private static ServerConn _serCon;
    private static CoalesceFramework _coalesceFramework;

    private static XsdEntity _entity;
    private static String _entityXml;
    private static String _fieldKey;

    private static String _testTemplateKey = null;

    /*--------------------------------------------------------------------------
    JUnit Before/After functions
    --------------------------------------------------------------------------*/

    protected abstract ServerConn getConnection();

    protected abstract ICoalescePersistor getPersistor(ServerConn conn);

    protected abstract CoalesceDataConnectorBase getDataConnector(ServerConn conn) throws CoalescePersistorException;

    protected static void setupBeforeClassBase(CoalescePersistorBaseTest tester) throws CoalesceException
    {
        CoalesceSettings.initialize(new LocalConfigurationsConnector());

        _serCon = tester.getConnection();

        ICoalescePersistor persistor = tester.getPersistor(_serCon);

        _coalesceFramework = new CoalesceFramework();
        _coalesceFramework.Initialize(persistor);

        CoalescePersistorBaseTest.cleanUpDatabase(tester);

        _entity = CoalescePersistorBaseTest.createEntity();
        _entityXml = _entity.toXml();

        _fieldKey = CoalescePersistorBaseTest.getCurrentStatusField(_entity).getKey();

        assertTrue(_coalesceFramework.SaveCoalesceEntity(_entity));

    }

    protected static void tearDownAfterClassBase(CoalescePersistorBaseTest tester)
    {

        CoalescePersistorBaseTest.cleanUpDatabase(tester);

        _serCon = null;
        _coalesceFramework = null;

    }

    private static void cleanUpDatabase(CoalescePersistorBaseTest tester)
    {
        try (CoalesceDataConnectorBase conn = tester.getDataConnector(_serCon))
        {
            List<String> objectKeys = _coalesceFramework.GetCoalesceEntityKeysForEntityId("EntityId",
                                                                                          "EntityIdType",
                                                                                          "TestEntity",
                                                                                          "Unit Test");

            for (String objectKey : objectKeys)
            {
                deleteEntity(conn, objectKey);
            }

            XsdEntity mission = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

            deleteEntity(conn, mission.getKey());

        }
        catch (Exception e)
        {
        }

    }

    @After
    public void tearDown()
    {
        if (!StringHelper.isNullOrEmpty(_testTemplateKey))
        {
            try (CoalesceDataConnectorBase conn = getDataConnector(_serCon))
            {
                conn.ExecuteCmd("delete from CoalesceEntityTemplate where TemplateKey = '" + _testTemplateKey + "'");
            }
            catch (Exception e)
            {
            }
            finally
            {
                _testTemplateKey = null;
            }
        }
    }

    /*--------------------------------------------------------------------------
    Tests
    --------------------------------------------------------------------------*/

    @Test
    public void testConnection() throws CoalescePersistorException, Exception
    {
        try (CoalesceDataConnectorBase conn = getDataConnector(_serCon))
        {
            conn.openConnection();
        }
    }

    protected abstract ServerConn getInvalidConnection();

    @Test(expected = SQLException.class)
    public void testFAILConnection() throws SQLException, Exception, CoalescePersistorException
    {
        ServerConn serConFail = getInvalidConnection();

        try (CoalesceDataConnectorBase conn = getDataConnector(serConFail))
        {
            conn.openConnection();
        }
    }

    @Test
    public void testSaveMissionEntity() throws CoalescePersistorException
    {
        XsdEntity entity = XsdEntity.create(CoalesceTypeInstances.TEST_MISSION);

        assertTrue(_coalesceFramework.SaveCoalesceEntity(entity));

    }

    @Test
    public void testSaveEntityAndXPath() throws CoalescePersistorException
    {
        // assertTrue(_coalesceFramework.SaveCoalesceEntity(_entity));

        // Get Field from DB Using XPath
        XsdField field = _coalesceFramework.GetCoalesceFieldByFieldKey(_fieldKey);
        assertNotNull(field);

        // Get Record from Entity
        XsdRecord record = (XsdRecord) field.getParent();
        assertNotNull(record);

        // Get Record from DB Using XPath
        XsdRecord recordDB = _coalesceFramework.GetCoalesceRecord(record.getKey());
        assertNotNull(recordDB);
        assertEquals(record.getName(), recordDB.getName());

    }

    @Test
    public void testEntityTemplate() throws SAXException, IOException, CoalescePersistorException
    {
        CoalesceEntityTemplate template = testTemplate(CoalesceEntityTemplate.create(_entity));
        assertTrue(_coalesceFramework.SaveCoalesceEntityTemplate(template));

        // String templateXml = template.toXml().replace("UTF-8", "UTF-16");
        String templateXml = template.toXml();

        String templateKey = _coalesceFramework.GetCoalesceEntityTemplateKey(_entity.getName(),
                                                                             _entity.getSource(),
                                                                             _entity.getVersion());
        assertFalse(StringHelper.isNullOrEmpty(templateKey));

        _testTemplateKey = templateKey;

        String templateXmlFromKey = _coalesceFramework.GetCoalesceEntityTemplateXml(templateKey);

        CoalesceAssert.assertXmlEquals(templateXml, templateXmlFromKey.replace("UTF-16", "UTF-8"), "UTF-8");

        String templateXmlFromAttr = _coalesceFramework.GetCoalesceEntityTemplateXml(_entity.getName(),
                                                                                     _entity.getSource(),
                                                                                     _entity.getVersion());
        CoalesceAssert.assertXmlEquals(templateXml, templateXmlFromAttr.replace("UTF-16", "UTF-8"), "UTF-8");

    }

    @Test
    public void testGetEntityMetaData() throws CoalescePersistorException
    {
        EntityMetaData objectKey = _coalesceFramework.GetCoalesceEntityIdAndTypeForKey(_entity.getKey());

        assertEquals(_entity.getEntityId(), objectKey.entityId);
        assertEquals(_entity.getKey().toUpperCase(), objectKey.entityKey.toUpperCase());
        assertEquals(_entity.getEntityIdType(), objectKey.entityType);
    }

    @Test
    public void testGetEntity() throws CoalescePersistorException
    {
        XsdEntity ent = _coalesceFramework.GetCoalesceEntity(_entity.getKey());

        CoalesceAssert.assertXmlEquals(_entityXml, ent.toXml(), "UTF-8");
    }

    @Test
    public void testCheckLastModified() throws CoalescePersistorException
    {
        // Test Entity
        DateTime lastModified = _coalesceFramework.GetCoalesceEntityLastModified(_entity.getKey(), "entity");
        assertEquals(lastModified, _entity.getLastModified());

        // Test Section
        XsdSection section = _entity.getSection("TestEntity/Live Status Section");

        lastModified = null;
        lastModified = _coalesceFramework.GetCoalesceEntityLastModified(section.getKey(), "section");
        assertEquals(lastModified, section.getLastModified());

    }

    //
    // @Test
    // public void testFAILCheckLastModified()
    // {
    // try
    // {
    // DateTime last_t Entity
    // lastModified = _coalesceFramework.GetCoalesceEntityLastModified(_entity.getKey(),
    // "linkage");
    // assertTrue(DateTimeComparator.getInstance().compare(lastModified, _entity.getLastModified()) == 1);
    // }
    // catch (Exception ex)
    // {
    // fail(ex.getMessage());
    // }
    // }
    //
    @Test
    public void testFAILCheckLastModified() throws CoalescePersistorException
    {
        DateTime lastModified;

        // Test Entity
        lastModified = CoalescePersistorBaseTest._coalesceFramework.GetCoalesceEntityLastModified(_entity.getKey(),
                                                                                                  "linkage");
        assertTrue(DateTimeComparator.getInstance().compare(lastModified, _entity.getLastModified()) == 1);

    }

    @Test
    public void testGetEntityByIdAndType() throws CoalescePersistorException
    {
        XsdEntity ent = _coalesceFramework.GetEntity(_entity.getEntityId(), _entity.getEntityIdType());

        CoalesceAssert.assertXmlEquals(_entityXml, ent.toXml(), "UTF-8");
    }

    @Test
    public void testGetEntityByNameAndIdAndType() throws CoalescePersistorException
    {
        XsdEntity ent = _coalesceFramework.GetEntity(_entity.getName(), _entity.getEntityId(), _entity.getEntityIdType());

        CoalesceAssert.assertXmlEquals(_entityXml, ent.toXml(), "UTF-8");
    }

    @Test
    public void testGetFieldValue() throws CoalescePersistorException
    {
        String fieldValue = _coalesceFramework.GetCoalesceFieldValue(_fieldKey);

        assertEquals("Test Status", fieldValue);
    }

    @Test
    public void testFAILGetFieldValue() throws CoalesceException
    {
        // Create a new entity, but do not save the entity
        XsdEntity entity = CoalescePersistorBaseTest.createEntity();

        String fieldValue = _coalesceFramework.GetCoalesceFieldValue(CoalescePersistorBaseTest.getCurrentStatusField(entity).getKey());

        assertNull(fieldValue);

    }

    @Test
    public void testGetEntityKeyForEntityId() throws CoalescePersistorException
    {
        String objectKey = _coalesceFramework.GetCoalesceEntityKeyForEntityId(_entity.getEntityId(),
                                                                              _entity.getEntityIdType(),
                                                                              _entity.getName());

        assertEquals(_entity.getKey().toUpperCase(), objectKey.toUpperCase());

    }

    public void testFAILGetEntityKeyForEntityId() throws CoalescePersistorException
    {

        String objectKey = _coalesceFramework.GetCoalesceEntityKeyForEntityId("", "", "");

        assertNull(objectKey);

    }

    @Test
    public void testGetEntityKeyForEntityIdName() throws CoalescePersistorException
    {
        List<String> objectKey = _coalesceFramework.GetCoalesceEntityKeysForEntityId(_entity.getEntityId(),
                                                                                     _entity.getEntityIdType(),
                                                                                     _entity.getName(),
                                                                                     _entity.getSource());
        assertFalse(objectKey.isEmpty());
    }

    @Test
    public void testFAILGetEntityKeyForEntityIdName() throws CoalescePersistorException
    {
        List<String> objectKey = _coalesceFramework.GetCoalesceEntityKeysForEntityId("", "", "", "");
        assertTrue(objectKey.isEmpty());
    }

    @Test
    public void testGetEntityKeysForEntityIdSource() throws CoalescePersistorException
    {
        List<String> objectKeys = _coalesceFramework.GetCoalesceEntityKeysForEntityId(_entity.getEntityId(),
                                                                                      _entity.getEntityIdType(),
                                                                                      _entity.getName());
        assertFalse(objectKeys.isEmpty());

        boolean found = false;
        for (String objectKey : objectKeys)
        {
            if (objectKey.equalsIgnoreCase(_entity.getKey()))
            {
                found = true;
                break;
            }
        }

        assertTrue("Object key not found in key list", found);
    }

    @Test
    public void testFAILGetEntityKeyForEntityIdSource() throws CoalescePersistorException
    {
        List<String> objectKeys = _coalesceFramework.GetCoalesceEntityKeysForEntityId("", "", "");
        assertTrue(objectKeys.isEmpty());
    }

    @Test
    public void testGetEntityKeyForEntityIdSource() throws CoalescePersistorException
    {
        String objectKey = _coalesceFramework.GetCoalesceEntityKeyForEntityId(_entity.getEntityId(),
                                                                              _entity.getEntityIdType(),
                                                                              _entity.getName(),
                                                                              _entity.getSource());
        assertEquals(_entity.getKey().toUpperCase(), objectKey.toUpperCase());

    }

    /*--------------------------------------------------------------------------
    Private Methods
    --------------------------------------------------------------------------*/

    private static XsdEntity createEntity() throws CoalesceException
    {
        XsdEntity entity = XsdEntity.create("TestEntity", "Unit Test", "1.0.0.0", "EntityId", "EntityIdType", "");

        XsdLinkageSection.create(entity, true);

        XsdSection section = XsdSection.create(entity, "Live Status Section", true);
        XsdRecordset recordSet = XsdRecordset.create(section, "Live Status Recordset");
        XsdFieldDefinition.create(recordSet, "CurrentStatus", ECoalesceFieldDataTypes.StringType);

        XsdRecord record = recordSet.addNew();
        record.setFieldValue("CurrentStatus", "Test Status");

        return entity;

    }

    private static XsdField getCurrentStatusField(XsdEntity entity)
    {
        XsdField field = (XsdField) entity.getDataObjectForNamePath("TestEntity/Live Status Section/Live Status Recordset/Live Status Recordset Record/CurrentStatus");

        return field;

    }

    private static void deleteEntity(CoalesceDataConnectorBase conn, String objectKey) throws CoalescePersistorException
    {
        String entityXml = _coalesceFramework.GetEntityXml(objectKey);

        XsdEntity entity = XsdEntity.create(entityXml);

        CoalescePersistorBaseTest.deleteEntity(conn, entity);

    }

    private static void deleteEntity(CoalesceDataConnectorBase conn, XsdDataObject xdo)
    {
        String table = "";

        switch (xdo.getType()) {
        case "entity":

            table = "CoalesceEntity";
            break;

        case "section":

            table = "CoalesceSection";
            break;

        case "recordset":

            table = "CoalesceRecordSet";
            break;

        case "fielddefinition":

            table = "CoalesceFieldDefinition";
            break;

        case "record":

            table = "CoalesceRecord";
            break;

        case "field":

            table = "CoalesceField";
            break;

        case "fieldhistory":

            table = "CoalesceFieldHistory";
            break;

        case "linkagesection":

            table = "CoalesceLinkageSection";
            break;

        case "linkage":

            table = "CoalesceLinkage";

        }

        if (!StringHelper.isNullOrEmpty(table))
        {
            CoalescePersistorBaseTest.pergeTestRecords(conn, table, xdo.getKey());
        }

        for (XsdDataObject child : xdo.getChildDataObjects().values())
        {
            CoalescePersistorBaseTest.deleteEntity(conn, child);
        }
    }

    private static void pergeTestRecords(CoalesceDataConnectorBase conn, String tableName, String key)
    {
        try
        {
            conn.ExecuteCmd("delete from " + tableName + " where ObjectKey = '" + key + "'");
        }
        catch (SQLException e)
        {
        }
    }

    private static CoalesceEntityTemplate testTemplate(CoalesceEntityTemplate template)
    {
        String templateXml = template.toXml();
        // System.out.println("Template: ");
        // System.out.println("*********\n" + templateXml);

        // Confirm Template
        assertFalse(StringHelper.isNullOrEmpty(templateXml));
        assertEquals("TestEntity", template.getName());
        assertEquals("Unit Test", template.getSource());
        assertEquals("1.0.0.0", template.getVersion());

        // Confirm Values
        NodeList nodeList = template.getDataObjectDocument().getElementsByTagName("*");

        for (int jj = 0; jj < nodeList.getLength(); jj++)
        {
            Node node = nodeList.item(jj);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                NamedNodeMap attributeList = node.getAttributes();

                for (int ii = 0; ii < attributeList.getLength(); ii++)
                {

                    Node attribute = attributeList.item(ii);

                    if (attribute.getNodeName().equalsIgnoreCase("name")
                            || attribute.getNodeName().equalsIgnoreCase("source")
                            || attribute.getNodeName().equalsIgnoreCase("version"))
                    {
                        assertNotNull(attribute.getNodeValue());
                    }
                    else
                    {
                        assertTrue(StringHelper.isNullOrEmpty(attribute.getNodeValue()));
                    }
                }
            }
        }

        // Create Entity from Template
        XsdEntity entity2 = template.createNewEntity();

        String entityXml = entity2.toXml();
        // System.out.println("Copy of Entity made from Template: " + entity2.getKey());
        // System.out.println("**********************************\n" + entityXml);

        // Confirm Entity
        assertFalse("Entity not generated", StringHelper.isNullOrEmpty(entityXml));
        assertTrue("Not a valid GUID", GUIDHelper.isValid(entity2.getKey()));

        assertEquals("TestEntity", entity2.getName());
        assertEquals("Unit Test", entity2.getSource());
        assertEquals("1.0.0.0", entity2.getVersion());

        return template;

    }
}