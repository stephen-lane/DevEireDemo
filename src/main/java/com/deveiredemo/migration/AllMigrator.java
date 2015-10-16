package com.deveiredemo.migration;

import com.psddev.dari.db.Database;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.AsyncQueue;

import org.slf4j.Logger;

import java.util.Arrays;

public class AllMigrator implements Migrator {

    @Override
    public String getName() {
        return "ALL";
    }

    @Override
    public String getDescription() {
        return "Runs all the migrators in the appropriate order.";
    }

    @Override
    public void migrate(
            Database foreignDatabase,
            boolean overwriteExisting,
            String additionalForeignQueryCriteria,
            AsyncQueue<Recordable> saveQueue,
            AsyncQueue<Recordable> saveUnsafelyQueue,
            AsyncQueue<Recordable> deleteQueue,
            Logger logger) {

        for (Migrator migrator : Arrays.asList(
                new TagMigrator(),
                new AuthorMigrator(),
                new ImageMigrator(),
                new VideoMigrator(),
                new GalleryMigrator(),
                new ArticleMigrator(),
                new ArticleLinkMigrator())) {

            logger.info("Starting " + migrator.getName());
            migrator.migrate(
                    foreignDatabase,
                    overwriteExisting,
                    additionalForeignQueryCriteria,
                    saveQueue,
                    saveUnsafelyQueue,
                    deleteQueue,
                    logger);

            logger.info("Done " + migrator.getName() + ". Sleeping...");
            try {
                // Sleeps for 20 seconds to let the queues clear out.
                Thread.sleep(20000);

            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
