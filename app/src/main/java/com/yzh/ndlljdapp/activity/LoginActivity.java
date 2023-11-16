package com.yzh.ndlljdapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yzh.ndlljdapp.MainActivity;
import com.yzh.ndlljdapp.R;
import com.yzh.ndlljdapp.config.AppConfig;
import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.entity.User;
import com.yzh.ndlljdapp.model.UserModel;
import com.yzh.ndlljdapp.util.MyDatabaseHelper;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //如果主题中有ActionBar，可通过下面语句隐藏ActionBar
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText mEtUsername=findViewById(R.id.et_username_act_login);
        EditText mEtPassword=findViewById(R.id.et_password_act_login);
        Button mBtnLogin=findViewById(R.id.btn_login_act_login);

//        Button mBtnRegister=findViewById(R.id.btn_login_act_register);

        //导入数据库到创建的文件中 文件不存在导入
        MyDatabaseHelper myHelper = new MyDatabaseHelper(LoginActivity.this);
        try {
            myHelper.CopyDBFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserModel userModel = new UserModel(getApplicationContext());
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "请输入用户名密码", Toast.LENGTH_SHORT).show();
                }
                else {
                    //查询数据库，比较用户名与密码
                    Handler handler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if (msg.what == Constants.LOGIN_CHECK_USER) {
                                User user = (User) msg.obj;
                                if (user != null){
                                    Toast.makeText(getApplicationContext(), user.getName()+"登录成功 " , Toast.LENGTH_SHORT).show();
                                    AppConfig.getInstance().setLoginUser(user);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "输入的账号密码有误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    userModel.checkUser(username,password,handler);
                }
            }
        });

/*        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册新用户测试
                Handler handler=new Handler(Looper.getMainLooper()){

                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(msg.what==Constants.LOGIN_REGISTER_USER){
                            User user = (User) msg.obj;
                            if (user != null)
                                Toast.makeText(getApplicationContext(), "注册成功 " + user.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                User user = new User("hj", "666666", 1);
                userModel.insertNewUser(user,handler);
            }
        });*/

/*        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改用户密码测试
                Handler handler=new Handler(Looper.getMainLooper()){

                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(msg.what==Constants.LOGIN_UPDATE_USER){
                            int num = (Integer) msg.obj;
                            if (num == 1)
                                Toast.makeText(getApplicationContext(), "修改用户密码成功!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                User user = new User("hj", "888888", 1);
                user.setId(2);
                userModel.updateUser(user,handler);
            }
        }); */

/*        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除用户
                Handler handler=new Handler(Looper.getMainLooper()){

                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(msg.what==Constants.LOGIN_DELETE_USER){
                            int num = (Integer) msg.obj;
                            if (num == 1)
                                Toast.makeText(getApplicationContext(), "删除用户成功!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                User user = new User("hj", "888888", 1);
                user.setId(2);
                userModel.deleteUser(user,handler);
            }
        });*/
    }


}