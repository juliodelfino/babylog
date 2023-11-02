package com.delfino.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class LogHealthStatsEntry extends LogEntry {

    //height in cm
    Double height;

    //weight in kg
    Double weight;

    Double headCircumference;
    Integer jaundiceLevel;
    String remarks;

    public static LogHealthStatsEntry parseDetails(LogEntry logEntry) throws ParseException {
        LogHealthStatsEntry lcve = new LogHealthStatsEntry();
        lcve.setId(logEntry.getId());
        lcve.setType(logEntry.getType());
        lcve.setTimestamp(logEntry.getTimestamp());
        Map details = (Map) logEntry.getDetails();
        lcve.setHeight((Double)details.get("height"));
        lcve.setWeight((Double)details.get("weight"));
        lcve.setHeadCircumference((Double)details.get("headCircumference"));
        lcve.setJaundiceLevel((Integer) details.get("jaundiceLevel"));
        lcve.setRemarks((String)details.get("remarks"));
        return lcve;
    }

    @Override
    public LogEntry withBundledDetails() {
        Map details = new HashMap();
        if (height != null) details.put("height", height);
        if (weight != null) details.put("weight", weight);
        if (headCircumference != null) details.put("headCircumference", headCircumference);
        if (jaundiceLevel != null) details.put("jaundiceLevel", jaundiceLevel);
        if (!StringUtils.isEmpty(remarks)) details.put("remarks", remarks);
        return new LogEntry(this.id, this.type, this.timestamp, details);
    }

}
