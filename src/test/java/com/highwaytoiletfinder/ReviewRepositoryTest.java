package com.highwaytoiletfinder;

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
        Toilet toilet = new Toilet();

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

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> allReviews = reviewRepository.findAll();

        assertThat(allReviews).hasSize(2);

        List<String> comments = allReviews.stream().map(Review::getComment).toList();
        assertThat(comments).containsExactlyInAnyOrder("Banheiro limpo e funcional.", "Excelente!");
    }

    @Test
    void findByToiletIdShouldReturnExistingReviewsForToilet() {
        Toilet toilet = new Toilet();

        toiletRepository.save(toilet);

        Review review1 = new Review();
        review1.setToilet(toilet);
        review1.setRatingGeneral(4);
        review1.setRatingCleanliness(3);
        review1.setRatingMaintenance(5);
        review1.setComment("Banheiro limpo e funcional.");
        review1.setCreatedAt(Instant.now());

        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setToilet(toilet);
        review2.setRatingGeneral(5);
        review2.setRatingCleanliness(5);
        review2.setRatingMaintenance(5);
        review2.setComment("Excelente!");
        review2.setCreatedAt(Instant.now());

        reviewRepository.save(review2);

        Review review3 = new Review();
        review3.setToilet(toilet);
        review3.setRatingGeneral(3);
        review3.setRatingCleanliness(2);
        review3.setRatingMaintenance(4);
        review3.setComment("Estava sujo.");
        review3.setCreatedAt(Instant.now());

        reviewRepository.save(review3);

        List<Review> allReviews = reviewRepository.findByToiletId(toilet.getId());

        assertThat(allReviews).hasSize(3);

        List<String> comments = allReviews.stream().map(Review::getComment).toList();
        assertThat(comments).containsExactlyInAnyOrder("Banheiro limpo e funcional.", "Excelente!", "Estava sujo.");
    }

}
