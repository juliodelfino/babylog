package com.delfino.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class LogMedicationEntry extends LogEntry {

    String name;
    String remarks;

    public static LogMedicationEntry parseDetails(LogEntry logEntry) throws ParseException {
        LogMedicationEntry lme = new LogMedicationEntry();
        lme.setId(logEntry.getId());
        lme.setType(logEntry.getType());
        lme.setTimestamp(logEntry.getTimestamp());
        Map details = (Map) logEntry.getDetails();
        lme.setName((String)details.get("name"));
        lme.setRemarks((String)details.get("remarks"));
        return lme;
    }

    @Override
    public LogEntry withBundledDetails() {
        Map details = new HashMap();
        details.put("name", name);
        if (!StringUtils.isEmpty(remarks)) details.put("remarks", remarks);
        return new LogEntry(this.id, this.type, this.timestamp, details);
    }

}
