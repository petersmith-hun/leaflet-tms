package hu.psprog.leaflet.tms.web.conversion;

import hu.psprog.leaflet.tms.core.entity.TranslationPack;
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
        return TranslationPackMetaInfo.getBuilder()
                .withId(source.getId())
                .withPackName(source.getPackName())
                .withLocale(source.getLocale())
                .withEnabled(source.isEnabled())
                .withCreated(source.getCreated())
                .build();
    }
}
