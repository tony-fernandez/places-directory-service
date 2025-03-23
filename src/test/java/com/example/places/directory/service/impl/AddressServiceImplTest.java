package com.example.places.directory.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.places.directory.converter.AddressConverter;
import com.example.places.directory.exception.AddressNotFoundException;
import com.example.places.directory.model.Address;
import com.example.places.directory.model.AddressInput;
import com.example.places.directory.persistence.model.AddressEntity;
import com.example.places.directory.persistence.repostitory.AddressRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

  private static final UUID ADDRESS_ID = UUID.randomUUID();
  private static final UUID PLACE_ID = UUID.randomUUID();

  @Mock
  private AddressConverter addressConverter;

  @Mock
  private AddressRepository addressRepository;

  @InjectMocks
  private AddressServiceImpl addressService;

  @Test
  void givenAddressNotFoundShouldThrowAddressNotFoundException() {
    AddressInput address = new AddressInput();
    when(addressRepository.findByIdAndPlaceId(ADDRESS_ID, PLACE_ID)).thenReturn(Optional.empty());

    assertThrows(AddressNotFoundException.class,
        () -> addressService.updateAddress(PLACE_ID, ADDRESS_ID, address));
  }

  @Test
  void shouldUpdateAddress() {
    AddressInput addressInput = new AddressInput();
    addressInput.setCountry("Germany");
    addressInput.setCity("Berlin");
    addressInput.setPostcode("98765");
    addressInput.setStreet("Street 2");
    AddressEntity addressEntity = new AddressEntity();
    addressEntity.setCity("Berlin");
    addressEntity.setCountry("Germany");
    addressEntity.setPostcode("12345");
    addressEntity.setStreet("Street 1");
    Address expectedUpdatedAddress = new Address();

    when(addressRepository.findByIdAndPlaceId(ADDRESS_ID, PLACE_ID)).thenReturn(Optional.of(addressEntity));
    when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
    when(addressConverter.convert(addressEntity)).thenReturn(expectedUpdatedAddress);

    Address actualUpdatedAddress = addressService.updateAddress(PLACE_ID, ADDRESS_ID, addressInput);

    assertThat(actualUpdatedAddress, is(equalTo(expectedUpdatedAddress)));
  }

}