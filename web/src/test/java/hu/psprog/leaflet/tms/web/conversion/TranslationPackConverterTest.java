package hu.psprog.leaflet.tms.web.conversion;

import hu.psprog.leaflet.tms.core.entity.TranslationDefinition;
import hu.psprog.leaflet.tms.core.entity.TranslationPack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TranslationPackConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class TranslationPackConverterTest {

    private static final UUID PACK_ID = UUID.randomUUID();
    private static final Date CREATED = new Date();
    private static final boolean ENABLED = true;
    private static final Locale LOCALE = Locale.ENGLISH;
    private static final String PACK_NAME = "test-pack";

    private static final TranslationPack TRANSLATION_PACK = TranslationPack.builder()
            .id(PACK_ID)
            .created(CREATED)
            .enabled(ENABLED)
            .locale(LOCALE)
            .packName(PACK_NAME)
            .definitions(List.of(
                    new TranslationDefinition("key1", "value1"),
                    new TranslationDefinition("key2", "value2")))
            .build();

    private static final hu.psprog.leaflet.translation.api.domain.TranslationPack API_TRANSLATION_PACK = hu.psprog.leaflet.translation.api.domain.TranslationPack.getBuilder()
            .withId(PACK_ID)
            .withCreated(CREATED)
            .withEnabled(ENABLED)
            .withLocale(LOCALE)
            .withPackName(PACK_NAME)
            .withDefinitions(List.of(
                    hu.psprog.leaflet.translation.api.domain.TranslationDefinition.getBuilder().withKey("key1").withValue("value1").build(),
                    hu.psprog.leaflet.translation.api.domain.TranslationDefinition.getBuilder().withKey("key2").withValue("value2").build()))
            .build();

    @InjectMocks
    private TranslationPackConverter converter;

    @Test
    public void shouldConvertInternalDomainTranslationPackToAPIDomain() {

        // when
        var result = converter.convert(TRANSLATION_PACK);

        // then
        assertThat(result, equalTo(API_TRANSLATION_PACK));
    }
}
