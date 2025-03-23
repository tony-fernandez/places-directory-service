package com.example.places.directory.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "address")
public class AddressEntity extends BaseEntity {

  @Column(name = "street")
  private String street;

  @Column(name = "city")
  private String city;

  @Column(name = "postcode")
  private String postcode;

  @Column(name = "country")
  private String country;

  @OneToOne(mappedBy = "address")
  private PlaceEntity place;

}
