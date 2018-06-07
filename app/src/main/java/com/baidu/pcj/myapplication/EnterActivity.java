package com.baidu.pcj.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.pcj.myapplication.activitys.ColorTrangleActivity;
import com.baidu.pcj.myapplication.activitys.Load3DObj;
import com.baidu.pcj.myapplication.activitys.RoundActivity;
import com.baidu.pcj.myapplication.activitys.TriangleActivity;
import com.baidu.pcj.myapplication.activitys.WithLigthRoundActivity;

import java.util.ArrayList;
import java.util.List;

public class EnterActivity extends Activity {

    private ListView mListView;
    // 数据
    private List<Data> mDatas;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        mListView = findViewById(R.id.rl_view);
        initData();
    }

    private void initData() {
        mAdapter = new ListViewAdapter();
        mDatas = new ArrayList<>();
        // 设置数据
        setData("三角形", TriangleActivity.class);
        setData("彩色三角形", ColorTrangleActivity.class);
        setData("球体", RoundActivity.class);
        setData("带光的球体", WithLigthRoundActivity.class);
        setData("load 3d obj", Load3DObj.class);
        mListView.setAdapter(mAdapter);
    }

    // Data
    private class Data {
        Class<?> aClass;
        String mtitle;
    }

    public void setData(String title, Class<?> mclass) {
        Data data = new Data();
        data.aClass = mclass;
        data.mtitle = title;
        mDatas.add(data);
    }

    // Adapter;
    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(EnterActivity.this).inflate(R.layout.activity_listview_item, null);
                view.setTag(new ViewHolder(view));
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.setData(mDatas.get(i), i);
            return view;
        }

        private class ViewHolder {
            private Button mName;

            public ViewHolder(View parent) {
                mName = (Button) parent.findViewById(R.id.tv_title);
            }

            public void setData(Data data, final int position) {
                mName.setText(data.mtitle);
                mName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EnterActivity.this, mDatas.get(position).aClass);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
