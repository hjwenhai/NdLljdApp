package com.yzh.ndlljdapp.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yzh.ndlljdapp.MainActivity;
import com.yzh.ndlljdapp.R;
import com.yzh.ndlljdapp.activity.LoginActivity;
import com.yzh.ndlljdapp.config.AppConfig;
import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.databinding.FragmentSettingBinding;
import com.yzh.ndlljdapp.entity.User;
import com.yzh.ndlljdapp.model.UserModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingModel settingModel =
                new ViewModelProvider(this).get(SettingModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textSetting;
//        settingModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //获取页面控件
        final Button btnSettingIp = binding.btnSettingIp;
        final Button btnModifyPassword = binding.btnModifyPassword;
        //控制器ip设置按钮
        btnSettingIp.setOnClickListener(v->{
            final AlertDialog dialog = new AlertDialog.Builder(getContext()).show();
            //设置背景色为透明，解决设置圆角后有白色直角的问题
            Window window=dialog.getWindow();
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            View ipContainer = LayoutInflater.from(getContext()).inflate(R.layout.dialog_setting_ip,null);
            final EditText input = ipContainer.findViewById(R.id.et_setting_ip);
            input.setText(AppConfig.getInstance().getIp());
            Button btnOk = ipContainer.findViewById(R.id.btn_ok_dialog_setting_ip);
            Button btnCancel = ipContainer.findViewById(R.id.btn_cancel_dialog_setting_ip);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //检查IP是否正确
                    //使用正则表达式过滤
                    String ipSet=input.getText().toString();
                    String re = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
                    // 编译正则表达式
                    Pattern pattern = Pattern.compile(re);
                    Matcher matcher = pattern.matcher(ipSet);
                    if(matcher.matches()){
                        AppConfig.getInstance().setIp(ipSet);
                        dialog.dismiss();
                    }
                    else {
                        input.setText("");
                        Toast.makeText(getContext(),"IP格式不正确，请重新设置！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.setContentView(ipContainer);
        });

        //修改密码按钮
        btnModifyPassword.setOnClickListener(v->{
            final User loginUser = AppConfig.getInstance().getLoginUser();
            if(loginUser==null){
                Toast.makeText(getContext(),"用户未登录！",Toast.LENGTH_SHORT).show();
                return;
            }

            final AlertDialog dialog = new AlertDialog.Builder(getContext()).show();
            //设置背景色为透明，解决设置圆角后有白色直角的问题
            Window window=dialog.getWindow();
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            View modifyContainer = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modify_password,null);
            final EditText oldPasswordEt = modifyContainer.findViewById(R.id.et_setting_old_password);
            final EditText newPasswordEt = modifyContainer.findViewById(R.id.et_setting_new_password);
            final EditText confirmPasswordEt = modifyContainer.findViewById(R.id.et_setting_confirm_password);
            Button btnOk = modifyContainer.findViewById(R.id.btn_ok_dialog_setting_modify);
            Button btnCancel = modifyContainer.findViewById(R.id.btn_cancel_dialog_setting_modify);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String oldPassword=oldPasswordEt.getText().toString().trim();
                    final String newPassword = newPasswordEt.getText().toString().trim();
                    final String confirmPassword = confirmPasswordEt.getText().toString().trim();
                    //检查是否为空
                    if (oldPassword.length() == 0) {
                        Toast.makeText(getContext(), "请填写旧密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPassword.length() == 0) {
                        Toast.makeText(getContext(), "请填写新密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (confirmPassword.length() == 0) {
                        Toast.makeText(getContext(), "请填写确认密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //检查旧密码是否填写正确
                    if(!loginUser.getPassword().equals(oldPassword)){
                        Toast.makeText(getContext(), "请填写正确的旧密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //检查新密码与确认密码是否一致
                    if(!newPassword.equals(confirmPassword)){
                        Toast.makeText(getContext(), "新密码与确认密码不一致！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //更新用户
                    loginUser.setPassword(newPassword);
                    final UserModel userModel = new UserModel(getContext());
                    final Handler handler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if (msg.what == Constants.LOGIN_UPDATE_USER) {
                                int reuslt = (int) msg.obj;
                                if (reuslt>0) {
                                    Toast.makeText(getContext(), "修改密码成功 ", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(getContext(), "修改密码失败", Toast.LENGTH_SHORT).show();
                                    loginUser.setPassword(oldPassword);
                                }
                            }
                            dialog.dismiss();
                        }
                    };
                    userModel.updateUser(loginUser,handler);
                }
            });
            dialog.setContentView(modifyContainer);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}