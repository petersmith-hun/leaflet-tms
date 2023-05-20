package hu.psprog.leaflet.tms.core.service;

import hu.psprog.leaflet.tms.core.entity.TranslationPack;
import hu.psprog.leaflet.tms.core.exception.TranslationPackCreationException;
import hu.psprog.leaflet.tms.core.exception.TranslationPackNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Translation packs handling operations interface.
 *
 * @author Peter Smith
 */
public interface TranslationManagementService {

    /**
     * Retrieves latest enabled translation packs by their names.
     * Non-existing packs will simply be skipped.
     * If a pack exists with the given, it will be returned in all available, enabled languages.
     * If a pack has multiple versions, the latest enabled will be returned.
     *
     * @param packs pack names to retrieve
     * @return available {@link TranslationPack}s as {@link Set}
     */
    Set<TranslationPack> retrieveLatestEnabledPacks(List<String> packs);

    /**
     * Retrieves all available translation pack.
     *
     * @return List of {@link TranslationPack} objects
     */
    List<TranslationPack> retrieveAllTranslationPack();

    /**
     * Retrieves pack identified by given ID as {@link UUID}.
     *
     * @param packID ID of the pack to return
     * @return existing {@link TranslationPack} identified by given ID or exception if not found
     * @throws TranslationPackNotFoundException if given translation pack not found
     */
    TranslationPack getPack(UUID packID) throws TranslationPackNotFoundException;

    /**
     * Creates a new translation pack.
     *
     * @param translationPackCreationRequest translation pack data to create
     * @return created {@link TranslationPack}
     * @throws TranslationPackCreationException if translation pack could not be created
     */
    TranslationPack createPack(TranslationPack translationPackCreationRequest) throws TranslationPackCreationException;

    /**
     * Changes status (enabled/disabled) of the translation pack identified by given ID.
     *
     * @param packID ID of the pack to change status of
     * @return modified {@link TranslationPack}
     * @throws TranslationPackNotFoundException if given translation pack not found
     */
    TranslationPack changeStatus(UUID packID) throws TranslationPackNotFoundException;

    /**
     * Deletes the translation pack identified by given ID.
     *
     * @param packID ID of the pack to delete
     * @throws TranslationPackNotFoundException if given translation pack not found
     */
    void deletePack(UUID packID) throws TranslationPackNotFoundException;
}
