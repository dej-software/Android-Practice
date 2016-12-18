package com.jikexueyuan.baiduwaimai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dej on 2016/10/6.
 * 商家信息listView的Adapter
 */
public class ShangjiaListAdapter extends BaseAdapter {

    private Context context;
    private List<ShangjiaItem> shangjiaItems;

    public ShangjiaListAdapter(Context context, List<ShangjiaItem> shangjiaItems) {
        this.context = context;
        this.shangjiaItems = shangjiaItems;
    }

    @Override
    public int getCount() {
        return shangjiaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return shangjiaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_list_cell, null);

            viewHolder.ivShangjia = (ImageView) convertView.findViewById(R.id.ivShangjia);
            viewHolder.ivBaiduPeisong = (ImageView) convertView.findViewById(R.id.ivBaiduPeisong);

            viewHolder.tvShangjiaName = (TextView) convertView.findViewById(R.id.tvShangjiaName);
            viewHolder.tvQuan = (TextView) convertView.findViewById(R.id.tvQuan);
            viewHolder.tvPiao = (TextView) convertView.findViewById(R.id.tvPiao);
            viewHolder.tvFu = (TextView) convertView.findViewById(R.id.tvFu);
            viewHolder.tvPei = (TextView) convertView.findViewById(R.id.tvPei);

            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

            viewHolder.tvAllSold = (TextView) convertView.findViewById(R.id.tvAllSold);
            viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            viewHolder.tvQisongCost = (TextView) convertView.findViewById(R.id.tvQisongCost);
            viewHolder.tvPeisongCost = (TextView) convertView.findViewById(R.id.tvPeisongCost);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvMianMsg = (TextView) convertView.findViewById(R.id.tvMianMsg);
            viewHolder.tvQuanMsg = (TextView) convertView.findViewById(R.id.tvQuanMsg);
            viewHolder.tvJianMsg = (TextView) convertView.findViewById(R.id.tvJianMsg);

            viewHolder.shangjiaMsgView = convertView.findViewById(R.id.ll_shangjia_msg);
            viewHolder.mianMsgView = convertView.findViewById(R.id.ll_mian_msg);
            viewHolder.quanMsgView = convertView.findViewById(R.id.ll_quan_msg);
            viewHolder.jianMsgView = convertView.findViewById(R.id.ll_jian_msg);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取商家相关信息并设置显示
        final ShangjiaItem shangjiaItem = (ShangjiaItem) getItem(position);
        System.out.println("ShangjiaItem position: " + position);
        System.out.println("ShangjiaItem boolean: " + shangjiaItem.isIvBaiduPeisongShow() + " "
                + shangjiaItem.isQuan() + " "
                + shangjiaItem.isPiao() + " "
                + shangjiaItem.isFu() + " "
                + shangjiaItem.isPei() + " "
        );

        viewHolder.ivShangjia.setImageResource(shangjiaItem.getIvShangjiaID());
        viewHolder.tvShangjiaName.setText(shangjiaItem.getShangjiaName());
        viewHolder.ratingBar.setRating(shangjiaItem.getRating());

        viewHolder.tvAllSold.setText("  已售" + shangjiaItem.getAllSold() + "份");
        viewHolder.tvDistance.setText(shangjiaItem.getDistance());
        viewHolder.tvQisongCost.setText("起送￥" + shangjiaItem.getQisongCost());
        viewHolder.tvPeisongCost.setText("|配送￥" + shangjiaItem.getPeisongCost());
        viewHolder.tvTime.setText("|平均" + shangjiaItem.getTime() + "分钟");

        if (!shangjiaItem.isIvBaiduPeisongShow()) {
            viewHolder.ivBaiduPeisong.setVisibility(View.GONE);
        } else {
            viewHolder.ivBaiduPeisong.setVisibility(View.VISIBLE);
        }

        if (!shangjiaItem.isQuan()) {
            viewHolder.tvQuan.setVisibility(View.GONE);
        } else {
            viewHolder.tvQuan.setVisibility(View.VISIBLE);
        }

        if (!shangjiaItem.isPiao()) {
            viewHolder.tvPiao.setVisibility(View.GONE);
        } else {
            viewHolder.tvPiao.setVisibility(View.VISIBLE);
        }

        if (!shangjiaItem.isFu()) {
            viewHolder.tvFu.setVisibility(View.GONE);
        }  else {
            viewHolder.tvFu.setVisibility(View.VISIBLE);
        }

        if (!shangjiaItem.isPei()) {
            viewHolder.tvPei.setVisibility(View.GONE);
        } else {
            viewHolder.tvPei.setVisibility(View.VISIBLE);
        }

        if (shangjiaItem.getMianMsg().equals("") && shangjiaItem.getQuanMsg().equals("") && shangjiaItem.getJianMsg().equals("")) {
            viewHolder.shangjiaMsgView.setVisibility(View.GONE);
        } else {
            viewHolder.shangjiaMsgView.setVisibility(View.VISIBLE);

            if (shangjiaItem.getMianMsg().equals("")) {
                viewHolder.mianMsgView.setVisibility(View.GONE);
            } else {
                viewHolder.mianMsgView.setVisibility(View.VISIBLE);
                viewHolder.tvMianMsg.setText(shangjiaItem.getMianMsg());
            }

            if (shangjiaItem.getQuanMsg().equals("")) {
                viewHolder.quanMsgView.setVisibility(View.GONE);
            } else {
                viewHolder.quanMsgView.setVisibility(View.VISIBLE);
                viewHolder.tvQuanMsg.setText(shangjiaItem.getQuanMsg());
            }

            if (shangjiaItem.getJianMsg().equals("")) {
                viewHolder.jianMsgView.setVisibility(View.GONE);
            } else {
                viewHolder.jianMsgView.setVisibility(View.VISIBLE);
                viewHolder.tvJianMsg.setText(shangjiaItem.getJianMsg());
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        // 在List中显示的View
        ImageView ivShangjia, ivBaiduPeisong;
        TextView tvShangjiaName;

        TextView tvQuan, tvPiao, tvFu, tvPei;
        RatingBar ratingBar;

        // 已售、距离、起送价、配送价、送餐时间
        TextView tvAllSold, tvDistance, tvQisongCost, tvPeisongCost, tvTime;
        // “免”信息、“券”信息、“减”信息
        TextView tvMianMsg, tvQuanMsg, tvJianMsg;

        View shangjiaMsgView, mianMsgView, quanMsgView, jianMsgView;
    }
}
