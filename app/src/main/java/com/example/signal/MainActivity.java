package com.example.signal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView tv_status;
    TextView TxStatus;
    Thread thread;
    TextView online;
    TextView carview;
    TextView btVoltage;
    TextView tvPing;

    ImageView upArr;
    ImageView doArr;
    ImageView riArr;
    ImageView leArr;
    ImageView clArr;
    ImageView alArr;

    TextView tvlt;
    TextView tvld;
    TextView tvrt;
    TextView tvrd;

    ImageView modeSelector;

    public Calendar calendar;

    //ESP record
    String ip="192.168.43.106";
    String reserveState="Tx00";
    Boolean rightFlag=false;
    long ty_count=0;

    Boolean arrowMode=true;

    //Button effect flag
    boolean KeyVib=true;
    //for ping
    long initMilli = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        online=findViewById(R.id.online);
        online.setBackgroundResource(R.drawable.textview_boder);
        carview=findViewById(R.id.carView);
        carview.setBackgroundResource(R.drawable.carview_boder);
        TxStatus=findViewById(R.id.status);
        TxStatus.setBackgroundResource(R.drawable.textview_boder);
        btVoltage=findViewById(R.id.bt_Voltage);
        btVoltage.setBackgroundResource(R.drawable.textview_boder);
        tvPing=findViewById(R.id.tv_ping);
        tvPing.setBackgroundResource(R.drawable.textview_boder);

        upArr=findViewById(R.id.up_arr);
        doArr=findViewById(R.id.do_arr);
        leArr=findViewById(R.id.le_arr);
        riArr=findViewById(R.id.ri_arr);
        clArr=findViewById(R.id.ar_Clock);
        alArr=findViewById(R.id.ar_antiClock);

        tvlt=findViewById(R.id.tv_lt);
        tvld=findViewById(R.id.tv_ld);
        tvrt=findViewById(R.id.tv_rt);
        tvrd=findViewById(R.id.tv_rd);


        modeSelector=findViewById(R.id.mode_selector);


        modeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrowMode)
                {
                    arrowMode =false;
                    modeSelector.setImageResource(R.drawable.backword);
                }else{
                    arrowMode =true;
                    modeSelector.setImageResource(R.drawable.forword);
                }
            }
        });

        upArr.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    reserveState="Tx11";
                    dataTransfer(ip,reserveState);
                    if(KeyVib)
                    {
                        buttonVb();
                    }
                    upArr.setImageResource(R.drawable.tauch);

                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    if(rightFlag)
                    {
                        reserveState="Tx00";
                    }else{
                        reserveState="Tx00";
                        dataTransfer(ip,"Tx00");
                    }
                    upArr.setImageResource(R.drawable.up_arr);

                }
                return true;
            }
        });

        doArr.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    reserveState="Tx22";
                    dataTransfer(ip,reserveState);
                    doArr.setImageResource(R.drawable.tauch);
                    if(KeyVib)
                    {
                        buttonVb();
                    }

                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    if(rightFlag)
                    {
                        reserveState="Tx00";
                    }else{
                        reserveState="Tx00";
                        dataTransfer(ip,"Tx00");
                    }
                    doArr.setImageResource(R.drawable.do_arr);

                }
                return true;
            }
        });

        riArr.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    rightFlag=true;
                    dataTransfer(ip,"Tx10");
                    riArr.setImageResource(R.drawable.tauch);
                    if(KeyVib)
                    {
                        buttonVb();
                    }

                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    rightFlag=false;
                    dataTransfer(ip,reserveState);
                    riArr.setImageResource(R.drawable.ri_arr);


                }
                return true;
            }
        });

        leArr.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {

                    rightFlag=true;
                    dataTransfer(ip,"Tx01");
                    leArr.setImageResource(R.drawable.tauch);
                    if(KeyVib)
                    {
                        buttonVb();
                    }


                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    rightFlag=false;
                    dataTransfer(ip,reserveState);
                    leArr.setImageResource(R.drawable.le_arr);

                }
                return true;
            }
        });

        clArr.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    rightFlag=true;
                    dataTransfer(ip,"Tx12");
                    clArr.setImageResource(R.drawable.tauch);
                    if(KeyVib)
                    {
                        buttonVb();
                    }

                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    rightFlag=false;
                    dataTransfer(ip,reserveState);
                    clArr.setImageResource(R.drawable.ar_clock);

                }
                return true;
            }
        });

        alArr.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    rightFlag=true;
                    dataTransfer(ip,"Tx21");
                    alArr.setImageResource(R.drawable.tauch);
                    if(KeyVib)
                    {
                        buttonVb();
                    }

                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    rightFlag=false;
                    dataTransfer(ip,reserveState);
                    alArr.setImageResource(R.drawable.ar_anticlock);

                }
                return true;
            }
        });


        ///////////////////////////////////////////////////////////
        //Process Program down
        //////////////////////////////////////////////////////////
        //for ping
        calendar = Calendar.getInstance();

        thread = new Thread(){
            boolean flag=true;
            @Override
            public void run() {
                try{
                    while(true){

                        if(flag) {
                            ty_count++;// Leter use it
                            online.setBackgroundColor(Color.parseColor("#ff0000"));
                            dataTransfer(ip,"Ty");
                            Date date=new Date();
                            initMilli =date.getTime();
                            flag=false;
                        }else{
//                            dataTransfer("192.168.43.2","H");
                            flag=true;
                        }

                        Thread.sleep(1000);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    protected void dataTransfer(String ipAddress,String data){
        OkHttpClient client = new OkHttpClient();

        String url = "http://"+ipAddress+"/"+data;
        TxStatus.setText(url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            feedbackProcess(myResponse);
                        }
                    });
                }
            }
        });
    }
    public void buttonVb()
    {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(50);
        }
    }
    String showFD="";
    public void feedbackProcess(String feedback)
    {
        showFD=feedback.replace("\n","")+"\n"+showFD;

        carview.setText(showFD);
        if(feedback.charAt(1)=='x')
        {
            switch (feedback.charAt(2)){
                case '0':
                    tvlt.setBackgroundColor(Color.parseColor("#000000"));
                    tvld.setBackgroundColor(Color.parseColor("#000000"));
                    break;
                case '1':
                    tvlt.setBackgroundColor(Color.parseColor("#00ff00"));
                    tvld.setBackgroundColor(Color.parseColor("#00ff00"));
                    break;
                case '2':
                    tvlt.setBackgroundColor(Color.parseColor("#ff0000"));
                    tvld.setBackgroundColor(Color.parseColor("#ff0000"));
                    break;
            }
            switch (feedback.charAt(3)){
                case '0':
                    tvrt.setBackgroundColor(Color.parseColor("#000000"));
                    tvrd.setBackgroundColor(Color.parseColor("#000000"));
                    break;
                case '1':
                    tvrt.setBackgroundColor(Color.parseColor("#00ff00"));
                    tvrd.setBackgroundColor(Color.parseColor("#00ff00"));
                    break;
                case '2':
                    tvrt.setBackgroundColor(Color.parseColor("#ff0000"));
                    tvrd.setBackgroundColor(Color.parseColor("#ff0000"));
                    break;
            }
            btVoltage.setText("X data");

        }else if(feedback.charAt(1)=='y'){
            online.setBackgroundColor(Color.parseColor("#00ff00"));
            setPing();
            btVoltage.setText("Y data");
        }

    }
    public void setPing()
    {
        Date date1=new Date();
        long delay=date1.getTime()-initMilli;
        tvPing.setText(delay+" ms");

    }

}
