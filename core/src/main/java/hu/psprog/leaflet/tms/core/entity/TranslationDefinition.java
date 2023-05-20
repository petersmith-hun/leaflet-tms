package hu.psprog.leaflet.tms.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Translation definition sub-document class.
 *
 * @author Peter Smith
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationDefinition {

    private String key;
    private String value;
}
