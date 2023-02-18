package com.natura.web.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    private Long id;

    private String name;

    private String type;

    private byte[] data;

    public Image(String name, String type, byte[] bytes) {
        this.name = name;
        this.type = type;
        this.data = bytes;
    }
}
