package com.example.places.directory.converter;

import com.example.places.directory.model.Place;
import com.example.places.directory.persistence.model.PlaceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceConverter implements Converter<PlaceEntity, Place> {

  private final AddressConverter addressConverter;
  private final GroupedOpeningHoursConverter groupedOpeningHoursConverter;
  private final OpeningHoursConverter openingHoursConverter;

  @Override
  public Place convert(PlaceEntity input) {
    if (input == null) {
      return null;
    }
    Place output = new Place();
    output.setId(input.getId());
    output.setName(input.getName());
    output.setDescription(input.getDescription());
    output.setAddress(addressConverter.convert(input.getAddress()));
    output.setOpeningHours(
        openingHoursConverter.convert(input.getOpeningHours().stream().toList()));
    output.setGroupedOpeningHours(
        groupedOpeningHoursConverter.convert(input.getOpeningHours()));
    return output;
  }

}
