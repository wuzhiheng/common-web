package com.wonders.commonweb.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;

/**
 * 创建日期：2017-12-19下午6:39:34
 * author:wuzhiheng
 */
@SuppressWarnings("all")
public class CommonUtil {
    private static char[] codes = {'1', '2', '3', '5', '6', '7', '8', '9', '0'};

    private static DecimalFormat df = new DecimalFormat("0");

    private static List<String> NO_LOG_PARAM = Arrays.asList("passWord","repeatPwd","n","p");

    /**
     * 只要用作把Integer参数放进Map中，如果参数为空，返回null
     *
     * @param str
     * @return return
     */
    public static Integer putInteger(String str) {
        return str != null && !"".equals(str) && str.matches("^\\d{1,}$") ? Integer.valueOf(str) : null;
    }

    /**
     * 只要用作把Long参数放进Map中，如果参数为空，返回null
     *
     * @param str
     * @return return
     */
    public static Long putLong(String str) {
        return str != null && !"".equals(str) && str.matches("^\\d{1,}$") ? Long.valueOf(str) : null;
    }


    /**
     * 返回一个1开头的8位数的整形数字
     *
     * @return return
     */
    public static Integer getNewId() {
        String id = "1";
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            id += random.nextInt(9);
        }
        return Integer.valueOf(id);
    }

    /**
     * 判断一个字符串是否为空
     *
     * @param str
     * @return return
     */
    public static boolean StringIsNull(String str) {
        return str == null || "".equals(str) || "null".equals(str);
    }

    public static String generateCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(codes[random.nextInt(codes.length - 1)]);
        }
        return sb.toString();
    }

    /**
     * 在内存中创建图像
     *
     * @param verificationCodes
     * @return
     * @throws Exception
     */
    public static BufferedImage generateVerificationImage(
            char[] verificationCodes) throws Exception {
        // 设定长宽
        int width = 120, height = 36;
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        // 设定字体
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(100, 200));
        for (int i = 0; i < 205; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // String sRand = "";
        int i = 0;
        for (char c : verificationCodes) {
            // String rand = String.valueOf(random.nextInt(10));
            // sRand += c;
            // 将认证码显示到图象中
            if (i % 2 == 0) {
                g.setFont(new Font("Times New Roman", Font.ITALIC, 28));
            } else {
                g.setFont(new Font("Times New Roman", Font.ITALIC, 25));
            }
            g.setColor(new Color(20 + random.nextInt(110), 20 + random
                    .nextInt(110), 20 + random.nextInt(110)));
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(c + "", 25 * i++ + 12, 27);
        }
        // 将认证码存入SESSION
        // ActionContext.getContext().getSession().put("rand", sRand);
        // 图象生效
        g.dispose();
        return image;
    }

    /**
     * 给定范围获得随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }


    /**
     * 判断集合是否为空
     *
     * @param list
     * @return return
     */
    public static boolean listNotBlank(List<?> list) {
        return list != null && list.size() > 0;
    }


    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 将一个对象转换为另一个对象
     *
     * @param <T1>      要转换的对象
     * @param <T2>      转换后的类
     * @param orimodel  要转换的对象
     * @param castClass 转换后的类
     * @return 转换后的对象
     */
    public static <T1, T2> T2 convertBean(T1 orimodel, Class<T2> castClass) {
        Class temp = castClass;
        T2 returnModel = null;
        try {
            returnModel = castClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("创建" + castClass.getName() + "对象失败");
        }
        List<Field> fieldlist = new ArrayList<Field>(); //要转换的字段集合
        while (castClass != null && //循环获取要转换的字段,包括父类的字段
                !castClass.getName().toLowerCase().equals("java.lang.object")) {
            fieldlist.addAll(Arrays.asList(castClass.getDeclaredFields()));
            castClass = (Class<T2>) castClass.getSuperclass(); //得到父类,然后赋给自己
        }
        for (Field field : fieldlist) {
            PropertyDescriptor getpd = null;
            PropertyDescriptor setpd = null;
            try {
                getpd = new PropertyDescriptor(field.getName(), orimodel.getClass());
                setpd = new PropertyDescriptor(field.getName(), returnModel.getClass());
            } catch (Exception e) {
                continue;
            }
            try {
                Method getMethod = getpd.getReadMethod();
                Object transValue = getMethod.invoke(orimodel);
                Method setMethod = setpd.getWriteMethod();
                setMethod.invoke(returnModel, transValue);
            } catch (Exception e) {
                //如果是integer转int类型的，报错跳过
                if (!(setpd.getPropertyType().equals(int.class) && getpd.getPropertyType().equals(Integer.class))) {
                    throw new RuntimeException("cast " + temp.getClass().getName() + " to "
                            + castClass.getName() + " failed");
                }
            }
        }
        return returnModel;
    }

    public static List<Integer> getIntList(String str) {
        List<Integer> list = new ArrayList<>();
        if (!StringIsNull(str)) {
            for (String s : str.split(",")) {
                list.add(new Integer(s));
            }
        }
        return list;
    }

    public static String getParamDesc(HttpServletRequest request) {
        Map<String, Object> result = new TreeMap<>();
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) (params.nextElement());
            if (CommonUtil.StringIsNull(request.getParameter(paramName)) || NO_LOG_PARAM.contains(paramName)) {
                continue;
            }
            result.put(paramName, URLDecoder.decode(request.getParameter(paramName)));

        }
        try {
            return new ObjectMapper().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String encryptPassword(String str) {
        return MD5Utils.encrypt(IConstant.SALT + str);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encryptPassword(12345678 + ""));
        System.out.println(URLEncoder.encode("12345678+", "utf-8"));
    }

}
