package com.highwaytoiletfinder.toilet.repository;

import com.highwaytoiletfinder.toilet.model.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ToiletRepository extends JpaRepository<Toilet, UUID> {
}
