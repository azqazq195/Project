package com.project.foret.util;

import com.project.foret.entity.ForetPhoto;
import com.project.foret.entity.MemberPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;

public class Util {
    @Autowired
    ServletContext servletContext;

    public void uploadFile(MultipartFile file, String folderName) {
    }
}
