package com.yzh.ndlljdapp.ui.controller_new;

import com.yzh.ndlljdapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yzh.ndlljdapp.databinding.CalinforItemViewBinding;
import com.yzh.ndlljdapp.databinding.FragmentControllerNewBinding;
import com.yzh.ndlljdapp.entity.CalInfor;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControllerNewFragment extends Fragment {

    private FragmentControllerNewBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ControllerNewViewModel controllerNewViewModel =
                new ViewModelProvider(this).get(ControllerNewViewModel.class);

        binding = FragmentControllerNewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        controllerNewViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //获取控制器最新数据按钮
        Button btnGetNestData = binding.btnGetNestData;
        btnGetNestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerNewViewModel.getNewestFlowDataFromController();
            }
        });

        DateFormat format=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        CalinforItemViewBinding calInforItemView = binding.calInforItemView;
        TextView calInforDevNoContent = calInforItemView.calInforDevNoContent;
        TextView calInforDateContent = calInforItemView.calInforDateContent;
        TextView calInforTypeContent = calInforItemView.calInforTypeContent;
        TextView calInforAccuracyContent = calInforItemView.calInforAccuracyContent;
        TextView calInforUncertaintyContent = calInforItemView.calInforUncertaintyContent;

        //calinfor监听
        controllerNewViewModel.getCalInfor().observe(getViewLifecycleOwner(), new Observer<CalInfor>() {
            @Override
            public void onChanged(CalInfor calInfor) {
                View calInforItemView = container.findViewById(R.id.calInfor_item_view);
                if(calInfor==null){
                    //隐藏
                    calInforItemView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    //显示
                    calInforItemView.setVisibility(View.VISIBLE);
                    calInforDevNoContent.setText(calInfor.getDevNo());
                    calInforDateContent.setText(format.format(new Date(calInfor.getDate())));
                    calInforTypeContent.setText(calInfor.getType()==0?"质量":"体积");
                    calInforAccuracyContent.setText(decimalFormat.format(calInfor.getAccuracy()));
                    calInforUncertaintyContent.setText(decimalFormat.format(calInfor.getUncertainty()));
                    //查询本地数据库是否有该记录

                }
            }
        });

        //生成检定表
        Button btnGetVerificationTable = calInforItemView.btnGetVerificationTable;
        btnGetVerificationTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerNewViewModel.getCalDatasFromController();
            }
        });

        //自动显示隐藏生成检定表按钮
        controllerNewViewModel.getIsInLocalDataBase().observe(getViewLifecycleOwner(),
                newValue->{
            if(newValue)
                btnGetVerificationTable.setVisibility(View.INVISIBLE);
            else
                btnGetVerificationTable.setVisibility(View.VISIBLE);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}