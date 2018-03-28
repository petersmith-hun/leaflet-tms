package hu.psprog.leaflet.tms.core.exception;

import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;

/**
 * @author Peter Smith
 */
public class TranslationPackCreationException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Failed to create translation pack for request [%s]";

    public TranslationPackCreationException(TranslationPackCreationRequest translationPackCreationRequest) {
        super(String.format(EXCEPTION_MESSAGE, translationPackCreationRequest));
    }
}
