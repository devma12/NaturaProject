package com.natura.web.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class IdentificationKey {

    private Long entryId;

    private Long speciesId;

    public  IdentificationKey(Long entry, Long species) {
        this.entryId = entry;
        this.speciesId = species;
    }

}