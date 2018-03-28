package hu.psprog.leaflet.tms.core.service.impl;

import hu.psprog.leaflet.tms.core.dao.TranslationPackDAO;
import hu.psprog.leaflet.tms.core.domain.TranslationPack;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TranslationManagementServiceImplTest {

    private static final String PACK_SHARED = "shared";
    private static final String PACK_APP_1 = "app1";
    private static final List<String> PACKS = Arrays.asList(PACK_SHARED, PACK_APP_1);
    private static final Locale LANGUAGE_HU = Locale.forLanguageTag("HU");

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

    private static Timestamp prepareTimestamp(int day) {

        long time = new Calendar.Builder()
                .setDate(2018, 2, day)
                .build()
                .getTimeInMillis();

        return new Timestamp(time);
    }

    private static TranslationPack prepareTranslationPack(String name, Locale locale, Timestamp created, boolean enabled) {
        return TranslationPack.getPackBuilder()
                .withId(UUID.randomUUID())
                .withCreated(created)
                .withEnabled(enabled)
                .withPackName(name)
                .withLocale(locale)
                .build();
    }
}
