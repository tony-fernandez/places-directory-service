package com.example.places.directory.service.impl;

import static java.lang.String.format;

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
import com.example.places.directory.service.OpeningHoursService;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpeningHoursServiceImpl implements OpeningHoursService {

  private static final Map<DayOfWeekEnum, DayOfWeek> DAY_OF_WEEK_MAP = Map.of(
      DayOfWeekEnum.MONDAY, DayOfWeek.MONDAY,
      DayOfWeekEnum.TUESDAY, DayOfWeek.TUESDAY,
      DayOfWeekEnum.WEDNESDAY, DayOfWeek.WEDNESDAY,
      DayOfWeekEnum.THURSDAY, DayOfWeek.THURSDAY,
      DayOfWeekEnum.FRIDAY, DayOfWeek.FRIDAY,
      DayOfWeekEnum.SATURDAY, DayOfWeek.SATURDAY,
      DayOfWeekEnum.SUNDAY, DayOfWeek.SUNDAY);

  private final OpeningHoursConverter openingHoursConverter;
  private final OpeningHoursInputConverter openingHoursInputConverter;
  private final OpeningHoursRepository openingHoursRepository;
  private final PlaceRepository placeRepository;

  @Override
  public OpeningHours addOpeningHours(UUID placeId, OpeningHoursInput openingHours) {
    PlaceEntity place = placeRepository.findById(placeId)
        .orElseThrow(() -> new PlaceNotFoundException(
            format("Place with id %s not found", placeId)));
    OpeningHoursEntity newOpeningHours = openingHoursInputConverter.convert(openingHours);
    newOpeningHours.setPlace(place);

    try {
      return openingHoursConverter.convert(openingHoursRepository.save(newOpeningHours));
    } catch (DataIntegrityViolationException e) {
      throw new DuplicateOpeningHoursException(
          "An existing opening hours with the same values already exist.");
    }
  }

  @Override
  public OpeningHours updateOpeningHours(UUID placeId, UUID openingHoursId,
      OpeningHoursInput openingHours) {
    return deleteOpeningHours(findOpeningHours(placeId, openingHoursId), openingHours);
  }

  @Override
  public void deleteOpeningHours(UUID placeId, UUID openingHoursId) {
    openingHoursRepository.delete(findOpeningHours(placeId, openingHoursId));
  }

  private OpeningHoursEntity findOpeningHours(UUID placeId, UUID openingHoursId) {
    return openingHoursRepository.findByIdAndPlaceId(openingHoursId, placeId)
        .orElseThrow(() -> new OpeningHoursNotFoundException(
            format("Opening hours for place with id %s and with id %s not found", placeId,
                openingHoursId)));
  }

  private OpeningHours deleteOpeningHours(OpeningHoursEntity openingHoursEntity,
      OpeningHoursInput openingHours) {
    openingHoursEntity.setOpeningTime(LocalTime.parse(openingHours.getOpeningTime()));
    openingHoursEntity.setClosingTime(LocalTime.parse(openingHours.getClosingTime()));
    openingHoursEntity.setClosed(Optional.ofNullable(openingHours.getClosed()).orElse(false));
    openingHoursEntity.setDayOfWeek(DAY_OF_WEEK_MAP.get(openingHours.getDayOfWeek()));
    return openingHoursConverter.convert(openingHoursRepository.save(openingHoursEntity));
  }

}
