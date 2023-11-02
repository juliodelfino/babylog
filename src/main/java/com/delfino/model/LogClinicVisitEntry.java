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
public class LogClinicVisitEntry extends LogEntry {

    String specialist;
    String location;
    String remarks;

    public static LogClinicVisitEntry parseDetails(LogEntry logEntry) throws ParseException {
        LogClinicVisitEntry lcve = new LogClinicVisitEntry();
        lcve.setId(logEntry.getId());
        lcve.setType(logEntry.getType());
        lcve.setTimestamp(logEntry.getTimestamp());
        Map details = (Map) logEntry.getDetails();
        lcve.setSpecialist((String)details.get("specialist"));
        lcve.setLocation((String)details.get("location"));
        lcve.setRemarks((String)details.get("remarks"));
        return lcve;
    }

    @Override
    public LogEntry withBundledDetails() {
        Map details = new HashMap();
        details.put("specialist", specialist);
        details.put("location", location);
        if (!StringUtils.isEmpty(remarks)) details.put("remarks", remarks);
        return new LogEntry(this.id, this.type, this.timestamp, details);
    }

}
