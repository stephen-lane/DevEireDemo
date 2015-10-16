package com.deveiredemo.migration;

import com.psddev.dari.db.AsyncDatabaseWriter;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.ForwardingDatabase;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.db.WebDatabase;
import com.psddev.dari.db.WriteOperation;
import com.psddev.dari.util.AsyncQueue;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.DebugFilter;
import com.psddev.dari.util.JspUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Settings;
import com.psddev.dari.util.Task;
import com.psddev.dari.util.TaskExecutor;
import com.psddev.dari.util.TypeDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@DebugFilter.Path("jsg-migrator")
@SuppressWarnings("serial")
public class MigrationDebugServlet extends HttpServlet {

    private static final String DEBUG_SCRIPT_NAME = "Jordan Spieth Upgrade Migration";
    private static final String EXECUTOR_NAME = DEBUG_SCRIPT_NAME;

    private static final String DEFAULT_REMOTE_DATABASE_NAME = "jordanspieth";
    private static final String DEFAULT_REMOTE_USERNAME = "debug";
    private static final String DEFAULT_REMOTE_URL = "http://upgrade-qa.jordanspieth.psdops.com/_debug/db-web";

    private static final String REMOTE_DATABASE_NAME_SETTING = "jsg/migration/remoteDatabaseName";
    private static final String REMOTE_USERNAME_SETTING = "jsg/migration/remoteUsername";
    private static final String REMOTE_PASSWORD_SETTING = "jsg/migration/remotePassword";
    private static final String REMOTE_URL_SETTING = "jsg/migration/remoteUrl";

    private static final String OVERWRITE_EXISTING_PARAMETER = "overwriteExisting";
    private static final String ADDITIONAL_FOREIGN_QUERY_CRITERIA_PARAMETER = "additionalForeignQueryCriteria";

    @Override
    @SuppressWarnings("unchecked")
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        new DebugFilter.PageWriter(getServletContext(), request, response) {
            {
                startPage(DEBUG_SCRIPT_NAME);
                {
                    writeStart("style", "type", "text/css");
                    writeEnd();

                    writeStart("script");
                    {
                        write("$(document).ready(function(){");
                        write("});");
                    }
                    writeEnd();

                    writeStart("h2").writeHtml(DEBUG_SCRIPT_NAME).writeEnd();

                    if (isAnyRunning()) {

                        writeStart("h3").writeHtml("The Migration Task is currently running. See progress on the Task Page.").writeEnd();
                        writeStart("a", "href", page.absoluteUrl("/_debug/task")).writeHtml("Click here").writeEnd();

                    } else if (!page.isFormPost()) {
                        writeStart("form",
                                "action", "?_cache=false",
                                "method", "POST",
                                "class", "");
                        {
                            for (Migrator migrator : findMigratorInstances()) {
                                writeStart("label", "class", "checkbox", "for", JspUtils.createId(page.getRequest()));
                                {
                                    writeTag("input",
                                            "id", JspUtils.getId(page.getRequest()),
                                            "type", "checkbox",
                                            "name", migrator.getClass().getName(),
                                            "value", "true");
                                    writeStart("h3").writeHtml(migrator.getName()).writeEnd();
                                    writeStart("p").writeHtml(migrator.getDescription()).writeEnd();
                                }
                                writeEnd();
                            }

                            writeStart("div", "class", "form-actions");
                            {
                                writeStart("label", "class", "checkbox", "for", JspUtils.createId(page.getRequest()));
                                {
                                    writeTag("input",
                                            "id", JspUtils.getId(page.getRequest()),
                                            "type", "checkbox",
                                            "name", OVERWRITE_EXISTING_PARAMETER,
                                            "value", "true");
                                    writeStart("p").writeHtml("Overwrite Existing?").writeEnd();
                                }
                                writeEnd();

                                writeStart("label", "class", "text", "for", JspUtils.createId(page.getRequest()));
                                {
                                    writeStart("p").writeHtml("Additional Foreign Query Criteria?").writeEnd();
                                    writeTag("input",
                                            "id", JspUtils.getId(page.getRequest()),
                                            "type", "text",
                                            "name", ADDITIONAL_FOREIGN_QUERY_CRITERIA_PARAMETER);
                                }
                                writeEnd();

                                writeTag("input",
                                        "class", "btn btn-primary btn-large",
                                        "type", "submit",
                                        "name", "action-init",
                                        "value", "Migrate Data");
                            }
                            writeEnd();
                        }
                        writeEnd();

                    } else if (page.paramNamesList().contains("action-init")) {

                        List<Migrator> migrators = findMigratorInstances().stream()
                                .filter((migrator) -> page.param(boolean.class, migrator.getClass().getName()))
                                .collect(Collectors.toList());

                        if (!migrators.isEmpty()) {

                            WebDatabase foreignDatabase = new WebDatabase();

                            foreignDatabase.setRemoteDatabase(Settings.getOrDefault(String.class, REMOTE_DATABASE_NAME_SETTING, DEFAULT_REMOTE_DATABASE_NAME));
                            foreignDatabase.setRemoteUsername(Settings.getOrDefault(String.class, REMOTE_USERNAME_SETTING, DEFAULT_REMOTE_USERNAME));
                            foreignDatabase.setRemotePassword(Settings.get(String.class, REMOTE_PASSWORD_SETTING));
                            foreignDatabase.setRemoteUrl(Settings.getOrDefault(String.class, REMOTE_URL_SETTING, DEFAULT_REMOTE_URL));

                            MigrationTask task = new MigrationTask(migrators, foreignDatabase,
                                    page.param(boolean.class, OVERWRITE_EXISTING_PARAMETER),
                                    page.param(String.class, ADDITIONAL_FOREIGN_QUERY_CRITERIA_PARAMETER));

                            writeStart("h3").writeHtml("Task submitted for " + DEBUG_SCRIPT_NAME).writeEnd();

                            task.submit();
                        } else {
                            writeStart("h3").writeHtml("No migrators selected").writeEnd();
                        }
                    }
                }
                endPage();
            }
        };
    }

    private static boolean isAnyRunning() {

        TaskExecutor executor = TaskExecutor.Static.getInstance(EXECUTOR_NAME);
        if (executor != null) {
            for (Object task : executor.getTasks()) {
                if (task instanceof Task) {
                    if (((Task) task).isRunning()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static List<Migrator> findMigratorInstances() {

        List<Migrator> migrators = new ArrayList<>();

        ClassFinder.findClasses(Migrator.class).forEach((migratorClass) -> {
            if (!migratorClass.isAnonymousClass()
                    && !Modifier.isAbstract(migratorClass.getModifiers())
                    && migratorClass.getAnnotation(Deprecated.class) == null) {

                try {
                    migrators.add(TypeDefinition.getInstance(migratorClass).newInstance());
                } catch (Exception e) {
                    // do nothing
                }
            }
        });

        Collections.sort(migrators, (Migrator o1, Migrator o2) -> ObjectUtils.compare(o1.getName(), o2.getName(), true));

        return migrators;
    }

    private static final class MigrationTask extends Task {

        private Iterable<Migrator> migrators;
        private Database foreignDatabase;
        private boolean overwriteExisting;
        private String additionalForeignQueryCriteria;

        private MigrationTask(Iterable<Migrator> migrators, Database foreignDatabase, boolean overwriteExisting, String additionalForeignQueryCriteria) {
            super(DEBUG_SCRIPT_NAME, DEBUG_SCRIPT_NAME);
            this.migrators = migrators;
            this.foreignDatabase = foreignDatabase;
            this.overwriteExisting = overwriteExisting;
            this.additionalForeignQueryCriteria = additionalForeignQueryCriteria;
        }

        @Override
        protected void doTask() throws Exception {
            if (migrators != null) {

                Logger logger = LoggerFactory.getLogger("Migration");

                Database migrationDatabase = getMigrationTaskDefaultDatabase(Database.Static.getDefault(), logger);
                try {
                    Database.Static.overrideDefault(migrationDatabase);

                    AsyncQueue<Recordable> saveQueue = new AsyncQueue<>();
                    AsyncQueue<Recordable> saveUnsafelyQueue = new AsyncQueue<>();
                    AsyncQueue<Recordable> deleteQueue = new AsyncQueue<>();

                    // 3 writer tasks
                    CountDownLatch latch = new CountDownLatch(3);

                    AsyncDatabaseWriter<Recordable> saveWriter = getDatabaseWriter(
                            migrationDatabase, saveQueue, WriteOperation.SAVE, latch, logger);

                    AsyncDatabaseWriter<Recordable> saveUnsafelyWriter = getDatabaseWriter(
                            migrationDatabase, saveUnsafelyQueue, WriteOperation.SAVE_UNSAFELY, latch, logger);

                    AsyncDatabaseWriter<Recordable> deleteWriter = getDatabaseWriter(
                            migrationDatabase, deleteQueue, WriteOperation.DELETE, latch, logger);

                    saveWriter.submit();
                    saveUnsafelyWriter.submit();
                    deleteWriter.submit();

                    for (Migrator migrator : migrators) {

                        try {
                            migrator.migrate(foreignDatabase, overwriteExisting, additionalForeignQueryCriteria,
                                    saveQueue, saveUnsafelyQueue, deleteQueue, logger);

                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);

                        } finally {
                            logger.info("Finished task [" + migrator.getName() + "].");
                        }
                    }

                    saveQueue.close();
                    saveUnsafelyQueue.close();
                    deleteQueue.close();

                    logger.info("Waiting for background tasks to complete");
                    latch.await();

                } finally {
                    Database.Static.restoreDefault();
                }

                logger.info("Finished all migration tasks.");
            }
        }

        private static Database getMigrationTaskDefaultDatabase(Database original, Logger logger) {

            ForwardingDatabase forwardingDb = new ForwardingDatabase() {

                private AtomicInteger counter = new AtomicInteger(0);

                @Override
                protected <T> Query<T> filterQuery(Query<T> query) {
                    return query.timeout(10.0).master().noCache();
                }

                @Override
                protected State filterState(State state) {
                    logger.info(counter.incrementAndGet() + ". Updating object ["
                            + state.getType().getLabel() + " - "
                            + state.getId() + " - " + state.getLabel() + "].");
                    return state;
                }
            };
            forwardingDb.setDelegate(original);

            return forwardingDb;
        }

        private AsyncDatabaseWriter<Recordable> getDatabaseWriter(Database database, AsyncQueue<Recordable> writeQueue, WriteOperation writeOperation, CountDownLatch latch, Logger logger) {
            return new AsyncDatabaseWriter<Recordable>(
                    EXECUTOR_NAME,
                    writeQueue,
                    database,
                    writeOperation,
                    200,
                    true) {

                @Override
                protected void beforeStart() {
                    super.beforeStart();
                }

                @Override
                protected void handleError(Recordable item, Exception error) {
                    logger.warn(String.format("Failed to consume item with ID [%s]! Cause: [%s]\nRaw Data: [%s]",
                            item.getState().getId().toString(),
                            error.getMessage(),
                            ObjectUtils.toJson(item.getState().getSimpleValues(), true)));
                }

                @Override
                protected void finished() {
                    latch.countDown();
                    try {
                        super.finished();
                    } catch (Exception e) {
                        logger.warn("Exception during finish: " + e.getMessage());
                    }
                }
            };
        }
    }
}
