package com.natura.web.server.configuration;

import com.natura.web.server.providers.CommentProvider;
import com.natura.web.server.providers.EntryProvider;
import com.natura.web.server.providers.FlowerProvider;
import com.natura.web.server.providers.IdentificationProvider;
import com.natura.web.server.providers.ImageProvider;
import com.natura.web.server.providers.InsectProvider;
import com.natura.web.server.providers.SpeciesProvider;
import com.natura.web.server.providers.UserProvider;
import com.natura.web.server.services.EntryService;
import com.natura.web.server.services.FlowerService;
import com.natura.web.server.services.IdentificationService;
import com.natura.web.server.services.ImageService;
import com.natura.web.server.services.InsectService;
import com.natura.web.server.services.SpeciesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestServicesConfiguration {

    @Value("${validation.countNeeded}")
    private Long validationCount;

    @Bean
    public SpeciesService speciesService(final SpeciesProvider speciesProvider) {
        return new SpeciesService(speciesProvider);
    }

    @Bean
    public FlowerService flowerService(final FlowerProvider flowerProvider) {
        return new FlowerService(flowerProvider);
    }

    @Bean
    public InsectService insectService(final InsectProvider insectProvider) {
        return new InsectService(insectProvider);
    }

    @Bean
    public ImageService imageService(final ImageProvider imageProvider) {
        return new ImageService(imageProvider);
    }

    @Bean
    public EntryService entryService(final ImageService imageService,
                        final UserProvider userProvider,
                        final EntryProvider entryProvider,
                        final SpeciesProvider speciesProvider,
                        final IdentificationProvider identificationProvider) {
        return new EntryService(imageService, userProvider, entryProvider, speciesProvider, identificationProvider);
    }

    @Bean
    public IdentificationService identificationService(final UserProvider userProvider,
                                 final EntryProvider entryProvider,
                                 final SpeciesProvider speciesProvider,
                                 final IdentificationProvider identificationProvider,
                                 final CommentProvider commentProvider) {
        return new IdentificationService(userProvider, entryProvider, speciesProvider, identificationProvider, commentProvider, validationCount);
    }
}
