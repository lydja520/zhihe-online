/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.zhihetech.online.util.ueditor.upload;

import cn.com.zhihetech.online.commons.AppConfig;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author pangongxia
 */
public class QiNiuUploadManager {

    private static final UploadManager UPLOADMANAGER = new UploadManager();
    private static final Auth AUTH = Auth.create(AppConfig.QiNiuConfig.AK,AppConfig.QiNiuConfig.SK);
    private static String token = AUTH.uploadToken(AppConfig.QiNiuConfig.BUCKET);
    private static Boolean bool = true;

    public static UploadManager getUploadManager() {
        return UPLOADMANAGER;
    }

    public static String getUpToken() {
        if (bool) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    token = AUTH.uploadToken(AppConfig.QiNiuConfig.BUCKET);
                }
            }, 3000, 3000, TimeUnit.SECONDS);
            bool = false;
        }
        return token;
    }

}
