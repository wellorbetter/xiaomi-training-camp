package com.example.weibo_wudongsheng.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.adapter.ViewPagerAdapter;
import com.example.weibo_wudongsheng.fragment.ImageFragment;
import com.example.weibo_wudongsheng.utils.PermissionHelper;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailItemActivity extends AppCompatActivity {

    ViewPager vp;
    TextView tv_number, tv_nickname;
    Button bt_download;
    List<String> images;
    ViewPagerAdapter viewPagerAdapter;
    List<Fragment> pages;
    ConstraintLayout cl;

    private PermissionHelper permissionHelper;
    private static final int REQUEST_PERMISSION_CODE = 200;
    private boolean permissionGranted = false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        bt_download.setOnClickListener(v -> {
            // 检查权限并下载图片
            if (permissionGranted) {
                downloadImage();
            } else {
                checkAndRequestPermission();
            }
        });

        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tv_number.setText(String.format("%d/%d", position + 1, images.size()));
            }
        });
        cl.setOnClickListener(v -> {
            finish();
        });
    }

    private void initData() {
        Intent intent = getIntent();
        images = intent.getStringArrayListExtra("images");
        String nickname = intent.getStringExtra("nickname");
        int position = intent.getIntExtra("position", 0);

        pages = new ArrayList<>();
        for (String img : images) {
            pages.add(new ImageFragment(img));
        }

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), pages);
        vp.setAdapter(viewPagerAdapter);

        tv_nickname.setText(nickname != null ? nickname : "默认昵称");
        tv_number.setText(String.format("%d/%d", position + 1, images.size()));
        vp.setCurrentItem(position);

        // 初始化 MMKV
        MMKV.initialize(this);
        sharedPreferences = MMKV.defaultMMKV();

        // 恢复权限状态
        permissionGranted = sharedPreferences.getBoolean("permission_granted", false);

        permissionHelper = new PermissionHelper(this,
                new ArrayList<String>() {{
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }});
    }

    private void initView() {
        vp = findViewById(R.id.vp);
        tv_number = findViewById(R.id.tv_number);
        tv_nickname = findViewById(R.id.tv_nickname);
        bt_download = findViewById(R.id.bt_download);
        cl = findViewById(R.id.cl);
    }

    private void checkAndRequestPermission() {
        // 检查权限状态
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }
    }

    private void downloadImage() {
        int currentItem = vp.getCurrentItem();
        String imageUrl = images.get(currentItem);

        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImageToGallery(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void saveImageToGallery(Bitmap bitmap) {
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            String saveMessage = "图片已保存到：" + imageFile.getAbsolutePath();
            Toast.makeText(this, saveMessage, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                // 存储权限状态
                sharedPreferences.edit().putBoolean("permission_granted", true).apply();
                downloadImage();
            } else {
                Toast.makeText(this, "权限被拒绝了，请在设置里面检查权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
