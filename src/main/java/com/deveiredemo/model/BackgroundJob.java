package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.task.JobSettings;

import net.redhogs.cronparser.CronExpressionDescriptor;
import org.joda.time.DateTime;
import quartz.CronExpression;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;

@Recordable.Embedded
public class BackgroundJob extends Record implements JobSettings {

    static {
        // disables logging from the net.redhogs.cronparser package.
        java.util.logging.Logger.getLogger(CronExpressionDescriptor.class.getName()).setLevel(Level.OFF);
    }

    @DisplayName("Allowed Host / IP Address")
    @ToolUi.Placeholder(dynamicText = "${content.allowedHostOrIpAddressPlaceholderText}", editable = false)
    private String allowedHostOrIpAddress;

    @ToolUi.Note("Check this box to enable this task on the job host specified above.")
    private boolean isEnabled;

    @ToolUi.NoteHtml("<span data-dynamic-html='${content.label}'></span>")
    private String cronExpression;

    public String getAllowedHostOrIpAddress() {
        return ObjectUtils.firstNonNull(allowedHostOrIpAddress, JsgSite.getInstance().getDefaultJobHostOrIpAddress());
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getAllowedHostOrIpAddressPlaceholderText() {
        return JsgSite.getInstance().getDefaultJobHostOrIpAddress();
    }

    /**
     * Checks whether a server running the task is whitelisted and is required to update data.
     */
    @Override
    public boolean isAllowedToRun() {

        String allowedHost;
        if (isEnabled() && (allowedHost = getAllowedHostOrIpAddress()) != null) {
            try {
                InetAddress localAddress = InetAddress.getLocalHost();

                InetAddress allowedAddress = InetAddress.getByName(allowedHost);

                if (localAddress.getHostAddress().equals(allowedAddress.getHostAddress())) {
                    return true;
                }
            } catch (UnknownHostException e) {
                // do nothing
            }
        }

        return false;
    }

    /**
     * Calculates the next run time based on these settings.
     *
     * @param dateTime the current time.

     * @return the next run time.
     */
    @Override
    public DateTime calculateRunTime(DateTime dateTime) {

        String cronString = getCronExpression();

        if (!StringUtils.isBlank(cronString)) {

            cronString = cronString.trim();
            try {
                CronExpression cron = new CronExpression(cronString);

                Date runtTime = cron.getNextValidTimeAfter(dateTime.toDate());

                if (runtTime != null) {
                    return new DateTime(runtTime);
                }

            } catch (Exception e) {
                // do nothing
            }
        }

        return null;
    }

    @Override
    public String getLabel() {

        String cronString = getCronExpression();

        if (!StringUtils.isBlank(cronString)) {

            cronString = cronString.trim();

            try {
                CronExpression cron = new CronExpression(cronString);

                StringBuilder builder = new StringBuilder();
                try {
                    String frequencyLabel = getFrequencyLabel();

                    if (!StringUtils.isBlank(frequencyLabel)) {
                        builder.append("Runs: ");
                        builder.append(frequencyLabel);
                    }

                } catch (Exception e) {
                    builder.append(cronString);
                }

                builder.append(" | Next run: ");

                Date nextValidDate = cron.getNextValidTimeAfter(new Date());
                if (nextValidDate != null) {
                    builder.append(nextValidDate.toString());

                } else {
                    builder.append("Never");
                }

                return builder.toString();

            } catch (Exception e) {
                return "Invalid cron expression";
            }

        } else {
            return null;
        }
    }

    private String getFrequencyLabel() {

        StringBuilder builder = new StringBuilder();

        String cronString = getCronExpression();

        if (!StringUtils.isBlank(cronString)) {

            cronString = cronString.trim();

            try {
                String cronDescription = CronExpressionDescriptor.getDescription(cronString);
                if (!cronDescription.isEmpty()) {
                    builder.append(cronDescription);
                }

            } catch (Exception e) {
                builder.append(cronString);
            }
        }

        return builder.toString();
    }
}
