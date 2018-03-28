package hu.psprog.leaflet.tms.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Translation pack meta information domain class.
 *
 * @author Peter Smith
 */
public class TranslationPackMetaInfo {

    @Id
    UUID id;
    String packName;
    Locale locale;
    boolean enabled;
    Date created;

    public UUID getId() {
        return id;
    }

    public String getPackName() {
        return packName;
    }

    public Locale getLocale() {
        return locale;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TranslationPackMetaInfo that = (TranslationPackMetaInfo) o;

        return new EqualsBuilder()
                .append(enabled, that.enabled)
                .append(id, that.id)
                .append(packName, that.packName)
                .append(locale, that.locale)
                .append(created, that.created)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(packName)
                .append(locale)
                .append(enabled)
                .append(created)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("packName", packName)
                .append("locale", locale)
                .append("enabled", enabled)
                .append("created", created)
                .toString();
    }

    public static TranslationPackMetaInfoBuilder getMetaInfoBuilder() {
        return new TranslationPackMetaInfoBuilder();
    }

    /**
     * Builder for {@link TranslationPackMetaInfo}.
     */
    public static final class TranslationPackMetaInfoBuilder {
        private UUID id;
        private String packName;
        private Locale locale;
        private boolean enabled;
        private Date created;

        private TranslationPackMetaInfoBuilder() {
        }

        public TranslationPackMetaInfoBuilder withId(UUID id) {
            this.id = id;
            return this;
        }

        public TranslationPackMetaInfoBuilder withPackName(String packName) {
            this.packName = packName;
            return this;
        }

        public TranslationPackMetaInfoBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public TranslationPackMetaInfoBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public TranslationPackMetaInfoBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public TranslationPackMetaInfo build() {
            TranslationPackMetaInfo translationPackMetaInfo = new TranslationPackMetaInfo();
            translationPackMetaInfo.locale = this.locale;
            translationPackMetaInfo.created = this.created;
            translationPackMetaInfo.id = this.id;
            translationPackMetaInfo.enabled = this.enabled;
            translationPackMetaInfo.packName = this.packName;
            return translationPackMetaInfo;
        }
    }
}
