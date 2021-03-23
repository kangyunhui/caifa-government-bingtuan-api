package com.junyi.baseapi.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author lijy
 * @create 2019-09-18 16:51
 * @description 验证码实体类
 */
@Data
@AllArgsConstructor
public class ImageCode {

    private BufferedImage image;
    private String code;
    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.code = code;
        this.image = image;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(getExpireTime());
    }
}
