package hu.psprog.leaflet.tms.core.conversion;

import hu.psprog.leaflet.translation.api.domain.TranslationDefinition;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link TranslationPackCreationRequestConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TranslationPackCreationRequestConverterTest {

    private static final TranslationPackCreationRequest TRANSLATION_PACK_CREATION_REQUEST = new TranslationPackCreationRequest();

    private static final String PACK_NAME = "test-pack";
    private static final Locale LOCALE = Locale.ENGLISH;
    private static final String KEY_1 = "key-1";
    private static final String KEY_2 = "key-2";
    private static final String VALUE_1 = "value-1";
    private static final String VALUE_2 = "value-2";

    private static final TranslationDefinition TRANSLATION_DEFINITION_1 = TranslationDefinition.getBuilder()
            .withKey(KEY_1)
            .withValue(VALUE_1)
            .build();
    private static final TranslationDefinition TRANSLATION_DEFINITION_2 = TranslationDefinition.getBuilder()
            .withKey(KEY_2)
            .withValue(VALUE_2)
            .build();

    static {
        Map<String, String> definitions = new HashMap<>();
        definitions.put(KEY_1, VALUE_1);
        definitions.put(KEY_2, VALUE_2);

        TRANSLATION_PACK_CREATION_REQUEST.setPackName(PACK_NAME);
        TRANSLATION_PACK_CREATION_REQUEST.setLocale(LOCALE);
        TRANSLATION_PACK_CREATION_REQUEST.setDefinitions(definitions);
    }

    @InjectMocks
    private TranslationPackCreationRequestConverter converter;

    @Test
    public void shouldConvert() {

        // when
        TranslationPack result = converter.convert(TRANSLATION_PACK_CREATION_REQUEST);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getId() instanceof UUID, is(true));
        assertThat(result.getPackName(), equalTo(PACK_NAME));
        assertThat(result.getLocale(), equalTo(LOCALE));
        assertThat(result.isEnabled(), is(true));
        assertThat(result.getCreated(), notNullValue());
        assertThat(result.getCreated() instanceof Date, is(true));
        assertThat(result.getDefinitions().size(), equalTo(2));
        assertThat(result.getDefinitions().containsAll(Arrays.asList(TRANSLATION_DEFINITION_1, TRANSLATION_DEFINITION_2)), is(true));
    }
}