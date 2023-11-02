package com.delfino.dao;

import com.delfino.db.JsonDb;
import com.delfino.db.JsonDbFactory;
import com.delfino.model.*;
import com.delfino.util.AppProperties;
import com.delfino.util.UiLogFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class LogsDao {

    private JsonDb<LogsSchema> jsonDb = JsonDbFactory.getInstance("data.json", LogsSchema.class);
    private final UiLogFormatter logFormatter = new UiLogFormatter();
    private final ObjectMapper mapper = logFormatter.getMapper();
    private final String timezone = AppProperties.get("timezone");

    public Collection<LogEntry> getAll() {

        return jsonDb.get().getLogs().values();
    }

    public boolean addLog(LogEntry log) {

        Map<Long, LogEntry> logsMap = jsonDb.get().getLogs();
        log.setId(Collections.max(logsMap.keySet()) + 1);
        logsMap.put(log.getId(), log.withBundledDetails());
        return jsonDb.save();
    }

    public boolean updateLog(Long id, LogEntry log) {

        Map<Long, LogEntry> logsMap = jsonDb.get().getLogs();
        logsMap.put(id, log.withBundledDetails());
        return jsonDb.save();
    }

    public boolean duplicateLog(Long id) throws IOException {

        LogEntry log = jsonDb.get().getLogs().get(id);
        return addLog(mapper.readValue(mapper.writeValueAsString(log), LogEntry.class));
    }

    public LogEntry getById(long id) {
        return jsonDb.get().getLogs().get(id);
    }

    public boolean deleteLog(Long id) {
        jsonDb.get().getLogs().remove(id);
        return jsonDb.save();
    }

    public Collection<LogEntry> searchByKeyword(String query) {
        return getAll().stream().filter(log ->
        {
            try {
                return mapper.writeValueAsString(log.withBundledDetails()).toLowerCase().contains(query.toLowerCase());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }).collect(Collectors.toList());
    }

    public LogEntry getLatestByType(String type) {
        return getAll().stream().sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
                .filter(e -> e.getType().equals(type)).findFirst().get();
    }

    @SneakyThrows
    public BabyStats getLatestStats() {

        LocalDate birthDate = LocalDate.parse("2020-09-05");
        long monthsBetween = ChronoUnit.MONTHS.between(birthDate, LocalDate.now());
        long weeksBetween = ChronoUnit.WEEKS.between(birthDate, LocalDate.now());

        LocalDate expectedBirthDate = LocalDate.parse("2020-10-12");
        long correctedMonthsBetween = ChronoUnit.MONTHS.between(expectedBirthDate, LocalDate.now());
        long correctedWeeksBetween = ChronoUnit.WEEKS.between(expectedBirthDate, LocalDate.now());

        LogEntry logEntry = getLatestByType("Feeding");
        LogFeedingEntry logFeedingEntry = LogFeedingEntry.parseDetails(logEntry);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd @ hh:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));

        LogEntry statsEntry = getLatestByType("Health Stats");
        LogHealthStatsEntry logHealthStatsEntry = LogHealthStatsEntry.parseDetails(statsEntry);

        BabyStats stats = new BabyStats();
        stats.setAgeInMonths(monthsBetween);
        stats.setAgeInWeeks(weeksBetween);
        stats.setCorrectedAgeInMonths(correctedMonthsBetween);
        stats.setCorrectedAgeInWeeks(correctedWeeksBetween);
        stats.setLastFeedingTime(dateFormat.format(logFeedingEntry.getTimestamp()));
        stats.setLastFeedingTimeAgo(logFormatter.timeAgo(logFeedingEntry.getTimestamp()));
        stats.setLastFeedingVolume(logFeedingEntry.getVolume());
        stats.setHealthStats(logHealthStatsEntry);
        return stats;
    }
}
