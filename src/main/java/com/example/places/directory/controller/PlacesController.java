package com.example.places.directory.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.example.places.directory.api.PlacesApi;
import com.example.places.directory.model.Address;
import com.example.places.directory.model.AddressInput;
import com.example.places.directory.model.OpeningHours;
import com.example.places.directory.model.OpeningHoursInput;
import com.example.places.directory.model.Place;
import com.example.places.directory.model.PlaceInput;
import com.example.places.directory.service.AddressService;
import com.example.places.directory.service.OpeningHoursService;
import com.example.places.directory.service.PlaceService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlacesController implements PlacesApi {

  private final AddressService addressService;
  private final OpeningHoursService openingHoursService;
  private final PlaceService placeService;

  @Override
  public ResponseEntity<Place> createPlace(PlaceInput placeInput) {
    return status(201).body(placeService.createPlace(placeInput));
  }

  @Override
  public ResponseEntity<Void> deletePlace(UUID id) {
    placeService.deletePlace(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<Place>> getPlaces() {
    return ok(placeService.getPlaces());
  }

  @Override
  public ResponseEntity<Place> getPlaceById(UUID placeId) {
    return ok(placeService.getPlace(placeId));
  }

  @Override
  public ResponseEntity<Address> updateAddress(UUID placeId, UUID addressId, AddressInput address) {
    return ok(addressService.updateAddress(placeId, addressId, address));
  }

  @Override
  public ResponseEntity<OpeningHours> addOpeningHours(UUID placeId,
      OpeningHoursInput openingHoursInput) {
    return status(201).body(openingHoursService.addOpeningHours(placeId, openingHoursInput));
  }

  @Override
  public ResponseEntity<Void> deleteOpeningHours(UUID placeId, UUID openingHoursId) {
    openingHoursService.deleteOpeningHours(placeId, openingHoursId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<OpeningHours> updateOpeningHours(UUID placeId, UUID openingHoursId,
      OpeningHoursInput openingHours) {
    return ok(openingHoursService.updateOpeningHours(placeId, openingHoursId, openingHours));
  }

}

