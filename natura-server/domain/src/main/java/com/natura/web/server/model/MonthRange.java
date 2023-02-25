package com.natura.web.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Month;

@Data
@AllArgsConstructor
public class MonthRange {

    private Month start;

    private Month end;

    public boolean overlap(MonthRange other) {
        if (start.compareTo(other.start) < 0) {
            return end.compareTo(other.end) >= 0;
        } else {
            return other.end.compareTo(start) >= 0;
        }
    }
}
