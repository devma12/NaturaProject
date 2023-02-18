package com.natura.web.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
public class Criteria {

    private Long id;

    private String name;

    private String value;

    private Set<Species> species;
}
