package com.incadencecorp.coalesce.framework.datamodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.incadencecorp.coalesce.common.helpers.StringHelper;
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

public class CoalesceEntitySyncShell {

    // -----------------------------------------------------------------------//
    // protected Member Variables
    // -----------------------------------------------------------------------//

    private Document _document;
    private Node _entityNode;

    // -----------------------------------------------------------------------//
    // Static Creates
    // -----------------------------------------------------------------------//

    /**
     * Creates a {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} based of an
     * {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity}.
     * 
     * @param entity The {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity} to create the shell from
     * @return The new {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}
     * 
     * @throws SAXException
     * @throws IOException
     */
    public static CoalesceEntitySyncShell create(CoalesceEntity entity) throws SAXException, IOException
    {
        return CoalesceEntitySyncShell.create(entity.toXml());
    }

    /**
     * Creates a {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} based of an (XML) String.
     * 
     * @param entitySyncShellXml (XML) String of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity} to
     *            create the shell from
     * @return The new {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}
     * 
     * @throws SAXException
     * @throws IOException
     */
    public static CoalesceEntitySyncShell create(String entitySyncShellXml) throws SAXException, IOException
    {
        return CoalesceEntitySyncShell.create(XmlHelper.loadXmlFrom(entitySyncShellXml));
    }

    /**
     * Creates a {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} based of an org.w3c.dom
     * Document.
     * 
     * @param doc org.w3c.dom Document Document of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity}
     *            to create the shell from
     * @return The new {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}
     * 
     * @throws SAXException
     * @throws IOException
     */
    public static CoalesceEntitySyncShell create(Document doc) throws SAXException, IOException
    {
        // Create a new CoalesceEntityTemplate
        CoalesceEntitySyncShell entitySyncShell = new CoalesceEntitySyncShell();

        // Initialize
        if (!entitySyncShell.initialize(doc)) return null;

        // return
        return entitySyncShell;
    }

    // -----------------------------------------------------------------------//
    // Initialization
    // -----------------------------------------------------------------------//

    /**
     * Initializes a previously new {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} based off
     * an {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity}.
     * 
     * @param entity {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity} to initialize the shell from
     * @return boolean indicating success/failure
     * 
     * @throws SAXException
     * @throws IOException
     */
    public boolean initialize(CoalesceEntity entity) throws SAXException, IOException
    {
        return this.initialize(entity.toXml());
    }

    /**
     * Initializes a previously new {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} based off
     * an (XML) String.
     * 
     * @param entityXml String (XML) String of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity} to
     *            initialize the shell from
     * @return boolean indicating success/failure
     * 
     * @throws SAXException
     * @throws IOException
     */
    public boolean initialize(String entityXml) throws SAXException, IOException
    {
        return this.initialize(XmlHelper.loadXmlFrom(entityXml));
    }

    /**
     * Initializes a previously new {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} based off
     * an org.w3c.dom Document.
     * 
     * @param doc org.w3c.dom Document Document of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity}
     *            to create the shell from
     * @return boolean indicating success/failure
     */
    public boolean initialize(Document doc)
    {
        // Prune Nodes
        CoalesceEntitySyncShell.pruneNodes(doc);

        // Set Document
        this.setCoalesceObjectDocument(doc);

        // return Success
        return true;
    }

    // -----------------------------------------------------------------------//
    // public Properties
    // -----------------------------------------------------------------------//

    /**
     * Returns the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}'s (org.w3c.dom Document)
     * Document.
     * 
     * @return org.w3c.dom Document of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}
     */
    public Document getCoalesceObjectDocument()
    {
        return this._document;
    }

    /**
     * Sets the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}'s (org.w3c.dom Document)
     * Document and EntityNode.
     * 
     * @param value org.w3c.dom Document Document to assign to the
     *            {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}
     */
    public void setCoalesceObjectDocument(Document value)
    {
        this._document = value;
        this._entityNode = value.getElementsByTagName("entity").item(0);
    }

    /**
     * Returns the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}'s (org.w3c.dom Node)
     * EntityNode.
     * 
     * @return org.w3c.dom Node of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}
     */
    public Node getEntityNode()
    {
        return this._entityNode;
    }

    /**
     * Sets the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}'s (org.w3c.dom Node)
     * EntityNode.
     * 
     * @param value org.w3c.dom Node to assign to the
     *            {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}
     * @throws ParserConfigurationException
     */
    public void setEntityNode(Node value) throws ParserConfigurationException
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        this._document = builder.newDocument();
        Node importedNode = this._document.importNode(value, true);
        this._document.appendChild(importedNode);

        this._entityNode = value;
    }

    /**
     * Returns the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}'s (XML) String of the
     * Document.
     * 
     * @return (XML) String of the Document.
     */
    public String toXml()
    {
        return XmlHelper.formatXml(this._document);
    }

    // -----------------------------------------------------------------------//
    // public Static Functions
    // -----------------------------------------------------------------------//

    /**
     * Creates and returns a clone/copy of the {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell}
     * passed in as a param.
     * 
     * @param syncShell {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} (original).
     * @return {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} (clone/copy).
     */
    public static CoalesceEntitySyncShell clone(CoalesceEntitySyncShell syncShell)
    {
        // Create new Instance
        CoalesceEntitySyncShell syncShellClone = new CoalesceEntitySyncShell();

        // Initialize
        // TODO: make sure .Clone's are same between vb and java. Java required a boolean.
        syncShellClone.initialize((Document) syncShell.getCoalesceObjectDocument().cloneNode(true));
        return syncShellClone;
    }

    /**
     * Returns a {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} containing the
     * LocalFullSyncShell changed nodes from the RemoteFullSyncShell.
     * 
     * @param localFullSyncShell {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} local copy.
     * @param remoteFullSyncShell {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} original.
     * @return {@link com.incadencecorp.coalesce.framework.datamodel.CoalesceEntitySyncShell} local copy's changes from the
     *         original.
     */
    public static CoalesceEntitySyncShell getRequiredChangesSyncShell(CoalesceEntitySyncShell localFullSyncShell,
                                                                      CoalesceEntitySyncShell remoteFullSyncShell)
    {
        // Create the RequiredChangesSyncShell as a Clone of the RemoteFullSyncShell. We will
        // then prune out nodes that aren't required recursively as we compare against
        // the nodes in LocalFullSyncShell.
        CoalesceEntitySyncShell requiredChangesSyncShell = CoalesceEntitySyncShell.clone(remoteFullSyncShell);

        if (requiredChangesSyncShell.equals(null)) return requiredChangesSyncShell;

        // Prune Unchanged Nodes
        CoalesceEntitySyncShell.pruneUnchangedNodes(CoalesceEntitySyncShell.generateMap(localFullSyncShell.getCoalesceObjectDocument()),
                                                    remoteFullSyncShell.getCoalesceObjectDocument(),
                                                    CoalesceEntitySyncShell.generateMap(requiredChangesSyncShell.getCoalesceObjectDocument()));

        return requiredChangesSyncShell;
    }

    // ----------------------------------------------------------------------//
    // Private Static Functions
    // ----------------------------------------------------------------------//

    /**
     * Remove nodes that do not require an update; called by GetRequiredChangesSyncShell.
     * 
     * @param nodeToPrune Node to check for updates.
     * @return boolean indicator of success/failure.
     */
    private static boolean pruneNodes(Node nodeToPrune)
    {
        try
        {
            boolean isSuccess = false;

            // TODO: Make sure the Node/Attribute "switch" is ok
            // Prune Unnecessary Attributes
            if (nodeToPrune.getAttributes() != null)
            {
                if (nodeToPrune.getAttributes().getLength() > 0)
                {
                    ArrayList<String> removeList = new ArrayList<String>();
                    NamedNodeMap attributeList = nodeToPrune.getAttributes();

                    // Find Attributes to Remove
                    for (int i = 0; i < attributeList.getLength(); i++)
                    {
                        // Get Attribute Name
                        String attributeName = attributeList.item(i).getNodeName();

                        switch (attributeName.toUpperCase()) {
                        case "KEY":
                        case "LASTMODIFIED":
                        case "HASH":
                            // Keep
                            break;
                        default:
                            // Mark for Deletion
                            removeList.add(attributeName);
                        }
                    }

                    // Remove Attributes
                    for (String attributeName : removeList)
                    {
                        attributeList.removeNamedItem(attributeName);
                    }
                }
            }

            // Prune Unnecessary Nodes
            if (nodeToPrune.hasChildNodes())
            {
                if (nodeToPrune.getChildNodes().getLength() > 0)
                {
                    ArrayList<Node> removeList = new ArrayList<Node>();
                    NodeList children = nodeToPrune.getChildNodes();

                    // Find Nodes to Remove
                    // for (Node ChildNode : NodeToPrune.getChildNodes()){
                    for (int i = 0; i < children.getLength(); i++)
                    {
                        Node childNode = children.item(i);

                        switch (childNode.getNodeType()) {
                        case Node.ELEMENT_NODE:
                            // Keep
                            break;
                        default:
                            // Add to Remove List
                            removeList.add(childNode);
                        }
                    }

                    // Remove
                    for (Node childNode : removeList)
                    {
                        nodeToPrune.removeChild(childNode);
                    }
                }
            }

            // Recurse Child Nodes
            for (int i = 0; i < nodeToPrune.getChildNodes().getLength(); i++)
            {
                Node childNode = nodeToPrune.getChildNodes().item(i);
                isSuccess = pruneNodes(childNode);
            }

            // return Success
            return isSuccess;

        }
        catch (Exception ex)
        {
            // return Failed Error
            return false;
        }
    }

    private static void pruneUnchangedNodes(HashMap<String, Node> localNodes,
                                            Node remoteSyncShellNode,
                                            HashMap<String, Node> requiredNodes)
    {
        /*
         * Recurse Child Nodes (Important: Because this us up front, we check leaf nodes first, which is necessary for
         * correct pruning. We rely on whether or not a node has children remaining as one of the decision points on whether
         * or not the node itself needs to remain.)
         */
        NodeList children = remoteSyncShellNode.getChildNodes();

        for (int ii = 0; ii < children.getLength(); ii++)
        {
            // Recursive
            CoalesceEntitySyncShell.pruneUnchangedNodes(localNodes, children.item(ii), requiredNodes);
        }

        // Check RemoteSyncShellNode
        String key = XmlHelper.getAttribute(remoteSyncShellNode, "key");

        if (!StringHelper.isNullOrEmpty(key))
        {
            // Evaluate Based on the Coalesce Object Type
            switch (remoteSyncShellNode.getNodeName().toUpperCase()) {
            case "FIELD":
            case "LINKAGE":
            case "FIELDHISTORY":
            case "FIELDDEFINITION":
                if (!CoalesceEntitySyncShell.isNewer(localNodes.get(key), remoteSyncShellNode))
                {
                    /*
                     * Local is newer or the same date; Prune from the RequiredChangesSyncShell IF there are no remaining
                     * children below the node. If there are children, then we keep the node even if it's older.
                     */
                    CoalesceEntitySyncShell.pruneNode(requiredNodes.get(key));
                }
                break;
            case "LINKAGESECTION":
            case "RECORD":
            case "RECORDSET":
            case "SECTION":
            case "ENTITY":
            default:
                /*
                 * For these Coalesce Objects, we will check the RequiredChangesSyncShell for the presence of this object's
                 * node. If the node is present, and it still has children, then we will keep the node in the
                 * RequiredChangesSyncShell. Since we prune leaves first, and work our way up the tree to the base Entity
                 * node, the presence of child nodes means that a child object to this object required updating, therefore we
                 * have to keep this object's node. If there are no child nodes, then we can prune this object's node.
                 */
                CoalesceEntitySyncShell.pruneNode(requiredNodes.get(key));
                break;
            }
        }

    }

    private static void pruneNode(Node node)
    {
        if (node != null && node.getParentNode() != null && !node.hasChildNodes())
        {
            node.getParentNode().removeChild(node);
        }
    }

    private static boolean isNewer(Node oldNode, Node newNode)
    {
        boolean isNewer = true;

        if (oldNode != null)
        {
            DateTime oldModified = XmlHelper.getAttributeAsDate(oldNode, "lastmodified");
            DateTime newLastModified = XmlHelper.getAttributeAsDate(newNode, "lastmodified");

            switch (oldModified.compareTo(newLastModified)) {
            case 0:
            case 1:
                isNewer = false;
                break;
            default:
                isNewer = true;
                break;
            }
        } // Else; Remote Node not found in LocalFullSyncShell; Keep in the RequiredChangesSyncShell

        return isNewer;
    }

    private static HashMap<String, Node> generateMap(Document doc)
    {

        HashMap<String, Node> nodeMap = new HashMap<String, Node>();

        NodeList nodeList = doc.getElementsByTagName("*");

        for (int jj = 0; jj < nodeList.getLength(); jj++)
        {
            Node node = nodeList.item(jj);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                String nodeKey = XmlHelper.getAttribute(node, "key");

                if (!StringHelper.isNullOrEmpty(nodeKey))
                {
                    nodeMap.put(nodeKey, node);
                }
            }
        }

        return nodeMap;

    }

}
