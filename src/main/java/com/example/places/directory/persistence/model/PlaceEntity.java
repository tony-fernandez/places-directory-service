package com.example.places.directory.persistence.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "place", indexes = {
    @Index(columnList = "name", name = "name_unique_index", unique = true)})
public class PlaceEntity extends BaseEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OpeningHoursEntity> openingHours = new HashSet<>();

}
