package com.example.places.directory.persistence.repostitory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.example.places.directory.persistence.model.OpeningHoursEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OpeningHoursRepositoryTest extends AbstractRepositoryTest {

  @Autowired
  private OpeningHoursRepository openingHoursRepository;

  @Test
  void shouldFindByPlaceIdAndId() {
    OpeningHoursEntity result = openingHoursRepository.findByIdAndPlaceId(openingHoursId, placeId)
        .orElse(null);

    assertThat(result.getId(), is(equalTo(openingHoursId)));
  }


}