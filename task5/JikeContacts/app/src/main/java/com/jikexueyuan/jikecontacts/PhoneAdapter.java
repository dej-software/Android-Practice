package com.jikexueyuan.jikecontacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dej on 2016/9/21.
 * 自定义的Adapter类 存放联系人View
 */
public class PhoneAdapter extends BaseAdapter {

    // 通信录列表集合
    private List<PhoneInfo> phoneInfoList;
    // 上下文
    private Context context;
    // 联系人View
//    private LinearLayout contactLayout;

    /**
     * 构造一个PhoneAdapter
     * @param phoneInfoList
     * @param context
     */
    public PhoneAdapter(List<PhoneInfo> phoneInfoList, Context context) {
        this.phoneInfoList = phoneInfoList;
        this.context = context;
    }

    /**
     * 返回List集合大小（联系人数量）
     * @return
     */
    @Override
    public int getCount() {
        return phoneInfoList.size();
    }

    /**
     * 返回List集合中的一个数据
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return phoneInfoList.get(position);
    }

    /**
     * 返回Item ID，与position一致
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获得联系人View
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = LayoutInflater.from(context); // 用来加载Layout
//        contactLayout = (LinearLayout) inflater.inflate(R.layout.contact_view, null);
//        // 绑定相应的控件
//        TextView tvPhoneName = (TextView) contactLayout.findViewById(R.id.tvPhoneName);
//        TextView tvPhoneNumber = (TextView) contactLayout.findViewById(R.id.tvPhoneNumber);
//        // 设置显示的联系人信息
//        tvPhoneName.setText(phoneInfoList.get(position).getName());
//        tvPhoneNumber.setText(phoneInfoList.get(position).getNumber());
//        return contactLayout;

        // 优化的getView方法 避免ListView加载数据或滑动时卡顿
        ContactViewHolder holder;
        if (convertView == null) {
            // 加载convertView
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_view, null);
            holder = new ContactViewHolder();
            // 绑定相应的控件
            holder.tvPhoneName = (TextView) convertView.findViewById(R.id.tvPhoneName);
            holder.tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
            // 设置标记
            convertView.setTag(holder);
        } else {
            // 已经加载了convertView
            holder = (ContactViewHolder) convertView.getTag();
        }

        // 设置显示的联系人信息
        PhoneInfo phoneInfo = phoneInfoList.get(position);
        if (phoneInfo != null) {
            holder.tvPhoneName.setText(phoneInfo.getName());
            holder.tvPhoneNumber.setText(phoneInfo.getNumber());
        }
        return convertView;
    }

    /**
     * 用于getView方法中的类 优化getView方法
     */
    private static class ContactViewHolder {
        TextView tvPhoneName;
        TextView tvPhoneNumber;
    }
}
