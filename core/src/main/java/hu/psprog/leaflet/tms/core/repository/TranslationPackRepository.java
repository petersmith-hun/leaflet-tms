package hu.psprog.leaflet.tms.core.repository;

import hu.psprog.leaflet.tms.core.domain.TranslationPack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * {@link TranslationPack} Mongo repository interface.
 *
 * @author Peter Smith
 */
@Repository
public interface TranslationPackRepository extends MongoRepository<TranslationPack, UUID> {

    /**
     * Returns list of {@link TranslationPack} records by given list of pack names.
     *
     * @param packs pack names to return packs by
     * @return list of {@link TranslationPack} objects
     */
    List<TranslationPack> findAllByPackNameIn(List<String> packs);
}
