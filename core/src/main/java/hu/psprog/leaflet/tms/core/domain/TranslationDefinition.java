package hu.psprog.leaflet.tms.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Peter Smith
 */
public class TranslationDefinition {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TranslationDefinition that = (TranslationDefinition) o;

        return new EqualsBuilder()
                .append(key, that.key)
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(key)
                .append(value)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("key", key)
                .append("value", value)
                .toString();
    }

    public static TranslationDefinitionBuilder getBuilder() {
        return new TranslationDefinitionBuilder();
    }

    /**
     * Builder for {@link TranslationDefinition}.
     */
    public static final class TranslationDefinitionBuilder {
        private String key;
        private String value;

        private TranslationDefinitionBuilder() {
        }

        public TranslationDefinitionBuilder withKey(String key) {
            this.key = key;
            return this;
        }

        public TranslationDefinitionBuilder withValue(String value) {
            this.value = value;
            return this;
        }

        public TranslationDefinition build() {
            TranslationDefinition translationDefinition = new TranslationDefinition();
            translationDefinition.value = this.value;
            translationDefinition.key = this.key;
            return translationDefinition;
        }
    }
}
