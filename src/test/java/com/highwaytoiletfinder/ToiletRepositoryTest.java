package com.highwaytoiletfinder;

import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "classpath:/application-test.properties")
public class ToiletRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Test
    void findAllFirstShouldBeEmpty() {
        List<Toilet> toilets = toiletRepository.findAll();
        assertThat(toilets).isEmpty();
    }

    @Test
    void findALl_shouldRetrieveAllToilets() {
        Place place1 = new Place();
        place1.setName("Posto A");
        place1.setAddress("Av. Central, 1000");
        place1.setLatitude(-27.12345);
        place1.setLongitude(-48.98765);
        place1.setGooglePlaceId("some-google-place-id");
        placeRepository.save(place1);

        Toilet toilet1 = new Toilet();
        toilet1.setPlace(place1);
        toilet1.setGender(Gender.BYGENDER);

        toiletRepository.save(toilet1);

        Place place2 = new Place();
        place2.setName("Posto B");
        place2.setAddress("Av. Central, 1000");
        place2.setLatitude(-27.12345);
        place2.setLongitude(-48.98765);
        place2.setGooglePlaceId("some-google-place-id");
        placeRepository.save(place2);

        Toilet toilet2 = new Toilet();
        toilet2.setPlace(place2);
        toilet2.setGender(Gender.BYGENDER);

        toiletRepository.save(toilet2);

        List<Toilet> allToilets = toiletRepository.findAll();

        assertThat(allToilets).hasSize(2);

        List<String> placeNames = allToilets.stream()
                .map(t -> t.getPlace().getName())
                .toList();

        assertThat(placeNames).containsExactlyInAnyOrder("Posto A", "Posto B");
    }

    @Test
    void findByToiletIdShouldReturnExistingToilet() {
        Place place = new Place();
        place.setName("Posto A");
        place.setAddress("Av. Central, 1000");
        place.setLatitude(-27.12345);
        place.setLongitude(-48.98765);
        place.setGooglePlaceId("some-google-place-id");
        placeRepository.save(place);

        Toilet toilet = new Toilet();
        toilet.setPlace(place);
        toilet.setGender(Gender.BYGENDER);

        toiletRepository.save(toilet);

        Optional<Toilet> result = toiletRepository.findById(toilet.getId());

        assertTrue(result.isPresent(), "Expected Optional to be present");
        assertEquals("Posto A", result.get().getPlace().getName());
    }
}
