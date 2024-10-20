package com.natura.web.server.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class ValidableItem {

  @Column(name = "validated", nullable = false)
  private boolean isValidated = false;
}
