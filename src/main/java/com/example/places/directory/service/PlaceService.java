package com.example.places.directory.service;

import com.example.places.directory.model.Place;
import com.example.places.directory.model.PlaceInput;
import java.util.List;
import java.util.UUID;

public interface PlaceService {

  /**
   * Creates a new place.
   *
   * @param placeInput the place input.
   * @return the created place.
   */
  Place createPlace(PlaceInput placeInput);

  /**
   * Deletes the place for the given place ID.
   *
   * @param placeId the place id.
   */
  void deletePlace(UUID placeId);

  /**
   * Gets the place for the given place ID.
   *
   * @param placeId the place id.
   * @return the place.
   */
  Place getPlace(UUID placeId);

  /**
   * Gets all places.
   *
   * @return the list of places.
   */
  List<Place> getPlaces();

}
