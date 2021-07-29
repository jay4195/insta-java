package com.jay.instagram.service;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {
    String uploadFile(MultipartFile multipartFile, HttpServletRequest request);
    byte[] getUploadPicture(String fileName);
    String getPictureUrl(String fileName);
    String getPictureFileName(String pictureUrl);
}
