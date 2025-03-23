package com.example.places.directory.converter;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.capitalize;

import com.example.places.directory.model.GroupedOpeningHours;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class GroupedOpeningHoursConverter {

  public List<GroupedOpeningHours> convert(Set<OpeningHoursEntity> input) {
    if(isEmpty(input)) {
      return emptyList();
    }
    List<OpeningHoursEntity> inputList = new ArrayList<>(input);
    inputList.sort(Comparator.comparing(OpeningHoursEntity::getDayOfWeek)
        .thenComparing(OpeningHoursEntity::getOpeningTime));

    List<GroupedOpeningHours> result = new ArrayList<>();

    Map<DayOfWeek, List<String>> closed = new LinkedHashMap<>();
    Map<DayOfWeek, List<String>> grouped = new LinkedHashMap<>();

    for (OpeningHoursEntity openingHours : inputList) {
      List<String> entries = grouped.getOrDefault(openingHours.getDayOfWeek(), new ArrayList<>());

      if (openingHours.isClosed()) {
        entries.add("Closed");
        grouped.put(openingHours.getDayOfWeek(), entries);
        continue;
      }

      DayOfWeek dayOfWeek = openingHours.getDayOfWeek();
      String openingTime = toTime(openingHours.getOpeningTime());
      String closingTime = toTime(openingHours.getClosingTime());

      entries.add(openingTime + " - " + closingTime);
      grouped.put(dayOfWeek, entries);
    }

    Map<String, List<DayOfWeek>> map = new LinkedHashMap<>();

    for (Entry<DayOfWeek, List<String>> entry : grouped.entrySet()) {
      String key = String.join(",", entry.getValue());
      List<DayOfWeek> daysOfTheWeek = map.getOrDefault(key, new ArrayList<>());
      daysOfTheWeek.add(entry.getKey());
      map.put(key, daysOfTheWeek);
    }

    for (Entry<String, List<DayOfWeek>> entry : map.entrySet()) {
      GroupedOpeningHours groupedOpeningHours = new GroupedOpeningHours();
      groupedOpeningHours.setDaysOfWeek(generateDaysOfTheWeekLabel(entry.getValue()));
      groupedOpeningHours.setOpeningHours(Stream.of(entry.getKey().split(",")).toList());
      result.add(groupedOpeningHours);
    }

    return result;
  }

  private String toTime(LocalTime time) {
    return time.format(DateTimeFormatter.ofPattern("HH:mm"));
  }

  private String generateDaysOfTheWeekLabel(List<DayOfWeek> daysOfTheWeek) {
    if (daysOfTheWeek.size() == 1) {
      return capitalize(daysOfTheWeek.getFirst().toString().toLowerCase());
    }
    return String.format("%s - %s", capitalize(daysOfTheWeek.getFirst().toString().toLowerCase()),
        capitalize(daysOfTheWeek.getLast().toString().toLowerCase()));
  }

}
