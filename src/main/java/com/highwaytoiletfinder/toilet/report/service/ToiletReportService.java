package com.highwaytoiletfinder.toilet.report.service;

import com.highwaytoiletfinder.common.enums.Status;
import com.highwaytoiletfinder.common.exceptions.ToiletNotFoundException;
import com.highwaytoiletfinder.common.exceptions.UserNotFoundException;
import com.highwaytoiletfinder.toilet.dto.response.ToiletResponseDTO;
import com.highwaytoiletfinder.toilet.mapper.ToiletMapper;
import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.report.model.ToiletReport;
import com.highwaytoiletfinder.toilet.report.repository.ToiletReportRepository;
import com.highwaytoiletfinder.toilet.repository.ToiletRepository;
import com.highwaytoiletfinder.toilet.service.ToiletService;
import com.highwaytoiletfinder.user.model.User;
import com.highwaytoiletfinder.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToiletReportService {

    private final ToiletReportRepository toiletReportRepository;
    private final ToiletRepository toiletRepository;
    private final UserRepository userRepository;
    private final ToiletMapper toiletMapper;
    private final ToiletService toiletService;

    private static final int REPORT_THRESHOLD = 5;

    @Transactional
    public ToiletResponseDTO reportToilet(UUID userId, UUID toiletId) {
        Toilet toilet = toiletRepository.findById(toiletId)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found with id: " + toiletId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        boolean alreadyReported = toiletReportRepository.existsByUserAndToilet(user, toilet);
        if (alreadyReported) {
            throw new IllegalArgumentException("You have already reported this toilet.");
        }

        ToiletReport report = ToiletReport.builder()
                .user(user)
                .toilet(toilet)
                .createdAt(Instant.now())
                .build();
        toiletReportRepository.save(report);

        long count = toiletReportRepository.countByToilet(toilet);
        if (count >= REPORT_THRESHOLD) {
            toilet.setStatus(Status.REJECTED);
            toiletRepository.save(toilet);
            toiletReportRepository.deleteByToilet(toilet);
        }

        return toiletMapper.toResponseDTO(toilet);
    }

    @Transactional
    public ToiletResponseDTO reactivateToilet(UUID toiletId) {
        Toilet toilet = toiletRepository.findById(toiletId)
                .orElseThrow(() -> new ToiletNotFoundException("Toilet not found with id: " + toiletId));

        if (toilet.getStatus() != Status.REJECTED) {
            throw new IllegalArgumentException("Only rejected toilets can be reactivated");
        }

        toiletService.updateToiletStatusBasedOnReviews(toilet);
        toiletRepository.save(toilet);

        toiletReportRepository.deleteByToilet(toilet);

        return toiletMapper.toResponseDTO(toilet);
    }
}
