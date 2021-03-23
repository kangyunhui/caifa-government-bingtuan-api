package com.junyi.baseapi.common.util;

import com.junyi.baseapi.common.model.ImageCode;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author lijy
 * @create 2019-09-17 15:31
 * @description 图形验证码工具类
 */
public final class CaptchaUtils {

    private static final int WIDTH = 90;
    private static final int HEIGHT = 20;
    private static final int RANDOM_SIZE = 4;
    /** 验证码过期时间（秒） */
    private static final int EXPIRE_SECOND = 60;

    public static ImageCode generate(ServletWebRequest request) {
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", WIDTH);
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", HEIGHT);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String sRand = "";
        int length =
                ServletRequestUtils.getIntParameter(request.getRequest(), "length", RANDOM_SIZE);
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(
                    new Color(
                            20 + random.nextInt(110),
                            20 + random.nextInt(110),
                            20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();
        return new ImageCode(image, sRand, EXPIRE_SECOND);
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
