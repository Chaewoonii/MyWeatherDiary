package com.cnu.diary.myweatherdiary.daily.content;

import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ContentImgHandler {

    public List<byte[]> imgToBase64(List<ContentDto> contentDtos) { // encoder
        List<byte[]> contentImgToByteList = new ArrayList<>();
        Iterator<ContentDto> iter = contentDtos.iterator();

        Base64.Encoder encoder = Base64.getEncoder();
        while (iter.hasNext()) {
            ContentDto contentDto = iter.next();
            byte[] contentDtoImgByte = contentDto.getImg().getBytes();
            byte[] encoding = encoder.encode(contentDtoImgByte);

            contentImgToByteList.add(encoding);
        }

        return contentImgToByteList;
    }


    public byte[] getImgBytes(String base64ImgString){
        if (base64ImgString.startsWith("data:image/jpeg;base64")){
            base64ImgString = base64ImgString.replace("data:image/jpeg;base64,", "");
        }
        return DatatypeConverter.parseBase64Binary(base64ImgString);
    }

    public void saveBase64ImgToLocal(byte[] imgBytes, String saveDir) throws IOException {
        FileOutputStream fos = new FileOutputStream(saveDir);
        fos.write(imgBytes);
        fos.close();
    }


    public String getBase64ImgFromLocal(String imgName) throws IOException{
        Base64.Encoder encoder = Base64.getEncoder();
        File f = new File(getClasspathDir() + imgName + ".jpeg");
        byte[] bt = new byte[(int) f.length()];
        FileInputStream fis = new FileInputStream(f);
        fis.read(bt);
        return new String(encoder.encode(bt));
    }

    public String getClasspathDir(){
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        if(contextClassLoader == null){
            contextClassLoader = ClassLoader.getSystemClassLoader();
        }

        String classpath = contextClassLoader.getResource("img").toString();
        if (classpath.startsWith("file:/")){
            classpath.replace("file:/","");
        }

        return classpath;
    }

    public void saveContentsImageLocal(String base64Img, String imgName) throws IOException {
        byte[] imgBytes = getImgBytes(base64Img);
        saveBase64ImgToLocal(imgBytes, getClasspathDir() + imgName + ".jpeg");
    }
}
