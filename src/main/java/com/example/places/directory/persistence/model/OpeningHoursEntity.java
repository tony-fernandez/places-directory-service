package com.example.places.directory.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "opening_hours", indexes = {
    @Index(columnList = "day_of_week,opening_time,closing_time,is_closed,place_id", name = "opening_hours_unique_index", unique = true)})
public class OpeningHoursEntity extends BaseEntity{

  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week", nullable = false)
  private DayOfWeek dayOfWeek;

  @Column(name = "opening_time")
  private LocalTime openingTime;

  @Column(name = "closing_time")
  private LocalTime closingTime;

  @Column(name = "is_closed", nullable = false, columnDefinition = "boolean default false")
  private boolean closed;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id", nullable = false)
  private PlaceEntity place;

}
