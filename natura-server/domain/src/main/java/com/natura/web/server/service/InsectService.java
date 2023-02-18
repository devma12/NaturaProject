package com.natura.web.server.service;

import com.natura.web.server.model.Insect;
import com.natura.web.server.provider.InsectProvider;

import java.util.List;
import java.util.Optional;

public class InsectService {

    private final InsectProvider insectProvider;

    public InsectService(final InsectProvider insectProvider) {
        this.insectProvider = insectProvider;
    }

    public Optional<Insect> getInsectById(Long entryId) {
        return insectProvider.getInsectById(entryId);
    }

    public List<Insect> getInsects() {
        return insectProvider.getInsects();
    }

    public List<Insect> getInsectsByCreator(Long userId) {
        return insectProvider.getInsectsByCreatedBy(userId);
    }
}
