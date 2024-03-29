package hu.psprog.leaflet.tms.web.rest.controller;

import hu.psprog.leaflet.bridge.client.domain.error.ErrorMessageResponse;
import hu.psprog.leaflet.bridge.client.domain.error.ValidationErrorMessageListResponse;
import hu.psprog.leaflet.bridge.client.domain.error.ValidationErrorMessageResponse;
import hu.psprog.leaflet.tms.core.exception.TranslationPackCreationException;
import hu.psprog.leaflet.tms.core.exception.TranslationPackNotFoundException;
import hu.psprog.leaflet.tms.core.service.TranslationManagementService;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;
import hu.psprog.leaflet.translation.api.domain.TranslationPackMetaInfo;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Translation management controller.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(TranslationController.PATH_TRANSLATIONS)
public class TranslationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TranslationController.class);

    private static final String UNEXPECTED_EXCEPTION_OCCURRED = "Unexpected exception occurred";

    private static final String PATH_PACK_ID = "/{packID}";
    private static final String PATH_STATUS = PATH_PACK_ID + "/status";
    private static final String PARAMETER_PACKS = "packs";
    private static final String PARAMETER_PACK_ID = "packID";

    static final String PATH_TRANSLATIONS = "/translations";

    private final TranslationManagementService translationManagementService;
    private final ConversionService conversionService;

    @Autowired
    public TranslationController(TranslationManagementService translationManagementService, ConversionService conversionService) {
        this.translationManagementService = translationManagementService;
        this.conversionService = conversionService;
    }

    /**
     * GET /translations?packs=pack1[,pack2,...]
     *
     * Returns latest enabled packs in all available languages by given pack names.
     * Directly for message source usage.
     *
     * @param packs pack names to retrieve
     * @return set of available translation packs
     */
    @RequestMapping(method = RequestMethod.GET, params = PARAMETER_PACKS)
    public ResponseEntity<Set<TranslationPack>> retrievePacks(@RequestParam(value = PARAMETER_PACKS) List<String> packs) {

        var translationPacks = translationManagementService.retrieveLatestEnabledPacks(packs)
                .stream()
                .map(translationPack -> conversionService.convert(translationPack, TranslationPack.class))
                .collect(Collectors.toSet());

        return ResponseEntity
                .ok(translationPacks);
    }

    /**
     * GET /translations
     * Returns meta information of existing packs.
     *
     * @return meta information list
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TranslationPackMetaInfo>> listStoredPacks() {

        var translationPacks = translationManagementService.retrieveAllTranslationPack()
                .stream()
                .map(translationPack -> conversionService.convert(translationPack, TranslationPackMetaInfo.class))
                .toList();

        return ResponseEntity
                .ok(translationPacks);
    }

    /**
     * GET /translations/{packID}
     * Returns translation pack identified by given ID.
     *
     * @param packID ID of the translation pack to return
     * @return TranslationPack identified by given ID or exception if it does not exist
     * @throws TranslationPackNotFoundException if given translation pack does not exist
     */
    @RequestMapping(method = RequestMethod.GET, path = PATH_PACK_ID)
    public ResponseEntity<TranslationPack> getPackByID(@PathVariable(PARAMETER_PACK_ID) UUID packID) throws TranslationPackNotFoundException {

        var translationPack = translationManagementService.getPack(packID);

        return ResponseEntity
                .ok(conversionService.convert(translationPack, TranslationPack.class));
    }

    /**
     * POST /translations
     * Creates a new translation pack.
     *
     * @param translationPackCreationRequest translation pack data (locale, pack name and definitions)
     * @param bindingResult validation results
     * @return created {@link TranslationPack}
     * @throws TranslationPackCreationException if translation pack could not be created
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createTranslationPack(@RequestBody @Valid TranslationPackCreationRequest translationPackCreationRequest, BindingResult bindingResult)
            throws TranslationPackCreationException {

        ResponseEntity<?> responseEntity;
        if (bindingResult.hasErrors()) {
            responseEntity = ResponseEntity
                    .badRequest()
                    .body(buildValidationErrorMessage(translationPackCreationRequest, bindingResult));
        } else {
            var translationPack = conversionService.convert(translationPackCreationRequest, hu.psprog.leaflet.tms.core.entity.TranslationPack.class);
            var savedTranslationPack = translationManagementService.createPack(translationPack);

            responseEntity = ResponseEntity
                    .created(createURI(savedTranslationPack))
                    .body(conversionService.convert(savedTranslationPack, TranslationPack.class));
        }

        return responseEntity;
    }

    /**
     * PUT /translations/{packID}/status
     * Changes status (enabled/disabled) of given translation pack.
     *
     * @param packID ID of the translation pack to change status of
     * @return modified {@link TranslationPack}
     * @throws TranslationPackNotFoundException if given translation pack does not exist
     */
    @RequestMapping(method = RequestMethod.PUT, path = PATH_STATUS)
    public ResponseEntity<TranslationPack> changePackStatus(@PathVariable(PARAMETER_PACK_ID) UUID packID) throws TranslationPackNotFoundException {

        var translationPack = translationManagementService.changeStatus(packID);

        return ResponseEntity
                .created(createURI(translationPack))
                .body(conversionService.convert(translationPack, TranslationPack.class));
    }

    /**
     * DELETE /translations/{packID}
     * Deletes given translation pack.
     *
     * @param packID ID of the translation pack to delete
     * @return empty response
     * @throws TranslationPackNotFoundException if given translation pack does not exist
     */
    @RequestMapping(method = RequestMethod.DELETE, path = PATH_PACK_ID)
    public ResponseEntity<Void> deleteTranslationPack(@PathVariable(PARAMETER_PACK_ID) UUID packID) throws TranslationPackNotFoundException {

        translationManagementService.deletePack(packID);

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * Exception handler for translation pack retrieval exceptions.
     *
     * @param exception exception that has been thrown
     * @return exception message with HTTP status 404
     */
    @ExceptionHandler(TranslationPackNotFoundException.class)
    ResponseEntity<ErrorMessageResponse> retrievalExceptionHandler(TranslationPackNotFoundException exception) {

        LOGGER.error("Failed to retrieve translation pack.", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildExceptionMessageForResponse(exception));

    }

    /**
     * Exception handler translation pack creation exceptions.
     *
     * @param exception exception that has been thrown
     * @return exception message with HTTP status 409
     */
    @ExceptionHandler(TranslationPackCreationException.class)
    ResponseEntity<ErrorMessageResponse> creationExceptionHandler(TranslationPackCreationException exception) {

        LOGGER.error("Failed to store translation pack.", exception);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildExceptionMessageForResponse(exception));
    }

    /**
     * Default exception handler.
     *
     * @param exception exception that has been thrown
     * @return exception message with HTTP status 500
     */
    @ExceptionHandler
    ResponseEntity<ErrorMessageResponse> defaultExceptionHandler(Exception exception) {

        LOGGER.error(UNEXPECTED_EXCEPTION_OCCURRED, exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildExceptionMessageForResponse());
    }

    private ValidationErrorMessageListResponse buildValidationErrorMessage(TranslationPackCreationRequest creationRequest, BindingResult bindingResult) {

        LOGGER.warn("Failed to validate translation pack creation request [{}].", creationRequest);

        return ValidationErrorMessageListResponse.getBuilder()
                .withValidation(bindingResult.getFieldErrors().stream()
                        .map(fieldError -> ValidationErrorMessageResponse.getBuilder()
                                .withField(fieldError.getField())
                                .withMessage(fieldError.getDefaultMessage())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private ErrorMessageResponse buildExceptionMessageForResponse() {

        return ErrorMessageResponse.getBuilder()
                .withMessage(UNEXPECTED_EXCEPTION_OCCURRED)
                .build();
    }

    private ErrorMessageResponse buildExceptionMessageForResponse(Exception exception) {

        return ErrorMessageResponse.getBuilder()
                .withMessage(exception.getMessage())
                .build();
    }

    private URI createURI(hu.psprog.leaflet.tms.core.entity.TranslationPack translationPack) {
        return URI.create(String.format("%s/%s", PATH_TRANSLATIONS, translationPack.getId()));
    }
}
