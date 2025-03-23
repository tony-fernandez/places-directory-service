package com.example.places.directory.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.example.places.directory.model.OpeningHours;
import com.example.places.directory.model.OpeningHoursInput.DayOfWeekEnum;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class OpeningHoursConverterTest extends AbstractConverterTest {

  @InjectMocks
  private OpeningHoursConverter openingHoursConverter;

  @Test
  void givenNullOpeningHoursEntityShouldReturnNull() {
    OpeningHoursEntity input = null;

    OpeningHours output = openingHoursConverter.convert(input);

    assertThat(output, is(nullValue()));
  }

  @Test
  void givenNoOpeningOrClosingTimesOpeningAndClosingTimesShouldNoBeSet() {
    OpeningHoursEntity input = new OpeningHoursEntity();
    input.setClosed(true);
    input.setDayOfWeek(DayOfWeek.MONDAY);
    input.setId(ID);

    OpeningHours output = openingHoursConverter.convert(input);

    assertThat(output.getClosed(), is(equalTo(true)));
    assertThat(output.getDayOfWeek().getValue(), is(DayOfWeekEnum.MONDAY.getValue()));
    assertThat(output.getOpeningTime(), is(nullValue()));
    assertThat(output.getClosingTime(), is(nullValue()));
    assertThat(output.getId(), is(equalTo(ID)));
  }

  @Test
  void shouldConvertAllValues() {
    OpeningHoursEntity input = new OpeningHoursEntity();
    input.setClosed(false);
    input.setDayOfWeek(DayOfWeek.MONDAY);
    input.setOpeningTime(OPENING_TIME);
    input.setClosingTime(CLOSING_TIME);
    input.setId(ID);

    OpeningHours output = openingHoursConverter.convert(input);

    assertThat(output.getClosed(), is(equalTo(false)));
    assertThat(output.getDayOfWeek().getValue(), is(DayOfWeekEnum.MONDAY.getValue()));
    assertThat(output.getOpeningTime(), is(equalTo(OPENING_TIME_S)));
    assertThat(output.getClosingTime(), is(equalTo(CLOSING_TIME_S)));
    assertThat(output.getId(), is(equalTo(ID)));
  }

  @Test
  void shouldConvertMultipleDays() {
    OpeningHoursEntity dayOne = new OpeningHoursEntity();
    dayOne.setClosed(false);
    dayOne.setDayOfWeek(DayOfWeek.MONDAY);
    dayOne.setOpeningTime(OPENING_TIME);
    dayOne.setClosingTime(CLOSING_TIME);
    OpeningHoursEntity dayTwo = new OpeningHoursEntity();
    dayTwo.setClosed(false);
    dayTwo.setDayOfWeek(DayOfWeek.TUESDAY);
    dayTwo.setOpeningTime(OPENING_TIME);
    dayTwo.setClosingTime(CLOSING_TIME);

    List<OpeningHours> results = openingHoursConverter.convert(List.of(dayOne, dayTwo));

    assertThat(results, hasSize(2));
  }

}