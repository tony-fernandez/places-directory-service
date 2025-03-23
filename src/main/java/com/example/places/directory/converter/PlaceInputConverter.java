package com.example.places.directory.converter;

import com.example.places.directory.model.PlaceInput;
import com.example.places.directory.persistence.model.AddressEntity;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import com.example.places.directory.persistence.model.PlaceEntity;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceInputConverter implements Converter<PlaceInput, PlaceEntity> {

  private final AddressInputConverter addressInputConverter;
  private final OpeningHoursInputConverter openingHoursInputConverter;

  @Override
  public PlaceEntity convert(PlaceInput input) {
    if(input == null) {
      return null;
    }
    PlaceEntity output = new PlaceEntity();
    AddressEntity address = addressInputConverter.convert(input.getAddress());
    output.setAddress(address);
    address.setPlace(output);
    output.setName(input.getName());
    output.setDescription(input.getDescription());
    List<OpeningHoursEntity> openingHours = openingHoursInputConverter.convert(
        input.getOpeningHours());
    openingHours.forEach(openingHour -> openingHour.setPlace(output));
    output.setOpeningHours(new HashSet<>(openingHours));
    return output;
  }

}
