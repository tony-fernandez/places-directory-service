package com.example.places.directory.converter;

import com.example.places.directory.model.OpeningHours;
import com.example.places.directory.model.OpeningHours.DayOfWeekEnum;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class OpeningHoursConverter implements Converter<OpeningHoursEntity, OpeningHours> {

  private static final Map<DayOfWeek, DayOfWeekEnum> DAY_OF_WEEK_MAP = Map.of(
      DayOfWeek.MONDAY, DayOfWeekEnum.MONDAY,
      DayOfWeek.TUESDAY, DayOfWeekEnum.TUESDAY,
      DayOfWeek.WEDNESDAY, DayOfWeekEnum.WEDNESDAY,
      DayOfWeek.THURSDAY, DayOfWeekEnum.THURSDAY,
      DayOfWeek.FRIDAY, DayOfWeekEnum.FRIDAY,
      DayOfWeek.SATURDAY, DayOfWeekEnum.SATURDAY,
      DayOfWeek.SUNDAY, DayOfWeekEnum.SUNDAY);

  @Override
  public OpeningHours convert(OpeningHoursEntity input) {
    if (input == null) {
      return null;
    }
    OpeningHours output = new OpeningHours();
    output.setClosed(input.isClosed());
    output.setDayOfWeek(DAY_OF_WEEK_MAP.get(input.getDayOfWeek()));
    output.setId(input.getId());
    output.setOpeningTime(
        Optional.ofNullable(input.getOpeningTime()).map(this::toTime).orElse(null));
    output.setClosingTime(
        Optional.ofNullable(input.getClosingTime()).map(this::toTime).orElse(null));
    return output;
  }

  private String toTime(LocalTime time) {
    return time.format(DateTimeFormatter.ofPattern("HH:mm"));
  }

}
