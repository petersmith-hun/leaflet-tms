package hu.psprog.leaflet.tms.core.dao.impl;

import hu.psprog.leaflet.tms.core.dao.TranslationPackDAO;
import hu.psprog.leaflet.tms.core.entity.TranslationPack;
import hu.psprog.leaflet.tms.core.repository.TranslationPackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link TranslationPackDAO}.
 *
 * @author Peter Smith
 */
@Component
public class TranslationPackDAOImpl implements TranslationPackDAO {

    private final TranslationPackRepository translationPackRepository;

    @Autowired
    public TranslationPackDAOImpl(TranslationPackRepository translationPackRepository) {
        this.translationPackRepository = translationPackRepository;
    }

    @Override
    public List<TranslationPack> findAll() {
        return translationPackRepository.findAll();
    }

    @Override
    public List<TranslationPack> findAllByPackNameIn(List<String> packs) {
        return translationPackRepository.findAllByPackNameIn(packs);
    }

    @Override
    public boolean exists(UUID packID) {
        return translationPackRepository.existsById(packID);
    }

    @Override
    public TranslationPack getByID(UUID packID) {
        return translationPackRepository.findById(packID).orElse(null);
    }

    @Override
    public TranslationPack save(TranslationPack translationPack) {
        return translationPackRepository.save(translationPack);
    }

    @Override
    public void setStatus(UUID packID, boolean enabled) {

        translationPackRepository.findById(packID).ifPresent(pack -> {
            pack.setEnabled(enabled);
            translationPackRepository.save(pack);
        });
    }

    @Override
    public void delete(UUID packID) {
        translationPackRepository.deleteById(packID);
    }
}
