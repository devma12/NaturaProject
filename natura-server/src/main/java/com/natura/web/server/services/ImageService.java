package com.natura.web.server.services;

import com.natura.web.server.entities.Image;
import com.natura.web.server.repository.ImageRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@AllArgsConstructor
@Service
public class ImageService {

  private ImageRepository imageRepository;

  public Image upload(MultipartFile file) throws IOException {
    log.debug("Original Image Byte Size - " + file.getBytes().length);
    Image img = new Image(file.getOriginalFilename(), file.getContentType(),
        compressBytes(file.getBytes()));
    Image stored = imageRepository.save(img);
    stored.setData(decompressBytes(stored.getData()));
    return stored;
  }

  public Image download(Long id) {
    Image retrievedImage = imageRepository.findById(id).orElse(null);
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
