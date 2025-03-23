package com.example.places.directory.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.places.directory.converter.OpeningHoursConverter;
import com.example.places.directory.converter.OpeningHoursInputConverter;
import com.example.places.directory.exception.DuplicateOpeningHoursException;
import com.example.places.directory.exception.OpeningHoursNotFoundException;
import com.example.places.directory.exception.PlaceNotFoundException;
import com.example.places.directory.model.OpeningHours;
import com.example.places.directory.model.OpeningHoursInput;
import com.example.places.directory.model.OpeningHoursInput.DayOfWeekEnum;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import com.example.places.directory.persistence.model.PlaceEntity;
import com.example.places.directory.persistence.repostitory.OpeningHoursRepository;
import com.example.places.directory.persistence.repostitory.PlaceRepository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class OpeningHoursServiceImplTest {

  private static final UUID PLACE_ID = UUID.randomUUID();
  private static final UUID OPENING_HOURS_ID = UUID.randomUUID();

  @Mock
  private OpeningHoursConverter openingHoursConverter;

  @Mock
  private OpeningHoursInputConverter openingHoursInputConverter;

  @Mock
  private OpeningHoursRepository openingHoursRepository;

  @Mock
  private PlaceRepository placeRepository;

  @InjectMocks
  private OpeningHoursServiceImpl openingHoursService;

  @Test
  void givenPlaceNotFoundShouldThrowPlaceNotFoundException() {
    OpeningHoursInput input = new OpeningHoursInput();
    when(placeRepository.findById(PLACE_ID)).thenReturn(Optional.empty());

    assertThrows(PlaceNotFoundException.class,
        () -> openingHoursService.addOpeningHours(PLACE_ID, input));
  }

  @Test
  void givenDuplicateOpeningHoursShouldThrowDuplicateOpeningHoursException() {
    OpeningHoursInput input = new OpeningHoursInput();
    PlaceEntity place = new PlaceEntity();
    OpeningHoursEntity entity = new OpeningHoursEntity();

    when(placeRepository.findById(PLACE_ID)).thenReturn(Optional.of(place));
    when(openingHoursInputConverter.convert(input)).thenReturn(entity);
    when(openingHoursRepository.save(entity))
        .thenThrow(new DataIntegrityViolationException(""));

    assertThrows(DuplicateOpeningHoursException.class,
        () -> openingHoursService.addOpeningHours(PLACE_ID, input));
  }

  @Test
  void shouldAddOpeningHours() {
    OpeningHoursInput input = new OpeningHoursInput();
    PlaceEntity place = new PlaceEntity();
    OpeningHoursEntity entity = new OpeningHoursEntity();
    OpeningHours expected = new OpeningHours();

    when(placeRepository.findById(PLACE_ID)).thenReturn(Optional.of(place));
    when(openingHoursInputConverter.convert(input)).thenReturn(entity);
    when(openingHoursRepository.save(entity)).thenReturn(entity);
    when(openingHoursConverter.convert(entity)).thenReturn(expected);

    OpeningHours actual = openingHoursService.addOpeningHours(PLACE_ID, input);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  void givenOpeningHoursNotFoundShouldThrowOpeningHoursNotFoundException() {
    when(openingHoursRepository.findByIdAndPlaceId(OPENING_HOURS_ID, PLACE_ID))
        .thenReturn(Optional.empty());

    assertThrows(OpeningHoursNotFoundException.class,
        () -> openingHoursService.deleteOpeningHours(PLACE_ID, OPENING_HOURS_ID));
  }

  @Test
  void shouldUpdateOpeningHours() {
    OpeningHoursInput input = createOpeningHoursInput();
    OpeningHoursEntity entity = new OpeningHoursEntity();
    OpeningHours expected = new OpeningHours();

    when(openingHoursRepository.findByIdAndPlaceId(OPENING_HOURS_ID, PLACE_ID))
        .thenReturn(Optional.of(entity));
    when(openingHoursRepository.save(entity)).thenReturn(entity);
    when(openingHoursConverter.convert(entity)).thenReturn(expected);

    OpeningHours actual = openingHoursService.updateOpeningHours(PLACE_ID, OPENING_HOURS_ID, input);

    assertThat(actual, is(equalTo(expected)));
    assertThat(entity.getOpeningTime(), is(equalTo(LocalTime.parse("09:00"))));
    assertThat(entity.getClosingTime(), is(equalTo(LocalTime.parse("17:00"))));
    assertThat(entity.getDayOfWeek(), is(equalTo(DayOfWeek.MONDAY)));
    assertThat(entity.isClosed(), is(false));
  }

  @Test
  void shouldDeleteOpeningHours() {
    OpeningHoursEntity entity = new OpeningHoursEntity();
    when(openingHoursRepository.findByIdAndPlaceId(OPENING_HOURS_ID, PLACE_ID))
        .thenReturn(Optional.of(entity));

    openingHoursService.deleteOpeningHours(PLACE_ID, OPENING_HOURS_ID);

    verify(openingHoursRepository).delete(entity);
  }

  private OpeningHoursInput createOpeningHoursInput() {
    OpeningHoursInput input = new OpeningHoursInput();
    input.setOpeningTime("09:00");
    input.setClosingTime("17:00");
    input.setDayOfWeek(DayOfWeekEnum.MONDAY);
    input.setClosed(false);
    return input;
  }
}
