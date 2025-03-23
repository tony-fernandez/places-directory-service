package com.example.places.directory.converter;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.example.places.directory.IntegrationTestConfiguration;
import com.example.places.directory.model.GroupedOpeningHours;
import com.example.places.directory.model.Place;
import com.example.places.directory.persistence.model.AddressEntity;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import com.example.places.directory.persistence.model.PlaceEntity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = IntegrationTestConfiguration.class, webEnvironment = RANDOM_PORT)
class PlaceConverterTest {

  private static final UUID ADDRESS_ID = UUID.randomUUID();
  private static final UUID PLACE_ID = UUID.randomUUID();

  @Autowired
  private PlaceConverter placeConverter;

  @Test
  void givenNullAddressEntityShouldReturnNull() {
    PlaceEntity input = null;

    Place output = placeConverter.convert(input);

    assertThat(output, is(nullValue()));
  }

  @Test
  void shouldConvert() {
    Set<OpeningHoursEntity> openingHours = new HashSet<>();
    openingHours.add(openingHours(MONDAY, null, null, true));
    openingHours.add(openingHours(TUESDAY, LocalTime.of(11, 30), LocalTime.of(15, 0), false));
    openingHours.add(openingHours(TUESDAY, LocalTime.of(18, 0), LocalTime.of(0, 0), false));
    openingHours.add(openingHours(WEDNESDAY, LocalTime.of(11, 30), LocalTime.of(15, 0), false));
    openingHours.add(openingHours(WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(0, 0), false));
    openingHours.add(openingHours(THURSDAY, LocalTime.of(11, 30), LocalTime.of(15, 0), false));
    openingHours.add(openingHours(THURSDAY, LocalTime.of(18, 0), LocalTime.of(0, 0), false));
    openingHours.add(openingHours(FRIDAY, LocalTime.of(11, 30), LocalTime.of(15, 0), false));
    openingHours.add(openingHours(FRIDAY, LocalTime.of(18, 0), LocalTime.of(0, 0), false));
    openingHours.add(openingHours(SATURDAY, LocalTime.of(18, 30), LocalTime.of(0, 0), false));
    openingHours.add(openingHours(SUNDAY, LocalTime.of(11, 30), LocalTime.of(15, 0), false));
    PlaceEntity input = new PlaceEntity();
    input.setId(PLACE_ID);
    input.setName("name");
    input.setDescription("description");
    input.setAddress(address());
    input.setOpeningHours(openingHours);

    Place output = placeConverter.convert(input);

    assertThat(output.getId(), is(PLACE_ID));
    assertThat(output.getName(), is("name"));
    assertThat(output.getDescription(), is("description"));
    assertThat(output.getAddress().getId(), is(ADDRESS_ID));
    assertThat(output.getAddress().getStreet(), is("street"));
    assertThat(output.getAddress().getPostcode(), is("postalCode"));
    assertThat(output.getAddress().getCity(), is("city"));
    assertThat(output.getAddress().getCountry(), is("country"));
    assertThat(output.getOpeningHours(), hasSize(11));
    assertThat(output.getGroupedOpeningHours(), hasSize(4));
    assertThat(output.getGroupedOpeningHours(), containsInAnyOrder(
        groupedOpeningHours("Monday", "Closed"),
        groupedOpeningHours("Tuesday - Friday", "11:30 - 15:00", "18:00 - 00:00"),
        groupedOpeningHours("Saturday", "18:30 - 00:00"),
        groupedOpeningHours("Sunday", "11:30 - 15:00")));
  }

  private static AddressEntity address() {
    AddressEntity address = new AddressEntity();
    address.setId(ADDRESS_ID);
    address.setStreet("street");
    address.setPostcode("postalCode");
    address.setCity("city");
    address.setCountry("country");
    return address;
  }

  private static OpeningHoursEntity openingHours(DayOfWeek dayOfWeek, LocalTime openingTime,
      LocalTime closingTime, boolean closed) {
    OpeningHoursEntity openingHours = new OpeningHoursEntity();
    openingHours.setId(UUID.randomUUID());
    openingHours.setDayOfWeek(dayOfWeek);
    openingHours.setOpeningTime(openingTime);
    openingHours.setClosingTime(closingTime);
    openingHours.setClosed(closed);
    return openingHours;
  }

  private static GroupedOpeningHours groupedOpeningHours(String daysOfWeek,
      String... openingHours) {
    GroupedOpeningHours groupedOpeningHours = new GroupedOpeningHours();
    groupedOpeningHours.setDaysOfWeek(daysOfWeek);
    groupedOpeningHours.setOpeningHours(List.of(openingHours));
    return groupedOpeningHours;
  }

}