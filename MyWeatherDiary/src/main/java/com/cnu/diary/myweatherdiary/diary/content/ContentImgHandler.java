package com.cnu.diary.myweatherdiary.diary.content;

import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.*;

@Slf4j
public class ContentImgHandler {

    public String getImgPath(String imgName){
        return getClasspathDir() + "/" + imgName + ".jpg";
    }

    public List<byte[]> imgToBase64(List<ContentDto> contentDtos) { // encoder
        List<byte[]> contentImgToByteList = new ArrayList<>();
        Iterator<ContentDto> iter = contentDtos.iterator();

        Base64.Encoder encoder = Base64.getEncoder();
        while (iter.hasNext()) {
            ContentDto contentDto = iter.next();
            byte[] contentDtoImgByte = contentDto.getImg().orElseThrow().getBytes();
            byte[] encoding = encoder.encode(contentDtoImgByte);

            contentImgToByteList.add(encoding);
        }

        return contentImgToByteList;
    }

/*    public static byte[] getImgBytesFromString(String base64ImgString){
        if (base64ImgString.startsWith("data:image/png;base64")){
            //base64ImgString = base64ImgString.replace("data:image/png;base64,", "");
        }
        return base64ImgString.getBytes(); //DatatypeConverter.parseBase64Binary(base64ImgString);
    }*/

    public static byte[] getImgBytesFromString(String base64ImgString){
        if (base64ImgString.startsWith("data:image/png;base64")){
            base64ImgString = base64ImgString.replace("data:image/png;base64,", "");
        }
        return DatatypeConverter.parseBase64Binary(base64ImgString);
    }

    public String saveBase64ImgToLocal(byte[] imgBytes, String saveDir) throws IOException {
        FileOutputStream fos = new FileOutputStream(saveDir);
        fos.write(imgBytes);
        fos.close();
        return saveDir;
    }

    public Optional<String> getBase64ImgFromLocal(String imgName){
        try{
            Base64.Encoder encoder = Base64.getEncoder();
            File f = new File(getImgPath(imgName));
            byte[] bt = new byte[(int) f.length()];
            FileInputStream fis = new FileInputStream(f);
            fis.read(bt);
            return Optional.of(new String(encoder.encode(bt)));
        }catch (Exception e){
            new ImgNotFoundException("No such image");
        }
        return Optional.empty();
    }

    public String getClasspathDir(){
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        if(contextClassLoader == null){
            contextClassLoader = ClassLoader.getSystemClassLoader();
        }

        String classpath = contextClassLoader.getResource("img").getPath();
        if (classpath.startsWith("file")){
            classpath.replace("file:/","");
        }

        return classpath;
    }

    public String saveContentsImageLocal(String base64Img, String imgName) throws IOException {
        byte[] imgBytes = getImgBytesFromString(base64Img);
        return saveBase64ImgToLocal(imgBytes, getImgPath(imgName));
    }

    public String deleteImg(String imgName) throws ImgNotFoundException{
        try{
            File img = new File(getImgPath(imgName));
            if (img.exists()){
                img.delete();
                return img.getName();
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        throw new ImgNotFoundException("Delete Image Failed:: No such Image");
    }
}
