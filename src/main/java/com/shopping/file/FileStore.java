package com.shopping.file;

import com.shopping.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
        // filename 받아서 fullPath 리턴
    }

//    public List<UploadFile> storeFiles(List<UploadFile> multipartFiles) throws IOException {
//        List<UploadFile> storeFileResult = new ArrayList<>();
//
//        for (MultipartFile multipartFile : multipartFiles) {
//            if (!multipartFile.isEmpty()) {
//                storeFileResult.add(storeFile(multipartFile));
//            }
//        }
//        return storeFileResult;
//    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        // 업로드한 파일로 변환
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        // 서버에 저장하는 파일명은 유효 id + 파일의 확장자
        String storeFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFileName, storeFileName);
    }

    private String createStoreFileName(String originalFileName) {
        //서버 내부에서 관리하는 파일명
        String uuid = UUID.randomUUID().toString(); // 유효 ID
        String ext = extractExt(originalFileName); // 확장자

        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        // 확장자 뽑아냄
        int position = originalFileName.lastIndexOf(".");
        return originalFileName.substring(position + 1);
    }
}
