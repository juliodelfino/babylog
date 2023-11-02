package com.delfino.model;

import lombok.AllArgsConstructor;
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
public class LogFeedingEntry extends LogEntry {

    String food;
    long volume;
    Boolean hiccups;
    String remarks;
    Long reflux;

    public static LogFeedingEntry parseDetails(LogEntry logEntry) throws ParseException {
        LogFeedingEntry lfe = new LogFeedingEntry();
        lfe.setId(logEntry.getId());
        lfe.setType(logEntry.getType());
        lfe.setTimestamp(logEntry.getTimestamp());
        Map details = (Map) logEntry.getDetails();
        lfe.setFood((String)details.get("food"));
        lfe.setVolume(NumberFormat.getInstance().parse(details.get("volume").toString()).longValue());
        lfe.setHiccups((Boolean)details.get("hiccups"));
        Long reflux = details.containsKey("reflux") ?
                NumberFormat.getInstance().parse(details.get("reflux").toString()).longValue() : null;
        lfe.setReflux(reflux);
        lfe.setRemarks((String)details.get("remarks"));
        return lfe;
    }

    @Override
    public LogEntry withBundledDetails() {
        Map details = new HashMap();
        details.put("food", food);
        details.put("volume", volume);
        if (hiccups != null) details.put("hiccups", hiccups);
        if (!StringUtils.isEmpty(remarks)) details.put("remarks", remarks);
        if (reflux != null) details.put("reflux", reflux);
        return new LogEntry(this.id, this.type, this.timestamp, details);
    }

}
