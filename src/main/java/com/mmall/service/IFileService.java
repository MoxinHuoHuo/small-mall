package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @auther lyd
 * @createDate 2018/11/7 20:42
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
