package com.example.places.directory.converter;

import static java.lang.String.format;

import com.example.places.directory.exception.InvalidOpeningHoursException;
import com.example.places.directory.exception.MissingTimeException;
import com.example.places.directory.model.OpeningHoursInput;
import com.example.places.directory.model.OpeningHoursInput.DayOfWeekEnum;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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

    if(output.isClosed()) {
      output.setOpeningTime(null);
      output.setClosingTime(null);
      return output;
    }

    if(input.getOpeningTime() == null || input.getClosingTime() == null) {
      throw new MissingTimeException("Opening time and closing time must be provided.");
    }

    output.setOpeningTime(toLocalTime(input.getOpeningTime()));
    output.setClosingTime(toLocalTime(input.getClosingTime()));

    if(output.getClosingTime().equals(LocalTime.of(0, 0))) {
      output.setClosingTime(output.getClosingTime().plusHours(24));
    }

    if(!isValidOpeningHours(output)) {
      throw new InvalidOpeningHoursException(format("Opening time %s must be before closing time %s.",
          input.getOpeningTime(), input.getClosingTime()));
    }

    return output;
  }

  private LocalTime toLocalTime(String time) {
    try {
      return LocalTime.parse(time);
    } catch (DateTimeParseException e) {
      throw new InvalidOpeningHoursException(
          format("Invalid time '%s'.  Please provide time correct format, i.e. '10:00'", time));
    }

  }

  private boolean isValidOpeningHours(OpeningHoursEntity output) {
    if(output.getClosingTime().equals(LocalTime.MIDNIGHT)) {
      return true;
    }
    return output.getOpeningTime().isBefore(output.getClosingTime());
  }

}
