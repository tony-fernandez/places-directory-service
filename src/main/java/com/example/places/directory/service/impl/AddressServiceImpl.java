package com.example.places.directory.service.impl;

import static java.lang.String.format;

import com.example.places.directory.converter.AddressConverter;
import com.example.places.directory.exception.AddressNotFoundException;
import com.example.places.directory.model.Address;
import com.example.places.directory.model.AddressInput;
import com.example.places.directory.persistence.model.AddressEntity;
import com.example.places.directory.persistence.repostitory.AddressRepository;
import com.example.places.directory.service.AddressService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressConverter addressConverter;
  private final AddressRepository addressRepository;

  @Override
  public Address updateAddress(UUID placeId, UUID addressId, AddressInput address) {
    AddressEntity addressEntity = addressRepository.findByIdAndPlaceId(addressId, placeId).orElseThrow(
        () -> new AddressNotFoundException(
            format("Address for place with id %s and with id %s not found", placeId,
                addressId)));
    return updateAddress(addressEntity, address);
  }

  private Address updateAddress(AddressEntity addressEntity, AddressInput address) {
    addressEntity.setStreet(address.getStreet());
    addressEntity.setCity(address.getCity());
    addressEntity.setPostcode(address.getPostcode());
    addressEntity.setCountry(address.getCountry());
    return addressConverter.convert(addressRepository.save(addressEntity));
  }

}
