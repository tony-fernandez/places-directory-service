package com.example.places.directory.converter;

import com.example.places.directory.model.Address;
import com.example.places.directory.persistence.model.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter implements Converter<AddressEntity, Address>{

  @Override
  public Address convert(AddressEntity input) {
    if (input == null) {
      return null;
    }
    Address output = new Address();
    output.setCity(input.getCity());
    output.setCountry(input.getCountry());
    output.setId(input.getId());
    output.setPostcode(input.getPostcode());
    output.setStreet(input.getStreet());
    return output;
  }
}
