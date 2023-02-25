package com.natura.web.server.configuration;

import com.natura.web.server.provider.CommentProvider;
import com.natura.web.server.provider.EntryProvider;
import com.natura.web.server.provider.FlowerProvider;
import com.natura.web.server.provider.IdentificationProvider;
import com.natura.web.server.provider.ImageProvider;
import com.natura.web.server.provider.InsectProvider;
import com.natura.web.server.provider.SpeciesProvider;
import com.natura.web.server.provider.UserProvider;
import com.natura.web.server.service.EntryService;
import com.natura.web.server.service.FlowerService;
import com.natura.web.server.service.IdentificationService;
import com.natura.web.server.service.ImageService;
import com.natura.web.server.service.InsectService;
import com.natura.web.server.service.SpeciesService;
import com.natura.web.server.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfiguration {

    @Value("${validation.countNeeded}")
    private Long validationCount;

    @Bean
    public UserService userService(final UserProvider userProvider) {
        return new UserService(userProvider);
    }

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
