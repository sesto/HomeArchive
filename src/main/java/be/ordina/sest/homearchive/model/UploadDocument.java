package be.ordina.sest.homearchive.model;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * Entity for document upload
 *
 */
@Data
public class UploadDocument {

    private MultipartFile file;
    private String description;

}
