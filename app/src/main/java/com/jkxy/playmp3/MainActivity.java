package com.jkxy.playmp3;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay;
    private Button btnStop;
    private Button btnPlayIntent;
    private Button btnPlayWithMediaPlay;
    private MediaPlayer mediaPlayer;

    private String fileName = "xiaowugui.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnPlayIntent = (Button) findViewById(R.id.btnPlayIntent);
        btnPlayWithMediaPlay = (Button) findViewById(R.id.btnPlayWithMediaPlay);

        btnPlay.setOnClickListener(this);
        btnPlayIntent.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPlayWithMediaPlay.setOnClickListener(this);
        if (!isFileExist(fileName)) {
            copyToMobile(fileName);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlay:
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.xiaowugui);
                    mediaPlayer.start();
                }
                break;
            case R.id.btnStop:
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case R.id.btnPlayIntent:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + getDir() + fileName), "audio/mp3");
                startActivity(intent);
                break;

            case R.id.btnPlayWithMediaPlay:
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(getDir()+fileName);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private String getDir(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/myplayer/";
    }

    private boolean isFileExist(String fileName) {
        boolean isExist=false;
        File file = new File(getDir() + fileName);
        if (file.exists()) {
            isExist=true;
        }
        return isExist;
    }

    private void copyToMobile(final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dir = new File(getDir());
                if (!dir.exists()) {
                    dir.mkdir();
                }

                InputStream fis = null;
                OutputStream fos = null;

                fis = getResources().openRawResource(R.raw.xiaowugui);

                File to = new File(getDir(), fileName);

                try {
                    fos = new FileOutputStream(to);
                    byte[] buf = new byte[4096];
                    while (true) {
                        int r = fis.read(buf);
                        if (r == -1) {
                            break;
                        }
                        fos.write(buf);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (fos != null) {
                        try {
                            fos.close();
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();
    }
}
