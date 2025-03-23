package com.example.places.directory.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.example.places.directory.model.Address;
import com.example.places.directory.persistence.model.AddressEntity;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class AddressConverterTest extends AbstractConverterTest {

  @InjectMocks
  private AddressConverter addressConverter;

  @Test
  void givenNullAddressEntityShouldReturnNull() {
    AddressEntity input = null;

    Address output = addressConverter.convert(input);

    assertThat(output, is(nullValue()));
  }

  @Test
  void shouldConvert() {
    UUID id = UUID.randomUUID();
    AddressEntity input = new AddressEntity();
    input.setCity(CITY);
    input.setCountry(COUNTRY);
    input.setId(id);
    input.setPostcode(POSTCODE);
    input.setStreet(STREET);

    Address output = addressConverter.convert(input);

    assertThat(output.getCity(), is(equalTo(CITY)));
    assertThat(output.getCountry(), is(equalTo(COUNTRY)));
    assertThat(output.getId(), is(equalTo(id)));
    assertThat(output.getPostcode(), is(equalTo(POSTCODE)));
    assertThat(output.getStreet(), is(equalTo(STREET)));
  }

}