package hu.psprog.leaflet.tms.core.service.impl;

import hu.psprog.leaflet.tms.core.dao.TranslationPackDAO;
import hu.psprog.leaflet.tms.core.exception.TranslationPackCreationException;
import hu.psprog.leaflet.tms.core.exception.TranslationPackNotFoundException;
import hu.psprog.leaflet.tms.core.service.TranslationManagementService;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;
import hu.psprog.leaflet.translation.api.domain.TranslationPackMetaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TranslationManagementService}.
 *
 * @author Peter Smith
 */
@Service
public class TranslationManagementServiceImpl implements TranslationManagementService {

    private static final Comparator<TranslationPack> TRANSLATION_PACK_COMPARATOR = Comparator
            .comparing(TranslationPack::getPackName)
            .thenComparing(translationPack -> translationPack.getLocale().getLanguage());

    private TranslationPackDAO translationPackDAO;
    private ConversionService conversionService;

    @Autowired
    public TranslationManagementServiceImpl(TranslationPackDAO translationPackDAO, ConversionService conversionService) {
        this.translationPackDAO = translationPackDAO;
        this.conversionService = conversionService;
    }

    @Override
    public Set<TranslationPack> retrieveLatestEnabledPacks(List<String> packs) {

        return translationPackDAO.findAllByPackNameIn(packs).stream()
                .filter(TranslationPack::isEnabled)
                .sorted(Comparator.comparing(TranslationPackMetaInfo::getCreated).reversed())
                .collect(Collectors.toCollection(() -> new TreeSet<>(TRANSLATION_PACK_COMPARATOR)));
    }

    @Override
    public List<TranslationPackMetaInfo> retrievePackMetaInfo() {

        return translationPackDAO.findAll().stream()
                .map(translationPack -> conversionService.convert(translationPack, TranslationPackMetaInfo.class))
                .collect(Collectors.toList());
    }

    @Override
    public TranslationPack getPack(UUID packID) throws TranslationPackNotFoundException {

        assertPackExistence(packID);

        return translationPackDAO.getByID(packID);
    }

    @Override
    public TranslationPack createPack(TranslationPackCreationRequest translationPackCreationRequest) throws TranslationPackCreationException {

        TranslationPack translationPackToSave = conversionService.convert(translationPackCreationRequest, TranslationPack.class);
        TranslationPack createdTranslationPack = translationPackDAO.save(translationPackToSave);

        if (Objects.isNull(createdTranslationPack)) {
            throw new TranslationPackCreationException(translationPackCreationRequest);
        }

        return createdTranslationPack;
    }

    @Override
    public TranslationPack changeStatus(UUID packID) throws TranslationPackNotFoundException {

        assertPackExistence(packID);
        translationPackDAO.setStatus(packID, !translationPackDAO.getByID(packID).isEnabled());

        return translationPackDAO.getByID(packID);
    }

    @Override
    public void deletePack(UUID packID) throws TranslationPackNotFoundException {

        assertPackExistence(packID);
        translationPackDAO.delete(packID);
    }

    private void assertPackExistence(UUID packID) throws TranslationPackNotFoundException {
        if (!translationPackDAO.exists(packID)) {
            throw new TranslationPackNotFoundException(packID);
        }
    }
}
