package com.example.places.directory.persistence.repostitory;

import com.example.places.directory.persistence.model.OpeningHoursEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHoursEntity, UUID> {

  Optional<OpeningHoursEntity> findByIdAndPlaceId(@Param("id") UUID id, @Param("placeId") UUID placeId);
}
