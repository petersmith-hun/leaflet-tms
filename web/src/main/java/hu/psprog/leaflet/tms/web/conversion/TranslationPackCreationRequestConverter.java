package hu.psprog.leaflet.tms.web.conversion;

import hu.psprog.leaflet.tms.core.entity.TranslationDefinition;
import hu.psprog.leaflet.tms.core.entity.TranslationPack;
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
 *  - enabled: every pack is created with enabled status by default
 *  - created: current time
 *
 * @author Peter Smith
 */
@Component
public class TranslationPackCreationRequestConverter implements Converter<TranslationPackCreationRequest, TranslationPack> {

    private static final boolean ENABLED_BY_DEFAULT = true;

    @Override
    public TranslationPack convert(TranslationPackCreationRequest source) {

        return TranslationPack.builder()
                .id(UUID.randomUUID())
                .packName(source.getPackName())
                .locale(source.getLocale())
                .definitions(source.getDefinitions().entrySet().stream()
                        .map(entry -> new TranslationDefinition(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()))
                .enabled(ENABLED_BY_DEFAULT)
                .created(new Date())
                .build();
    }
}
