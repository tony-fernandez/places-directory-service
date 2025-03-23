package com.example.places.directory.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.places.directory.converter.PlaceConverter;
import com.example.places.directory.converter.PlaceInputConverter;
import com.example.places.directory.exception.DuplicateOpeningHoursException;
import com.example.places.directory.exception.ExistingPlaceException;
import com.example.places.directory.exception.PlaceNotFoundException;
import com.example.places.directory.model.Place;
import com.example.places.directory.model.PlaceInput;
import com.example.places.directory.persistence.model.PlaceEntity;
import com.example.places.directory.persistence.repostitory.PlaceRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class PlaceServiceImplTest {

  private static final UUID PLACE_ID = UUID.randomUUID();

  @Mock
  private PlaceConverter placeConverter;

  @Mock
  private PlaceInputConverter placeInputConverter;

  @Mock
  private PlaceRepository placeRepository;

  @InjectMocks
  private PlaceServiceImpl placeService;

  @Test
  void shouldCreatePlace() {
    PlaceInput input = new PlaceInput();
    PlaceEntity entity = new PlaceEntity();
    Place expected = new Place();
    when(placeInputConverter.convert(input)).thenReturn(entity);
    when(placeRepository.save(entity)).thenReturn(entity);
    when(placeConverter.convert(entity)).thenReturn(expected);

    Place actual = placeService.createPlace(input);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  void givenDuplicatePlaceNameShouldThrowExistingPlaceException() {
    PlaceInput input = new PlaceInput();
    input.setName("Test Place");
    PlaceEntity entity = new PlaceEntity();
    when(placeInputConverter.convert(input)).thenReturn(entity);
    when(placeRepository.save(entity))
        .thenThrow(new DataIntegrityViolationException(""));

    assertThrows(ExistingPlaceException.class,
        () -> placeService.createPlace(input));
  }

  @Test
  void givenDuplicateOpeningHoursShouldThrowDuplicateOpeningHoursException() {
    PlaceInput input = new PlaceInput();
    PlaceEntity entity = new PlaceEntity();
    when(placeInputConverter.convert(input)).thenReturn(entity);
    when(placeRepository.save(entity))
        .thenThrow(new DataIntegrityViolationException("opening_hours_unique_index"));

    assertThrows(DuplicateOpeningHoursException.class,
        () -> placeService.createPlace(input));
  }

  @Test
  void givenNonExistentPlaceShouldThrowPlaceNotFoundException() {
    when(placeRepository.findById(PLACE_ID)).thenReturn(Optional.empty());

    assertThrows(PlaceNotFoundException.class,
        () -> placeService.getPlace(PLACE_ID));
  }

  @Test
  void shouldGetPlace() {
    PlaceEntity entity = new PlaceEntity();
    Place expected = new Place();
    when(placeRepository.findById(PLACE_ID)).thenReturn(Optional.of(entity));
    when(placeConverter.convert(entity)).thenReturn(expected);

    Place actual = placeService.getPlace(PLACE_ID);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  void shouldGetPlaces() {
    PlaceEntity entity = new PlaceEntity();
    Place expected = new Place();
    List<PlaceEntity> entities = Collections.singletonList(entity);
    List<Place> expectedPlaces = Collections.singletonList(expected);
    when(placeRepository.findAll()).thenReturn(entities);
    when(placeConverter.convert(entities)).thenReturn(expectedPlaces);

    List<Place> actual = placeService.getPlaces();

    assertThat(actual, is(equalTo(expectedPlaces)));
  }

  @Test
  void shouldDeletePlace() {
    PlaceEntity entity = new PlaceEntity();
    when(placeRepository.findById(PLACE_ID)).thenReturn(Optional.of(entity));

    placeService.deletePlace(PLACE_ID);

    verify(placeRepository).delete(entity);
  }
}