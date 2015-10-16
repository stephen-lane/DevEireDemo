package com.psddev.util.task;

import com.psddev.dari.util.RepeatingTask;

import org.joda.time.DateTime;

import java.util.concurrent.atomic.AtomicReference;

public abstract class Job extends RepeatingTask {

    public static final double DARI_TASK_FILTER_PERIODIC_DELAY_SECONDS = 1.0;

    /**
     * Number of milliseconds to cache the task settings before fetching a
     * fresh copy.
     */
    private static final int SETTINGS_UPDATE_DELAY = 5000;

    private static final long DEFAULT_RUN_TIME_CALCULATION_DELAY = Math.round(
            DARI_TASK_FILTER_PERIODIC_DELAY_SECONDS * 1000);

    private final AtomicReference<DateTime> previousSettingsCheckTime = new AtomicReference<>();
    private final AtomicReference<JobSettings> settingsReference = new AtomicReference<>();

    private final long calculationDelay;

    /**
     * Creates this job with the default calculation delay as defined by the
     * dari {@link com.psddev.dari.util.TaskFilter} and has a default periodic
     * delay of {@link Job#DARI_TASK_FILTER_PERIODIC_DELAY_SECONDS}
     * seconds. If this job is not being managed by {@code TaskFilter} and with
     * a different periodic delay, then you should use the
     * {@link #Job(long) alternate constructor} and specify the
     * delay.
     */
    public Job() {
        this(null, null, DEFAULT_RUN_TIME_CALCULATION_DELAY);
    }

    /**
     * Creates this job with the default calculation delay as defined by the
     * dari {@link com.psddev.dari.util.TaskFilter} and has a default periodic
     * delay of {@link Job#DARI_TASK_FILTER_PERIODIC_DELAY_SECONDS}
     * seconds. If this job is not being managed by {@code TaskFilter} and with
     * a different periodic delay, then you should use the
     * {@link #Job(long) alternate constructor} and specify the
     * delay.
     *
     * @param executor the executor name.
     * @param name the task name.
     */
    public Job(String executor, String name) {
        this(executor, name, DEFAULT_RUN_TIME_CALCULATION_DELAY);
    }

    /**
     * Constructs this job with the specified calculation delay.
     *
     * @param calculationDelay the delay in milliseconds between successive
     *                         {@link #calculateRunTime(org.joda.time.DateTime)}
     *                         invocations.
     */
    public Job(long calculationDelay) {
        this(null, null, calculationDelay);
    }

    /**
     *
     * @param executor the executor name.
     * @param name the task name.
     * @param calculationDelay the delay in milliseconds between successive
     *                         {@link #calculateRunTime(org.joda.time.DateTime)}
     *                         invocations.
     */
    public Job(String executor, String name, long calculationDelay) {
        super(executor, name);
        this.calculationDelay = calculationDelay;
    }

    /**
     * @return the repeating task settings for this job.
     */
    protected abstract JobSettings getJobSettings();

    /**
     * Calculates the next run time based on repeating task settings.
     */
    @Override
    protected final DateTime calculateRunTime(DateTime currentTime) {

        JobSettings settings = getCachedRepeatingTaskSettings();

        if (settings != null && settings.isAllowedToRun()) {

            DateTime runTime = settings.calculateRunTime(currentTime.minus(this.calculationDelay));
            if (runTime != null) {
                return runTime;
            }
        }

        // Never runs since the date is always ahead of the current time.
        return new DateTime().plus(this.calculationDelay);
    }

    private JobSettings getCachedRepeatingTaskSettings() {

        /*
         * The compiler complains, but the parent class calls calculateRunTime
         * during object creation which in turn calls this method, meanwhile
         * the instance variables of this class may not be instantiated yet.
         * Arguably bad design in RepeatingTask, but this check guards against it.
         */
        if (previousSettingsCheckTime == null || settingsReference == null) {
            return null;
        }

        DateTime now = new DateTime();
        DateTime previousCheckTime = previousSettingsCheckTime.get();
        if (previousCheckTime == null || previousCheckTime.isBefore(now.minus(SETTINGS_UPDATE_DELAY))) {
            previousSettingsCheckTime.set(now);

            try {
                settingsReference.set(getJobSettings());
            } catch (RuntimeException e) {
                // do nothing
            }
        }

        return settingsReference.get();
    }
}
