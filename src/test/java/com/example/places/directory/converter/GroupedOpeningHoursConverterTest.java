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

import com.example.places.directory.model.GroupedOpeningHours;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupedOpeningHoursConverterTest {

  @InjectMocks
  private GroupedOpeningHoursConverter groupedOpeningHoursConverter;

  @Test
  void givenEmptySetShouldReturnEmptyList() {
    Set<OpeningHoursEntity> openingHours = new HashSet<>();

    List<GroupedOpeningHours> results = groupedOpeningHoursConverter.convert(openingHours);

    assertThat(results, hasSize(0));
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

    List<GroupedOpeningHours> results = groupedOpeningHoursConverter.convert(openingHours);

    assertThat(results, hasSize(4));
    assertThat(results, containsInAnyOrder(
        groupedOpeningHours("Monday", "Closed"),
        groupedOpeningHours("Tuesday - Friday", "11:30 - 15:00", "18:00 - 00:00"),
        groupedOpeningHours("Saturday", "18:30 - 00:00"),
        groupedOpeningHours("Sunday", "11:30 - 15:00")));
  }

  private static void assertOpeningHours(List<GroupedOpeningHours> results, String dayOfWeek, List<String> openingHours) {
    GroupedOpeningHours groupedOpeningHours = results.stream()
        .filter(i -> i.getDaysOfWeek().equals(dayOfWeek)).findFirst().orElse(null);
    assertThat(groupedOpeningHours.getOpeningHours(), containsInAnyOrder(openingHours.toArray()));
  }

  private OpeningHoursEntity openingHours(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime, boolean closed) {
    OpeningHoursEntity openingHours = new OpeningHoursEntity();
    openingHours.setId(UUID.randomUUID());
    openingHours.setDayOfWeek(dayOfWeek);
    openingHours.setOpeningTime(openingTime);
    openingHours.setClosingTime(closingTime);
    openingHours.setClosed(closed);
    return openingHours;
  }

  private static GroupedOpeningHours groupedOpeningHours(String daysOfWeek, String... openingHours) {
    GroupedOpeningHours groupedOpeningHours = new GroupedOpeningHours();
    groupedOpeningHours.setDaysOfWeek(daysOfWeek);
    groupedOpeningHours.setOpeningHours(List.of(openingHours));
    return groupedOpeningHours;
  }

}