package com.example.places.directory.service;

import com.example.places.directory.model.Address;
import com.example.places.directory.model.AddressInput;
import java.util.UUID;

public interface AddressService {

  /**
   * Updates the address for the given place id and address id.
   *
   * @param placeId   the place id.
   * @param addressId the address id.
   * @param address   the address data.
   * @return the updated address.
   */
  Address updateAddress(UUID placeId, UUID addressId, AddressInput address);

}
