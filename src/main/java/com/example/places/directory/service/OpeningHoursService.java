package com.example.places.directory.service;

import com.example.places.directory.model.OpeningHours;
import com.example.places.directory.model.OpeningHoursInput;
import java.util.UUID;

public interface OpeningHoursService {

  /**
   * Adds new opening hours.
   *
   * @param placeId      the place id.
   * @param openingHours the opening hours.
   * @return the created opening hours.
   */
  OpeningHours addOpeningHours(UUID placeId, OpeningHoursInput openingHours);

  /**
   * Updates the opening hours for the given place id and opening hours id.
   *
   * @param placeId        the place id.
   * @param openingHoursId the opening hour's id.
   * @param openingHours   the opening hours.
   * @return the updated opening hours.
   */
  OpeningHours updateOpeningHours(UUID placeId, UUID openingHoursId,
      OpeningHoursInput openingHours);

  /**
   * Deletes the opening hours for the given place id and opening hour's id.
   *
   * @param placeId        the place id.
   * @param openingHoursId the opening hour's id.
   */
  void deleteOpeningHours(UUID placeId, UUID openingHoursId);
}
