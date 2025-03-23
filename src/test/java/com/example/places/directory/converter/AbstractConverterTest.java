package com.example.places.directory.converter;

import java.time.LocalTime;
import java.util.UUID;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class AbstractConverterTest {

  protected static final UUID ID = UUID.randomUUID();
  protected static final LocalTime OPENING_TIME = LocalTime.of(10, 0);
  protected static final LocalTime CLOSING_TIME = LocalTime.of(17, 0);
  protected static final String OPENING_TIME_S = "10:00";
  protected static final String CLOSING_TIME_S = "17:00";
  protected static final String CITY = "Zurich";
  protected static final String STREET = "Bahnhofstrasse 120";
  protected static final String POSTCODE = "8000";
  protected static final String COUNTRY = "Switzerland";
  protected static final String NAME = "name";
  protected static final String DESCRIPTION = "description";
}
