package com.example.day9.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.day9.R;
import com.example.day9.beans.SearchResponseData;
import com.example.day9.utils.PermissionHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {
    ImageView iv_game_icon;
    TextView tv_game_brief, tv_game_intro, tv_game_num, tv_game_time, tv_game_tag, tv_game_version;
    Button bt_game_download;
    RatingBar rb_game_score;
    PermissionHelper helper;
    List<String> permissions;
    private SearchResponseData.GameInfoPage.GameInfo gameinfo;

    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initData();
        initView();
        initEvent();
    }

    private void initEvent() {
        bt_game_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadGame();
            }
        });
    }

    private void initData() {
        permissions = new ArrayList<>();
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        helper = new PermissionHelper(this, permissions);
        gameinfo = getIntent().getParcelableExtra("gameinfo");
    }

    private void initView() {
        iv_game_icon = findViewById(R.id.iv_game_icon);
        tv_game_brief = findViewById(R.id.tv_game_brief);
        tv_game_intro = findViewById(R.id.tv_game_intro);
        tv_game_num = findViewById(R.id.tv_game_num);
        tv_game_time = findViewById(R.id.tv_game_time);
        tv_game_tag = findViewById(R.id.tv_game_tag);
        tv_game_version = findViewById(R.id.tv_game_version);
        bt_game_download = findViewById(R.id.bt_game_download);
        rb_game_score = findViewById(R.id.rb_game_score);

        if (gameinfo != null) {
            Glide.with(this)
                    .load(gameinfo.getIcon())
                    .into(iv_game_icon);
            tv_game_brief.setText(gameinfo.getBrief());
            tv_game_intro.setText(gameinfo.getIntroduction());
            tv_game_num.setText(gameinfo.getPlayNumFormat());
            tv_game_time.setText(gameinfo.getCreateTime());
            tv_game_version.setText(gameinfo.getVersionName());
            tv_game_tag.setText(gameinfo.getTags());
            rb_game_score.setRating(gameinfo.getScore().floatValue());
        }
    }

    private void downloadGame() {
        if (helper.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startDownload();
        } else {
            permissions = new ArrayList<>();
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            helper = new PermissionHelper(this, permissions);
            helper.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void startDownload() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(gameinfo.getApkUrl())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    inputStream = response.body().byteStream();
                    File directory = new File(Environment.getExternalStorageDirectory(), "Download");
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    File outputFile = new File(directory, "AppStore.apk");
                    fileOutputStream = new FileOutputStream(outputFile);

                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailActivity.this, "下载完成，保存在 " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else {
                Toast.makeText(this, "需要存储权限来下载文件", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
