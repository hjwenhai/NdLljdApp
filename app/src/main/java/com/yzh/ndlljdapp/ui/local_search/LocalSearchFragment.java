package com.yzh.ndlljdapp.ui.local_search;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.databinding.FragmentLocalSearchBinding;
import com.yzh.ndlljdapp.recyleviewitem.CalinforAdpter;
import com.yzh.ndlljdapp.recyleviewitem.LocalSearchAdapter;
import com.yzh.ndlljdapp.util.usbHelper.ExportFileToUsb;

import java.util.Calendar;
import java.util.Date;

public class LocalSearchFragment extends Fragment {

    private FragmentLocalSearchBinding binding;
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
        LocalSearchModel localSearchModel =
                new ViewModelProvider(this).get(LocalSearchModel.class);

        binding = FragmentLocalSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        localSearchModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //获取页面控件
        final EditText devNoEt=binding.localSearchDevno;
        final TextView startDateTv=binding.localSearchStartDateTimeTv;
        final TextView endDateTv=binding.localSearchEndDateTimeTv;
        final EditText customerEt=binding.localSearchCustomer;
        final EditText manufacturerEt=binding.localSearchManufacturer;
        final ImageButton clearDateImBtn=binding.localSearchClearDateTime;
        final ImageButton searchImBtn=binding.localSearchSearchBtn;
        final RecyclerView searchRecycleview = binding.localSearchRecycleview;

        //viewmodel 数据绑定控件
        localSearchModel.getSearchDevNo().observe(getViewLifecycleOwner(),devNoEt::setText);
        //搜索时间绑定
        localSearchModel.getSeartchStartDate().observe(getViewLifecycleOwner(),date -> {
            if (date != null)
                startDateTv.setText("起始时间："+ Constants.DATE_FORMAT.format(date));
            else
                startDateTv.setText("起始时间：空");
        });
        localSearchModel.getSeartchEndDate().observe(getViewLifecycleOwner(),date -> {
            if (date != null)
                endDateTv.setText("结束时间："+Constants.DATE_FORMAT.format(date));
            else
                endDateTv.setText("结束时间：空");
        });
        Date startDate=localSearchModel.getSeartchStartDate().getValue();
        Date endDate=localSearchModel.getSeartchEndDate().getValue();
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
                        localSearchModel.getSeartchStartDate().setValue(startCalendar.getTime());
                    }
                },startCalendar.get(Calendar.HOUR_OF_DAY),startCalendar.get(Calendar.MINUTE),true);
                tpg.show();
            }
        };

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
                        localSearchModel.getSeartchEndDate().setValue(endCalendar.getTime());
                    }
                },endCalendar.get(Calendar.HOUR_OF_DAY),endCalendar.get(Calendar.MINUTE),true);
                tpg.show();
            }
        };

        //起始时间设置
        endDateTv.setOnClickListener(v -> {
            DatePickerDialog dpd=new DatePickerDialog(getContext(),onEndDateSetListener,endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH),endCalendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });
        //清除时间按钮
        clearDateImBtn.setOnClickListener(v -> {
            localSearchModel.getSeartchStartDate().setValue(null);
            localSearchModel.getSeartchEndDate().setValue(null);
        });
        //Recycleview初始化
        searchRecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        final LocalSearchAdapter localSearchAdapter = new LocalSearchAdapter(localSearchModel.getFlowDocs(),exportFileToUsb);
        searchRecycleview.setAdapter(localSearchAdapter);
        //搜索按钮监听
        searchImBtn.setOnClickListener(v->{
            Date sdt=localSearchModel.getSeartchStartDate().getValue();
            Date edt=localSearchModel.getSeartchEndDate().getValue();
            //搜索项检查
            String devNo=devNoEt.getText().toString().trim();
            String customer=customerEt.getText().toString().trim();
            String manufacturer=manufacturerEt.getText().toString().trim();
            if(devNo.length()==0 && (sdt==null || edt==null) && customer.length()==0 && manufacturer.length()==0){
                Toast.makeText(getContext(),"至少有一项不能为空！",Toast.LENGTH_SHORT).show();
                return;
            }
            if(sdt!=null && edt!=null && edt.before(sdt)){
                Toast.makeText(getContext(),"起始时间要早于结束时间!",Toast.LENGTH_SHORT).show();
                return;
            }
            localSearchModel.getSearchDevNo().setValue(devNo);
            localSearchModel.getCustomer().setValue(customer);
            localSearchModel.getManufactuer().setValue(manufacturer);
            localSearchModel.searchFlowDocs(localSearchAdapter);
        });

//       root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//           @Override
//           public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//               if(oldBottom!=0 && bottom!=0 && bottom-oldBottom>0){
//                   localSearchAdapter.notifyDataSetChanged();
//               }
//           }
//       });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}