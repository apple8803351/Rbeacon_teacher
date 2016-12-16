package com.example.user.rbeacon_teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WeiHx on 2016/9/25.
 */
public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Student> Students;

    public MyAdapter(Context context, List<Student> Students){
        myInflater = LayoutInflater.from(context);
        this.Students = Students;
    }

    @Override
    public int getCount() {
        return Students.size();
    }

    @Override
    public Object getItem(int arg0) {
        return Students.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return Students.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果內容是空白
        if(convertView==null){
            // 指定目標LAYOUT
            convertView = myInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.lvAtt),
                    (TextView) convertView.findViewById(R.id.lvName),
                    (TextView) convertView.findViewById(R.id.lvNo)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Student students = (Student) getItem(position);
        holder.lvAtt.setText(students.getName());
        holder.lvName.setText(students.getNumber());
        holder.lvNo.setText("曠課：" + students.getAbs() + "\n請假：" + students.getAfl());
        return convertView;
    }

    private class ViewHolder {
        TextView lvAtt;
        TextView lvName;
        TextView lvNo;
        public ViewHolder(TextView lvName, TextView lvMac, TextView lvUUID){
            this.lvAtt = lvName;
            this.lvName = lvMac;
            this.lvNo = lvUUID;
        }
    }
}


