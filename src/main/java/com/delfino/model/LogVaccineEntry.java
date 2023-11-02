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
public class LogVaccineEntry extends LogMedicationEntry {

    String location;

    public static LogVaccineEntry parseDetails(LogEntry logEntry) throws ParseException {
        LogVaccineEntry lve = new LogVaccineEntry();
        lve.setId(logEntry.getId());
        lve.setType(logEntry.getType());
        lve.setTimestamp(logEntry.getTimestamp());
        Map details = (Map) logEntry.getDetails();
        lve.setName((String)details.get("name"));
        lve.setRemarks((String)details.get("remarks"));
        lve.setLocation((String)details.get("location"));
        return lve;
    }

    @Override
    public LogEntry withBundledDetails() {
        LogEntry log = super.withBundledDetails();
        ((Map)log.getDetails()).put("location", location);
        return log;
    }

}
