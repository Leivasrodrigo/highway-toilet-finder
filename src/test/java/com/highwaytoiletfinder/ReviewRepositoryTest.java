package com.highwaytoiletfinder;

import com.highwaytoiletfinder.place.model.Place;
import com.highwaytoiletfinder.place.repository.PlaceRepository;
import com.highwaytoiletfinder.review.model.Review;
import com.highwaytoiletfinder.review.repository.ReviewRepository;
import com.highwaytoiletfinder.toilet.enums.Gender;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:/application-test.properties")
public class ReviewRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Test
    void findAllFirstShouldBeEmpty() {
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).isEmpty();
    }

    @Test
    void findALl_shouldRetrieveAllReviews() {
        Place place = new Place();
        place.setName("Posto A");
        place.setAddress("Av. Central, 1000");
        place.setLatitude(-27.12345);
        place.setLongitude(-48.98765);
        place.setGooglePlaceId("some-google-place-id");
        placeRepository.save(place);

        Toilet toilet = new Toilet();
        toilet.setPlace(place);
        toilet.setGender(Gender.MALE);

        toiletRepository.save(toilet);

        Review review1 = new Review();
        review1.setToilet(toilet);
        review1.setRatingGeneral(4);
        review1.setRatingCleanliness(3);
        review1.setRatingMaintenance(5);
        review1.setComment("Banheiro limpo e funcional.");
        review1.setCreatedAt(Instant.now());

        Review review2 = new Review();
        review2.setToilet(toilet);
        review2.setRatingGeneral(5);
        review2.setRatingCleanliness(5);
        review2.setRatingMaintenance(5);
        review2.setComment("Excelente!");
        review2.setCreatedAt(Instant.now());

        User user = new User();
        user.setName("John Doe");
        user.setEmail("test@example.com");
        userRepository.save(user);

        review1.setUser(user);
        review2.setUser(user);

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> allReviews = reviewRepository.findAll();

        assertThat(allReviews).hasSize(2);

        List<String> comments = allReviews.stream().map(Review::getComment).toList();
        assertThat(comments).containsExactlyInAnyOrder("Banheiro limpo e funcional.", "Excelente!");
    }

    @Test
    void findByToiletIdShouldReturnExistingReviewsForToilet() {
        Place place = new Place();
        place.setName("Posto A");
        place.setAddress("Av. Central, 1000");
        place.setLatitude(-27.12345);
        place.setLongitude(-48.98765);
        place.setGooglePlaceId("some-google-place-id");
        placeRepository.save(place);

        Toilet toilet = new Toilet();
        toilet.setPlace(place);
        toilet.setGender(Gender.MALE);

        toiletRepository.save(toilet);

        User user = new User();
        user.setName("John Doe");
        user.setEmail("test@example.com");
        userRepository.save(user);

        Review review1 = new Review();
        review1.setToilet(toilet);
        review1.setRatingGeneral(4);
        review1.setRatingCleanliness(3);
        review1.setRatingMaintenance(5);
        review1.setComment("Banheiro limpo e funcional.");
        review1.setCreatedAt(Instant.now());
        review1.setUser(user);

        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setToilet(toilet);
        review2.setRatingGeneral(5);
        review2.setRatingCleanliness(5);
        review2.setRatingMaintenance(5);
        review2.setComment("Excelente!");
        review2.setCreatedAt(Instant.now());
        review2.setUser(user);

        reviewRepository.save(review2);

        Review review3 = new Review();
        review3.setToilet(toilet);
        review3.setRatingGeneral(3);
        review3.setRatingCleanliness(2);
        review3.setRatingMaintenance(4);
        review3.setComment("Estava sujo.");
        review3.setCreatedAt(Instant.now());
        review3.setUser(user);

        reviewRepository.save(review3);

        List<Review> allReviews = reviewRepository.findByToiletId(toilet.getId());

        assertThat(allReviews).hasSize(3);

        List<String> comments = allReviews.stream().map(Review::getComment).toList();
        assertThat(comments).containsExactlyInAnyOrder("Banheiro limpo e funcional.", "Excelente!", "Estava sujo.");
    }

}
