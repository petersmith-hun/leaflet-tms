package hu.psprog.leaflet.tms.web.exception.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Validation error message list response model.
 *
 * @author Peter Smith
 */
public class ValidationErrorMessageResponse extends ErrorMessageResponse {

    private String field;

    public String getField() {
        return field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ValidationErrorMessageResponse that = (ValidationErrorMessageResponse) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(field, that.field)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(field)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("field", field)
                .append("message", message)
                .toString();
    }

    public static ValidationErrorMessageResponseBuilder getExtendedBuilder() {
        return new ValidationErrorMessageResponseBuilder();
    }

    /**
     * Builder for {@link ValidationErrorMessageResponse}.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ValidationErrorMessageResponseBuilder {
        private String message;
        private String field;

        private ValidationErrorMessageResponseBuilder() {
        }

        public ValidationErrorMessageResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ValidationErrorMessageResponseBuilder withField(String field) {
            this.field = field;
            return this;
        }

        public ValidationErrorMessageResponse build() {
            ValidationErrorMessageResponse validationErrorMessageResponse = new ValidationErrorMessageResponse();
            validationErrorMessageResponse.field = this.field;
            validationErrorMessageResponse.message = this.message;
            return validationErrorMessageResponse;
        }
    }
}
