package com.blog.service.impl;

import com.blog.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException{
        String name  = file.getOriginalFilename();;

        String randomId = UUID.randomUUID().toString();
        String fileRandomName = randomId.concat(name.substring(name.lastIndexOf(".")));

        String filePath = path + File.separator+fileRandomName;

        //create dir if not has
        File f = new File(path);
        if(!f.exists())
            f.mkdir();

        //file copy
        //Copies file from ınput to filepath
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileRandomName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException{
        String fullPath = path + File.separator + fileName;
        InputStream is = new FileInputStream(fullPath);
        return is;

    }
}
