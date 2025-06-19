package com.example.day5.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.day5.Bean.QuesInfo;
import com.example.day5.R;

import java.util.List;

public class LVAdapter extends BaseAdapter {
    List<QuesInfo> items;
    LayoutInflater inflater;
    public LVAdapter(Context context, List<QuesInfo> items) {
        // 记得传一个context过来，不然没法渲染
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i) ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    class ViewHolder {
        TextView tv_item;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            // inflate布局
            view = inflater.inflate(R.layout.ques_item, viewGroup, false);
            //holder保存控件
            holder = new ViewHolder();
            holder.tv_item = (TextView) view.findViewById(R.id.tv_item);
            //将holder放入当前视图中
            view.setTag(holder);
        } else {
            //复用holder
            holder = (ViewHolder) view.getTag();
        }
        String text = items.get(i).getQues();
        holder.tv_item.setText(text);
        Log.d("LVAdapter", "Binding data: " + i + text); // 添加日志打印
        holder.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "暂未完善", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
