package com.example.places.directory.converter;

import com.example.places.directory.model.AddressInput;
import com.example.places.directory.persistence.model.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressInputConverter implements Converter<AddressInput, AddressEntity> {

  @Override
  public AddressEntity convert(AddressInput input) {
    if (input == null) {
      return null;
    }
    AddressEntity output = new AddressEntity();
    output.setStreet(input.getStreet());
    output.setCity(input.getCity());
    output.setPostcode(input.getPostcode());
    output.setCountry(input.getCountry());
    return output;
  }

}
