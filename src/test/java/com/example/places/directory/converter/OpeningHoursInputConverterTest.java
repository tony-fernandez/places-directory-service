package com.example.places.directory.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.places.directory.exception.InvalidOpeningHoursException;
import com.example.places.directory.exception.MissingTimeException;
import com.example.places.directory.model.OpeningHoursInput;
import com.example.places.directory.model.OpeningHoursInput.DayOfWeekEnum;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class OpeningHoursInputConverterTest extends AbstractConverterTest {

  @InjectMocks
  private OpeningHoursInputConverter openingHoursInputConverter;

  @Test
  void givenNullOpeningHoursEntityShouldReturnNull() {
    OpeningHoursInput input = null;

    OpeningHoursEntity output = openingHoursInputConverter.convert(input);

    assertThat(output, is(nullValue()));
  }

  @Test
  void givenClosedIsTrueOpeningAndClosingTimesShouldNoBeSet() {
    OpeningHoursInput input = new OpeningHoursInput();
    input.setClosed(true);
    input.setDayOfWeek(DayOfWeekEnum.MONDAY);

    OpeningHoursEntity output = openingHoursInputConverter.convert(input);

    assertThat(output.isClosed(), is(equalTo(true)));
    assertThat(output.getDayOfWeek().getValue(), is(DayOfWeek.MONDAY.getValue()));
    assertThat(output.getOpeningTime(), is(nullValue()));
    assertThat(output.getClosingTime(), is(nullValue()));
  }

  @Test
  void givenOpeningTimeNotSetShouldThrowMissingOpeningHoursException() {
    OpeningHoursInput input = new OpeningHoursInput();
    input.setClosed(false);
    input.setDayOfWeek(DayOfWeekEnum.MONDAY);
    input.setClosingTime(CLOSING_TIME_S);

    assertThrows(MissingTimeException.class, () -> openingHoursInputConverter.convert(input));
  }

  @Test
  void givenClosingTimeNotSetShouldThrowMissingOpeningHoursException() {
    OpeningHoursInput input = new OpeningHoursInput();
    input.setClosed(false);
    input.setDayOfWeek(DayOfWeekEnum.MONDAY);
    input.setOpeningTime(OPENING_TIME_S);

    assertThrows(MissingTimeException.class, () -> openingHoursInputConverter.convert(input));
  }

  @Test
  void givenInvalidOpeningTimeShouldThrowException() {
    OpeningHoursInput input = new OpeningHoursInput();
    input.setClosed(false);
    input.setDayOfWeek(DayOfWeekEnum.MONDAY);
    input.setOpeningTime("invalid");
    input.setClosingTime(CLOSING_TIME_S);

    assertThrows(InvalidOpeningHoursException.class, () -> openingHoursInputConverter.convert(input));
  }

  @Test
  void givenInvalidClosingTimeShouldThrowException() {
    OpeningHoursInput input = new OpeningHoursInput();
    input.setClosed(false);
    input.setDayOfWeek(DayOfWeekEnum.MONDAY);
    input.setOpeningTime(OPENING_TIME_S);
    input.setClosingTime("invalid");

    assertThrows(InvalidOpeningHoursException.class, () -> openingHoursInputConverter.convert(input));
  }

  @Test
  void givenClosingTimeBeforeOpeningTimeShouldThrowInvalidOpeningHoursException() {
    OpeningHoursInput input = new OpeningHoursInput();
    input.setClosed(false);
    input.setDayOfWeek(DayOfWeekEnum.MONDAY);
    input.setOpeningTime(CLOSING_TIME_S);
    input.setClosingTime(OPENING_TIME_S);

    assertThrows(InvalidOpeningHoursException.class, () -> openingHoursInputConverter.convert(input));
  }

  @Test
  void shouldConvertAllValues() {
    OpeningHoursInput input = new OpeningHoursInput();
    input.setClosed(false);
    input.setDayOfWeek(DayOfWeekEnum.MONDAY);
    input.setOpeningTime(OPENING_TIME_S);
    input.setClosingTime(CLOSING_TIME_S);

    OpeningHoursEntity output = openingHoursInputConverter.convert(input);

    assertThat(output.isClosed(), is(equalTo(false)));
    assertThat(output.getDayOfWeek(), is(DayOfWeek.MONDAY));
    assertThat(output.getOpeningTime(), is(equalTo(OPENING_TIME)));
    assertThat(output.getClosingTime(), is(equalTo(CLOSING_TIME)));
  }

  @Test
  void shouldConvertMultipleDays() {
    OpeningHoursInput dayOne = new OpeningHoursInput();
    dayOne.setClosed(false);
    dayOne.setDayOfWeek(DayOfWeekEnum.MONDAY);
    dayOne.setOpeningTime(OPENING_TIME_S);
    dayOne.setClosingTime(CLOSING_TIME_S);
    OpeningHoursInput dayTwo = new OpeningHoursInput();
    dayTwo.setClosed(false);
    dayTwo.setDayOfWeek(DayOfWeekEnum.TUESDAY);
    dayTwo.setOpeningTime(OPENING_TIME_S);
    dayTwo.setClosingTime(CLOSING_TIME_S);

    List<OpeningHoursEntity> results = openingHoursInputConverter.convert(List.of(dayOne, dayTwo));

    assertThat(results, hasSize(2));
  }

}