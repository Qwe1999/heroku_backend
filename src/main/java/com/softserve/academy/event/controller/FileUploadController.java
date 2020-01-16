package com.softserve.academy.event.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Api(value = "Uploading photo")
@RestController
@PropertySource("classpath:application.properties")
public class FileUploadController {

    @Value("${image.upload.dir}")
    private String imageUploadDir;

    private static final long MAX_UPLOAD_SIZE = 2L * 1024 * 1024;  // 2 MB

    @ApiOperation(value = "Upload a picture to the server")
    @PostMapping(value = "fileupload", headers = ("content-type=multipart/*"))
    public ResponseEntity upload(@RequestParam("file") List<MultipartFile> inputFiles) throws IOException {
        for (MultipartFile inputFile : inputFiles) {
            if (!isValidPhoto(inputFile)) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        for (MultipartFile inputFile : inputFiles) {
            File destinationFile = new File(imageUploadDir + File.separator + inputFile.getOriginalFilename());
            inputFile.transferTo(destinationFile);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private boolean isValidPhoto(MultipartFile file) {
        return file.getSize() < MAX_UPLOAD_SIZE && !file.isEmpty();
    }
}


