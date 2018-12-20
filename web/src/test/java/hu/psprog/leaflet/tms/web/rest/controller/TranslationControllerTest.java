package hu.psprog.leaflet.tms.web.rest.controller;

import hu.psprog.leaflet.tms.core.exception.TranslationPackCreationException;
import hu.psprog.leaflet.tms.core.exception.TranslationPackNotFoundException;
import hu.psprog.leaflet.tms.core.service.TranslationManagementService;
import hu.psprog.leaflet.tms.web.exception.model.ErrorMessageResponse;
import hu.psprog.leaflet.tms.web.exception.model.ValidationErrorMessageListResponse;
import hu.psprog.leaflet.tms.web.exception.model.ValidationErrorMessageResponse;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link TranslationController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TranslationControllerTest {

    private static final UUID PACK_ID = UUID.randomUUID();
    private static final URI EXPECTED_LOCATION = URI.create("/translations/" + PACK_ID);
    private static final List<String> PACKS = Collections.singletonList("pack1");
    private static final TranslationPackCreationRequest TRANSLATION_PACK_CREATION_REQUEST = new TranslationPackCreationRequest();
    private static final TranslationPack TRANSLATION_PACK = TranslationPack.getPackBuilder().withId(PACK_ID).build();
    private static final String VIOLATED_FIELD = "field1";
    private static final String DEFAULT_MESSAGE = "violation message";
    private static final ValidationErrorMessageListResponse EXPECTED_VALIDATION_ERROR_BODY = ValidationErrorMessageListResponse.getBuilder()
            .withValidation(Collections.singletonList(ValidationErrorMessageResponse.getExtendedBuilder()
                    .withField(VIOLATED_FIELD)
                    .withMessage(DEFAULT_MESSAGE)
                    .build()))
            .build();

    @Mock
    private TranslationManagementService translationManagementService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private TranslationController translationController;

    @Test
    public void shouldRetrievePacks() {

        // when
        ResponseEntity<?> result = translationController.retrievePacks(PACKS);

        // then
        verify(translationManagementService).retrieveLatestEnabledPacks(PACKS);
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void shouldListStoredPacks() {

        // when
        ResponseEntity<?> result = translationController.listStoredPacks();

        // then
        verify(translationManagementService).retrievePackMetaInfo();
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void shouldGetPackByID() throws TranslationPackNotFoundException {

        // when
        ResponseEntity<?> result = translationController.getPackByID(PACK_ID);

        // then
        verify(translationManagementService).getPack(PACK_ID);
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void shouldCreateTranslationPack() throws TranslationPackCreationException {

        // given
        given(bindingResult.hasErrors()).willReturn(false);
        given(translationManagementService.createPack(TRANSLATION_PACK_CREATION_REQUEST)).willReturn(TRANSLATION_PACK);

        // when
        ResponseEntity<?> result = translationController.createTranslationPack(TRANSLATION_PACK_CREATION_REQUEST, bindingResult);

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(result.getBody(), equalTo(TRANSLATION_PACK));
        assertThat(result.getHeaders().getLocation(), equalTo(EXPECTED_LOCATION));
    }

    @Test
    public void shouldCreateTranslationPackWithValidationError() throws TranslationPackCreationException {

        // given
        given(bindingResult.hasErrors()).willReturn(true);
        given(bindingResult.getFieldErrors()).willReturn(Collections.singletonList(new FieldError("object", VIOLATED_FIELD, DEFAULT_MESSAGE)));

        // when
        ResponseEntity<?> result = translationController.createTranslationPack(TRANSLATION_PACK_CREATION_REQUEST, bindingResult);

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(result.getBody(), equalTo(EXPECTED_VALIDATION_ERROR_BODY));
    }

    @Test
    public void shouldChangePackStatus() throws TranslationPackNotFoundException {

        // given
        given(translationManagementService.changeStatus(PACK_ID)).willReturn(TRANSLATION_PACK);

        // when
        ResponseEntity<?> result = translationController.changePackStatus(PACK_ID);

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(result.getBody(), equalTo(TRANSLATION_PACK));
        assertThat(result.getHeaders().getLocation(), equalTo(EXPECTED_LOCATION));
    }

    @Test
    public void shouldDeleteTranslationPack() throws TranslationPackNotFoundException {

        // when
        ResponseEntity<?> result = translationController.deleteTranslationPack(PACK_ID);

        // then
        verify(translationManagementService).deletePack(PACK_ID);
        assertThat(result.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

    @Test
    public void shouldHandleRetrievalException() {

        // when
        ResponseEntity<ErrorMessageResponse> result = translationController.retrievalExceptionHandler(new TranslationPackNotFoundException(PACK_ID));

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(result.getBody().getMessage(), equalTo(String.format("Requested translation pack [%s] not found", PACK_ID)));
    }

    @Test
    public void shouldHandleCreationException() {

        // when
        ResponseEntity<ErrorMessageResponse> result = translationController.creationExceptionHandler(new TranslationPackCreationException(TRANSLATION_PACK_CREATION_REQUEST));

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.CONFLICT));
        assertThat(result.getBody().getMessage(), equalTo(String.format("Failed to create translation pack for request [%s]", TRANSLATION_PACK_CREATION_REQUEST)));
    }

    @Test
    public void shouldHandleUnknownException() {

        // when
        ResponseEntity<ErrorMessageResponse> result = translationController.defaultExceptionHandler(new RuntimeException("any other exception"));

        // then
        assertThat(result.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(result.getBody().getMessage(), equalTo("Unexpected exception occurred"));
    }
}