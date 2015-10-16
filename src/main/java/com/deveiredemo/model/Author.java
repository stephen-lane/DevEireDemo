package com.deveiredemo.model;

import com.psddev.cms.db.Content;

import com.deveiredemo.migration.MigratedContent;

public class Author extends Content implements MigratedContent {

    private String name;

    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
