package com.jay.instagram.service.impl;

import com.jay.instagram.config.ServerConfig;
import com.jay.instagram.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private static final int fileNameLength = 25;
    private static final String defaultAvatarName = "default_avatar.jpg";
    @Autowired
    ServerConfig serverConfig;

    @Override
    public String uploadFile(MultipartFile multipartFile, HttpServletRequest request) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            //服务器根目录的路径
            String path = serverConfig.getServerDirPath();
            //获取文件名称
            String filename = getUploadFileName(multipartFile);
            //将文件上传的服务器根目录下的\\upload\\{fileName.hashcode()}文件夹
            String uploadPath = path + "upload" + File.separator + hashFolder(filename);
            File file = new File(uploadPath, filename);
            FileUtils.copyInputStreamToFile(inputStream, file);
//            log.info(uploadPath);
            inputStream.close();
            return filename;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    @Override
    public byte[] getUploadPicture(String fileName) {
        String path;
        if (fileName.equals(defaultAvatarName)) {
            path = serverConfig.getServerDirPath() + "upload" + File.separator + fileName;
        } else {
            path = serverConfig.getServerDirPath() + "upload" + File.separator + hashFolder(fileName) + File.separator + fileName;
        }
        File file = new File(path);
        byte[] bytes;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            inputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            bytes = new byte[0];
        }
        return bytes;
    }

    @Override
    public String getPictureUrl(String fileName) {
        if (fileName.equals("")) {
            return serverConfig.getImgSrcPath() + "default_avatar.jpg";
        } else {
            return serverConfig.getImgSrcPath() + fileName;
        }
    }

    @Override
    public String getPictureFileName(String pictureUrl) {
        String fileName = pictureUrl.substring(pictureUrl.lastIndexOf("/") + 1);
        return fileName;
    }

    @Override
    public boolean deletePicture(String fileName) {
        String filePath = serverConfig.getServerDirPath() + "upload" + File.separator + hashFolder(fileName) + File.separator + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                log.info("File {} deleted!", filePath);
                return true;
            }
            log.error("File {} not deleted!", filePath);
            return false;
        }
        log.error("No such file {}!", filePath);
        return false;
    }

    /**
     * @info 获取上传文件的名称,新文件名为原文件名加上时间戳
     * @param multipartFile
     * @return 文件名
     */
    private String getUploadFileName(MultipartFile multipartFile) {
        String uploadFileName = multipartFile.getOriginalFilename();
        String fileName = uploadFileName.substring(0,
                uploadFileName.lastIndexOf("."));
        // 总长度小于50
        if (fileName.length() > fileNameLength) {
            fileName = fileName.substring(0, fileNameLength);
        }
        String type = uploadFileName.substring(uploadFileName.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        String name = fileName + "_" + timeStr + type;
        return name;
    }

    /**
     *
     * @param filename
     * @return 返回fileName的哈希子文件夹
     */
    private String hashFolder(String filename){
        //得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashCode = filename.hashCode();
        int dir1 = hashCode & 0xf;  //0--15
        return String.valueOf(dir1);
    }
}
