package hu.psprog.leaflet.tms.web.conversion;

import hu.psprog.leaflet.tms.core.entity.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationDefinition;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts a {@link TranslationPack} (internal domain) object to {@link hu.psprog.leaflet.translation.api.domain.TranslationPack} (API domain).
 *
 * @author Peter Smith
 */
@Component
public class TranslationPackConverter implements Converter<TranslationPack, hu.psprog.leaflet.translation.api.domain.TranslationPack> {

    @Override
    public hu.psprog.leaflet.translation.api.domain.TranslationPack convert(TranslationPack source) {

        return hu.psprog.leaflet.translation.api.domain.TranslationPack.getBuilder()
                .withId(source.getId())
                .withPackName(source.getPackName())
                .withLocale(source.getLocale())
                .withEnabled(source.isEnabled())
                .withDefinitions(convertDefinitions(source))
                .withCreated(source.getCreated())
                .build();
    }

    private List<TranslationDefinition> convertDefinitions(TranslationPack source) {

        return source.getDefinitions()
                .stream()
                .map(translationDefinition -> TranslationDefinition.getBuilder()
                        .withKey(translationDefinition.getKey())
                        .withValue(translationDefinition.getValue())
                        .build())
                .toList();
    }
}
