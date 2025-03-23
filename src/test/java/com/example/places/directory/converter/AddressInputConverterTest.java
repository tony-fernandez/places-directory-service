package com.example.places.directory.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.example.places.directory.model.AddressInput;
import com.example.places.directory.persistence.model.AddressEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class AddressInputConverterTest extends AbstractConverterTest {

  @InjectMocks
  private AddressInputConverter addressInputConverter;

  @Test
  void givenNullAddressInputShouldReturnNull() {
    AddressInput input = null;

    AddressEntity output = addressInputConverter.convert(input);

    assertThat(output, is(nullValue()));
  }

  @Test
  void shouldConvert() {
    AddressInput input = new AddressInput();
    input.setCity(CITY);
    input.setCountry(COUNTRY);
    input.setPostcode(POSTCODE);
    input.setStreet(STREET);

    AddressEntity output = addressInputConverter.convert(input);

    assertThat(output.getCity(), is(equalTo(CITY)));
    assertThat(output.getCountry(), is(equalTo(COUNTRY)));
    assertThat(output.getPostcode(), is(equalTo(POSTCODE)));
    assertThat(output.getStreet(), is(equalTo(STREET)));
  }
}