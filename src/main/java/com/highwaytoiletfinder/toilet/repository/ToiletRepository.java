package com.highwaytoiletfinder.toilet.repository;

import com.highwaytoiletfinder.toilet.model.Toilet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ToiletRepository extends JpaRepository<Toilet, UUID> {
    @Override
    @EntityGraph(attributePaths = {"reviews"})
    List<Toilet> findAll();

    @Override
    @EntityGraph(attributePaths = {"reviews"})
    Optional<Toilet> findById(UUID id);
}
