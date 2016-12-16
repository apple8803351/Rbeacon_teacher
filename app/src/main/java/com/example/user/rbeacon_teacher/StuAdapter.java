package com.example.user.rbeacon_teacher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by WeiHx on 2016/9/25.
 */
public class StuAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Student> Students;
    private List<Student> targetStudents;
    private List<Bitmap> bitmaps;
    private List<String> bitmapsNo;

    public StuAdapter(Context context, List<Student> Students, List<Bitmap> bitmaps, List<String> bitmapsNo){
        myInflater = LayoutInflater.from(context);
        this.Students = Students;
        this.targetStudents = Students;
        this.bitmaps = bitmaps;
        this.bitmapsNo = bitmapsNo;
    }

    public StuAdapter(Context context, List<Student> Students, List<Student> targetStudents, List<Bitmap> bitmaps, List<String> bitmapsNo){
        myInflater = LayoutInflater.from(context);
        this.Students = Students;
        this.targetStudents = targetStudents;
        this.bitmaps = bitmaps;
        this.bitmapsNo = bitmapsNo;
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
        String url = "http://180.177.198.50:81/img/";
        DecimalFormat mDecimalFormat = new DecimalFormat("#.##");
        ViewHolder holder = null;

        // 如果內容是空白
        if(convertView==null){
            // 指定目標LAYOUT
            convertView = myInflater.inflate(R.layout.list_student, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.textViewSLNow),
                    (TextView) convertView.findViewById(R.id.textViewSLNo),
                    (TextView) convertView.findViewById(R.id.textViewSLName),
                    (ImageView) convertView.findViewById(R.id.imageSLFace)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Student students = (Student) getItem(position);

        Double att = ( 1 - (students.getAbs() / 20.0)) * 100;
        holder.lvAtt.setText(mDecimalFormat.format(att) + "%");
        holder.lvName.setText(students.getName());
        holder.lvNo.setText(students.getNumber());

        while (bitmapsNo.size() < 20)
        {
            Log.i("loading","等待圖片載入中");
        }

        for(int i=0; i<bitmapsNo.size(); i++)
        {
            if(students.getNumber().equals(bitmapsNo.get(i)))
            {
                Log.i("Now pic :" + bitmapsNo.get(i), students.getNumber() );
                holder.lvIm.setImageBitmap(bitmaps.get(i));
            }
        }
        //holder.lvIm.setImageBitmap(bitmaps.get(0));
        boolean tar = false;
        int colorR = 0xffff3030, colorB = 0xff2545ff;
        if(targetStudents.size() != Students.size())
        {
            for(int i=0; i<targetStudents.size(); i++)
            {
                if(students.getNumber().equals(targetStudents.get(i).getNumber()))
                {
                    tar = true;
                }
            }
        }

        if(tar)
        {
            holder.lvAtt.setTextColor(colorB);
            holder.lvName.setTextColor(colorB);
            holder.lvNo.setTextColor(colorB);
        }
        else if(students.getAbs() > 11)
        {
            holder.lvAtt.setTextColor(colorR);
            holder.lvName.setTextColor(colorR);
            holder.lvNo.setTextColor(colorR);
        }
        else
        {
            holder.lvAtt.setTextColor(0xff434343);
            holder.lvName.setTextColor(0xff434343);
            holder.lvNo.setTextColor(0xff434343);
        }
        /*mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:

                        break;
                }
                super.handleMessage(msg);
            }
        };
        loadPic.handleWebPic(url + students.getNumber() + ".jpg", mHandler);*/
        return convertView;
    }

    private class ViewHolder {
        TextView lvAtt;
        TextView lvName;
        TextView lvNo;
        ImageView lvIm;
        public ViewHolder(TextView lvName, TextView lvMac, TextView lvUUID, ImageView lvIm){
            this.lvAtt = lvName;
            this.lvName = lvMac;
            this.lvNo = lvUUID;
            this.lvIm = lvIm;
        }
    }
}


