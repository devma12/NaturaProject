package com.natura.web.server.model;

import lombok.Data;

@Data
public abstract class ValidableItem {

    private boolean isValidated;

    public ValidableItem() {
        this.isValidated = false;
    }
}
