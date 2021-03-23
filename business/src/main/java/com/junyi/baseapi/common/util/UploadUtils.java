package com.junyi.baseapi.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/** 文件上传服务 */
public class UploadUtils {

    /**
     * 保存文件
     *
     * @param file
     * @param ossPath
     * @return
     */
    public static String saveFile(MultipartFile file, String ossPath) {
        try {
            String filePath = "";
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 文件上传路径
            String uuid = CommonUtils.getUUID();
            String commonPath =
                    getTimePath() + uuid + fileName.substring(fileName.lastIndexOf('.'));
            filePath = ossPath + commonPath;
            File dest = new File(filePath.trim());
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return commonPath;
        } catch (IOException e) {
            throw new IllegalArgumentException("保存文件失败");
        }
    }

    /**
     * 时间路径
     *
     * @return
     */
    private static String getTimePath() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int date = now.get(Calendar.DAY_OF_MONTH);
        int minute = now.get(Calendar.HOUR_OF_DAY);
        String filePath = "";
        if (year != 0) {
            filePath += year + "/";
        }
        if (month != 0) {
            filePath += month + "/";
        }
        if (date != 0) {
            filePath += date + "/";
        }
        if (minute != 0) {
            filePath += minute + "/";
        }
        return filePath;
    }
}
