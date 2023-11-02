package com.delfino.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UiLogEntry {

    Long id;
    String type;
    String time;
    String description;
}
