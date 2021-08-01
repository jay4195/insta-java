package com.jay.instagram.controller;

import com.alibaba.fastjson.JSONObject;
import com.jay.instagram.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping()
@CrossOrigin
public class FileController {
    @Autowired
    FileService fileService;

    @RequestMapping(value = "/upload",method= RequestMethod.POST)
    @ResponseBody
    public JSONObject upload(@RequestParam("file") MultipartFile multipartFile,
                             HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJson = new JSONObject();
        String fileName = fileService.uploadFile(multipartFile, request);
        if (fileName != null && fileName.equals("")) {
            responseJson.put("message", "File upload Failed!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            responseJson.put("secure_url", fileService.getPictureUrl(fileName));
        }
        return responseJson;
    }

    @RequestMapping(value="/upload/{fileName}",method= RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getUploadPicture(@PathVariable(value = "fileName") String fileName) {
        return fileService.getUploadPicture(fileName);
    }
}
