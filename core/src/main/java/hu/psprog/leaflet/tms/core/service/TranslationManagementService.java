package hu.psprog.leaflet.tms.core.service;

import hu.psprog.leaflet.tms.core.exception.TranslationPackCreationException;
import hu.psprog.leaflet.tms.core.exception.TranslationPackNotFoundException;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;
import hu.psprog.leaflet.translation.api.domain.TranslationPackMetaInfo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Peter Smith
 */
public interface TranslationManagementService {

    Set<TranslationPack> retrieveLatestEnabledPacks(List<String> packs);

    List<TranslationPackMetaInfo> retrievePackMetaInfo();

    TranslationPack getPack(UUID packID) throws TranslationPackNotFoundException;

    TranslationPack createPack(TranslationPackCreationRequest translationPackCreationRequest) throws TranslationPackCreationException;

    TranslationPack changeStatus(UUID packID) throws TranslationPackNotFoundException;

    void deletePack(UUID packID) throws TranslationPackNotFoundException;
}
