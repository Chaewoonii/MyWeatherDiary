package com.cnu.diary.myweatherdiary.aws;

import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwsS3ServiceTest {

    @Autowired
    private AwsS3Service s3Service;

    @Autowired
    private ContentImgHandler contentImgHandler;

    String imgName;

    @Test
    void testAccess(){
        s3Service.testAccess();
    }

    @Test
    @Order(1)
    void uploadTest() {
        String testImg = contentImgHandler.getBase64ImgFromLocal("testImg").get();
        imgName = UUID.randomUUID().toString();

        s3Service.uploadFile("image/jpg", imgName, testImg);
    }
    @Test
    @Order(2)
    void downloadTest(){
        String downloadPath = "C:/chaeun";
        s3Service.downloadFile(downloadPath, imgName);
    }

    @Test
    @Order(3)
    void getBase64StringTest(){
        String imgBytes = s3Service.getImgBytesFromS3(imgName);
        log.info(imgBytes);
    }

    @Test
    @Order(4)
    void deleteImgTest(){
        s3Service.deleteImg(imgName);
        log.info("Delete success: "+imgName);
    }



}