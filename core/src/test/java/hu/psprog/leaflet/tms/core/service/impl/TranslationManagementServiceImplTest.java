package hu.psprog.leaflet.tms.core.service.impl;

import hu.psprog.leaflet.tms.core.dao.TranslationPackDAO;
import hu.psprog.leaflet.tms.core.entity.TranslationPack;
import hu.psprog.leaflet.tms.core.exception.TranslationPackCreationException;
import hu.psprog.leaflet.tms.core.exception.TranslationPackNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link TranslationManagementServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TranslationManagementServiceImplTest {

    private static final String PACK_SHARED = "shared";
    private static final String PACK_APP_1 = "app1";
    private static final List<String> PACKS = Arrays.asList(PACK_SHARED, PACK_APP_1);
    private static final Locale LANGUAGE_HU = Locale.forLanguageTag("HU");
    private static final UUID PACK_ID = UUID.randomUUID();

    private static final TranslationPack TRANSLATION_PACK_SHARED_EN_LATEST_DISABLED = prepareTranslationPack(PACK_SHARED, Locale.ENGLISH, prepareTimestamp(27), false);
    private static final TranslationPack TRANSLATION_PACK_SHARED_EN_PREVIOUS_DISABLED = prepareTranslationPack(PACK_SHARED, Locale.ENGLISH, prepareTimestamp(25), false);
    private static final TranslationPack TRANSLATION_PACK_SHARED_EN_FIRST_ENABLED = prepareTranslationPack(PACK_SHARED, Locale.ENGLISH, prepareTimestamp(1), true);
    private static final TranslationPack TRANSLATION_PACK_SHARED_HU_LATEST_ENABLED = prepareTranslationPack(PACK_SHARED, LANGUAGE_HU, prepareTimestamp(27), true);
    private static final TranslationPack TRANSLATION_PACK_SHARED_HU_PREVIOUS_ENABLED = prepareTranslationPack(PACK_SHARED, LANGUAGE_HU, prepareTimestamp(25), true);
    private static final TranslationPack TRANSLATION_PACK_SHARED_HU_FIRST_DISABLED = prepareTranslationPack(PACK_SHARED, LANGUAGE_HU, prepareTimestamp(1), false);
    private static final TranslationPack TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED = prepareTranslationPack(PACK_APP_1, Locale.ENGLISH, prepareTimestamp(25), true);
    private static final TranslationPack TRANSLATION_PACK_APP1_EN_LATEST_DISABLED = prepareTranslationPack(PACK_APP_1, Locale.ENGLISH, prepareTimestamp(27), false);
    private static final TranslationPack TRANSLATION_PACK_APP1_HU_PREVIOUS_DISABLED = prepareTranslationPack(PACK_APP_1, LANGUAGE_HU, prepareTimestamp(25), false);
    private static final TranslationPack TRANSLATION_PACK_APP1_HU_LATEST_ENABLED = prepareTranslationPack(PACK_APP_1, LANGUAGE_HU, prepareTimestamp(27), true);

    private static final List<TranslationPack> EXISTING_TRANSLATION_PACKS = Arrays.asList(
            TRANSLATION_PACK_APP1_HU_PREVIOUS_DISABLED,
            TRANSLATION_PACK_APP1_HU_LATEST_ENABLED,
            TRANSLATION_PACK_APP1_EN_LATEST_DISABLED,
            TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED,
            TRANSLATION_PACK_SHARED_EN_FIRST_ENABLED,
            TRANSLATION_PACK_SHARED_EN_LATEST_DISABLED,
            TRANSLATION_PACK_SHARED_EN_PREVIOUS_DISABLED,
            TRANSLATION_PACK_SHARED_HU_PREVIOUS_ENABLED,
            TRANSLATION_PACK_SHARED_HU_LATEST_ENABLED,
            TRANSLATION_PACK_SHARED_HU_FIRST_DISABLED);

    @Mock
    private TranslationPackDAO translationPackDAO;

    @InjectMocks
    private TranslationManagementServiceImpl translationManagementService;

    @Test
    public void shouldReturnLatestEnabledPacks() {

        // given
        given(translationPackDAO.findAllByPackNameIn(PACKS)).willReturn(EXISTING_TRANSLATION_PACKS);

        // when
        Set<TranslationPack> result = translationManagementService.retrieveLatestEnabledPacks(PACKS);

        // then
        assertThat(result.size(), equalTo(4));
        assertThat(result.containsAll(Arrays.asList(
                TRANSLATION_PACK_SHARED_EN_FIRST_ENABLED,
                TRANSLATION_PACK_SHARED_HU_LATEST_ENABLED,
                TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED,
                TRANSLATION_PACK_APP1_HU_LATEST_ENABLED)), is(true));
    }

    @Test
    public void shouldRetrievePackMetaInfo() {

        // given
        given(translationPackDAO.findAll()).willReturn(Collections.singletonList(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED));

        // when
        List<TranslationPack> result = translationManagementService.retrieveAllTranslationPack();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0), equalTo(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED));
    }

    @Test
    public void shouldGetPack() throws TranslationPackNotFoundException {

        // given
        given(translationPackDAO.exists(PACK_ID)).willReturn(true);
        given(translationPackDAO.getByID(PACK_ID)).willReturn(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED);

        // when
        TranslationPack result = translationManagementService.getPack(PACK_ID);

        // then
        assertThat(result, equalTo(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED));
    }

    @Test
    public void shouldGetPackThrowException() {

        // given
        given(translationPackDAO.exists(PACK_ID)).willReturn(false);

        // when
        Assertions.assertThrows(TranslationPackNotFoundException.class, () -> translationManagementService.getPack(PACK_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldCreatePack() throws TranslationPackCreationException {

        // given
        given(translationPackDAO.save(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED)).willReturn(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED);

        // when
        TranslationPack result = translationManagementService.createPack(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED);

        // then
        assertThat(result, equalTo(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED));
    }

    @Test
    public void shouldCreatePackThrowException() {

        // given
        given(translationPackDAO.save(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED)).willReturn(null);

        // when
        Assertions.assertThrows(TranslationPackCreationException.class, () -> translationManagementService.createPack(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED));

        // then
        // exception expected
    }

    @Test
    public void shouldChangeStatusWithDisabledPack() throws TranslationPackNotFoundException {

        // given
        given(translationPackDAO.exists(PACK_ID)).willReturn(true);
        given(translationPackDAO.getByID(PACK_ID)).willReturn(TRANSLATION_PACK_APP1_EN_LATEST_DISABLED);

        // when
        translationManagementService.changeStatus(PACK_ID);

        // then
        verify(translationPackDAO).setStatus(PACK_ID, true);
    }

    @Test
    public void shouldChangeStatusWithEnabledPack() throws TranslationPackNotFoundException {

        // given
        given(translationPackDAO.exists(PACK_ID)).willReturn(true);
        given(translationPackDAO.getByID(PACK_ID)).willReturn(TRANSLATION_PACK_APP1_EN_PREVIOUS_ENABLED);

        // when
        translationManagementService.changeStatus(PACK_ID);

        // then
        verify(translationPackDAO).setStatus(PACK_ID, false);
    }

    @Test
    public void shouldChangeStatusThrowException() {

        // given
        given(translationPackDAO.exists(PACK_ID)).willReturn(false);

        // when
        Assertions.assertThrows(TranslationPackNotFoundException.class, () -> translationManagementService.changeStatus(PACK_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldDeletePack() throws TranslationPackNotFoundException {

        // given
        given(translationPackDAO.exists(PACK_ID)).willReturn(true);

        // when
        translationManagementService.deletePack(PACK_ID);

        // then
        verify(translationPackDAO).delete(PACK_ID);
    }

    @Test
    public void shouldDeletePackThrowException() {

        // given
        given(translationPackDAO.exists(PACK_ID)).willReturn(false);

        // when
        Assertions.assertThrows(TranslationPackNotFoundException.class, () -> translationManagementService.deletePack(PACK_ID));

        // then
        // exception expected
    }

    private static Timestamp prepareTimestamp(int day) {

        long time = new Calendar.Builder()
                .setDate(2018, 2, day)
                .build()
                .getTimeInMillis();

        return new Timestamp(time);
    }

    private static TranslationPack prepareTranslationPack(String name, Locale locale, Timestamp created, boolean enabled) {

        return TranslationPack.builder()
                .id(UUID.randomUUID())
                .created(created)
                .enabled(enabled)
                .packName(name)
                .locale(locale)
                .build();
    }
}
