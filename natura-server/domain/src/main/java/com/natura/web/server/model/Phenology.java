package com.natura.web.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@NoArgsConstructor
@Data
public class Phenology {

    private Long id;

    private MonthRange range;

    public Phenology(Month start, Month end) {
        this.range = new MonthRange(start, end);
    }

    public Month getStart() {
        if (this.range == null) {
            return null;
        }
        return this.range.getStart();
    }

    public Month getEnd() {
        if (this.range == null) {
            return null;
        }
        return this.range.getEnd();
    }
}
