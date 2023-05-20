package hu.psprog.leaflet.tms.core.exception;

import hu.psprog.leaflet.tms.core.entity.TranslationPack;

/**
 * Exception to throw when a translation pack could not be created.
 *
 * @author Peter Smith
 */
public class TranslationPackCreationException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Failed to create translation pack for request [%s]";

    public TranslationPackCreationException(TranslationPack translationPackCreationRequest) {
        super(String.format(EXCEPTION_MESSAGE, translationPackCreationRequest));
    }
}
