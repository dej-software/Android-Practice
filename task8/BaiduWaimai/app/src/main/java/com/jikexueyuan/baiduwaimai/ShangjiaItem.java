package com.jikexueyuan.baiduwaimai;

/**
 * Created by dej on 2016/10/6.
 * 在首页中显示的商家信息
 */
public class ShangjiaItem {
    private int ivShangjiaID;
    private boolean ivBaiduPeisongShow;
    // 商家名
    private String shangjiaName;
    // 支持 券、票、付、赔
    private boolean isQuan, isPiao, isFu, isPei;
    // 商家评分
    private float rating;
    // 已售、距离、起送价、配送价、送餐时间
    private String allSold, distance, qisongCost, peisongCost, time;
    // “免”信息、“券”信息、“减”信息
    private String mianMsg, quanMsg, jianMsg;

    public ShangjiaItem(int ivShangjiaID, boolean ivBaiduPeisongShow, String shangjiaName,
                        boolean isQuan, boolean isPiao, boolean isFu, boolean isPei, float rating, String allSold, String distance, String qisongCost, String peisongCost, String time,
                        String mianMsg, String quanMsg, String jianMsg) {
        this.ivShangjiaID = ivShangjiaID;
        this.ivBaiduPeisongShow = ivBaiduPeisongShow;
        this.shangjiaName = shangjiaName;
        this.isQuan = isQuan;
        this.isPiao = isPiao;
        this.isFu = isFu;
        this.isPei = isPei;
        this.rating = rating;
        this.allSold = allSold;
        this.distance = distance;
        this.qisongCost = qisongCost;
        this.peisongCost = peisongCost;
        this.time = time;
        this.mianMsg = mianMsg;
        this.quanMsg = quanMsg;
        this.jianMsg = jianMsg;
    }

    public int getIvShangjiaID() {
        return ivShangjiaID;
    }

    public boolean isIvBaiduPeisongShow() {
        return ivBaiduPeisongShow;
    }

    public String getShangjiaName() {
        return shangjiaName;
    }

    public boolean isQuan() {
        return isQuan;
    }

    public boolean isPiao() {
        return isPiao;
    }

    public boolean isFu() {
        return isFu;
    }

    public boolean isPei() {
        return isPei;
    }

    public float getRating() {
        return rating;
    }

    public String getAllSold() {
        return allSold;
    }

    public String getDistance() {
        return distance;
    }

    public String getQisongCost() {
        return qisongCost;
    }

    public String getPeisongCost() {
        return peisongCost;
    }

    public String getTime() {
        return time;
    }

    public String getMianMsg() {
        return mianMsg;
    }

    public String getQuanMsg() {
        return quanMsg;
    }

    public String getJianMsg() {
        return jianMsg;
    }
}
