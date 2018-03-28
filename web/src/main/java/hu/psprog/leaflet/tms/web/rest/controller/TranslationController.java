package hu.psprog.leaflet.tms.web.rest.controller;

import hu.psprog.leaflet.tms.core.domain.TranslationPack;
import hu.psprog.leaflet.tms.core.domain.TranslationPackCreationRequest;
import hu.psprog.leaflet.tms.core.domain.TranslationPackMetaInfo;
import hu.psprog.leaflet.tms.core.exception.TranslationPackCreationException;
import hu.psprog.leaflet.tms.core.exception.TranslationPackNotFoundException;
import hu.psprog.leaflet.tms.core.service.TranslationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Translation management controller.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(TranslationController.PATH_TRANSLATIONS)
public class TranslationController {

    private static final String PATH_PACK_ID = "/{packID}";
    private static final String PATH_STATUS = PATH_PACK_ID + "/status";

    static final String PATH_TRANSLATIONS = "/translations";

    private TranslationManagementService translationManagementService;

    @Autowired
    public TranslationController(TranslationManagementService translationManagementService) {
        this.translationManagementService = translationManagementService;
    }

    @RequestMapping(method = RequestMethod.GET, params = "packs")
    public ResponseEntity<Set<TranslationPack>> retrievePacks(@RequestParam(value = "packs") List<String> packs) {

        return ResponseEntity
                .ok(translationManagementService.retrieveLatestEnabledPacks(packs));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TranslationPackMetaInfo>> listStoredPacks() {

        return ResponseEntity
                .ok(translationManagementService.retrievePackMetaInfo());
    }

    @RequestMapping(method = RequestMethod.GET, path = PATH_PACK_ID)
    public ResponseEntity<TranslationPack> getPackByID(@PathVariable("packID") UUID packID) throws TranslationPackNotFoundException {

        return ResponseEntity
                .ok(translationManagementService.getPack(packID));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createTranslationPack(@RequestBody @Valid TranslationPackCreationRequest translationPackCreationRequest, BindingResult bindingResult)
            throws TranslationPackCreationException {

        ResponseEntity<?> responseEntity;
        if (bindingResult.hasErrors()) {
            responseEntity = ResponseEntity
                    .badRequest()
                    .body(bindingResult.getAllErrors());
        } else {
            TranslationPack translationPack = translationManagementService.createPack(translationPackCreationRequest);
            responseEntity = ResponseEntity
                    .created(createURI(translationPack))
                    .body(translationPack);
        }

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.PUT, path = PATH_STATUS)
    public ResponseEntity<TranslationPack> changePackStatus(@PathVariable("packID") UUID packID) throws TranslationPackNotFoundException {

        TranslationPack translationPack = translationManagementService.changeStatus(packID);

        return ResponseEntity
                .created(createURI(translationPack))
                .body(translationPack);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = PATH_PACK_ID)
    public ResponseEntity<Void> deleteTranslationPack(@PathVariable("packID") UUID packID) throws TranslationPackNotFoundException {

        translationManagementService.deletePack(packID);

        return ResponseEntity
                .noContent()
                .build();
    }

    private URI createURI(TranslationPack translationPack) {
        return URI.create(PATH_TRANSLATIONS + "/" + translationPack.getId());
    }
}
