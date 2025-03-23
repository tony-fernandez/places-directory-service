package com.example.places.directory.service.impl;

import static java.lang.String.format;

import com.example.places.directory.converter.PlaceConverter;
import com.example.places.directory.converter.PlaceInputConverter;
import com.example.places.directory.exception.DuplicateOpeningHoursException;
import com.example.places.directory.exception.ExistingPlaceException;
import com.example.places.directory.exception.PlaceNotFoundException;
import com.example.places.directory.model.Place;
import com.example.places.directory.model.PlaceInput;
import com.example.places.directory.persistence.model.PlaceEntity;
import com.example.places.directory.persistence.repostitory.PlaceRepository;
import com.example.places.directory.service.PlaceService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

  private final PlaceConverter placeConverter;
  private final PlaceInputConverter placeInputConverter;
  private final PlaceRepository placeRepository;

  @Override
  public Place createPlace(PlaceInput placeInput) {
    try {
      PlaceEntity newPlace = placeInputConverter.convert(placeInput);
      return placeConverter.convert(placeRepository.save(newPlace));
    } catch (DataIntegrityViolationException e) {
      if(e.getMessage().contains("opening_hours_unique_index")) {
        throw new DuplicateOpeningHoursException("Invalid opening hours.  Please provide unique opening hours for each day of the week.");
      }
      throw new ExistingPlaceException(format("Place with name '%s' already exists", placeInput.getName()));
    }
  }

  @Override
  public void deletePlace(UUID placeId) {
    placeRepository.delete(findPlaceEntity(placeId));
  }

  @Override
  public Place getPlace(UUID placeId) {
    return placeConverter.convert(findPlaceEntity(placeId));
  }

  @Override
  public List<Place> getPlaces() {
    // This is a naive implementation. In a real-world application, you would want to use pagination.
    return placeConverter.convert(placeRepository.findAll());
  }

  private PlaceEntity findPlaceEntity(UUID placeId) {
    return placeRepository.findById(placeId)
        .orElseThrow(() -> new PlaceNotFoundException(
            format("Place with id %s not found", placeId)));
  }

}
