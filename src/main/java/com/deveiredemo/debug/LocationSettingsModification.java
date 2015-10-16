package com.deveiredemo.debug;

import com.psddev.dari.db.Modification;
import com.psddev.location.tool.LocationSettings;

/**
 * Addresses validation exception thrown during bootstrap when saving the
 * LocationSettings singleton.
 */
public class LocationSettingsModification extends Modification<LocationSettings> {

    @Override
    public void beforeSave() {
        if (getOriginalObject().getName() == null) {
            getOriginalObject().setName("Location");
        }
    }
}
