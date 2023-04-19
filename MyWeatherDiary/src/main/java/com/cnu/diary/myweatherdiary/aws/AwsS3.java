package com.cnu.diary.myweatherdiary.aws;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AwsS3 {

    private final AmazonS3 s3;

    protected String uploadFile(String folderName, String fileName){
        /*to-do*/
        return fileName;
    }


}
