package com.example.places.directory.converter;

import com.example.places.directory.model.OpeningHoursInput;
import com.example.places.directory.model.OpeningHoursInput.DayOfWeekEnum;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class OpeningHoursInputConverter implements
    Converter<OpeningHoursInput, OpeningHoursEntity> {

  private static final Map<DayOfWeekEnum, DayOfWeek> DAY_OF_WEEK_MAP = Map.of(
      DayOfWeekEnum.MONDAY, DayOfWeek.MONDAY,
      DayOfWeekEnum.TUESDAY, DayOfWeek.TUESDAY,
      DayOfWeekEnum.WEDNESDAY, DayOfWeek.WEDNESDAY,
      DayOfWeekEnum.THURSDAY, DayOfWeek.THURSDAY,
      DayOfWeekEnum.FRIDAY, DayOfWeek.FRIDAY,
      DayOfWeekEnum.SATURDAY, DayOfWeek.SATURDAY,
      DayOfWeekEnum.SUNDAY, DayOfWeek.SUNDAY);

  @Override
  public OpeningHoursEntity convert(OpeningHoursInput input) {
    if (input == null) {
      return null;
    }
    OpeningHoursEntity output = new OpeningHoursEntity();
    output.setDayOfWeek(DAY_OF_WEEK_MAP.get(input.getDayOfWeek()));
    output.setClosed(Optional.ofNullable(input.getClosed()).orElse(false));
    output.setOpeningTime(
        Optional.ofNullable(input.getOpeningTime()).map(this::toLocalTime).orElse(null));
    output.setClosingTime(
        Optional.ofNullable(input.getClosingTime()).map(this::toLocalTime).orElse(null));
    return output;
  }

  private LocalTime toLocalTime(String time) {
    return LocalTime.parse(time);
  }

}
