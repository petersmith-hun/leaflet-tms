package hu.psprog.leaflet.tms.core.service.impl;

import hu.psprog.leaflet.tms.core.dao.TranslationPackDAO;
import hu.psprog.leaflet.tms.core.entity.TranslationPack;
import hu.psprog.leaflet.tms.core.exception.TranslationPackCreationException;
import hu.psprog.leaflet.tms.core.exception.TranslationPackNotFoundException;
import hu.psprog.leaflet.tms.core.service.TranslationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final TranslationPackDAO translationPackDAO;

    @Autowired
    public TranslationManagementServiceImpl(TranslationPackDAO translationPackDAO) {
        this.translationPackDAO = translationPackDAO;
    }

    @Override
    public Set<TranslationPack> retrieveLatestEnabledPacks(List<String> packs) {

        return translationPackDAO.findAllByPackNameIn(packs).stream()
                .filter(TranslationPack::isEnabled)
                .sorted(Comparator.comparing(TranslationPack::getCreated).reversed())
                .collect(Collectors.toCollection(() -> new TreeSet<>(TRANSLATION_PACK_COMPARATOR)));
    }

    @Override
    public List<TranslationPack> retrieveAllTranslationPack() {
        return translationPackDAO.findAll();
    }

    @Override
    public TranslationPack getPack(UUID packID) throws TranslationPackNotFoundException {

        assertPackExistence(packID);

        return translationPackDAO.getByID(packID);
    }

    @Override
    public TranslationPack createPack(TranslationPack translationPackCreationRequest) throws TranslationPackCreationException {

        TranslationPack createdTranslationPack = translationPackDAO.save(translationPackCreationRequest);

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
