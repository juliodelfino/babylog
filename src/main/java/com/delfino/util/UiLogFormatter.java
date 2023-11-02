package com.delfino.util;

import com.delfino.model.LogEntry;
import com.delfino.model.UiLogEntry;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class UiLogFormatter {

    private final ObjectMapper mapper = getMapper();
    private final String timezone = AppProperties.get("timezone");

    public Map<String, List<UiLogEntry>> formatLogs(Collection<LogEntry> logEntries) {
        Map<String, List<UiLogEntry>> logMap = logEntries.stream().collect(Collectors.groupingBy(row -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            return simpleDateFormat.format(row.getTimestamp());
        }, Collectors.mapping(x -> {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                timeFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                return new UiLogEntry(x.getId(), x.getType(), timeFormat.format(x.getTimestamp()), mapper.writeValueAsString(x.getDetails()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }, Collectors.toList())));

        List<String> keys = new ArrayList(logMap.keySet());
        keys.sort(Comparator.reverseOrder());

        LinkedHashMap orderedLogMap = new LinkedHashMap();
        keys.stream().forEach(k -> {
            List<UiLogEntry> v = logMap.get(k);
            v.sort((l1, l2) -> l2.getTime().compareTo(l1.getTime()));
            orderedLogMap.put(k, v);
        });
        return orderedLogMap;
    }

    //Source: https://stackoverflow.com/questions/42508736/how-to-change-string-time-stamp-into-human-readable-date-format
    public String timeAgo(Date dateAgo) {
        long time_ago = dateAgo.toInstant().toEpochMilli() / 1000;
        long cur_time = Calendar.getInstance().getTimeInMillis() / 1000;
        long time_elapsed = cur_time - time_ago;
        long seconds = time_elapsed;
        int minutes = Math.round(time_elapsed / 60);
        int hours = Math.round(time_elapsed / 3600);
        int days = Math.round(time_elapsed / 86400);

        // Seconds
        if (seconds <= 60) {
            return "just now";
        }
        //Minutes
        else if (minutes <= 60) {
            if (minutes == 1) {
                return "1 min ago";
            } else {
                return minutes + " mins ago";
            }
        }
        //Hours
        else if (hours <= 24) {
            if (hours == 1) {
                return "an hour ago";
            } else {
                return hours + " hrs ago";
            }
        }
        //Days
        else {
            if (days == 1) {
                return "yesterday";
            } else {
                return days + " days ago";
            }
        }
    }

    public ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        return mapper;
    }
}
