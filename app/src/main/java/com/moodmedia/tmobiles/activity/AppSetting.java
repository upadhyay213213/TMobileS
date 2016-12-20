package com.moodmedia.tmobiles.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moodmedia.tmobiles.R;

import java.util.ArrayList;

public class AppSetting extends Activity{

    private ImageView back;
    private Button mSupport ;
    private TextView mSupportText;
    private Button mSendLog ;
    private TextView mSendLogTextt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appsettings);
        initializeUI();
    }

    private void initializeUI(){
        back = (ImageView) findViewById(R.id.ivback);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSetting.this.finish();
            }
        });

        mSupport = (Button) findViewById(R.id.sendreportBtn);
        mSupportText = (TextView) findViewById(R.id.sendreportText);
        mSendLog = (Button) findViewById(R.id.sendLogBtN);
        mSendLogTextt = (TextView) findViewById(R.id.sendLogText);

        mSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        mSupportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        mSendLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLog();
            }
        });
        mSendLogTextt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLog();
            }
        });
    }



    private void sendEmail(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"mix@moodmedia.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AppSetting.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLog(){
        String root = Environment.getExternalStorageDirectory().toString();

        String fileConnectExisting = root + "/TMOBILE_SIGNATUTE_LOGS/TmobileS.txt";

        //Intent emailIntent = new Intent(Intent.ACTION_SEND);
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        // set the type to 'email'
        emailIntent.setType("text/plain");
        String to[] = {"profusionapplogs@moodmedia.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        ArrayList<Uri> uris = new ArrayList<Uri>();
        Uri u1 = Uri.parse("file://" + fileConnectExisting);
        uris.add(u1);
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileConnectExisting));
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileSetupNew));
        // the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "TMobileSignature Logs");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }
}
