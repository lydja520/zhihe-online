package cn.com.zhihetech.online.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ydc on 2016/7/3.
 */
public class CatchImgUtils {

    // 地址
    private static final String URL = "http://www.csdn.net";
    // 编码
    private static final String ECODING = "UTF-8";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";

    /***
     * 获取HTML内容
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String getHTML(String url) throws Exception {
        URL uri = new URL(url);
        URLConnection connection = uri.openConnection();
        InputStream in = connection.getInputStream();
        byte[] buf = new byte[1024];
        int length = 0;
        StringBuffer sb = new StringBuffer();
        while ((length = in.read(buf, 0, buf.length)) > 0) {
            sb.append(new String(buf, ECODING));
        }
        in.close();
        return sb.toString();
    }

    /***
     * 获取ImageUrl地址
     *
     * @param HTML
     * @return
     */
    public static List<String> getImageUrl(String HTML) {
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
        List<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        return listImgUrl;
    }

    /***
     * 获取ImageSrc地址
     *
     * @param HTML
     * @return
     */
    public static List<String> getImageSrc(String HTML) {

        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
        List<String> listImgSrc = new ArrayList<String>();
        while (matcher.find()) {
            String imgUrl = matcher.group();
            Matcher matcher1 = Pattern.compile(IMGSRC_REG).matcher(imgUrl);
            while (matcher1.find()) {
                listImgSrc.add(matcher1.group().substring(0, matcher1.group().length() - 1));
            }
        }
        return listImgSrc;
    }


    /***
     * 获取html中第一张图片ImageSrc地址
     *
     * @param HTML
     * @return
     */
    public static String getFirstImageSrc(String HTML) {

        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
        List<String> listImgSrc = new ArrayList<String>();
        while (matcher.find()) {
            String imgUrl = matcher.group();
            Matcher matcher1 = Pattern.compile(IMGSRC_REG).matcher(imgUrl);
            while (matcher1.find()) {
                listImgSrc.add(matcher1.group().substring(0, matcher1.group().length() - 1));
            }
            if (listImgSrc.size() > 0) {
                return listImgSrc.get(0);
            }
        }
        return null;
    }

    /***
     * 下载图片
     *
     * @param listImgSrc
     */
    public static void download(List<String> listImgSrc) {
        try {
            for (String url : listImgSrc) {
                String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
                URL uri = new URL(url);
                InputStream in = uri.openStream();
                FileOutputStream fo = new FileOutputStream(new File(imageName));
                byte[] buf = new byte[1024];
                int length = 0;
                System.out.println("开始下载:" + url);
                while ((length = in.read(buf, 0, buf.length)) != -1) {
                    fo.write(buf, 0, length);
                }
                in.close();
                fo.close();
                System.out.println(imageName + "下载完成");
            }
        } catch (Exception e) {
            System.out.println("下载失败");
        }
    }

}
