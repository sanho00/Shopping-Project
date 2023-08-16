package com.shopping.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imgId;

    private String uploadFileName; // 고객이 업로드한 파일명
    private String storeFileName; // 서버 내부에서 관리하는 파일명

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
