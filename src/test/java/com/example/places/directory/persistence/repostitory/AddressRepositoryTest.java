package com.example.places.directory.persistence.repostitory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.example.places.directory.persistence.model.AddressEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AddressRepositoryTest extends AbstractRepositoryTest {

  @Autowired
  private AddressRepository addressRepository;

  @Test
  void shouldFindByPlaceIdAndId() {
    AddressEntity result = addressRepository.findByIdAndPlaceId(addressId, placeId)
        .orElse(null);

    assertThat(result.getId(), is(equalTo(addressId)));
  }

}