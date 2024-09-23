package com.project.bakerymanagementsystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateInterval {
    private LocalDate startDate;
    private LocalDate endDate;

    public DateInterval() {}

    public DateInterval(LocalDate startDate, LocalDate finishDate) {
        this.startDate = startDate;
        this.endDate = finishDate;
     }
}
