package hu.psprog.leaflet.tms.core.dao;

import hu.psprog.leaflet.translation.api.domain.TranslationPack;

import java.util.List;
import java.util.UUID;

/**
 * DAO interface for {@link TranslationPack} persistence operations.
 *
 * @author Peter Smith
 */
public interface TranslationPackDAO {

    /**
     * Returns all {@link TranslationPack} records as {@link List}.
     *
     * @return list of {@link TranslationPack} entries
     */
    List<TranslationPack> findAll();

    /**
     * Returns {@link TranslationPack} records as {@link List} where pack name is in the given collection.
     *
     * @param packs pack names to filter to
     * @return list of {@link TranslationPack} entries
     */
    List<TranslationPack> findAllByPackNameIn(List<String> packs);

    /**
     * Checks if given {@link TranslationPack} exists.
     *
     * @param packID ID of the pack to check existence of
     * @return {@code true} is given pack exists, {@code false} otherwise
     */
    boolean exists(UUID packID);

    /**
     * Returns {@link TranslationPack} identified by given pack ID.
     *
     * @param packID ID of the pack to return
     * @return TranslationPack object if found, {@code null} otherwise
     */
    TranslationPack getByID(UUID packID);

    /**
     * Stores given {@link TranslationPack} object.
     *
     * @param translationPack {@link TranslationPack} object to store
     * @return created {@link TranslationPack}
     */
    TranslationPack save(TranslationPack translationPack);

    /**
     * Updates status (enabled/disabled) of the given pack.
     *
     * @param packID ID of the pack to update status of
     * @param enabled pass {@code true} to enable pack, {@code false} to disable
     */
    void setStatus(UUID packID, boolean enabled);

    /**
     * Deletes pack identified by given packID.
     *
     * @param packID ID of the pack to delete
     */
    void delete(UUID packID);
}
