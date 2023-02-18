package com.natura.web.server.persistence.database;

import com.natura.web.server.mapper.IdentificationMapper;
import com.natura.web.server.mapper.UserMapper;
import com.natura.web.server.model.Identification;
import com.natura.web.server.model.User;
import com.natura.web.server.persistence.database.repository.IdentificationRepository;
import com.natura.web.server.provider.IdentificationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DatabaseIdentificationProvider implements IdentificationProvider {

    @Autowired
    IdentificationMapper identificationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    IdentificationRepository identificationRepository;

    @Override
    public Identification save(Identification identification) {
        return identificationMapper.map(identificationRepository.save(identificationMapper.map(identification)));
    }

    @Override
    public Optional<Identification> getIdentificationByEntryIdAndSpeciesId(Long entryId, Long speciesId) {
        return identificationMapper.map(identificationRepository.findByIdEntryIdAndIdSpeciesId(entryId, speciesId));
    }

    @Override
    public List<Identification> getIdentificationsBySuggestedByUser(User user) {
        return identificationMapper.map(identificationRepository.findBySuggestedBy(userMapper.map(user)));
    }

    @Override
    public List<Identification> getIdentificationsByEntryId(Long entryId) {
        return identificationMapper.map(identificationRepository.findByIdEntryId(entryId));
    }
}
