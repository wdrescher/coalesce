package com.incadencecorp.coalesce.framework.datamodel;

import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.NullArgumentException;

import com.incadencecorp.coalesce.common.classification.Marking;
import com.incadencecorp.coalesce.common.helpers.LocaleConverter;

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

/**
 * This class represents the association between two Coalesce Entities.
 * 
 * @author Derek C.
 */
public class CoalesceLinkage extends CoalesceObjectHistory implements ICoalesceLinkage {

    /**
     * Default name for linkage elements.
     */
    public static final String NAME = "Linkage";

    private Linkage _entityLinkage;

    /**
     * Creates an {@link CoalesceLinkage} and ties it to its parent
     * {@link CoalesceLinkageSection} .
     * 
     * @param parent {@link CoalesceLinkageSection} , the linkage section that
     *            this new linkage will belong to
     * @return {@link CoalesceLinkage} , the new linkage to describe a
     *         relationship between two classes
     */
    public static CoalesceLinkage create(CoalesceLinkageSection parent)
    {
        if (parent == null)
            throw new NullArgumentException("parent");

        CoalesceLinkage newLinkage = new CoalesceLinkage();

        Linkage entityLinkage = new Linkage();
        parent.getEntityLinkageSection().getLinkage().add(entityLinkage);

        if (!newLinkage.initialize(parent, entityLinkage))
            return null;

        newLinkage.setName(NAME);

        parent.addChildCoalesceObject(newLinkage);

        return newLinkage;

    }

    /**
     * Initializes a previously new {@link CoalesceLinkage} and ties it to its
     * parent {@link CoalesceLinkageSection} .
     * 
     * @param parent {@link CoalesceLinkageSection} , the linkage section that
     *            this new linkage will belong to
     * @param linkage Linkage, the linkage describing a relationship between two
     *            classes
     * @return boolean indicator of success/failure
     */
    protected boolean initialize(CoalesceLinkageSection parent, Linkage linkage)
    {
        if (parent == null)
            throw new NullArgumentException("parent");
        if (linkage == null)
            throw new NullArgumentException("linkage");

        setParent(parent);
        _entityLinkage = linkage;

        return super.initialize(_entityLinkage);
    }

    // -----------------------------------------------------------------------//
    // public Properties
    // -----------------------------------------------------------------------//

    @Override
    public Marking getClassificationMarking()
    {
        return new Marking(_entityLinkage.getClassificationmarking());
    }

    @Override
    public void setClassificationMarking(Marking value)
    {
        _entityLinkage.setClassificationmarking(value.toPortionString());
        updateLastModified();
    }

    @Override
    public String getEntity1Key()
    {
        return _entityLinkage.getEntity1Key();
    }

    @Override
    public void setEntity1Key(String value)
    {
        _entityLinkage.setEntity1Key(value);
        updateLastModified();
    }

    @Override
    public String getEntity1Name()
    {
        return _entityLinkage.getEntity1Name();
    }

    @Override
    public void setEntity1Name(String value)
    {
        _entityLinkage.setEntity1Name(value);
        updateLastModified();
    }

    @Override
    public String getEntity1Source()
    {
        return _entityLinkage.getEntity1Source();
    }

    @Override
    public void setEntity1Source(String value)
    {
        _entityLinkage.setEntity1Source(value);
        updateLastModified();
    }

    @Override
    public String getEntity1Version()
    {
        return _entityLinkage.getEntity1Version();
    }

    @Override
    public void setEntity1Version(String value)
    {
        _entityLinkage.setEntity1Version(value);
        updateLastModified();
    }

    @Override
    public String getEntity2Key()
    {
        return _entityLinkage.getEntity2Key();
    }

    @Override
    public void setEntity2Key(String value)
    {
        _entityLinkage.setEntity2Key(value);
        updateLastModified();
    }

    @Override
    public String getEntity2Name()
    {
        return _entityLinkage.getEntity2Name();
    }

    @Override
    public void setEntity2Name(String value)
    {
        _entityLinkage.setEntity2Name(value);
        updateLastModified();
    }

    @Override
    public String getEntity2Source()
    {
        return _entityLinkage.getEntity2Source();
    }

    @Override
    public void setEntity2Source(String value)
    {
        _entityLinkage.setEntity2Source(value);
        updateLastModified();
    }

    @Override
    public String getEntity2Version()
    {
        return _entityLinkage.getEntity2Version();
    }

    @Override
    public void setEntity2Version(String value)
    {
        _entityLinkage.setEntity2Version(value);
        updateLastModified();
    }

    @Override
    public int getEntity2ObjectVersion()
    {
        return _entityLinkage.getEntity2Objectversion();
    }

    @Override
    public void setEntity2ObjectVersion(int value)
    {
        _entityLinkage.setEntity2Objectversion(value);
    }

    @Override
    public Locale getInputLang()
    {
        return _entityLinkage.getInputlang();
    }

    @Override
    public void setInputLang(Locale value)
    {
        _entityLinkage.setInputlang(value);
        updateLastModified();
    }

    @Override
    public ELinkTypes getLinkType()
    {
        return ELinkTypes.getTypeForLabel(_entityLinkage.getLinktype());
    }

    @Override
    public void setLinkType(ELinkTypes value)
    {
        _entityLinkage.setLinktype(value.getLabel());
        updateLastModified();
    }

    @Override
    public String getLabel()
    {
        return _entityLinkage.getLabel();
    }

    @Override
    public void setLabel(String value)
    {
        _entityLinkage.setLabel(value);
    }

    // -----------------------------------------------------------------------//
    // public Methods
    // -----------------------------------------------------------------------//

    /**
     * Sets the two entities key, name, source and version as well as the link
     * type, classification, modified by, input language, dates created and
     * modified and active status.
     * 
     * @param entity1 {@link CoalesceEntity} belonging to the first entity.
     *            Provides the entity's key, name, source and version
     * @param linkType ELinkTypes value for the relationship type identification
     *            between the entities
     * @param entity2 {@link CoalesceEntity} belonging to the second entity.
     *            Provides the entity's key, name, source and version
     * @param classificationMarking Marking of the classification of the
     *            relationship
     * @param modifiedBy identification of who entered the relationship
     * @param modifiedByIP
     * @param label
     * @param inputLang language that the relationship was created in
     * @param isReadOnly
     */
    public void establishLinkage(CoalesceEntity entity1,
                                 ELinkTypes linkType,
                                 CoalesceEntity entity2,
                                 Marking classificationMarking,
                                 String modifiedBy,
                                 String modifiedByIP,
                                 String label,
                                 Locale inputLang,
                                 boolean isReadOnly)
    {

        establishLinkage(entity1,
                         linkType,
                         entity2.getKey(),
                         entity2.getName(),
                         entity2.getSource(),
                         entity2.getVersion(),
                         entity2.getObjectVersion(),
                         classificationMarking,
                         modifiedBy,
                         modifiedByIP,
                         label,
                         inputLang,
                         isReadOnly);

    }

    /**
     * Creates a link between entity1 and entity2
     * 
     * @param entity1
     * @param linkType
     * @param entity2Key
     * @param entity2Name
     * @param entity2Source
     * @param entity2Version
     * @param entity2ObjectVersion
     * @param classificationMarking
     * @param modifiedBy
     * @param modifiedByIP
     * @param label
     * @param inputLang
     * @param isReadOnly
     */
    public void establishLinkage(CoalesceEntity entity1,
                                 ELinkTypes linkType,
                                 String entity2Key,
                                 String entity2Name,
                                 String entity2Source,
                                 String entity2Version,
                                 int entity2ObjectVersion,
                                 Marking classificationMarking,
                                 String modifiedBy,
                                 String modifiedByIP,
                                 String label,
                                 Locale inputLang,
                                 boolean isReadOnly)
    {
        createHistory(modifiedBy, modifiedByIP, null);

        if (isReadOnly)
        {
            setStatus(ECoalesceObjectStatus.READONLY);
        }
        else
        {
            setStatus(ECoalesceObjectStatus.ACTIVE);
        }

        // Set Values
        setObjectVersion(entity1.getObjectVersion());
        setLabel(label);

        setEntity1Key(entity1.getKey());
        setEntity1Name(entity1.getName());
        setEntity1Source(entity1.getSource());
        setEntity1Version(entity1.getVersion());

        setLinkType(linkType);

        setEntity2Key(entity2Key);
        setEntity2Name(entity2Name);
        setEntity2Source(entity2Source);
        setEntity2Version(entity2Version);
        setEntity2ObjectVersion(entity2ObjectVersion);

        setClassificationMarking(classificationMarking);
        setModifiedBy(modifiedBy);
        setModifiedByIP(modifiedByIP);
        setInputLang(inputLang);

        updateLastModified();
    }

    // -----------------------------------------------------------------------//
    // public Shared Methods
    // -----------------------------------------------------------------------//

    @Override
    protected boolean prune(CoalesceObjectType child)
    {
        boolean isSuccessful = false;

        if (child instanceof History)
        {
            isSuccessful = _entityLinkage.getHistory().remove(child);
        }

        return isSuccessful;
    }

    @Override
    protected boolean setExtendedAttributes(String name, String value)
    {
        switch (name.toLowerCase()) {
        case "entity1key":
            setEntity1Key(value);
            return true;
        case "entity1name":
            setEntity1Name(value);
            return true;
        case "entity1source":
            setEntity1Source(value);
            return true;
        case "entity1version":
            setEntity1Version(value);
            return true;
        case "linktype":
            ELinkTypes type = ELinkTypes.getTypeForLabel(value);
            
            if (type == ELinkTypes.UNDEFINED) {
                type = ELinkTypes.valueOf(value);
            }
            
            setLinkType(type);
            return true;
        case "entity2key":
            setEntity2Key(value);
            return true;
        case "entity2name":
            setEntity2Name(value);
            return true;
        case "entity2source":
            setEntity2Source(value);
            return true;
        case "entity2version":
            setEntity2Version(value);
            return true;
        case "entity2objectversion":
            setEntity2ObjectVersion(Integer.valueOf(value));
            return true;
        case "classificationmarking":
            setClassificationMarking(new Marking(value));
            return true;
        case "inputlang":

            Locale inputLang = LocaleConverter.parseLocale(value);

            if (inputLang == null)
                return false;

            setInputLang(inputLang);

            return true;

        default:
            if (setOtherAttribute(name, value))
            {
                updateLastModified();
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    protected Map<QName, String> getAttributes()
    {
        Map<QName, String> map = super.getAttributes();

        map.put(new QName("entity1key"), _entityLinkage.getEntity1Key());
        map.put(new QName("entity1name"), _entityLinkage.getEntity1Name());
        map.put(new QName("entity1source"), _entityLinkage.getEntity1Source());
        map.put(new QName("entity1version"), _entityLinkage.getEntity1Version());
        map.put(new QName("linktype"), _entityLinkage.getLinktype());
        map.put(new QName("entity2key"), _entityLinkage.getEntity2Key());
        map.put(new QName("entity2name"), _entityLinkage.getEntity2Name());
        map.put(new QName("entity2source"), _entityLinkage.getEntity2Source());
        map.put(new QName("entity2version"), _entityLinkage.getEntity2Version());
        map.put(new QName("classificationmarking"), _entityLinkage.getClassificationmarking());

        if (_entityLinkage.getEntity2Objectversion() == null)
        {
            map.put(new QName("entity2objectversion"), "0");
        }
        else
        {
            map.put(new QName("entity2objectversion"), Integer.toString(_entityLinkage.getEntity2Objectversion()));
        }

        if (_entityLinkage.getInputlang() == null)
        {
            map.put(new QName("inputlang"), null);
        }
        else
        {
            map.put(new QName("inputlang"), _entityLinkage.getInputlang().toString());
        }

        return map;
    }

    protected Linkage getBaseLinkage()
    {
        return _entityLinkage;
    }

}
