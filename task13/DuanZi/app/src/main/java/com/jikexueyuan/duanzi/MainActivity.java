package com.jikexueyuan.duanzi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshListView.OnPullRefreshListener {

    private static final String TAG = "MainActivity";

    // 请求数据的wordpress网站地址 cat_name=后需接分类名 此程序使用 "段子"
    private static final String MAIN_URL = "http://xuexifanfan.applinzi.com";
    // ?cat_name=文章分类名&page=页数&offset=偏移
    private static final String REQUEST_URL = "http://xuexifanfan.applinzi.com/latestpost.php?cat_name=";
    private static final String CAT_NAME = "段子";
    // 每页要加载的文章数 最好是稍大一点，保证满屏
    // 不然到底会自动加载，如果从服务器又刚好没有得到新的数据，就会处于一直加载状态，这样程序逻辑不好处理
    private static final int PAGE = 10;

    // 分类名不存在时网站返回 null 字符
    private static final String RETURN_STR = "null";
    // 异步任务类型
    private static final int TASK_TYPE_REFRESH = 0; // 刷新
    private static final int TASK_TYPE_LOAD = 1; // 加载

    // 笑话列表
    private static List<Joke> jokeList = new ArrayList<>();

    private TextView toolbar_title;
    private Button toolbar_btn;
    private PullToRefreshListView lvJoke;
    private JokeListAdapter adapter;

    // 当前页数
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        // 设置Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // toolbar的标题
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        // toolbar的自定义按钮 点击时用于打开WordPress网页
        toolbar_btn = (Button) findViewById(R.id.toolbar_btn);
        toolbar_btn.setOnClickListener(this);

        // 显示笑话的ListView
        lvJoke = (PullToRefreshListView) findViewById(R.id.joke_list);
        lvJoke.setOnItemClickListener(this);
        lvJoke.setOnPullRefreshListener(this);

        // 设置新的adapter更新列表
        adapter = new JokeListAdapter(jokeList, MainActivity.this);
        lvJoke.setAdapter(adapter);

        currentPage = 0;

        // 加载数据
        updateJokeListView();
    }

    /**
     * 请求服务器的URL格式
     *
     * @param offset
     * @return
     */
    private String initRequestUrl(int offset) {
        String url = null;
        try {
            url = (REQUEST_URL + URLEncoder.encode(CAT_NAME, "UTF-8") + "&page=" + PAGE + "&offset=" + offset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, url);
        return url;
    }

    private Toast myToast = null;

    /**
     * 自定义Toast 避免持续提示
     *
     * @param context
     * @param text
     */
    private void customToast(Context context, CharSequence text) {
        if (myToast == null) {
            myToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            myToast.setText(text);
        }
        myToast.show();
    }

    /**
     * 按钮点击监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_btn:
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse(MAIN_URL));
                startActivity(i);
                break;
        }
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    private boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                Log.d(TAG, "网络已连接");
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    Log.d(TAG, "网络可用");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 更新ListView
     */
    private void updateJokeListView() {
        GetPostDataTask task = new GetPostDataTask(this, TASK_TYPE_REFRESH);
        task.execute(initRequestUrl(0));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(MainActivity.this, ReadActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("joke", jokeList.get(position - 1)); // 减去1是因为加了一个头部
        i.putExtras(b);
        startActivity(i);
    }

    /**
     * PullToRefreshListView中回调的下拉刷新方法
     */
    @Override
    public void onRefresh() {
        currentPage = 0;
        GetPostDataTask task = new GetPostDataTask(this, TASK_TYPE_REFRESH);
        task.execute(initRequestUrl(0));
    }

    /**
     * PullToRefreshListView中回调的下拉加载方法
     */
    @Override
    public void onLoadMore() {
        // 根据jokeList.size来加载下一页
        int offset = jokeList.size();
        // 如果第一页数据项小于一页大小 则不用再加载第二页
        if (offset < PAGE) {
            lvJoke.onLoadMoreComplete();
            return;
        }

        GetPostDataTask task = new GetPostDataTask(this, TASK_TYPE_LOAD);
        task.execute(initRequestUrl((currentPage * PAGE) + 1));
    }

    /**
     * AsyncTask
     */
    private class GetPostDataTask extends AsyncTask<String, Void, String> {
        private Context context;
        // 任务类型 0为刷新 1为加载
        private int taskType;

        public GetPostDataTask(Context context, int taskType) {
            this.context = context;
            this.taskType = taskType;
        }

        @Override
        protected String doInBackground(String... params) {

            if (!networkIsAvailable()) {
                Log.d(TAG, "网络不可用，从缓存中加载离线数据");
                return null;
            }

            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;

            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(2000);

                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                String line;
                StringBuilder strBuilder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    strBuilder.append(line);
                }

                br.close();
                isr.close();
                is.close();

                System.out.println(strBuilder.toString());
                return strBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ConnectException e) { //连接超时
//                e.printStackTrace();
                System.err.println(e);
            } catch (IOException e) {
//                e.printStackTrace();
                System.err.println(e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
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
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            System.out.println("onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "currentPage：" + currentPage);
            System.out.println("onPostExecute");
            super.onPostExecute(s);

            if (RETURN_STR.equals(s)) {
                System.out.println("分类名不存在");
                return;
            }

            // 没有从服务器中得到数据
            if (s == null) {
                customToast(MainActivity.this, "网络异常，正在获取缓存数据");
                if (updateJokeList(getLocalData(initStoreFileName(currentPage)), this.taskType)) {
                    currentPage++;
                }
            } else {
                // 如果从服务器中得到数据，更新列表及本地缓存
                if (updateJokeList(s, this.taskType)) {
                    saveDataToLocal(initStoreFileName(currentPage), s);
                    currentPage++;
                }
            }

            Log.e(TAG, "currentPage 2：" + currentPage);
            // 完成更新后
            switch (this.taskType) {
                // 上拉刷新
                case TASK_TYPE_REFRESH:
                    lvJoke.onRefreshComplete();
                    break;
                // 下拉加载
                case TASK_TYPE_LOAD:
                    lvJoke.onLoadMoreComplete();
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    /**
     * 把数据更新到jokeList中
     *
     * @param data
     * @return true：更新成功 false：更新失败-data为null或者不符合WordPress文章的JSON格式数据
     */
    private boolean updateJokeList(String data, int flag) {
        if (data == null) {
            if (flag == TASK_TYPE_REFRESH) {
                customToast(MainActivity.this, "网络异常，且缓存不存在");
            } else if (flag == TASK_TYPE_LOAD) {
                customToast(MainActivity.this, "已加载完缓存");
            }

            return false;
        }

        try {
            JSONArray array = new JSONArray(data);
            if (array.length() != 0) {
                // 如果flag为0 是下拉刷新，则先清空list
                if (flag == TASK_TYPE_REFRESH) {
                    if (!jokeList.isEmpty()) {
                        jokeList.clear();
                    }
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    // JSON数据 "ID" "post_title" "post_date" "post_content"
                    jokeList.add(new Joke(object.getString("ID"), object.getString("post_title"), object.getString("post_date"), object.getString("post_content")));
                }

                // 更新列表
                adapter.notifyDataSetChanged();
                lvJoke.invalidateViews();

                return true;
            } else {
                if (flag == TASK_TYPE_LOAD) {
                    customToast(MainActivity.this, "已加载到最后");
                }
                Log.e(TAG, "无新数据 data:" + data);
            }
        } catch (JSONException e) {
            // 如果解析JSON数据失败
//                e.printStackTrace();
            Log.e(TAG, "数据格式错误 data:" + data);
            customToast(MainActivity.this, "服务器返回数据异常");
            System.err.println(e);
            return false;
        }

        return false;
    }

    /**
     * 设置缓存文件名
     *
     * @return
     */
    private String initStoreFileName(int pageNumber) {
        // 缓存文件
        String data_name = "data" + pageNumber + ".json";
        Log.i(TAG, "StoreFileName:" + data_name);
        return data_name;
    }

    /**
     * 保存数据到本地
     *
     * @param fileName
     * @param data
     */
    private void saveDataToLocal(String fileName, String data) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(data);
            osw.flush();
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从本地文件中获取离线数据
     *
     * @param fileName
     * @return
     */
    private String getLocalData(String fileName) {
        FileInputStream fis = null;
        InputStreamReader isr = null;

        try {
            fis = openFileInput(fileName);
            isr = new InputStreamReader(fis, "UTF-8");

            char[] input = new char[fis.available()];
            isr.read(input);

            isr.close();
            fis.close();

            return String.valueOf(input);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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

        return null;
    }

//    /**
//     * 用来处理用户看到哪一页
//     */
//    private SharedPreferences preferences = getSharedPreferences("pagenumber", MODE_PRIVATE);;
//
//    private void savePageNumber(int pageNumber) {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt("pagenumber", pageNumber);
//        editor.commit();
//    }
//
//    private int getPageNumber() {
//        return preferences.getInt("pagenumber", -1);
//    }
}
