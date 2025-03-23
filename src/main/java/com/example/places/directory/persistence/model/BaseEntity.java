package com.example.places.directory.persistence.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

  @Id
  @UuidGenerator(style = Style.AUTO)
  private UUID id;

}
