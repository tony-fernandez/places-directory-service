package com.example.places.directory.controller;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.example.places.directory.IntegrationTestConfiguration;
import com.example.places.directory.model.Address;
import com.example.places.directory.model.AddressInput;
import com.example.places.directory.model.GroupedOpeningHours;
import com.example.places.directory.model.OpeningHours;
import com.example.places.directory.model.OpeningHours.DayOfWeekEnum;
import com.example.places.directory.model.OpeningHoursInput;
import com.example.places.directory.model.Place;
import com.example.places.directory.model.PlaceInput;
import com.example.places.directory.model.Problem;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

@ActiveProfiles("test")
@SpringBootTest(classes = IntegrationTestConfiguration.class, webEnvironment = RANDOM_PORT)
class PlacesControllerTest {

  private static final String PLACES = "/places";
  private static final Map<OpeningHours.DayOfWeekEnum, OpeningHoursInput.DayOfWeekEnum> DAY_OF_WEEK_MAP = Map.of(
      OpeningHours.DayOfWeekEnum.MONDAY, OpeningHoursInput.DayOfWeekEnum.MONDAY,
      OpeningHours.DayOfWeekEnum.TUESDAY, OpeningHoursInput.DayOfWeekEnum.TUESDAY,
      OpeningHours.DayOfWeekEnum.WEDNESDAY, OpeningHoursInput.DayOfWeekEnum.WEDNESDAY,
      OpeningHours.DayOfWeekEnum.THURSDAY, OpeningHoursInput.DayOfWeekEnum.THURSDAY,
      OpeningHours.DayOfWeekEnum.FRIDAY, OpeningHoursInput.DayOfWeekEnum.FRIDAY,
      OpeningHours.DayOfWeekEnum.SATURDAY, OpeningHoursInput.DayOfWeekEnum.SATURDAY,
      OpeningHours.DayOfWeekEnum.SUNDAY, OpeningHoursInput.DayOfWeekEnum.SUNDAY);
  private static final String PLACE_WITH_ID_S_NOT_FOUND = "Place with id %s not found";

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void shouldCreateNewPlace() {
    PlaceInput placeInput = place("Vapiano", "Best Italian pizza in town");

    ResponseEntity<Place> response = createPlace(placeInput);

    List<GroupedOpeningHours> groupedOpeningHours = response.getBody().getGroupedOpeningHours();
    assertThat(response.getStatusCode(), is(equalTo(CREATED)));
    assertThat(groupedOpeningHours, containsInAnyOrder(
        groupedOpeningHours("Monday", "Closed"),
        groupedOpeningHours("Tuesday - Friday", "11:30 - 15:00", "18:00 - 00:00"),
        groupedOpeningHours("Saturday", "18:30 - 00:00"),
        groupedOpeningHours("Sunday", "11:30 - 15:00")));
  }

  @Test
  void givenPlaceAlreadyExistsShouldReturnConflictHttpCode() {
    PlaceInput placeInput = place("Wagamama", "Best Japanese food in town");
    createPlace(placeInput);

    ResponseEntity<Problem> response = restTemplate.postForEntity(placesUrl(), placeInput,
        Problem.class);

    assertThat(response.getStatusCode(), is(equalTo(CONFLICT)));
    assertThat(response.getBody().getMessage(),
        is(equalTo("Place with name 'Wagamama' already exists")));
  }

  @Test
  void givenDuplicateOpeningHoursShouldReturnConflictHttpCode() {
    OpeningHoursInput openingHoursInput1 = new OpeningHoursInput();
    openingHoursInput1.setDayOfWeek(OpeningHoursInput.DayOfWeekEnum.MONDAY);
    openingHoursInput1.setOpeningTime("10:00");
    openingHoursInput1.setClosingTime("17:00");
    OpeningHoursInput openingHoursInput2 = new OpeningHoursInput();
    openingHoursInput2.setDayOfWeek(OpeningHoursInput.DayOfWeekEnum.MONDAY);
    openingHoursInput2.setOpeningTime("10:00");
    openingHoursInput2.setClosingTime("17:00");
    PlaceInput placeInput = place("Yellow River", "Best Chinese food in town");
    placeInput.setOpeningHours(List.of(openingHoursInput1, openingHoursInput2));

    ResponseEntity<Problem> response = restTemplate.postForEntity(placesUrl(), placeInput,
        Problem.class);

    assertThat(response.getStatusCode(), is(equalTo(CONFLICT)));
    assertThat(response.getBody().getMessage(), is(equalTo(
        "Invalid opening hours.  Please provide unique opening hours for each day of the week.")));
  }

  @Test
  void shouldDeletePlace() {
    PlaceInput placeInput = place("Pizza Hut", "Not quite the best pizza in town");
    ResponseEntity<Place> response = createPlace(placeInput);
    UUID placeId = response.getBody().getId();

    ResponseEntity<Void> deleteResponse = restTemplate.exchange(placesUrl(placeId), DELETE, null,
        Void.class);

    assertThat(deleteResponse.getStatusCode(), is(equalTo(NO_CONTENT)));
  }

  @Test
  void givenIncorrectPlaceIdDeletePlaceShouldReturnNotFound() {
    UUID placeId = UUID.randomUUID();

    ResponseEntity<Problem> response = restTemplate.exchange(placesUrl(placeId), DELETE, null,
        Problem.class);

    assertThat(response.getStatusCode(), is(equalTo(NOT_FOUND)));
    assertThat(response.getBody().getMessage(), is(equalTo(
        format(PLACE_WITH_ID_S_NOT_FOUND, placeId))));
  }

  @Test
  void shouldFindPlaceById() {
    PlaceInput placeInput = place("TGI Fridays", "Food used to be good, not anymore");
    ResponseEntity<Place> response = createPlace(placeInput);
    UUID placeId = response.getBody().getId();

    ResponseEntity<Place> placeResponse = restTemplate.getForEntity(placesUrl(placeId),
        Place.class);

    assertThat(placeResponse.getStatusCode(), is(equalTo(OK)));
  }

  @Test
  void givenPlaceDoesNotExistShouldReturnNotFoundHttpCode() {
    UUID placeId = UUID.randomUUID();
    ResponseEntity<Problem> response = restTemplate.getForEntity(placesUrl(placeId),
        Problem.class);

    assertThat(response.getStatusCode(), is(equalTo(NOT_FOUND)));
    assertThat(response.getBody().getMessage(), is(equalTo(
        format(PLACE_WITH_ID_S_NOT_FOUND, placeId))));
  }

  @Test
  void shouldFindAllPlaces() {
    createPlace(place("One new place", "First place"));
    createPlace(place("Another new place", "Second place"));

    ResponseEntity<List<Place>> response = restTemplate.exchange(placesUrl(), GET, null,
        new ParameterizedTypeReference<>() {
        });

    assertThat(response.getStatusCode(), is(equalTo(OK)));
    assertThat(response.getBody(), is(not(empty())));
  }

  @Test
  void shouldUpdateAddress() {
    PlaceInput placeInput = place("McDonald's", "Fast food");
    ResponseEntity<Place> response = createPlace(placeInput);
    Place place = response.getBody();
    UUID placeId = place.getId();
    Address existingAddress = place.getAddress();
    assertThat(existingAddress.getStreet(), is(equalTo("Street 1")));
    assertThat(existingAddress.getCity(), is(equalTo("City 1")));
    AddressInput addressForUpdate = createAddressForUpdate(existingAddress);

    ResponseEntity<Address> addressResponse = restTemplate.exchange(
        addressesUrl(placeId, existingAddress.getId()),
        PUT, new HttpEntity<>(addressForUpdate), Address.class);

    assertThat(addressResponse.getStatusCode(), is(equalTo(OK)));
    assertThat(addressResponse.getBody().getStreet(), is(equalTo("New street")));
    assertThat(addressResponse.getBody().getCity(), is(equalTo("New city")));
  }

  @Test
  void givenIncorrectAddressIdUpdateAddressShouldReturnNotFound() {
    Address address = new Address();
    UUID placeId = UUID.randomUUID();
    UUID addressId = UUID.randomUUID();

    ResponseEntity<Problem> response = restTemplate.exchange(
        addressesUrl(placeId, addressId),
        PUT, new HttpEntity<>(address), Problem.class);

    assertThat(response.getStatusCode(), is(equalTo(NOT_FOUND)));
    assertThat(response.getBody().getMessage(), is(equalTo(
        format("Address for place with id %s and with id %s not found", placeId,
            addressId))));

  }

  @Test
  void givenNewOpeningHoursForNonExistingPlaceShouldReturnNotFound() {
    UUID placeId = UUID.randomUUID();

    ResponseEntity<Problem> response = restTemplate.exchange(
        openingHoursUrl(placeId),
        POST, new HttpEntity<>(new OpeningHoursInput()), Problem.class);

    assertThat(response.getStatusCode(), is(equalTo(NOT_FOUND)));
    assertThat(response.getBody().getMessage(), is(equalTo(
        format(PLACE_WITH_ID_S_NOT_FOUND, placeId))));
  }

  @Test
  void shouldCreateNewOpeningHours() {
    PlaceInput placeInput = place("Wendy Burgers", "Kinda old burgers");
    ResponseEntity<Place> response = createPlace(placeInput);
    Place place = response.getBody();
    UUID placeId = place.getId();

    OpeningHours mondayOpeningHours = place.getOpeningHours().stream()
        .filter(i -> i.getDayOfWeek().equals(DayOfWeekEnum.MONDAY)).findFirst().orElse(null);

    deleteOpeningHours(placeId, mondayOpeningHours.getId());

    OpeningHoursInput openingHoursInput = openingHour(OpeningHoursInput.DayOfWeekEnum.MONDAY, null,
        null, true);

    ResponseEntity<OpeningHours> openingHoursResponse = restTemplate.exchange(
        openingHoursUrl(placeId), POST, new HttpEntity<>(openingHoursInput), OpeningHours.class);

    assertThat(openingHoursResponse.getStatusCode(), is(equalTo(CREATED)));
  }

  @Test
  void shouldUpdateOpeningHours() {
    PlaceInput placeInput = place("KFC", "Finger licking good");
    ResponseEntity<Place> response = createPlace(placeInput);
    Place place = response.getBody();
    UUID placeId = place.getId();
    List<OpeningHours> allOpeningHours = place.getOpeningHours();
    OpeningHours firstOpeningHours = allOpeningHours.get(0);
    OpeningHoursInput openingHoursForUpdate = createOpeningHoursForUpdate(firstOpeningHours);

    ResponseEntity<OpeningHours> openingHoursResponse = restTemplate.exchange(
        openingHoursUrl(placeId, firstOpeningHours.getId()), PUT,
        new HttpEntity<>(openingHoursForUpdate), OpeningHours.class);

    assertThat(openingHoursResponse.getStatusCode(), is(equalTo(OK)));
    assertThat(openingHoursResponse.getBody().getOpeningTime(), is(equalTo("11:00")));
    assertThat(openingHoursResponse.getBody().getClosingTime(), is(equalTo("18:00")));
  }

  @Test
  void givenIncorrectOpeningHoursIdUpdateOpeningHoursShouldReturnNotFound() {
    OpeningHours openingHours = new OpeningHours();
    UUID placeId = UUID.randomUUID();
    UUID openingHoursId = UUID.randomUUID();

    ResponseEntity<Problem> response = restTemplate.exchange(
        openingHoursUrl(placeId, openingHoursId), PUT,
        new HttpEntity<>(openingHours), Problem.class);

    assertThat(response.getStatusCode(), is(equalTo(NOT_FOUND)));
    assertThat(response.getBody().getMessage(), is(equalTo(
        format("Opening hours for place with id %s and with id %s not found", placeId,
            openingHoursId))));
  }

  @Test
  void givenIncorrectOpeningHoursIdDeleteOpeningHoursShouldReturnNotFound() {
    OpeningHours openingHours = new OpeningHours();
    UUID placeId = UUID.randomUUID();
    UUID openingHoursId = UUID.randomUUID();

    ResponseEntity<Problem> response = restTemplate.exchange(
        openingHoursUrl(placeId, openingHoursId), DELETE,
        new HttpEntity<>(openingHours), Problem.class);

    assertThat(response.getStatusCode(), is(equalTo(NOT_FOUND)));
    assertThat(response.getBody().getMessage(), is(equalTo(
        format("Opening hours for place with id %s and with id %s not found", placeId,
            openingHoursId))));
  }

  @Test
  void shouldDeleteExistingOpeningHours() {
    PlaceInput placeInput = place("McDonalds", "Best fries in town");
    ResponseEntity<Place> response = createPlace(placeInput);
    Place place = response.getBody();
    UUID placeId = place.getId();
    List<OpeningHours> allOpeningHours = place.getOpeningHours();
    OpeningHours firstOpeningHours = allOpeningHours.get(0);

    ResponseEntity<Void> openingHoursResponse = deleteOpeningHours(placeId,
        firstOpeningHours.getId());

    assertThat(openingHoursResponse.getStatusCode(), is(equalTo(NO_CONTENT)));
  }

  private ResponseEntity<Place> createPlace(PlaceInput placeInput) {
    return restTemplate.postForEntity(placesUrl(), placeInput, Place.class);
  }

  private ResponseEntity<Void> deleteOpeningHours(UUID placeId, UUID openingHoursId) {
    return restTemplate.exchange(openingHoursUrl(placeId, openingHoursId), DELETE, null,
        Void.class);
  }

  private PlaceInput place(String name, String description) {
    PlaceInput placeInput = new PlaceInput();
    placeInput.setName(name);
    placeInput.setDescription(description);
    placeInput.setAddress(address());
    placeInput.setOpeningHours(openingHours());
    return placeInput;
  }

  private AddressInput address() {
    AddressInput addressInput = new AddressInput();
    addressInput.setStreet("Street 1");
    addressInput.setCity("City 1");
    addressInput.setPostcode("12345");
    addressInput.setCountry("Country 1");
    return addressInput;
  }

  private AddressInput createAddressForUpdate(Address existingAddress) {
    AddressInput addressInput = new AddressInput();
    addressInput.setStreet("New street");
    addressInput.setCity("New city");
    addressInput.setPostcode(existingAddress.getPostcode());
    addressInput.setCountry(existingAddress.getCountry());
    return addressInput;
  }

  private List<OpeningHoursInput> openingHours() {
    return Stream.of(
        openingHour(OpeningHoursInput.DayOfWeekEnum.MONDAY, null, null, true),
        openingHour(OpeningHoursInput.DayOfWeekEnum.TUESDAY, "11:30", "15:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.TUESDAY, "18:00", "00:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.WEDNESDAY, "11:30", "15:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.WEDNESDAY, "18:00", "00:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.THURSDAY, "11:30", "15:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.THURSDAY, "18:00", "00:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.FRIDAY, "11:30", "15:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.FRIDAY, "18:00", "00:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.SATURDAY, "18:30", "00:00", false),
        openingHour(OpeningHoursInput.DayOfWeekEnum.SUNDAY, "11:30", "15:00", false)
    ).toList();
  }

  private OpeningHoursInput openingHour(OpeningHoursInput.DayOfWeekEnum dayOfWeek,
      String openingTime, String closingTime, boolean closed) {
    OpeningHoursInput openingHoursInput = new OpeningHoursInput();
    openingHoursInput.setClosed(closed);

    if (!closed) {
      openingHoursInput.setOpeningTime(openingTime);
      openingHoursInput.setClosingTime(closingTime);
    }

    openingHoursInput.setDayOfWeek(dayOfWeek);
    return openingHoursInput;
  }

  private OpeningHoursInput createOpeningHoursForUpdate(OpeningHours existingOpeningHours) {
    OpeningHoursInput openingHoursInput = new OpeningHoursInput();
    openingHoursInput.setDayOfWeek(DAY_OF_WEEK_MAP.get(existingOpeningHours.getDayOfWeek()));
    openingHoursInput.setOpeningTime("11:00");
    openingHoursInput.setClosingTime("18:00");
    openingHoursInput.setClosed(existingOpeningHours.getClosed());
    return openingHoursInput;
  }

  private static GroupedOpeningHours groupedOpeningHours(String daysOfWeek, String... openingHours) {
    GroupedOpeningHours groupedOpeningHours = new GroupedOpeningHours();
    groupedOpeningHours.setDaysOfWeek(daysOfWeek);
    groupedOpeningHours.setOpeningHours(List.of(openingHours));
    return groupedOpeningHours;
  }

  private String placesUrl() {
    return UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path(PLACES).toUriString();
  }

  private String placesUrl(UUID placeId) {
    return UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path(PLACES)
        .path("/{placeId}")
        .buildAndExpand(placeId).toUriString();
  }

  private String addressesUrl(UUID placeId, UUID addressId) {
    return UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path(PLACES)
        .path("/{placeId}")
        .path("/addresses")
        .path("/{addressId}")
        .buildAndExpand(placeId, addressId).toUriString();
  }

  private String openingHoursUrl(UUID placeId) {
    return UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path(PLACES)
        .path("/{placeId}")
        .path("/opening-hours")
        .buildAndExpand(placeId).toUriString();
  }

  private String openingHoursUrl(UUID placeId, UUID openingHoursId) {
    return UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path(PLACES)
        .path("/{placeId}")
        .path("/opening-hours")
        .path("/{openingHoursId}")
        .buildAndExpand(placeId, openingHoursId).toUriString();
  }

}