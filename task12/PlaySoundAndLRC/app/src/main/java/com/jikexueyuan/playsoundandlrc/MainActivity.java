package com.jikexueyuan.playsoundandlrc;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // 播放按钮 显示歌词
    private Button btnStartPlay, btnStopPlay;
    private TextView tvLRC;

    // 存储歌词信息
    private TreeMap<Integer, String> lrcMap;
    private TreeMap<Integer, Integer> lrcKey;
    // 播放器
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时需停掉mediaPlayer
        isPlaying = false;
    }

    /**
     * 初始化
     */
    private void initView() {
        btnStartPlay = (Button) findViewById(R.id.btnStartPlay);
        btnStopPlay = (Button) findViewById(R.id.btnStopPlay);

        tvLRC = (TextView) findViewById(R.id.tvLRC);

        btnStartPlay.setOnClickListener(this);
        btnStopPlay.setOnClickListener(this);

        initLRC();
        showLRC();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartPlay:
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.music);
                    mediaPlayer.start();
                    isPlaying = true;
                }
                break;
            case R.id.btnStopPlay:
                isPlaying = false;
                break;
        }
    }

    /**
     * 初始化歌词 存入TreeMap
     */
    private void initLRC() {
        lrcMap = new TreeMap<>();
        lrcKey = new TreeMap<>();
        InputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            fis = getResources().openRawResource(R.raw.music_lrc);
            isr = new InputStreamReader(fis, "UTF-8");
            reader = new BufferedReader(isr);

            String str;
            while ((str = reader.readLine()) != null) {
                str = str.replace("[", "");
                String[] lrcData = str.split("]");
                // 歌词内容部分
                String lrcContent = "";
                // 歌词中含有 ] 字符
                if (lrcData.length > 2) {
                    for (int i = 1; i < lrcData.length; i++) {
                        lrcContent += lrcData[i];
                    }
                } else if (lrcData.length == 2) {
                    lrcContent = lrcData[1];
                } else if (lrcData.length == 1) {
                    lrcContent = "";
                }

                // 歌词文件的时间头
                String tmpStr = lrcData[0].replace(":", ".");
                tmpStr = tmpStr.replace(".", "@");
                String[] lrcTime = tmpStr.split("@");
                if (lrcTime.length == 3) {
                    // 分 \ 秒 \ 毫秒
                    int m = Integer.parseInt(lrcTime[0]);
                    int s = Integer.parseInt(lrcTime[1]);
                    int ms = Integer.parseInt(lrcTime[2]) * 10;
                    int currTime = (m * 60 + s) * 1000 + ms;
//                    System.out.println(currTime + " " + lrcContent);
                    lrcMap.put(currTime, lrcContent);
                }
            }

            reader.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally");
            // 如果发生异常 在finally中也要执行流关闭
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 遍历lrcMap 把key值放入一个新的TreeMap lrcKey
        Iterator it = lrcMap.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            int key = (int) it.next();
            lrcKey.put(count, key);
            count++;
//            System.out.println("Key:" + key + " Str:" + lrcMap.get(key));
        }
    }

    /**
     * 显示歌词  使用线程
     */
    private void showLRC() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                        String tmpStr = null;
                        if (isPlaying) {
                            int count = 0;
                            for (int i = 0; i < lrcKey.size(); i++) {
                                if (lrcKey.get(i) < mediaPlayer.getCurrentPosition()) {
                                    count = i;
                                }
                            }

                            tmpStr = lrcMap.get(lrcKey.get(count));
                            if (tmpStr != null) {
                                final String finalTmpStr = tmpStr;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvLRC.setText(finalTmpStr);
                                    }
                                });
                            }
                        } else {
                            // 在这里停止播放 不然会导致上面的mediaPlayer.getCurrentPosition()异常
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
