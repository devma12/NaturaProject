package com.natura.web.server.provider;

import com.natura.web.server.model.Identification;
import com.natura.web.server.model.User;

import java.util.List;
import java.util.Optional;

public interface IdentificationProvider {
    Identification save(Identification identification);

    Optional<Identification> getIdentificationByEntryIdAndSpeciesId(Long entryId, Long speciesId);

    List<Identification> getIdentificationsBySuggestedByUser(User user);

    List<Identification> getIdentificationsByEntryId(Long entryId);

}
