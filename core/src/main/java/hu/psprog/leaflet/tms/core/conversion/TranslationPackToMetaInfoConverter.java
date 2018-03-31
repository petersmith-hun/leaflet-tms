package hu.psprog.leaflet.tms.core.conversion;

import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackMetaInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link TranslationPack} to {@link TranslationPackMetaInfo}.
 *
 * @author Peter Smith
 */
@Component
public class TranslationPackToMetaInfoConverter implements Converter<TranslationPack, TranslationPackMetaInfo> {

    @Override
    public TranslationPackMetaInfo convert(TranslationPack source) {
        return TranslationPackMetaInfo.getMetaInfoBuilder()
                .withId(source.getId())
                .withPackName(source.getPackName())
                .withLocale(source.getLocale())
                .withEnabled(source.isEnabled())
                .withCreated(source.getCreated())
                .build();
    }
}
