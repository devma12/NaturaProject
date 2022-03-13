package com.natura.web.server.providers;

import com.natura.web.server.model.Insect;

import java.util.List;
import java.util.Optional;

public interface InsectProvider {

    Optional<Insect> getInsectById(Long id);

    List<Insect> getInsects();

    List<Insect> getInsectsByCreatedBy(Long userId);
}
