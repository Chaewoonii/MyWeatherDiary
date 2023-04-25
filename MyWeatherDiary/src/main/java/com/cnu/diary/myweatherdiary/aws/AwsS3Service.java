package com.cnu.diary.myweatherdiary.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler;
import com.cnu.diary.myweatherdiary.exception.ImgNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.List;

import static com.cnu.diary.myweatherdiary.diary.content.ContentImgHandler.getImgBytesFromString;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Service {

    @Autowired
    private final AmazonS3 s3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void testAccess(){
        List<Bucket> buckets = s3.listBuckets();
        log.info("buckets -> {}", buckets);
    }

    /**
     * 이미지 태그 지우고 AWS에 디코딩 해서 저장
     * 불러올때는 MIME 알아서 처리
     * 디코딩된 바이너리 데이터를 MIME 라이브러리에 넣으면
     * 해당 바이너리가 무슨 타입인지(ex, image/png, image/jpeg)를 반환해줌
     * data:image/png;base64,  <= 이거를 직접 만들어서 바이너리를 base64인코딩 한 결과 앞에 붙여서
     * 프론트에 리턴해주면 ! 이미지가 뜹니다.
     * */

    public String uploadFile(String contentType, String imgName, String imgString){
        byte[] imgBytes = getImgBytesFromString(imgString);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imgBytes.length);
        objectMetadata.setContentType(contentType); // image/png
        s3.putObject(new PutObjectRequest(this.bucketName, imgName, new ByteArrayInputStream(imgBytes), objectMetadata));
        String url = s3.getUrl(bucketName, imgName).toString();
        log.info(url);
        return url;
    }

    public String downloadFile(String downloadFilePath, String imgName){
        S3Object object = s3.getObject(bucketName, imgName);
        S3ObjectInputStream objectContentInputStream = object.getObjectContent();
        try {
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFilePath+ "/" + imgName + ".png"));
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while((bytesRead = objectContentInputStream.read(bytesArray)) != -1) {
                outputStream.write(bytesArray, 0, bytesRead);
            }

            outputStream.close();
            objectContentInputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return downloadFilePath;
    }

    public String getImgBytesFromS3(String imgName){
        Base64.Encoder encoder = Base64.getEncoder();

        S3ObjectInputStream s3ObjectInputStream;
        try{
            s3ObjectInputStream = s3.getObject(bucketName, imgName).getObjectContent();
        } catch (Exception e){
            log.info("getOject Failed: {} / {}",bucketName, imgName);
            return "";
        }
        log.info("getOject Successed: {} / {}",bucketName, imgName);
        byte[] bytesArray = new byte[4096];
        try {
            s3ObjectInputStream.read(bytesArray);
        } catch (IOException e) {
            throw new ImgNotFoundException("No Such Image: "+imgName);
        }
        return "data:image/png;base64," + new String(encoder.encode(bytesArray));
    }

    public void deleteImg(String imgName){
        s3.deleteObject(bucketName, imgName);
    }


}
