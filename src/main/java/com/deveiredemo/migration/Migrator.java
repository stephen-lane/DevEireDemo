package com.deveiredemo.migration;

import com.psddev.dari.db.Database;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.AsyncQueue;

import org.slf4j.Logger;

public interface Migrator {

    String getName();

    String getDescription();

    void migrate(Database foreignDatabase,
                 boolean overwriteExisting,
                 String additionalForeignQueryCriteria,
                 AsyncQueue<Recordable> saveQueue,
                 AsyncQueue<Recordable> saveUnsafelyQueue,
                 AsyncQueue<Recordable> deleteQueue,
                 Logger logger);
}
