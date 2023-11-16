package com.yzh.ndlljdapp.ui.controller_search;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.databinding.FragmentControllerSearchBinding;
import com.yzh.ndlljdapp.entity.CalInfor;
import com.yzh.ndlljdapp.recyleviewitem.CalInforItem;
import com.yzh.ndlljdapp.recyleviewitem.CalinforAdpter;
import com.yzh.ndlljdapp.util.usbHelper.ExportFileToUsb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class ControllerSearchFragment extends Fragment {

    private FragmentControllerSearchBinding binding;
    //导出UsbFile接口，在MainActivity 中实现
    private ExportFileToUsb exportFileToUsb;
    /**
     * 当fragment加载到MainActivity时获取MainActivity
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            exportFileToUsb=(ExportFileToUsb) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity 必须实现ExportFileToUsb接口");
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ControllerSearchViewModel controllerSearchViewModel =
                new ViewModelProvider(this).get(ControllerSearchViewModel.class);

        binding = FragmentControllerSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textDashboard;
        controllerSearchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //获取页面控件
        final EditText devNoEt=binding.controllerSearchDevno;
//        final Button startDateBtn=binding.controllerSearchStartDateTimeBtn;
//        final Button endDateBtn=binding.controllerSearchEndDateTime;
        final TextView startDateTv=binding.controllerSearchStartDateTimeTv;
        final  TextView endDateTv=binding.controllerSearchEndDateTimeTv;
        final ImageButton clearDateImBtn=binding.controllerSearchClearDateTime;
        final ImageButton searchImBtn=binding.controllerSearchSearchBtn;
        final RecyclerView searchRecycleview = binding.controllerSearchRecycleview;
        //viewmodel 数据绑定控件
        controllerSearchViewModel.getSearchDevNo().observe(getViewLifecycleOwner(),devNoEt::setText);
        //搜索时间绑定
        controllerSearchViewModel.getSeartchStartDate().observe(getViewLifecycleOwner(),date -> {
            if (date != null)
                startDateTv.setText("起始时间："+ Constants.DATE_FORMAT.format(date));
            else
                startDateTv.setText("起始时间：空");
        });
        controllerSearchViewModel.getSeartchEndDate().observe(getViewLifecycleOwner(),date -> {
            if (date != null)
                endDateTv.setText("结束时间："+Constants.DATE_FORMAT.format(date));
            else
                endDateTv.setText("结束时间：空");
        });
        Date startDate=controllerSearchViewModel.getSeartchStartDate().getValue();
        Date endDate=controllerSearchViewModel.getSeartchEndDate().getValue();
        Calendar startCalendar=Calendar.getInstance();
        if (startDate != null)
            startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        if (endDate != null)
            endCalendar.setTime(endDate);
        //起始日期监听器
        final DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCalendar.set(year,month,dayOfMonth);
                //弹出时间选择框
                TimePickerDialog tpg=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        startCalendar.set(Calendar.MINUTE,minute);
                        controllerSearchViewModel.getSeartchStartDate().setValue(startCalendar.getTime());
                    }
                },startCalendar.get(Calendar.HOUR_OF_DAY),startCalendar.get(Calendar.MINUTE),true);
                tpg.show();
            }
        };
        //起始时间设置按钮
/*        startDateBtn.setOnClickListener(v -> {
            DatePickerDialog dpd=new DatePickerDialog(getContext(),onStartDateSetListener,startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });*/
        //起始时间设置
        startDateTv.setOnClickListener(v -> {
            DatePickerDialog dpd=new DatePickerDialog(getContext(),onStartDateSetListener,startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        //停止日期监听器
        final DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endCalendar.set(year,month,dayOfMonth);
                //弹出时间选择框
                TimePickerDialog tpg=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        endCalendar.set(Calendar.MINUTE,minute);
                        controllerSearchViewModel.getSeartchEndDate().setValue(endCalendar.getTime());
                    }
                },endCalendar.get(Calendar.HOUR_OF_DAY),endCalendar.get(Calendar.MINUTE),true);
                tpg.show();
            }
        };
        //起始时间设置按钮
/*        endDateBtn.setOnClickListener(v -> {
            DatePickerDialog dpd=new DatePickerDialog(getContext(),onEndDateSetListener,endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        }); */
        //起始时间设置
        endDateTv.setOnClickListener(v -> {
            DatePickerDialog dpd=new DatePickerDialog(getContext(),onEndDateSetListener,endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });
        //清除时间按钮
        clearDateImBtn.setOnClickListener(v -> {
            controllerSearchViewModel.getSeartchStartDate().setValue(null);
            controllerSearchViewModel.getSeartchEndDate().setValue(null);
        });
        //Recycleview初始化
        searchRecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        final CalinforAdpter calinforAdpter = new CalinforAdpter(controllerSearchViewModel.getCalInfortItems().getValue(),controllerSearchViewModel,exportFileToUsb);
        searchRecycleview.setAdapter(calinforAdpter);
        //搜索按钮监听
        searchImBtn.setOnClickListener(v->{
            Date sdt=controllerSearchViewModel.getSeartchStartDate().getValue();
            Date edt=controllerSearchViewModel.getSeartchEndDate().getValue();
            //搜索项检查
            if(devNoEt.getText().toString().trim().length()==0 && (sdt==null || edt==null)){
                Toast.makeText(getContext(),"出厂编号与时间搜索选项至少有一项不能为空！",Toast.LENGTH_SHORT).show();
                return;
            }
            if(sdt!=null && edt!=null && edt.before(sdt)){
                Toast.makeText(getContext(),"起始时间要早于结束时间!",Toast.LENGTH_SHORT).show();
                return;
            }
            String devNo=devNoEt.getText().toString().trim();
            controllerSearchViewModel.getSearchDevNo().setValue(devNo);
            controllerSearchViewModel.searchCalinforsFromController(calinforAdpter);


        });

        //搜索结果变化
//        controllerSearchViewModel.getCalInfortItems().observe(getViewLifecycleOwner(), new Observer<List<CalInforItem>>() {
//            @Override
//            public void onChanged(List<CalInforItem> calInforItems) {
//                Toast.makeText(getContext(),"calinforites change",Toast.LENGTH_SHORT).show();
//                calinforAdpter.notifyDataSetChanged();
//            }
//        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}