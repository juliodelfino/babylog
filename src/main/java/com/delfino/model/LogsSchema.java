package com.delfino.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class LogsSchema {

    private Map<Long, LogEntry> logs = new LinkedHashMap<>();
}
