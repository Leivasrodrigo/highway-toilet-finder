package com.highwaytoiletfinder;

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

//    @Autowired
//    private ReviewRepository reviewRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Test
    void findAllFirstShouldBeEmpty() {
        List<Toilet> toilets = toiletRepository.findAll();
        assertThat(toilets).isEmpty();
    }

    @Test
    void findALl_shouldRetrieveAllToilets() {
        Toilet toilet1 = new Toilet();
        toilet1.setName("Posto A");

        toiletRepository.save(toilet1);

        Toilet toilet2 = new Toilet();
        toilet2.setName("Posto B");

        toiletRepository.save(toilet2);

        List<Toilet> allToilets = toiletRepository.findAll();

        assertThat(allToilets).hasSize(2);

        List<String> comments = allToilets.stream().map(Toilet::getName).toList();
        assertThat(comments).containsExactlyInAnyOrder("Posto A", "Posto B");
    }

    @Test
    void findByToiletIdShouldReturnExistingToilet() {
        Toilet toilet = new Toilet();
        toilet.setName("Posto A");

        toiletRepository.save(toilet);

        Optional<Toilet> result = toiletRepository.findById(toilet.getId());

        assertTrue(result.isPresent(), "Expected Optional to be present");
        assertEquals("Posto A", result.get().getName());
    }
}
