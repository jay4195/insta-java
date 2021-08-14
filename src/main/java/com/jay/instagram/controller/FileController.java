package com.jay.instagram.controller;

import com.alibaba.fastjson.JSONObject;
import com.jay.instagram.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequestMapping()
@CrossOrigin
public class FileController {
    @Autowired
    FileService fileService;

    private static final Long pictureExpireTime = 3600L;

    private static final String prefix = "cache_picture::file_";

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

    /**
     * 将图片缓存进redis
     * @param fileName
     * @return
     */
    @RequestMapping(value="/upload/{fileName}",method= RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getUploadPicture(@PathVariable(value = "fileName") String fileName) {
        Jedis jedis = new Jedis();
        byte[] pictureValue = null;
        if (jedis.get(prefix + fileName) == null) {
            pictureValue = fileService.getUploadPicture(fileName);
            jedis.set((prefix + fileName).getBytes(StandardCharsets.UTF_8), pictureValue);
            jedis.expire(prefix + fileName, pictureExpireTime);
            log.info("[Picture Cached] fileName: {}", fileName);
        } else {
            pictureValue = jedis.get((prefix + fileName).getBytes(StandardCharsets.UTF_8));
        }
        return pictureValue;
    }
}
