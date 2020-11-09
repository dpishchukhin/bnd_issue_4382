package com.github.dpishchukhin.bnd.issue_4282.tools.logback.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author dpishchukhin
 */
public class MessageFilter extends TurboFilter {

    private String excludeMarkers = "";
    private String loggerName = "com.github.dpishchukhin";
    private boolean supportGc = true;

    private Set<Marker> excludeMarkersList = new HashSet<>();

    @Override
    public void start() {
        excludeMarkersList = excludeMarkers(excludeMarkers);

        super.start();
    }

    @Override
    public FilterReply decide(final Marker marker, final Logger logger, final Level level,
                              final String format, final Object[] params, final Throwable t) {
        if (!logger.getName().startsWith(loggerName)) {
            return FilterReply.NEUTRAL;
        }

        if (excludeMarkersList.contains(marker)) {
            return FilterReply.NEUTRAL;
        } else {
            return FilterReply.DENY;
        }
    }

    public String getExcludeMarkers() {
        return excludeMarkers;
    }

    public void setExcludeMarkers(final String excludeMarkers) {
        this.excludeMarkers = excludeMarkers;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(final String loggerName) {
        this.loggerName = loggerName;
    }

    public boolean isSupportGc() {
        return supportGc;
    }

    public void setSupportGc(boolean supportGc) {
        this.supportGc = supportGc;
    }

    private Set<Marker> excludeMarkers(final String markersToExclude) {
        final List<String> listOfMarkers = Arrays.asList(markersToExclude.split("\\s*,\\s*"));
        return listOfMarkers.stream()
            .map(MarkerFactory::getMarker)
            .collect(toSet());
    }
}
