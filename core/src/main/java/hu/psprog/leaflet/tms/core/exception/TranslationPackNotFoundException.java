package hu.psprog.leaflet.tms.core.exception;

import java.util.UUID;

/**
 * @author Peter Smith
 */
public class TranslationPackNotFoundException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Requested translation pack [%s] not found";

    public TranslationPackNotFoundException(UUID packID) {
        super(String.format(EXCEPTION_MESSAGE, packID));
    }
}
