package hu.psprog.leaflet.tms.core.conversion;

import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackMetaInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TranslationPackToMetaInfoConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TranslationPackToMetaInfoConverterTest {

    private static final UUID PACK_ID = UUID.randomUUID();
    private static final Date CREATED = new Date();
    private static final boolean ENABLED = true;
    private static final Locale LOCALE = Locale.ENGLISH;
    private static final String PACK_NAME = "test-pack";

    private static final TranslationPack TRANSLATION_PACK = TranslationPack.getPackBuilder()
            .withId(PACK_ID)
            .withCreated(CREATED)
            .withEnabled(ENABLED)
            .withLocale(LOCALE)
            .withPackName(PACK_NAME)
            .build();

    private static final TranslationPackMetaInfo TRANSLATION_PACK_META_INFO = TranslationPackMetaInfo.getMetaInfoBuilder()
            .withId(PACK_ID)
            .withCreated(CREATED)
            .withEnabled(ENABLED)
            .withLocale(LOCALE)
            .withPackName(PACK_NAME)
            .build();

    @InjectMocks
    private TranslationPackToMetaInfoConverter converter;

    @Test
    public void shouldConvert() {

        // when
        TranslationPackMetaInfo result = converter.convert(TRANSLATION_PACK);

        // then
        assertThat(result, equalTo(TRANSLATION_PACK_META_INFO));
    }
}