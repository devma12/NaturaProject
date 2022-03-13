package com.natura.web.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@NoArgsConstructor
@Data
public class Phenology {

    private Long id;

    private Month start;

    private Month end;

    private Species species;

    public Phenology(Month start, Month end) {
        this.start = start;
        this.end = end;
    }
}
