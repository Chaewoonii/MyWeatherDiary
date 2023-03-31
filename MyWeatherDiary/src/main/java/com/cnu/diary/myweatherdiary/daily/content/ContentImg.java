package com.cnu.diary.myweatherdiary.daily.content;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class ContentImg {

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

    public List<String> base64ToImg(List<byte[]> contentImgToByteList) { // decoder
        List<String> contentByteToImgList = new ArrayList<>();
        Iterator<byte[]> iter = contentImgToByteList.iterator();

        Base64.Decoder decoder = Base64.getDecoder();
        while (iter.hasNext()) {
            byte[] byteImg = iter.next();

            byte[] decoding = decoder.decode(byteImg);
            contentByteToImgList.add(new String(decoding));
        }
        return contentByteToImgList;
    }
}
