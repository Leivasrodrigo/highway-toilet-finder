package com.highwaytoiletfinder.toilet.report.repository;

import com.highwaytoiletfinder.toilet.model.Toilet;
import com.highwaytoiletfinder.toilet.report.model.ToiletReport;
import com.highwaytoiletfinder.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ToiletReportRepository extends JpaRepository<ToiletReport, UUID> {
    boolean existsByUserAndToilet(User user, Toilet toilet);
    long countByToilet(Toilet toilet);
    void deleteByToilet(Toilet toilet);
}
