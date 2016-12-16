package com.example.user.rbeacon_teacher;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.example.user.rbeacon_teacher.R.style.dialog;

public class StudentMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    OkHttpClient client = new OkHttpClient();
    OkHttpClient okHttpClient;
    //private String path = "http://172.30.4.231:8080/SQL/student.php";
    private String url = "http://180.177.198.50:81/img/";
    private String path = "http://180.177.198.50:81/SQL/student.php";
    private String locapath = "http://180.177.198.50:81/SQL/beacon.php";
    ImageButton[][] imageButtons = new ImageButton[7][7];
    Boolean[][] hereHasStudent = new Boolean[7][7];
    String[][] hereHasStudentID = new String[7][7];

    JSONArray jsonArray, stuLocaJsonArray;
    private MyAdapter adapter;
    private StuAdapter stuAdapter;
    ArrayAdapter<String> adapterNote;
    ArrayList<Student> Allstudent = new ArrayList<>();
    ArrayList<Student> studentArrayList = new ArrayList<>();
    ArrayList<String> Notes, noteOption, bitmapsNo;
    ListView studentlistView, listViewNote, listViewClass;
    NoteDBHelper helper;
    Cursor cursor;
    CursorAdapter cursorAdapter;
    boolean studentToastCheck;

    private DownloadWebPicture loadPic;
    private Handler mHandler;
    private ArrayList<Bitmap> bitmaps;

    CheckBox checkBox;
    View main_include, rand_include, note_include, class_include, data_include, class_table_include;
    ImageView smFace;
    ImageButton imageButtonC, imageButtonClass, imageButtonNote ,imageButtonRandom, imageButtonNoteAdd, imageButtonAngry, imageButtonMessage;
    Button buttonRand, smTab1, smTab2;
    TextView textViewRandAns, smNow, smNo, smName, smData;
    EditText editTextRandArea1, editTextRandArea2, editTextRandCount, editTextNoteInput;

    //int hahatest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createViews();
        createImageButton();
        getStudentData();
        /*while (Allstudent.size() == 0)
        {
            Log.i("loading","等待學生資料匯入中");
        }*/

        // 定時執行
        TimerTask timerTask = new TimerTask(){
            public void run(){
                if(Allstudent.size()!=0)
                {
                    getStudentLoca();
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 10000); // 定時執行, 延遲時間, 下次執行時間
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_main, menu);
        return true;
    }

    // 監聽返回鍵
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            new AlertDialog.Builder(StudentMainActivity.this)
                    .setTitle("離開此程式")
                    .setMessage("你確定要離開？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 右上角按鈕事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(StudentMainActivity.this,"專題展示期間禁止使用設定功能！避免APP壞掉不能畢業！", Toast.LENGTH_SHORT);
            return true;
        }

        if (id == R.id.action_quit) { new AlertDialog.Builder(StudentMainActivity.this)
                .setTitle("離開此程式")
                .setMessage("你確定要離開？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //  側欄按鈕
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // 課表資料
        if (id == R.id.nav_classdata)
        {
            main_include.setVisibility(View.INVISIBLE);
            class_include.setVisibility(View.INVISIBLE);
            class_table_include.setVisibility(View.VISIBLE);
            rand_include.setVisibility(View.INVISIBLE);
            note_include.setVisibility(View.INVISIBLE);
            data_include.setVisibility(View.INVISIBLE);
        }

        // 課堂名單
        else if (id == R.id.nav_classpeople)
        {
            adapter = new MyAdapter(StudentMainActivity.this, Allstudent);
            stuAdapter = new StuAdapter(StudentMainActivity.this, Allstudent, studentArrayList, bitmaps, bitmapsNo);
            listViewClass.setAdapter(stuAdapter);
            main_include.setVisibility(View.INVISIBLE);
            class_include.setVisibility(View.VISIBLE);
            class_table_include.setVisibility(View.INVISIBLE);
            rand_include.setVisibility(View.INVISIBLE);
            note_include.setVisibility(View.INVISIBLE);
            data_include.setVisibility(View.INVISIBLE);
            imageButtonC.setImageDrawable(getResources().getDrawable(R.drawable.button_c_b));
            imageButtonClass.setImageDrawable(getResources().getDrawable(R.drawable.button_class_b));
            imageButtonRandom.setImageDrawable(getResources().getDrawable(R.drawable.button_random_b));
            imageButtonNote.setImageDrawable(getResources().getDrawable(R.drawable.button_note_b));
        }

        // 登出
        else if (id == R.id.nav_logout)
        {
            Toast.makeText(StudentMainActivity.this, "體驗版無變更帳號功能，請先升級帳戶至尊爵版！",Toast.LENGTH_LONG).show();
        }

        // 關於我們
        else if (id == R.id.nav_us)
        {
            String strURL = "https://www.facebook.com/ymshp/?fref=ts";
            Intent ie = new Intent(Intent.ACTION_VIEW, Uri.parse(strURL));
            startActivity(ie);
        }

        // 手動輸入學生位置
        else if (id == R.id.nav_addStudent)
        {
            final View viewSk = LayoutInflater.from(StudentMainActivity.this).inflate(R.layout.student_key_in, null);
            new AlertDialog.Builder(StudentMainActivity.this)
                    .setTitle(R.string.input_student)
                    .setView(viewSk)
                    .setPositiveButton(R.string.input_student_key_in, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editTextSkID = (EditText) viewSk.findViewById(R.id.edittextStudentKeyInID);
                            EditText editTextSkX = (EditText) viewSk.findViewById(R.id.edittextStudentKeyInX);
                            EditText editTextSkY = (EditText) viewSk.findViewById(R.id.edittextStudentKeyInY);
                            if(!Allstudent.isEmpty() && !editTextSkID.getText().toString().equals(""))
                            {
                                boolean studentAlive = false;
                                for(int i=0; i<Allstudent.size(); i++)
                                {
                                    if(editTextSkX.getText().toString().equals("") || editTextSkY.getText().toString().equals("") || Integer.valueOf(editTextSkX.getText().toString()) > 6 || Integer.valueOf(editTextSkX.getText().toString()) < 0 || Integer.valueOf(editTextSkY.getText().toString()) > 6 || Integer.valueOf(editTextSkY.getText().toString()) < 0) Toast.makeText(StudentMainActivity.this, "超出教室範圍", Toast.LENGTH_LONG).show();
                                    else if(editTextSkID.getText().toString().equals(Allstudent.get(i).getNumber()))
                                    {
                                        checkStudent(Allstudent.get(i));
                                        putStudent(Integer.valueOf(editTextSkX.getText().toString()), Integer.valueOf(editTextSkY.getText().toString()), Allstudent.get(i));
                                        AddList(Allstudent.get(i));
                                        studentAlive = true;
                                    }
                                }
                                if(!studentAlive)  Toast.makeText(StudentMainActivity.this, "該學生並未修習本課程", Toast.LENGTH_LONG).show();
                            }
                            else  Toast.makeText(StudentMainActivity.this, "尚未匯入伺服器學生名單，請確認網路連線正常", Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();
        }

        //Demo X 模式
        else if (id == R.id.nav_demoX)
        {
            int[] Demop = {1,3,6,9,12}, Demox = {2,4,1,3,1}, DemoY = {6,6,5,6,6};
            for(int i=0; i < Demop.length; i++)
            {
                checkStudent(Allstudent.get(Demop[i]));
                putStudent(Demox[i], DemoY[i], Allstudent.get(Demop[i]));
                AddList(Allstudent.get(Demop[i]));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 資料庫取得學生位置
    private void getStudentLoca()
    {
        new Thread()
        {
            public void run()
            {
                try{
                    okHttpClient = new OkHttpClient();
                    okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
                    okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);

                    Request request = new Request.Builder()
                            .url(locapath)
                            .build();

                    final String locajson = client.newCall(request).execute().body().string();
                    stuLocaJsonArray = new JSONArray(locajson);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //Toast.makeText(StudentMainActivity.this, "已成功取得學生座標", Toast.LENGTH_LONG).show();
                                for (int i = 0; i < stuLocaJsonArray.length(); i++) {
                                    for (int j = 0; j < Allstudent.size(); j++) {
                                        if (stuLocaJsonArray.getJSONObject(i).getString("ID").equals(Allstudent.get(j).getNumber())) {
                                            checkStudent(Allstudent.get(j));
                                            int x = Integer.valueOf(stuLocaJsonArray.getJSONObject(i).getString("X"));
                                            int y = Integer.valueOf(stuLocaJsonArray.getJSONObject(i).getString("Y"));
                                            while (hereHasStudent[x][y])
                                            {
                                                x = (x == 6)? x - 1 : x + 1;
                                            }
                                            putStudent( x, y, Allstudent.get(j));
                                            studentArrayList.add(Allstudent.get(j));
                                            adapter = new MyAdapter(StudentMainActivity.this,studentArrayList);
                                            stuAdapter = new StuAdapter(StudentMainActivity.this,studentArrayList, bitmaps, bitmapsNo);
                                            studentlistView.setAdapter(adapter);
                                            //AddList(Allstudent.get(j));
                                        }
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                Log.e("!", e.toString());
                                Toast.makeText(StudentMainActivity.this, "佈置學生失敗", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    Log.e("Error",e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StudentMainActivity.this, "從伺服器取得學生座標失敗！\n請確認您的網路環境，或聯絡客服人員", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }.start();

    }

    // 主頁座位表按鈕事件
    ImageButton.OnClickListener iclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton imageButton = (ImageButton)v;
            for(int i=0; i<7; i++)
            {
                for(int j=0; j<7; j++)
                {
                    if(imageButton.getId() == imageButtons[i][j].getId() && hereHasStudent[i][j])
                    {
                        for(int k=0; k<studentArrayList.size(); k++)
                        {
                            if(hereHasStudentID[i][j] == studentArrayList.get(k).getNumber())
                            {
                                setAlertDialogToastbox(imageButton, studentArrayList.get(k));
                            }
                        }
                    }
                }
            }
        }
    };

    // 座位表按鈕事件的訊息窗
    private void setAlertDialogToastbox(ImageButton imageButton, final Student student){
        final View item = LayoutInflater.from(StudentMainActivity.this).inflate(R.layout.toastbox, null); // 設定引用介面
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentMainActivity.this, dialog) // 設定STYLE及內容
                .setView(item);
                /*.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })*/

        //  取得視窗並定下位置
        AlertDialog ad = alertDialog.create();
        WindowManager.LayoutParams lp;
        Window window = ad.getWindow();
        lp = window.getAttributes();
        //lp.alpha = 0.5f;
        int[] imagelocation = new int[2];
        imageButton.getLocationOnScreen(imagelocation);
        lp.x = (int)(imagelocation[0]/2.5) -100;
        lp.y = (int)(imagelocation[1]/2.5) -100; //100
        //Toast.makeText(StudentMainActivity. this, imagelocation[0] + "," + imagelocation[1], Toast.LENGTH_SHORT).show();
        window.setAttributes(lp);
        DecimalFormat mDecimalFormat = new DecimalFormat("#.##");

        //將學生資料匯入訊息窗
        TextView textViewToastId = (TextView)item.findViewById(R.id.textViewToastId);
        TextView textViewToastName = (TextView)item.findViewById(R.id.textViewToastName);
        TextView textViewToastData = (TextView)item.findViewById(R.id.textViewToastData);
        Button buttonToastStudentData = (Button)item.findViewById(R.id.buttonToastbutton);
        Button buttonToastCheck = (Button)item.findViewById(R.id.buttonToastCheck);

        studentToastCheck = false;
        textViewToastId.setText(student.getNumber());
        textViewToastName.setText(student.getName());
        Double score = Double.parseDouble(mDecimalFormat.format( student.getScoreMid() + Math.random() * 10));
        if(student.getScorePar() == 0.0) student.setScorePar(((score>100)? score - (score - 100) - 6: score));
        textViewToastData.setText("　　曠課" + student.getAbs() +"次　請假" + student.getAfl() + "次　　\n期中成績：" + student.getScoreMid() + "\n目前平時成績：" + student.getScorePar());
        buttonToastStudentData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToStudentData(student);
            }
        });
        buttonToastCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!studentToastCheck)Toast.makeText(StudentMainActivity.this, student.getNumber() + " " + student.getName() + " 已標記為到課",Toast.LENGTH_SHORT).show();
                else Toast.makeText(StudentMainActivity.this, student.getNumber() + " " + student.getName() + " 已取消其到課，標記為未到課",Toast.LENGTH_SHORT).show();
            }
        });
        ad.show();
    }

    // 推播功能的訊息窗
    private void setAlertDialog1Event(){
        new AlertDialog.Builder(StudentMainActivity.this)
                .setTitle(R.string.lunch_time)
                .setMessage(R.string.want_to_eat)

                .setNegativeButton(R.string.wait_a_minute, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), R.string.i_am_hungry, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton(R.string.not_hungry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    // 從伺服器取得學生陣列
    private void getStudentData()
    {
        new Thread()
        {
            public void run()
            {
                try{
                    Request request = new Request.Builder()
                            .url(path)
                            .build();
                    final String json = client.newCall(request).execute().body().string();
                    jsonArray = new JSONArray(json);

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        String id = jsonArray.getJSONObject(i).getString("ID");
                        String name = jsonArray.getJSONObject(i).getString("姓名");
                        String sex = jsonArray.getJSONObject(i).getString("性別");
                        String phone = jsonArray.getJSONObject(i).getString("手機");
                        String afl = jsonArray.getJSONObject(i).getString("leave");
                        String late = jsonArray.getJSONObject(i).getString("late");
                        String abs = jsonArray.getJSONObject(i).getString("nobody");
                        String scoreMid = jsonArray.getJSONObject(i).getString("期中成績");
                        String scoreFin = jsonArray.getJSONObject(i).getString("期末成績");

                        Student student = new Student(id, name, sex, phone, Double.valueOf(scoreMid), Double.valueOf(scoreFin), Integer.valueOf(afl), Integer.valueOf(late), Integer.valueOf(abs));
                        //Log.i("",id);Log.i("",name);Log.i("",sex);Log.i("",phone);Log.i("",attend);Log.i("",afl);Log.i("",late);Log.i("",abs);Log.i("",scoreMid);Log.i("",scoreFin);
                        Allstudent.add(student);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StudentMainActivity.this, "已成功從伺服器匯入課程資料", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (Exception e)
                {
                    Log.e("Error",e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StudentMainActivity.this, "從伺服器匯入課程資料失敗！\n請確認您的網路環境，或聯絡客服人員", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                Log.i("bitmaps","Start load pic");

                // 存入全部學生圖片
                bitmaps = new ArrayList<>();
                bitmapsNo = new ArrayList<>();
                final DownloadWebPicture xloadPic = new DownloadWebPicture();
                for(int i=0; i<Allstudent.size(); i++)
                {

                    Log.i("Loading pic", url + Allstudent.get(i).getNumber() + ".jpg");
                    bitmaps.add(xloadPic.getUrlPic(url + Allstudent.get(i).getNumber() + ".jpg"));
                    Log.i("Loading pic No." + String.valueOf(bitmapsNo.size()), Allstudent.get(i).getNumber());
                    bitmapsNo.add(Allstudent.get(i).getNumber());
                }
            }
        }.start();
    }

    // 確認座位表上是否已經有學生，若有則逐出
    private void checkStudent(Student student)
    {
        for(int x=0; x<7; x++)
        {
            for(int y=0; y<7; y++)
            {
                if(hereHasStudentID[x][y].equals(student.getNumber()))
                {
                    outStudent(x,y);
                }
            }
        }
    }

    // 放置學生
    private void putStudent(int x, int y, Student student)
    {
        imageButtons[x][y].setImageDrawable(getResources().getDrawable(R.drawable.button_people));
        imageButtons[x][y].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        hereHasStudent[x][y] = true;
        hereHasStudentID[x][y] = student.getNumber();
    }

    // 逐出學生
    private void outStudent(int x,int y)
    {
        imageButtons[x][y].setImageDrawable(getResources().getDrawable(R.color.e8e8e8));
        hereHasStudent[x][y] = false;
        for(int i=0; i<studentArrayList.size(); i++)
        {
            if(hereHasStudentID[x][y] == studentArrayList.get(i).getNumber())
            {
                studentArrayList.remove(i);
                hereHasStudentID[x][y] = "";
            }
        }
    }

    // 增加學生
    private void AddList(Student student)
    {
        studentArrayList.add(student);
        adapter = new MyAdapter(StudentMainActivity.this,studentArrayList);
        stuAdapter = new StuAdapter(StudentMainActivity.this,studentArrayList, bitmaps, bitmapsNo);
        studentlistView.setAdapter(adapter);
        listViewClass.setAdapter(stuAdapter);
    }

    // 隨機抽選學生
    private String randstudent()
    {
        ArrayList<Student> randStudents = new ArrayList<Student>();
        ArrayList<Integer> randnumber = new ArrayList<>();
        int randArea1 = 0; int randArea2 = 100;
        if(editTextRandArea1.getText().toString() != "" && editTextRandArea2.getText().toString() != "")
        {
            randArea1 = Integer.valueOf(editTextRandArea1.getText().toString());
            randArea2 = Integer.valueOf(editTextRandArea2.getText().toString());
        }

        int round = Integer.valueOf(editTextRandCount.getText().toString());
        if( round > studentArrayList.size() || round < 1)
        {
            Toast.makeText(StudentMainActivity.this, "輸入人數錯誤", Toast.LENGTH_SHORT).show();
        }
        else {
            // 使用洗牌法取亂數
            // 放入數字
            for (int i = 0; i < studentArrayList.size(); i++) {
                randnumber.add(i);
            }

            // 洗牌
            for (int i = 0; i < randnumber.size(); i++) {
                int index = (int) (Math.random() * randnumber.size());
                int temp = randnumber.get(index);
                randnumber.set(index, randnumber.get(i));
                randnumber.set(i, temp);
            }

            // 抽牌
            for (int i = 0; i < round; i++) {
                Student student = studentArrayList.get(randnumber.get(i));
                if (Integer.valueOf(student.getNumber().substring(7, 9)) > randArea1 && Integer.valueOf(student.getNumber().substring(7, 9)) < randArea2) // 判斷是否在範圍內
                {
                    randStudents.add(student); // 如果在範圍內，放入抽中的學生區
                } else {
                    round++; // 如果不在範圍內，多抽一次
                }
            }

            String randAns = "";
            for (int i = 0; i < randStudents.size(); i++) {
                randAns += randStudents.get(i).getNumber() + "  " + randStudents.get(i).getName() + " \n";
            }
            return randAns;
        }
        return "";
    }

    // 宣告元件
    private void createViews()
    {
        main_include = (View)findViewById(R.id.main_include);
        rand_include = (View)findViewById(R.id.rand_include);
        note_include = (View)findViewById(R.id.note_include);
        class_include = (View)findViewById(R.id.class_include);
        data_include = (View)findViewById(R.id.data_include);
        class_table_include = (View)findViewById(R.id.class_table_include);
        studentlistView = (ListView)findViewById(R.id.studentClassListview);
        studentlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpToStudentData(studentArrayList.get(position));
            }
        });

        imageButtonC = (ImageButton)findViewById(R.id.imageButtonC);
        imageButtonClass = (ImageButton)findViewById(R.id.imageButtonClass);
        imageButtonRandom = (ImageButton)findViewById(R.id.imageButtonRandom);
        imageButtonNote = (ImageButton)findViewById(R.id.imageButtonN);
        helper = new NoteDBHelper(getApplicationContext());


        // 分頁按鈕
        // 點名
        imageButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_include.setVisibility(View.VISIBLE);
                class_include.setVisibility(View.INVISIBLE);
                class_table_include.setVisibility(View.INVISIBLE);
                rand_include.setVisibility(View.INVISIBLE);
                note_include.setVisibility(View.INVISIBLE);
                data_include.setVisibility(View.INVISIBLE);
                imageButtonC.setImageDrawable(getResources().getDrawable(R.drawable.button_c));
                imageButtonClass.setImageDrawable(getResources().getDrawable(R.drawable.button_class_b));
                imageButtonRandom.setImageDrawable(getResources().getDrawable(R.drawable.button_random_b));
                imageButtonNote.setImageDrawable(getResources().getDrawable(R.drawable.button_note_b));
            }
        });

        // 學生列表
        imageButtonClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_include.setVisibility(View.INVISIBLE);
                class_include.setVisibility(View.VISIBLE);
                class_table_include.setVisibility(View.INVISIBLE);
                rand_include.setVisibility(View.INVISIBLE);
                note_include.setVisibility(View.INVISIBLE);
                data_include.setVisibility(View.INVISIBLE);
                imageButtonC.setImageDrawable(getResources().getDrawable(R.drawable.button_c_b));
                imageButtonClass.setImageDrawable(getResources().getDrawable(R.drawable.button_class));
                imageButtonRandom.setImageDrawable(getResources().getDrawable(R.drawable.button_random_b));
                imageButtonNote.setImageDrawable(getResources().getDrawable(R.drawable.button_note_b));
                adapter = new MyAdapter(StudentMainActivity.this,studentArrayList);
                stuAdapter = new StuAdapter(StudentMainActivity.this,studentArrayList, bitmaps, bitmapsNo);
                listViewClass.setAdapter(stuAdapter);
            }
        });

        // 抽籤
        imageButtonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_include.setVisibility(View.INVISIBLE);
                class_include.setVisibility(View.INVISIBLE);
                class_table_include.setVisibility(View.INVISIBLE);
                rand_include.setVisibility(View.VISIBLE);
                note_include.setVisibility(View.INVISIBLE);
                data_include.setVisibility(View.INVISIBLE);
                imageButtonC.setImageDrawable(getResources().getDrawable(R.drawable.button_c_b));
                imageButtonClass.setImageDrawable(getResources().getDrawable(R.drawable.button_class_b));
                imageButtonRandom.setImageDrawable(getResources().getDrawable(R.drawable.button_random));
                imageButtonNote.setImageDrawable(getResources().getDrawable(R.drawable.button_note_b));
            }
        });

        // 記事
        imageButtonNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_include.setVisibility(View.INVISIBLE);
                class_include.setVisibility(View.INVISIBLE);
                class_table_include.setVisibility(View.INVISIBLE);
                rand_include.setVisibility(View.INVISIBLE);
                note_include.setVisibility(View.VISIBLE);
                data_include.setVisibility(View.INVISIBLE);
                imageButtonC.setImageDrawable(getResources().getDrawable(R.drawable.button_c_b));
                imageButtonClass.setImageDrawable(getResources().getDrawable(R.drawable.button_class_b));
                imageButtonRandom.setImageDrawable(getResources().getDrawable(R.drawable.button_random_b));
                imageButtonNote.setImageDrawable(getResources().getDrawable(R.drawable.button_note));
            }
        });

        // 教授很不爽鍵
        imageButtonAngry = (ImageButton)findViewById(R.id.imageButtonAngry);
        imageButtonAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlertDialog1Event();
            }
        });

        // 訊息鍵
        imageButtonMessage = (ImageButton)findViewById(R.id.imageButtonMessage);
        imageButtonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonNote.callOnClick();
                editTextNoteInput.setText("@王昱閔 ");
            }
        });

        // 課程學生頁面
        listViewClass = (ListView)findViewById(R.id.listviewClass);

        // 學生資料頁面
        smTab1 = (Button)findViewById(R.id.buttonSMtab1);
        smTab2 = (Button)findViewById(R.id.buttonSNtab2);
        smNow = (TextView)findViewById(R.id.textViewSMNow);
        smNo = (TextView)findViewById(R.id.textViewSMNo);
        smName = (TextView)findViewById(R.id.textViewSMName);
        smData = (TextView)findViewById(R.id.textViewSMdata);
        smFace = (ImageView)findViewById(R.id.imageSMFace);

        // 抽籤頁面
        textViewRandAns = (TextView)findViewById(R.id.textViewRandAns);
        editTextRandArea1 = (EditText)findViewById(R.id.editTextRandArea1);
        editTextRandArea2 = (EditText)findViewById(R.id.editTextRandArea2);
        editTextRandCount = (EditText)findViewById(R.id.editTextRandCount);
        buttonRand = (Button) findViewById(R.id.buttonRand);
        buttonRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentArrayList.isEmpty()) Toast.makeText(StudentMainActivity.this, "目前沒有學生到課！", Toast.LENGTH_LONG).show();
                else textViewRandAns.setText(randstudent());
            }
        });

        checkBox = (CheckBox)findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) Toast.makeText(StudentMainActivity.this, "開啟自動點名功能，系統將自動掃描學生並標記為到課", Toast.LENGTH_LONG).show();
                else Toast.makeText(StudentMainActivity.this, "關閉自動點名功能", Toast.LENGTH_LONG).show();
            }
        });

        // 記事本長按選項
        noteOption = new ArrayList<>();
        noteOption.add(getString(R.string.delete));

        // 記事頁面
        editTextNoteInput = (EditText)findViewById(R.id.editText_noteEnter);
        listViewNote = (ListView)findViewById(R.id.listView_note);
        imageButtonNoteAdd = (ImageButton)findViewById(R.id.imageButtonNoteAdd);
        Notes = new ArrayList<>();
        adapterNote = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Notes);
        listViewNote.setAdapter(cursorAdapter);

        // 記事本資料庫
        initDB();

        // 新增記事
        imageButtonNoteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextNoteInput.getText().toString().equals(""))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis()) ;
                    helper.insert(formatter.format(curDate) + "\n" + editTextNoteInput.getText().toString());
                    cursor.requery();
                    cursorAdapter.notifyDataSetChanged();
                    editTextNoteInput.setText("");
                    listViewNote.setAdapter(cursorAdapter);
                }
            }
        });

        // 刪除記事
        listViewNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                cursor.moveToPosition(pos);
                new android.app.AlertDialog.Builder(StudentMainActivity.this)
                        .setItems(noteOption.toArray(new String[noteOption.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                        new android.app.AlertDialog.Builder(StudentMainActivity.this)
                                                .setTitle("刪除列")
                                                .setMessage("你確定要刪除？")
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        helper.delete(cursor.getInt(0));
                                                        cursor.requery();
                                                        cursorAdapter.notifyDataSetChanged();
                                                    }
                                                })
                                                .setNegativeButton("否", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                            }
                        }).show();
                return false;
            }
        });
    }

    // 讀取記事本資料庫
    private void initDB()
    {
        helper = new NoteDBHelper(getApplicationContext());
        cursor = helper.select();
        cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.adapter, cursor,
                new String[]{"item_text"},
                new int[]{R.id.textViewNoteAda},
                android.widget.SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        listViewNote.setAdapter(cursorAdapter);
    }

    // 進入學生資料頁面
    private void jumpToStudentData(final Student student)
    {
        main_include.setVisibility(View.INVISIBLE);
        class_include.setVisibility(View.INVISIBLE);
        rand_include.setVisibility(View.INVISIBLE);
        note_include.setVisibility(View.INVISIBLE);
        data_include.setVisibility(View.VISIBLE);
        imageButtonC.setImageDrawable(getResources().getDrawable(R.drawable.button_c_b));
        imageButtonClass.setImageDrawable(getResources().getDrawable(R.drawable.button_class_b));
        imageButtonRandom.setImageDrawable(getResources().getDrawable(R.drawable.button_random_b));
        imageButtonNote.setImageDrawable(getResources().getDrawable(R.drawable.button_note_b));

        smNow.setText("未到課");
        for(int i=0; i< studentArrayList.size(); i++)
        {
            if(studentArrayList.get(i).equals(student)) smNow.setText("到課");
        }
        smName.setText(student.getName());
        smNo.setText(student.getNumber());
        if(student.getScorePar() == 0.0)
        {
            DecimalFormat mDecimalFormat = new DecimalFormat("#.##");
            Double score = Double.parseDouble(mDecimalFormat.format(student.getScoreMid() + Math.random() * 10));
            student.setScorePar(((score > 100) ? score - (score - 100) - 6 : score));
        }
        smData.setText( student.getSex() + "性　電話：" + student.getPhone() + "\n曠課" + student.getAbs() +"次\n請假" + student.getAfl() + "次\n遲到" + student.getLate()+"次\n" + "期中成績：" + student.getScoreMid() + "\n期末成績" + student.getScoreFin() + "\n平時成績：" + student.getScorePar());
        smTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smData.setText(student.getSex() + "性　電話：" + student.getPhone() + "\n曠課" + student.getAbs() +"次\n請假" + student.getAfl() + "次\n遲到" + student.getLate()+"次\n" + "期中成績：" + student.getScoreMid() + "\n期末成績" + student.getScoreFin() + "\n平時成績：" + student.getScorePar());
                smTab1.setBackground(getResources().getDrawable(R.drawable.button_tab1));
                smTab2.setBackground(getResources().getDrawable(R.drawable.button_tab2));
            }
        });

        smTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smData.setText("個人介紹：未填");
                smTab1.setBackground(getResources().getDrawable(R.drawable.button_tab2));
                smTab2.setBackground(getResources().getDrawable(R.drawable.button_tab1));
            }
        });

        // 讀取圖片
        loadPic = new DownloadWebPicture();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        smFace.setImageBitmap(loadPic.getImg());
                        break;
                }
                super.handleMessage(msg);
            }
        };
        loadPic.handleWebPic(url + student.getNumber() + ".jpg", mHandler);
    }

    // 宣告座位表按鈕
    private void createImageButton()
    {
        imageButtons[0][0]= (ImageButton)findViewById(R.id.imageButton11);
        imageButtons[0][1]= (ImageButton)findViewById(R.id.imageButton12);
        imageButtons[0][2]= (ImageButton)findViewById(R.id.imageButton13);
        imageButtons[0][3]= (ImageButton)findViewById(R.id.imageButton14);
        imageButtons[0][4]= (ImageButton)findViewById(R.id.imageButton15);
        imageButtons[0][5]= (ImageButton)findViewById(R.id.imageButton16);
        imageButtons[0][6]= (ImageButton)findViewById(R.id.imageButton17);
        imageButtons[1][0]= (ImageButton)findViewById(R.id.imageButton21);
        imageButtons[1][1]= (ImageButton)findViewById(R.id.imageButton22);
        imageButtons[1][2]= (ImageButton)findViewById(R.id.imageButton23);
        imageButtons[1][3]= (ImageButton)findViewById(R.id.imageButton24);
        imageButtons[1][4]= (ImageButton)findViewById(R.id.imageButton25);
        imageButtons[1][5]= (ImageButton)findViewById(R.id.imageButton26);
        imageButtons[1][6]= (ImageButton)findViewById(R.id.imageButton27);
        imageButtons[2][0]= (ImageButton)findViewById(R.id.imageButton31);
        imageButtons[2][1]= (ImageButton)findViewById(R.id.imageButton32);
        imageButtons[2][2]= (ImageButton)findViewById(R.id.imageButton33);
        imageButtons[2][3]= (ImageButton)findViewById(R.id.imageButton34);
        imageButtons[2][4]= (ImageButton)findViewById(R.id.imageButton35);
        imageButtons[2][5]= (ImageButton)findViewById(R.id.imageButton36);
        imageButtons[2][6]= (ImageButton)findViewById(R.id.imageButton37);
        imageButtons[3][0]= (ImageButton)findViewById(R.id.imageButton41);
        imageButtons[3][1]= (ImageButton)findViewById(R.id.imageButton42);
        imageButtons[3][2]= (ImageButton)findViewById(R.id.imageButton43);
        imageButtons[3][3]= (ImageButton)findViewById(R.id.imageButton44);
        imageButtons[3][4]= (ImageButton)findViewById(R.id.imageButton45);
        imageButtons[3][5]= (ImageButton)findViewById(R.id.imageButton46);
        imageButtons[3][6]= (ImageButton)findViewById(R.id.imageButton47);
        imageButtons[4][0]= (ImageButton)findViewById(R.id.imageButton51);
        imageButtons[4][1]= (ImageButton)findViewById(R.id.imageButton52);
        imageButtons[4][2]= (ImageButton)findViewById(R.id.imageButton53);
        imageButtons[4][3]= (ImageButton)findViewById(R.id.imageButton54);
        imageButtons[4][4]= (ImageButton)findViewById(R.id.imageButton55);
        imageButtons[4][5]= (ImageButton)findViewById(R.id.imageButton56);
        imageButtons[4][6]= (ImageButton)findViewById(R.id.imageButton57);
        imageButtons[5][0]= (ImageButton)findViewById(R.id.imageButton61);
        imageButtons[5][1]= (ImageButton)findViewById(R.id.imageButton62);
        imageButtons[5][2]= (ImageButton)findViewById(R.id.imageButton63);
        imageButtons[5][3]= (ImageButton)findViewById(R.id.imageButton64);
        imageButtons[5][4]= (ImageButton)findViewById(R.id.imageButton65);
        imageButtons[5][5]= (ImageButton)findViewById(R.id.imageButton66);
        imageButtons[5][6]= (ImageButton)findViewById(R.id.imageButton67);
        imageButtons[6][0]= (ImageButton)findViewById(R.id.imageButton71);
        imageButtons[6][1]= (ImageButton)findViewById(R.id.imageButton72);
        imageButtons[6][2]= (ImageButton)findViewById(R.id.imageButton73);
        imageButtons[6][3]= (ImageButton)findViewById(R.id.imageButton74);
        imageButtons[6][4]= (ImageButton)findViewById(R.id.imageButton75);
        imageButtons[6][5]= (ImageButton)findViewById(R.id.imageButton76);
        imageButtons[6][6]= (ImageButton)findViewById(R.id.imageButton77);
        for(int i=0;i<7;i++)
        {
            for(int j=0;j<7;j++)
            {
                imageButtons[i][j].setOnClickListener(iclickListener);
                hereHasStudent[i][j] = false;
                hereHasStudentID[i][j] = "";
            }
        }
    }
}
