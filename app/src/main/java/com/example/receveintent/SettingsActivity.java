package com.example.receveintent;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
 String headlinesStatus="",sportsStatus="",studentStatus="",technologyStatus="";
    Switch headlinesSw,technologySw,sportsSw,studentSw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        RelativeLayout relativeLayout=findViewById(R.id.relativelayoutstg);
        Button button=findViewById(R.id.submitTopic);
        headlinesSw = (Switch) findViewById(R.id.switch1);
        technologySw = (Switch) findViewById(R.id.switch2);
        sportsSw = (Switch) findViewById(R.id.switch3);
        studentSw = (Switch) findViewById(R.id.switch4);
        //currentTopic=new Preference(this).getCurrentTopic();
        headlinesStatus=new Preference(this).getHeadlinesPreference();
        technologyStatus=new Preference(this).getTechnologyPreference();
        sportsStatus=new Preference(this).getSportsPreference();
        studentStatus=new Preference(this).getStudentPreference();
        enableSelectedSwitches(headlinesStatus,technologyStatus,studentStatus,sportsStatus);


       //enableSelectedSwitch(currentTopic);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(sportsSw.getTextOff().equals("OFF")&&studentSw.getTextOff().equals("OFF")&&technologySw.getTextOff().equals("OFF")&&headlinesSw.getTextOff().equals("OFF"))
               {
                   Toast.makeText(SettingsActivity.this,"Please select any one topic otherwise default will be saved",Toast.LENGTH_SHORT).show();
                   headlinesSw.setTextOff("ON");
                   headlinesStatus="HeadLines";
                   headlinesSw.setChecked(true);
                   headlinesSw.setTextColor(Color.parseColor("#FF03DAC5"));
                   new Preference(getApplicationContext()).writeSharedPreferenceAsHeadLines(headlinesStatus);
                   new MyFirebaseMessageInit().initProcess(getApplicationContext(),headlinesStatus);


                   //enableSelectedSwitch(currentTopic);
               }
               else {
                   if(studentSw.getTextOff().equals("ON"))
                   {
                       new MyFirebaseMessageInit().initProcess(getApplicationContext(),studentStatus);
                   }
                   if(sportsSw.getTextOff().equals("ON"))
                   {
                       new MyFirebaseMessageInit().initProcess(getApplicationContext(),sportsStatus);
                   }
                   if(technologySw.getTextOff().equals("ON"))
                   {
                       new MyFirebaseMessageInit().initProcess(getApplicationContext(),technologyStatus);
                   }
                   if(headlinesSw.getTextOff().equals("ON"))
                   {
                       new MyFirebaseMessageInit().initProcess(getApplicationContext(),headlinesStatus);
                   }
                   Toast.makeText(getApplicationContext(),"Successfully saved your changes",Toast.LENGTH_SHORT).show();
               }
           }
       });
        sportsSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    sportsStatus="Sports";
                    sportsSw.setChecked(true);
                    sportsSw.setTextOff("ON");
                    new Preference(getApplicationContext()).writeSharedPreferenceAsSports(sportsStatus);
                    sportsSw.setTextColor(Color.parseColor("#FF03DAC5"));


                }
                else
                {
                    sportsSw.setChecked(false);
                    sportsStatus="";
                    sportsSw.setTextOff("OFF");
                    new Preference(getApplicationContext()).writeSharedPreferenceAsSports(sportsStatus);
                    sportsSw.setTextColor(Color.parseColor("#090909"));
                }
            }
        });


        headlinesSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    headlinesStatus="HeadLines";
                    headlinesSw.setChecked(true);
                    headlinesSw.setTextOff("ON");
                    headlinesSw.setTextColor(Color.parseColor("#FF03DAC5"));
                    new Preference(getApplicationContext()).writeSharedPreferenceAsHeadLines(headlinesStatus);


                }
                else
                {
                    headlinesStatus="";
                    headlinesSw.setChecked(false);
                    headlinesSw.setTextOff("OFF");
                    new Preference(getApplicationContext()).writeSharedPreferenceAsHeadLines(headlinesStatus);
                    headlinesSw.setTextColor(Color.parseColor("#090909"));

                }
            }
        });
        technologySw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    technologyStatus="Technology";
                    technologySw.setChecked(true);
                    technologySw.setTextOff("ON");
                    technologySw.setTextColor(Color.parseColor("#FF03DAC5"));
                    new Preference(getApplicationContext()).writeSharedPreferenceAsTechnology(technologyStatus);

                }
                else
                {
                    technologyStatus="";
                    technologySw.setChecked(false);
                    technologySw.setTextOff("OFF");
                    new Preference(getApplicationContext()).writeSharedPreferenceAsTechnology(technologyStatus);
                    technologySw.setTextColor(Color.parseColor("#090909"));


                }
            }
        });
        studentSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    studentStatus="Students";
                    studentSw.setChecked(true);
                    studentSw.setTextOff("ON");
                    studentSw.setTextColor(Color.parseColor("#FF03DAC5"));
                    new Preference(getApplicationContext()).writeSharedPreferenceAsStudents(studentStatus);

                }
                else
                {
                    studentStatus="";
                    studentSw.setChecked(false);
                    studentSw.setTextOff("OFF");
                    new Preference(getApplicationContext()).writeSharedPreferenceAsStudents(studentStatus);
                    studentSw.setTextColor(Color.parseColor("#090909"));
                }
            }
        });




    }

    private void enableSelectedSwitches(String headlinesStatus, String technologyStatus, String studentStatus, String sportsStatus) {

        if(headlinesStatus.equals("HeadLines"))
        {
            headlinesSw.setChecked(true);
            headlinesSw.setTextOff("ON");
            headlinesSw.setTextColor(Color.parseColor("#FF03DAC5"));
        }
        if(technologyStatus.equals("Technology"))
        {
            technologySw.setChecked(true);
            technologySw.setTextOff("ON");
            technologySw.setTextColor(Color.parseColor("#FF03DAC5"));
        }
        if(sportsStatus.equals("Sports"))
        {
            sportsSw.setChecked(true);
            sportsSw.setTextOff("ON");
            sportsSw.setTextColor(Color.parseColor("#FF03DAC5"));
        }
        if(studentStatus.equals("Students"))
        {
            studentSw.setTextOff("ON");
            studentSw.setChecked(true);
            studentSw.setTextColor(Color.parseColor("#FF03DAC5"));
        }
    }

    public void enableSelectedSwitch(String topic)
    {
        if(topic.equals("HeadLines"))
        {
            headlinesSw.setChecked(true);
        }
        else
        {
            if(topic.equals("Technology"))
            {
                technologySw.setChecked(true);
            }
            else
            {
                if(topic.equals("Sports"))
                {
                    sportsSw.setChecked(true);
                }
                else {
                    studentSw.setChecked(true);
                }
            }
        }
    }
}
