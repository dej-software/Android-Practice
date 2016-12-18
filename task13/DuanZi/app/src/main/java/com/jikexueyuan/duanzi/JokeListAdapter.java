package com.jikexueyuan.duanzi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dej on 2016/10/29.
 */
public class JokeListAdapter extends BaseAdapter {

    private List<Joke> jokeList;
    private Context context;

    public JokeListAdapter(List<Joke> jokeList, Context context) {
        this.jokeList = jokeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return jokeList.size();
    }

    @Override
    public Object getItem(int position) {
        return jokeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // 加载convertView
            convertView = LayoutInflater.from(context).inflate(R.layout.joke_cell, null);
            // 创建ViewHolder并绑定控件
            holder = new ViewHolder();
            holder.jokeTitle = (TextView) convertView.findViewById(R.id.joke_title);
            holder.jokeData = (TextView) convertView.findViewById(R.id.joke_data);
            // 设置标记
            convertView.setTag(holder);
        } else {
            // 使用已存在的ViewHolder 提高ListView的效率
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置ListView要显示的内容
        Joke joke = jokeList.get(position);
        if (joke != null) {
            holder.jokeTitle.setText(joke.getTitle());
            holder.jokeData.setText("发布日期：" + joke.getDate());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView jokeTitle;
        TextView jokeData;
    }
}
