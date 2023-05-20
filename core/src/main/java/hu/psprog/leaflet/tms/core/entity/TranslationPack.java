package hu.psprog.leaflet.tms.core.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Translation pack document class.
 *
 * @author Peter Smith
 */
@Document
@Data
@Builder
public class TranslationPack {

    private UUID id;
    private String packName;
    private Locale locale;
    private boolean enabled;
    private Date created;
    private List<TranslationDefinition> definitions;
}
