package com.yzh.ndlljdapp;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.UsbFile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.yzh.ndlljdapp.databinding.ActivityMainBinding;
import com.yzh.ndlljdapp.util.usbHelper.ExportFileToUsb;
import com.yzh.ndlljdapp.util.usbHelper.USBBroadCastReceiver;
import com.yzh.ndlljdapp.util.usbHelper.UsbHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * 实现UsbListener接口，ExportFileToUsb接口。
 */
public class MainActivity extends AppCompatActivity  implements USBBroadCastReceiver.UsbListener, ExportFileToUsb {

    private ActivityMainBinding binding;
    //USB文件列表相关
    private ArrayList<UsbFile> usbList;
    private UsbHelper usbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //如果主题中有ActionBar，可通过下面语句隐藏ActionBar
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_controller_new, R.id.navigation_controller_search, R.id.navigation_local_search,R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //下面这条语句是将底部导航栏切换时，actionbar变化标题，如果有这条语句，主题中设置不能设置无标题。
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        initUsbFile();

    }

    /**
     * 初始化 USB文件列表
     */
    private void initUsbFile() {
        usbHelper = new UsbHelper(this, this);
        usbList = new ArrayList<>();
        updateUsbFile(0);
    }
    /**
     * 更新 USB 文件列表
     */
    private void updateUsbFile(int position) {
        UsbMassStorageDevice[] usbMassStorageDevices = usbHelper.getDeviceList();
        Log.e("updateUsbFile","usbMassStorageDevices.length:"+usbMassStorageDevices.length);
        if (usbMassStorageDevices.length > 0) {
            //存在USB
            usbList.clear();
            usbList.addAll(usbHelper.readDevice(usbMassStorageDevices[position]));
        } else {
//            Log.e("MainActivity", "No Usb Device");
            usbList.clear();
        }
    }

    /**
     * ExportFileToUsb接口实现
     * 将本地文件复制到USB中
     * @param file 本地文件
     */
    @Override
    public void copyLocalFileToUsb(final File file) {
        //复制到本地的文件路径
        new Thread(new Runnable() {
            @Override
            public void run() {
                //这儿还需更新USB File,在插入U盘获取权限后,因为平板有个USB浏览器，可能USB文件会发生变化
                updateUsbFile(0);
                //复制结果
                if(usbList.size()==0 || usbHelper.getCurrentFolder()==null){
//                    Log.e("copyLocalFileToUsb","currentFolder is Null");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"请插入U盘",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                final boolean result = usbHelper.saveSDFileToUsb(file, usbHelper.getCurrentFolder(), new UsbHelper.DownloadProgressListener() {
                    @Override
                    public void downloadProgress(final int progress) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String text = "From Local : " + file.getName()
                                        + "\nTo Usb : " + usbHelper.getCurrentFolder().getName()
                                        + "\nProgress : " + progress;
//                                showProgressTv.setText(text);
                            }
                        });
                    }
                });
                //主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result) {
//                            openUsbFile(usbHelper.getCurrentFolder());
                            Toast.makeText(MainActivity.this, "导出成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "导出失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    //下面四个方法是UsbListener的实现
    @Override
    public void insertUsb(UsbDevice device_add) {
        //获取usb files,会获取USB权限。
        if (usbList.size() == 0) {
            updateUsbFile(0);
        }
        Toast.makeText(MainActivity.this,"U盘已插入",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeUsb(UsbDevice device_remove) {
        updateUsbFile(0);
        Toast.makeText(MainActivity.this,"U盘已移除",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getReadUsbPermission(UsbDevice usbDevice) {
    }

    @Override
    public void failedReadUsb(UsbDevice usbDevice) {

    }


    @Override
    protected void onDestroy() {
        usbHelper.finishUsbHelper();
        super.onDestroy();
    }
}