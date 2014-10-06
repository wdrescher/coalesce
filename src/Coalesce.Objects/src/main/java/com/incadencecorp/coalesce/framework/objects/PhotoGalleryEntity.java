package com.incadencecorp.coalesce.framework.objects;

import com.incadencecorp.coalesce.framework.datamodel.CoalesceFieldDefinition;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecordset;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceSection;
import com.incadencecorp.coalesce.framework.datamodel.ECoalesceFieldDataTypes;

public class PhotoGalleryEntity extends ActionBaseEntity {

    // ----------------------------------------------------------------------//
    // Static Properties
    // ----------------------------------------------------------------------//

    public static final String Name = ActionBaseEntity.Name;
    public static final String Source = "Photos: Supporting";

    // ----------------------------------------------------------------------//
    // Protected Member Variables
    // ----------------------------------------------------------------------//

    private CoalesceRecordset _photographRecordset;

    // ----------------------------------------------------------------------//
    // Initialization
    // ----------------------------------------------------------------------//

    @Override
    public boolean initialize()
    {
        CoalesceSection section;

        // Already Initialized?
        if (_photographRecordset != null) return false;

        // Initialize Entity
        if (!super.initialize(PhotoGalleryEntity.Source, "1.0", "")) return false;

        // Create Live Section
        section = CoalesceSection.create(this, "Photographs Section");
        _photographRecordset = CoalesceRecordset.create(section, "Photographs Recordset");

        CoalesceFieldDefinition.create(_photographRecordset, "Photo", ECoalesceFieldDataTypes.FileType);
        CoalesceFieldDefinition.create(_photographRecordset, "OriginalFilename", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(_photographRecordset, "Height", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(_photographRecordset, "Width", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(_photographRecordset, "Description", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(_photographRecordset, "BagTagID", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(_photographRecordset, "DegreesFromTrueNorth", ECoalesceFieldDataTypes.StringType);
        CoalesceFieldDefinition.create(_photographRecordset, "PhotoDateTimeUTC", ECoalesceFieldDataTypes.DateTimeType);
        CoalesceFieldDefinition.create(_photographRecordset, "PhotoLocation", ECoalesceFieldDataTypes.GeocoordinateType);

        // Initialize References
        return this.initializeReferences();
    }

    @Override
    protected boolean initializeReferences()
    {
        if (!super.initializeReferences()) return false;

        if (_photographRecordset == null)
        {
            _photographRecordset = (CoalesceRecordset) getDataObjectForNamePath(getName()
                    + "/Photographs Section/Photographs Recordset");

        }

        return _photographRecordset != null;
    }

    // ----------------------------------------------------------------------//
    // Entity Fields
    // ----------------------------------------------------------------------//

}
