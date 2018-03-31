package hu.psprog.leaflet.tms.core.conversion;

import hu.psprog.leaflet.translation.api.domain.TranslationDefinition;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Converts {@link TranslationPackCreationRequest} to {@link TranslationPack}.
 * Defaults set:
 *  - id: random UUID
 *  - enabled: every packs are created with enabled status by default
 *  - created: current time
 *
 * @author Peter Smith
 */
@Component
public class TranslationPackCreationRequestConverter implements Converter<TranslationPackCreationRequest, TranslationPack> {

    private static final boolean ENABLED_BY_DEFAULT = true;

    @Override
    public TranslationPack convert(TranslationPackCreationRequest source) {

        return TranslationPack.getPackBuilder()
                .withId(UUID.randomUUID())
                .withPackName(source.getPackName())
                .withLocale(source.getLocale())
                .withDefinitions(source.getDefinitions().entrySet().stream()
                        .map(entry -> TranslationDefinition.getBuilder()
                                .withKey(entry.getKey())
                                .withValue(entry.getValue())
                                .build())
                        .collect(Collectors.toList()))
                .withEnabled(ENABLED_BY_DEFAULT)
                .withCreated(new Date())
                .build();
    }
}
