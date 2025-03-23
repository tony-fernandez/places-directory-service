package com.example.places.directory.persistence.repostitory;

import com.example.places.directory.persistence.model.PlaceEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, UUID> {

}
