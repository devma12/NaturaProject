package com.natura.web.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Image {

    private Long id;

    private String name;

    private String type;

    private byte[] data;

    public Image() {
        this.id = Long.valueOf(-1);
    }

    public Image(String name, String type, byte[] bytes) {
        this();
        this.name = name;
        this.type = type;
        this.data = bytes;
    }
}
