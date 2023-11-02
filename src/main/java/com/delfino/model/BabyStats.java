package com.delfino.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BabyStats {
    private long ageInMonths;
    private long ageInWeeks;
    private long correctedAgeInMonths;
    private long correctedAgeInWeeks;
    private String lastFeedingTime;
    private String lastFeedingTimeAgo;
    private long lastFeedingVolume;
    private LogHealthStatsEntry healthStats;
}
