package hu.psprog.leaflet.tms.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Translation pack (meta information with definitions) domain class.
 *
 * @author Peter Smith
 */
@Document
public class TranslationPack extends TranslationPackMetaInfo {

    private List<TranslationDefinition> definitions;

    public List<TranslationDefinition> getDefinitions() {
        return definitions;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TranslationPack that = (TranslationPack) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(definitions, that.definitions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(definitions)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("definitions", definitions)
                .append("id", id)
                .append("packName", packName)
                .append("locale", locale)
                .append("enabled", enabled)
                .append("created", created)
                .toString();
    }

    public static TranslationPackBuilder getPackBuilder() {
        return new TranslationPackBuilder();
    }

    /**
     * Builder for {@link TranslationPack}.
     */
    public static final class TranslationPackBuilder {
        private UUID id;
        private String packName;
        private Locale locale;
        private boolean enabled;
        private Date created;
        private List<TranslationDefinition> definitions;

        private TranslationPackBuilder() {
        }

        public TranslationPackBuilder withDefinitions(List<TranslationDefinition> definitions) {
            this.definitions = definitions;
            return this;
        }

        public TranslationPackBuilder withId(UUID id) {
            this.id = id;
            return this;
        }

        public TranslationPackBuilder withPackName(String packName) {
            this.packName = packName;
            return this;
        }

        public TranslationPackBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public TranslationPackBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public TranslationPackBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public TranslationPack build() {
            TranslationPack translationPack = new TranslationPack();
            translationPack.definitions = this.definitions;
            translationPack.locale = this.locale;
            translationPack.created = this.created;
            translationPack.id = this.id;
            translationPack.enabled = this.enabled;
            translationPack.packName = this.packName;
            return translationPack;
        }
    }
}
