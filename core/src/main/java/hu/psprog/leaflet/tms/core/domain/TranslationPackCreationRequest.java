package hu.psprog.leaflet.tms.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Map;

/**
 * Creation request model for translation packs.
 *
 * @author Peter Smith
 */
public class TranslationPackCreationRequest {

    @NotNull
    private Locale locale;

    @NotEmpty
    private String packName;

    @NotEmpty
    private Map<String, String> definitions;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<String, String> definitions) {
        this.definitions = definitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TranslationPackCreationRequest that = (TranslationPackCreationRequest) o;

        return new EqualsBuilder()
                .append(locale, that.locale)
                .append(packName, that.packName)
                .append(definitions, that.definitions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(locale)
                .append(packName)
                .append(definitions)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("locale", locale)
                .append("packName", packName)
                .append("definitions", definitions)
                .toString();
    }

    public static TranslationPackCreationRequestBuilder getBuilder() {
        return new TranslationPackCreationRequestBuilder();
    }

    /**
     * Builder for {@link TranslationPackCreationRequest}.
     */
    public static final class TranslationPackCreationRequestBuilder {
        private Locale locale;
        private String packName;
        private Map<String, String> definitions;

        private TranslationPackCreationRequestBuilder() {
        }

        public TranslationPackCreationRequestBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public TranslationPackCreationRequestBuilder withPackName(String packName) {
            this.packName = packName;
            return this;
        }

        public TranslationPackCreationRequestBuilder withDefinitions(Map<String, String> definitions) {
            this.definitions = definitions;
            return this;
        }

        public TranslationPackCreationRequest build() {
            TranslationPackCreationRequest translationPackCreationRequest = new TranslationPackCreationRequest();
            translationPackCreationRequest.setLocale(locale);
            translationPackCreationRequest.setPackName(packName);
            translationPackCreationRequest.setDefinitions(definitions);
            return translationPackCreationRequest;
        }
    }
}
