package com.example.places.directory.persistence.repostitory;

import static java.util.stream.Collectors.toSet;

import com.example.places.directory.persistence.model.AddressEntity;
import com.example.places.directory.persistence.model.OpeningHoursEntity;
import com.example.places.directory.persistence.model.PlaceEntity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class AbstractRepositoryTest {

  @Autowired
  private PlaceRepository placeRepository;

  protected UUID addressId;
  protected UUID openingHoursId;
  protected UUID placeId;

  @BeforeEach
  void setup() {
    AddressEntity address = new AddressEntity();
    address.setCity("city");
    address.setCountry("country");
    address.setPostcode("postcode");
    address.setStreet("street");
    final PlaceEntity place = new PlaceEntity();
    place.setName("name");
    place.setDescription("description");
    place.setAddress(address);
    address.setPlace(place);
    Set<OpeningHoursEntity> openingHours = Stream.of(DayOfWeek.values()).map(this::openingHour)
        .collect(toSet());
    openingHours.forEach(i -> i.setPlace(place));
    place.setOpeningHours(openingHours);
    PlaceEntity savedPlace = placeRepository.save(place);
    addressId = place.getAddress().getId();
    openingHoursId = place.getOpeningHours().iterator().next().getId();
    placeId = savedPlace.getId();
  }

  private OpeningHoursEntity openingHour(DayOfWeek dayOfWeek) {
    OpeningHoursEntity openingHours = new OpeningHoursEntity();
    openingHours.setDayOfWeek(dayOfWeek);
    openingHours.setOpeningTime(LocalTime.of(10, 0));
    openingHours.setClosingTime(LocalTime.of(17, 0));
    return openingHours;
  }

}
