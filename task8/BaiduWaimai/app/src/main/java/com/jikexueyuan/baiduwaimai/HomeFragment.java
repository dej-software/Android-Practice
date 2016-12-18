package com.jikexueyuan.baiduwaimai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dej on 2016/10/5.
 */
public class HomeFragment extends Fragment {

    // Home页中的布局控件
    private ListView listView;
    private View headView;
    private GridView gridView;

    // listView的Adapter相关
    private List<ShangjiaItem> shangjiaItems;
    private ShangjiaListAdapter shangjiaListAdapter;

    // gridView的Adapter相关
    private List<Map<String, Object>> headGrids;
    private SimpleAdapter simpleAdapter;

    // gridView中的资源
    private int[] residGrids = new int[]{R.drawable.eat, R.drawable.buy, R.drawable.fruit, R.drawable.tea, R.drawable.tuhao, R.drawable.newdian, R.drawable.deliver, R.drawable.medicine};
    private String[] textGrids = new String[]{"餐饮", "超市购", "水果生鲜", "下午茶", "土豪特供", "新店", "百度配送", "网上送药"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initGridView();
        listView = (ListView) rootView.findViewById(R.id.lvHome);
        // 添加listView的头视图
        listView.addHeaderView(headView);
        initListView();

        return rootView;
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        // 添加信息
        shangjiaItems = new ArrayList<>();
        shangjiaItems.add(new ShangjiaItem(R.drawable.shopone, true, "正宗重庆酸辣粉(ABC)",
                true, true, true, true, 1.0f, "100", "1.0km", "15", "25", "45",
                "免费配送", "本店新用户可领取新用户券5元", "在线支付满15减1元"));
        shangjiaItems.add(new ShangjiaItem(R.drawable.shoptwo, false, "正宗重庆酸辣粉（南城分店）",
                true, false, true, true, 1.5f, "100", "1.0km", "15", "25", "45",
                "免费配送", "本店新用户可领取新用户券5元", "在线支付满15减1元"));
        shangjiaItems.add(new ShangjiaItem(R.drawable.shopthree, true, "正宗重庆酸辣粉",
                true, true, false, false, 2.0f, "100", "1.0km", "15", "25", "45",
                "免费配送", "本店新用户可领取新用户券5元", "在线支付满15减1元"));
        shangjiaItems.add(new ShangjiaItem(R.drawable.shopone, true, "正宗重庆酸辣粉(ABC)",
                true, true, true, true, 2.5f, "100", "1.0km", "15", "25", "45",
                "免费配送", "", "在线支付满15减1元"));
        shangjiaItems.add(new ShangjiaItem(R.drawable.shoptwo, false, "正宗重庆酸辣粉（南城分店）",
                true, false, true, true, 3.0f, "100", "1.0km", "15", "25", "45",
                "免费配送", "本店新用户可领取新用户券5元", ""));
        shangjiaItems.add(new ShangjiaItem(R.drawable.shopthree, true, "正宗重庆酸辣粉",
                true, true, false, false, 4.0f, "100", "1.0km", "15", "25", "45",
                "免费配送", "本店新用户可领取新用户券5元", "在线支付满15减1元"));

        // 设置Adapter
        shangjiaListAdapter = new ShangjiaListAdapter(getActivity(), shangjiaItems);
        listView.setAdapter(shangjiaListAdapter);
    }

    /**
     * 初始化GridView
     */
    private void initGridView() {
        // gridView放置在headView中
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_head, null);
        gridView = (GridView) headView.findViewById(R.id.gridView);

        headGrids = new ArrayList<>();

        for (int i = 0; i < residGrids.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", residGrids[i]);
            map.put("text", textGrids[i]);
            headGrids.add(map);
        }

        simpleAdapter = new SimpleAdapter(getActivity(), headGrids,
                R.layout.home_head_cell, new String[]{"image", "text"}, new int[]{R.id.ibHeadGrid, R.id.tvHeadGrid});

        gridView.setAdapter(simpleAdapter);
    }
}
