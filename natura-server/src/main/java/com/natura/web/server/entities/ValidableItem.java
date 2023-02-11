package com.natura.web.server.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ValidableItem {

    @Column(name="validated", nullable = false)
    private boolean isValidated;

    public ValidableItem() {
        this.isValidated = false;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }
}
