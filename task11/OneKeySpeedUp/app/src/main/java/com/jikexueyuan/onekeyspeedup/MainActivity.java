package com.jikexueyuan.onekeyspeedup;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 在AndroidManifest.xml中设置android:theme="@android:style/Theme.NoDisplay"
 * 表示点击图标时不显示界面 只进行加速操作
 */
public class MainActivity extends Activity {

    private ActivityManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  获取ACTIVITY_SERVICE
        manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        long clearMemSize = clearMemory();
        // 格式化输出信息
        String memSizeFmt = new DecimalFormat(".00000000").format((double) clearMemSize / 1048576);
        System.out.println(clearMemSize);
        System.out.println(memSizeFmt);
        Toast.makeText(this, "清理了" + memSizeFmt + "M内存", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 清理内存
     * 返回清理掉的内存大小
     *
     * @return
     */
    private long clearMemory() {
        // 使用ActivityManager获取运行的程序
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = manager.getRunningAppProcesses();

        // 清理之前的可用内存
        long beforeMem = getAvailMem();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
            //importance表示该进程的重要程度，数值越低就越重要，一般大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程为后台进程
            if (appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                String[] appPkgList = appProcessInfo.pkgList;
                for (int i = 0; i < appPkgList.length; i++) {
                    manager.killBackgroundProcesses(appPkgList[i]);
                }
            }
        }

        return getAvailMem() - beforeMem;
    }

    /**
     * 获取系统可用内存 单位kb
     *
     * @return
     */
    private long getAvailMem() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }
}
