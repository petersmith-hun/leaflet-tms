package hu.psprog.leaflet.tms.core.dao.impl;

import hu.psprog.leaflet.tms.core.repository.TranslationPackRepository;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link TranslationPackDAOImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class TranslationPackDAOImplTest {

    private static final UUID PACK_ID = UUID.randomUUID();
    private static final TranslationPack TRANSLATION_PACK = TranslationPack.getPackBuilder().withId(PACK_ID).build();

    @Mock
    private TranslationPackRepository translationPackRepository;

    @InjectMocks
    private TranslationPackDAOImpl translationPackDAO;

    @Test
    public void shouldFindAll() {

        // when
        translationPackDAO.findAll();

        // then
        verify(translationPackRepository).findAll();
    }

    @Test
    public void shouldFindAllByPackNameList() {

        // given
        List<String> packs = Arrays.asList("pack1", "pack2");

        // when
        translationPackDAO.findAllByPackNameIn(packs);

        // then
        verify(translationPackRepository).findAllByPackNameIn(packs);
    }

    @Test
    public void shouldCheckExistence() {

        // when
        translationPackDAO.exists(PACK_ID);

        // then
        verify(translationPackRepository).existsById(PACK_ID);
    }

    @Test
    public void shouldGetByID() {

        // when
        translationPackDAO.getByID(PACK_ID);

        // then
        verify(translationPackRepository).findById(PACK_ID);
    }

    @Test
    public void shouldSave() {

        // when
        translationPackDAO.save(TRANSLATION_PACK);

        // then
        verify(translationPackRepository).save(TRANSLATION_PACK);
    }

    @Test
    public void shouldSetStatus() {

        // given
        given(translationPackRepository.findById(PACK_ID)).willReturn(Optional.of(TRANSLATION_PACK));

        // when
        translationPackDAO.setStatus(PACK_ID, true);

        // then
        assertThat(TRANSLATION_PACK.isEnabled(), is(true));
        verify(translationPackRepository).save(TRANSLATION_PACK);
    }

    @Test
    public void shouldDelete() {

        // when
        translationPackDAO.delete(PACK_ID);

        // then
        verify(translationPackRepository).deleteById(PACK_ID);
    }
}