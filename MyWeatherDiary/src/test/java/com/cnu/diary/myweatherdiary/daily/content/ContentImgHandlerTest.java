package com.cnu.diary.myweatherdiary.daily.content;

import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.*;

@Slf4j
class ContentImgHandlerTest {

    ContentImgHandler contentImgHandler = new ContentImgHandler();

    @Test
    void testImgSave() throws IOException {
        //Given
        BufferedReader reader = new BufferedReader(new FileReader("D:/Project/cnu_teamPjt/MyWeatherDiary/MyWeatherDiary/src/main/resources/img/testImg.txt"));
        String str;
        String base64ImgString = "";
        while ((str = reader.readLine()) != null ){
            base64ImgString += str;
        }
        base64ImgString = base64ImgString.replace("data:image/jpeg;base64,", "");
        System.out.println(base64ImgString);
        reader.close();

        byte[] imgBytes = DatatypeConverter.parseBase64Binary(base64ImgString);
        FileOutputStream fos = new FileOutputStream(contentImgHandler.getClasspathDir() + "test2.jpeg");

        fos.write(imgBytes);
        fos.close();
    }

    @Test
    void testBase64ToImg(){
        log.info("classpath dir: {}", contentImgHandler.getClasspathDir());
    }

}