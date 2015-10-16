package com.deveiredemo.model;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectType;

/**
 * Converts brightspot-social Tweet objects into jordanspieth Tweet objects on save.
 */
public class TweetModification extends Modification<com.psddev.social.Tweet> {

    @Override
    public void afterSave() {
        if (ObjectType.getInstance(com.psddev.social.Tweet.class).equals(getState().getType())) {
            getState().setTypeId(ObjectType.getInstance(Tweet.class).getId());
            save();
        }
    }
}
