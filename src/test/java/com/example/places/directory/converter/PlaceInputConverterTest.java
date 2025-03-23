package com.example.places.directory.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

import com.example.places.directory.model.AddressInput;
import com.example.places.directory.model.OpeningHoursInput;
import com.example.places.directory.model.PlaceInput;
import com.example.places.directory.persistence.model.AddressEntity;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import com.example.places.directory.persistence.model.PlaceEntity;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class PlaceInputConverterTest extends AbstractConverterTest {

  @Mock
  private AddressInputConverter addressInputConverter;

  @Mock
  private OpeningHoursInputConverter openingHoursInputConverter;

  @InjectMocks
  private PlaceInputConverter placeInputConverter;

  @Test
  void givenNullAddressEntityShouldReturnNull() {
    PlaceInput input = null;

    PlaceEntity output = placeInputConverter.convert(input);

    assertThat(output, is(nullValue()));
  }

  @Test
  void shouldConvert() {
    AddressInput addressInput = new AddressInput();
    AddressEntity addressOutput = new AddressEntity();
    OpeningHoursInput openingHoursInput1 = new OpeningHoursInput();
    OpeningHoursInput openingHoursInput2 = new OpeningHoursInput();
    OpeningHoursEntity openingHoursOutput1 = new OpeningHoursEntity();
    OpeningHoursEntity openingHoursOutput2 = new OpeningHoursEntity();
    PlaceInput input = new PlaceInput();
    input.setAddress(addressInput);
    input.setName(NAME);
    input.setDescription(DESCRIPTION);
    input.setOpeningHours(List.of(openingHoursInput1, openingHoursInput2));
    when(addressInputConverter.convert(addressInput)).thenReturn(addressOutput);
    when(openingHoursInputConverter.convert(List.of(openingHoursInput1, openingHoursInput2))).thenReturn(
        List.of(openingHoursOutput1, openingHoursOutput2));

    PlaceEntity output = placeInputConverter.convert(input);

    assertThat(output.getAddress(), is(equalTo(addressOutput)));
    assertThat(output.getDescription(), is(equalTo(DESCRIPTION)));
    assertThat(output.getName(), is(equalTo(NAME)));
    assertThat(output.getOpeningHours(), containsInAnyOrder(openingHoursOutput1, openingHoursOutput2));
  }

}