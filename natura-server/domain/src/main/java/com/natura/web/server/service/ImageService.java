package com.natura.web.server.service;

import com.natura.web.server.exception.InvalidDataException.InvalidFileException;
import com.natura.web.server.model.Image;
import com.natura.web.server.provider.ImageProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageService {

    private final ImageProvider imageProvider;

    public ImageService(final ImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }

    public Image upload(String filename, String contentType, InputStream inputStream) throws InvalidFileException {
        try {
            System.out.println("Original Image Byte Size - " + inputStream.available());
            Image img = new Image(filename, contentType,
                    compressBytes(inputStream.readAllBytes()));
            Image stored = imageProvider.save(img);
            stored.setData(decompressBytes(stored.getData()));
            return stored;
        } catch (IOException e) {
            throw new InvalidFileException(e.getMessage(), e);
        }
    }

    public Image download(Long id) {
        Image retrievedImage = imageProvider.getImageById(id).orElse(null);
        if (retrievedImage != null) {
            return new Image(retrievedImage.getName(), retrievedImage.getType(),
                    decompressBytes(retrievedImage.getData()));
        } else {
            return null;
        }

    }

    /**
     * compress the image bytes before storing it in the database
     *
     * @param data
     */
    private static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    /**
     * uncompress the image bytes before returning it to the angular application
     *
     * @param data
     */
    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
