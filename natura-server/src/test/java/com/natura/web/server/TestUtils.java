package com.natura.web.server;

import com.natura.web.server.entities.Phenology;
import com.natura.web.server.entities.Species;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static Species createDefaultButterfly() {
        Species species = new Species();
        species.setCommonName("Petite tortue");
        species.setScientificName("Aglais urticae");
        species.setType(Species.Type.Insect);
        species.setOrder("Lepidoptera");
        species.setFamily("Nymphalidae");
        List<Phenology> phenologies = new ArrayList<>();
        phenologies.add(new Phenology(Month.APRIL, Month.SEPTEMBER));
        species.setPhenologies(phenologies);
        species.setHabitatType("rural");
        return species;
    }
}
