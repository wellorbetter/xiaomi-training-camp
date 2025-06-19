package com.example.leanandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.leanandroid.AIDL.AidlInterface1;
import com.example.leanandroid.Activities.Launch.SingleInstance;
import com.example.leanandroid.Activities.Launch.SingleTask;
import com.example.leanandroid.Activities.Launch.SingleTop;
import com.example.leanandroid.Activities.Launch.Standard;
import com.example.leanandroid.BroudCastReceiver.BroadCastReceiver1;
import com.example.leanandroid.R;
import com.example.leanandroid.Service.BindService1;
import com.example.leanandroid.Service.IntentService1;
import com.example.leanandroid.Service.AIDLService1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button bt_0, bt_1, bt_2, bt_3, bt_launch, bt_iv1, bt_bs1, bt_dbs1, bt_aidl, bt_bc1, bt_bc2, bt_cp;
    String TAG = "MainActivity";
    private ServiceConnection serviceConnection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("My Application", "onCreate: MainActivity");
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        bt_0 = findViewById(R.id.bt_0);
        bt_1 = findViewById(R.id.bt_1);
        bt_2 = findViewById(R.id.bt_2);
        bt_3 = findViewById(R.id.bt_3);
        bt_launch = findViewById(R.id.bt_launch);
        bt_iv1 = findViewById(R.id.bt_iv1);
        bt_bs1 = findViewById(R.id.bt_bs1);
        bt_dbs1 = findViewById(R.id.bt_dbs1);
        bt_aidl = findViewById(R.id.bt_aidl);
        bt_bc1 = findViewById(R.id.bt_bc1);
        bt_bc2 = findViewById(R.id.bt_bc2);
        bt_cp = findViewById(R.id.bt_cp);
        bt_cp.setOnClickListener(this);
        bt_bc1.setOnClickListener(this);
        bt_bc2.setOnClickListener(this);
        bt_aidl.setOnClickListener(this);
        bt_dbs1.setOnClickListener(this);
        bt_bs1.setOnClickListener(this);
        bt_iv1.setOnClickListener(this);
        bt_launch.setOnClickListener(this);
        bt_0.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        serviceConnection= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                BindService1.MyBinder iBinder1 = (BindService1.MyBinder) iBinder;
                Toast.makeText(getApplicationContext(), "绑定成功", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), String.valueOf(iBinder1.getCount()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }



    private void getContacts() throws Exception {
        //①查询raw_contacts表获得联系人的id
        ContentResolver resolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //查询联系人数据
        Cursor cursor = resolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        do {
            // 小问题，有可能会为空
            //获取联系人姓名,手机号码
            try {
                @SuppressLint("Range") String cName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String cNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d(TAG, "getContacts: " + cName + cNum);
            } catch (Exception exception) {
                throw new Exception(exception);
            };
        } while (cursor.moveToNext());
        cursor.close();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "申请失败", Toast.LENGTH_SHORT).show();
            } else {
                callPhone();
            }
        } else if (requestCode == 2) {
            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "申请失败", Toast.LENGTH_SHORT).show();
            } else {
                // 申请成功就读
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getContacts();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        }
    }

    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:10086");
        intent.setData(uri);
        startActivity(intent);
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        // Launch：Standard
        if (view.getId() == R.id.bt_0) {
            Intent intent = new Intent(this, Standard.class);
            startActivity(intent);
            // Launch：SingleTask
        } else if (view.getId() == R.id.bt_1) {
            Intent intent = new Intent(this, SingleTask.class);
            startActivity(intent);
            // Launch：SingleTop
        } else if (view.getId() == R.id.bt_2) {
            Intent intent = new Intent(this, SingleTop.class);
            startActivity(intent);
            // Launch：SingleInstance
        } else if (view.getId() == R.id.bt_3) {
            Intent intent = new Intent(this, SingleInstance.class);
            startActivity(intent);
            // Launch：隐式跳转拨号
        } else if (view.getId() == R.id.bt_launch) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "已经授权了");
                callPhone();
            } else {
                Log.d(TAG, "没有授权，正在请求");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 1);
            }
        // 启动启动IntentService1(不绑定)
        } else if (view.getId() == R.id.bt_iv1){
            // 只跳转，不绑定
            Intent intent = new Intent(this, IntentService1.class);
            startService(intent);
            // 绑定BindService1
        } else if (view.getId() == R.id.bt_bs1) {
            Intent intent = new Intent(this, BindService1.class);

            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            // 解绑BindService1
        } else if (view.getId() == R.id.bt_dbs1) {
            unbindService(serviceConnection);
            // AIDL跨进程通信
        } else if (view.getId() == R.id.bt_aidl) {
            Intent intent = new Intent(this, AIDLService1.class);
            // 绑定服务，即客户端实现接口
            bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    // 拿到接口
                    AidlInterface1 aidlInterface1 = AidlInterface1.Stub.asInterface(iBinder);
                    try {
                        // 启动服务
                        Toast.makeText(getApplicationContext(), String.valueOf(aidlInterface1.add(2, 3)), Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {

                }
            },  BIND_AUTO_CREATE);
            // 静态广播 这个是隐式的，静态隐式广播需要setFlag
        } else if (view.getId() == R.id.bt_bc1) {
            Intent intent = new Intent();
            intent.setFlags(0x1000000);
            intent.setAction("mybroadcast");
            sendBroadcast(intent);
            // 动态注册广播
        } else if (view.getId() == R.id.bt_bc2) {
            Intent intent = new Intent();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            this.registerReceiver(new BroadCastReceiver1(), filter);
            // ContentProvider读取联系人数据
        } else if (view.getId() == R.id.bt_cp) {
            // 权限请求
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},2);
                // 没有权限就申请
            }else {
                // 加载contenprovider读取
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getContacts();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }
    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: ");
        super.onRestart();
    }
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: ");
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }
}