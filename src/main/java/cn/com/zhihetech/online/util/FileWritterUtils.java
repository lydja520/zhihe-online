package cn.com.zhihetech.online.util;

import cn.com.zhihetech.online.exception.SystemException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ydc on 2016/7/3.
 */
public class FileWritterUtils {

    public static void writerFileToLocalhost(String filePath, String fileName, String fileContent) {

        File file = new File(filePath + fileName);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {

            if (!file.exists()) {
                file.createNewFile();
            }

            fileWriter = new FileWriter(file.getAbsoluteFile());
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(fileContent);

        } catch (Exception e) {
            throw new SystemException("文件写入错误", e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
