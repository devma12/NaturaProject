package com.natura.web.server.model;

import lombok.Data;

import java.util.Set;

@Data
public class Criteria {

    private Long id;

    private String name;

    private String value;

    private Set<Species> species;
}
