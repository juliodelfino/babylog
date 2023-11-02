package com.delfino.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class LogEntry {

    Long id;
    String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXX")
    Date timestamp;
    Object details;

    public LogEntry withBundledDetails() {
        return this;
    }
}
