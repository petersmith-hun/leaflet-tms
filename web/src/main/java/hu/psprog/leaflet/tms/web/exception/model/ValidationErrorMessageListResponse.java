package hu.psprog.leaflet.tms.web.exception.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Validation error message response model.
 *
 * @author Peter Smith
 */
public class ValidationErrorMessageListResponse {

    private List<ValidationErrorMessageResponse> validation;

    public List<ValidationErrorMessageResponse> getValidation() {
        return validation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ValidationErrorMessageListResponse that = (ValidationErrorMessageListResponse) o;

        return new EqualsBuilder()
                .append(validation, that.validation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(validation)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("validation", validation)
                .toString();
    }

    public static ValidationErrorMessageListResponseBuilder getBuilder() {
        return new ValidationErrorMessageListResponseBuilder();
    }

    /**
     * Builder for {@link ValidationErrorMessageListResponse}.
     */
    public static final class ValidationErrorMessageListResponseBuilder {
        private List<ValidationErrorMessageResponse> validation;

        private ValidationErrorMessageListResponseBuilder() {
        }

        public ValidationErrorMessageListResponseBuilder withValidation(List<ValidationErrorMessageResponse> validation) {
            this.validation = validation;
            return this;
        }

        public ValidationErrorMessageListResponse build() {
            ValidationErrorMessageListResponse validationErrorMessageListResponse = new ValidationErrorMessageListResponse();
            validationErrorMessageListResponse.validation = this.validation;
            return validationErrorMessageListResponse;
        }
    }
}
