package com.psddev.util.task;

import org.joda.time.DateTime;

/**
 * Settings to power a {@link Job}.
 */
public interface JobSettings {

    public boolean isAllowedToRun();

    public DateTime calculateRunTime(DateTime currentTime);

    public String getLabel();
}
