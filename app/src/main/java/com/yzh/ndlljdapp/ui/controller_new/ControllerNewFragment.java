package com.yzh.ndlljdapp.ui.controller_new;

import com.yzh.ndlljdapp.R;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.config.ValidationTableType;
import com.yzh.ndlljdapp.databinding.CalinforItemViewBinding;
import com.yzh.ndlljdapp.databinding.FragmentControllerNewBinding;
import com.yzh.ndlljdapp.entity.CalData;
import com.yzh.ndlljdapp.entity.CalInfor;
import com.yzh.ndlljdapp.entity.FlowDoc;
import com.yzh.ndlljdapp.entity.ValidationTableAuxMessage;
import com.yzh.ndlljdapp.model.FlowDocModel;
import com.yzh.ndlljdapp.util.TemplateToWord;
import com.yzh.ndlljdapp.util.WordResultCallBack;
import com.yzh.ndlljdapp.util.WpsUtil;
import com.yzh.ndlljdapp.util.usbHelper.ExportFileToUsb;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ControllerNewFragment extends Fragment {

    private FragmentControllerNewBinding binding;
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
        final Spinner auxTypeSpinner = calInforItemView.auxTypeSpinner; //生成列表选择框

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
                    controllerNewViewModel.queryFlowDoc(getContext());
                    //根据流量计类型,表格选项选择不同的报表选项
                    String[] rType;
                    if (calInfor.getType() == 0) {
                        rType = Constants.QUALITY_ARRAY;
                    } else {
                        rType = Constants.VOLUME_ARRAY;
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,rType);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    auxTypeSpinner.setAdapter(adapter);
                    //默认选择第一个
                }
            }
        });

        //生成检定表按钮
        Button btnGetVerificationTable = calInforItemView.btnGetVerificationTable;
        //打开检定表按钮
        Button  btnCalinforOpenTable= calInforItemView.btnCalinforOpenTable;
        //导出检定表按钮
        Button  btnCalinforExportTable= calInforItemView.btnExportTable;
        //打开，导出检定表按钮布局名称,可隐藏
        LinearLayout calinforOpenExportLayout = calInforItemView.calinforOpenExportLayout;
        //检定表名称
        TextView calInforFilepathContent = calInforItemView.calInforFilepathContent;
        //辅助信息布局可隐藏 客户名称 制造厂家
        LinearLayout auxLayout = calInforItemView.auxLayout;
        //文件名称布局，可隐藏。
        LinearLayout calInforFilepathLayout = calInforItemView.calInforFilepathLayout;
         final boolean[] btnGetVerificationTableClicked = {false}; //当生成检定表按钮按下时，此位置1. 防止页面切换时，检定数据添加了监听，数据变化会生成word

        btnGetVerificationTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从控制器获取检定数据
                controllerNewViewModel.getCalDatasFromController();
                btnGetVerificationTableClicked[0] =true;
            }
        });
        //打开检定表按钮
        btnCalinforOpenTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calInforFilepathContent.getText()!=null && calInforFilepathContent.getText().length()>0)// 打开文档
                {
                   WpsUtil.openFile(v.getContext(),new File(Constants.OUT_WORD_PATH + "/"+calInforFilepathContent.getText()));
                }
            }
        });
        //导出检定表按钮
        btnCalinforExportTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calInforFilepathContent.getText()!=null && calInforFilepathContent.getText().length()>0)// 导出文档
                {
                    if(exportFileToUsb!=null){
                        //要复制本地的文件路径
                        final File file = new File(Constants.OUT_WORD_PATH + "/" + calInforFilepathContent.getText().toString());
                        exportFileToUsb.copyLocalFileToUsb(file);
                    }
                }
            }
        });
        //检定数据添加监听，获取到新的检定数据，生成word 文档
        LiveData<List<CalData>> calDatas = controllerNewViewModel.getCalDatas();
        calDatas.observe(getViewLifecycleOwner(), new Observer<List<CalData>>() {
            @Override
            public void onChanged(List<CalData> calDataList) {

                //按钮按下后生成检定数据
                if(btnGetVerificationTableClicked[0]){
                    btnGetVerificationTableClicked[0]=false;
                    //有检定数据，且在本地数据库中没有记录
                    if(calDataList!=null && calDataList.size()>0 && controllerNewViewModel.getFlowDoc()!=null  && controllerNewViewModel.getFlowDoc().getValue()==null){
                        Log.d("检定数据变化",calDatas.toString());
                        //检定表模板必须选择
                        if(calInforItemView.auxTypeSpinner.getSelectedItemPosition()==-1) {
                            Toast.makeText(getContext(),R.string.toast_tableTemplateName,Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //获取辅助信息
                        String customerContent = calInforItemView.auxCustomerContent.getText().toString().trim();
                        String manufacturerContent = calInforItemView.auxManufacturerContent.getText().toString().trim();
                        if(customerContent.length()==0 || manufacturerContent.length()==0){
                            Toast.makeText(getContext(),R.string.toast_customer_manufacturer,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        controllerNewViewModel.setText("正在生成word文档!");
                        CalInfor calInfor = controllerNewViewModel.getCalInfor().getValue();
                        ValidationTableAuxMessage auxMessage = new ValidationTableAuxMessage(customerContent, manufacturerContent);
                        HashMap<String, String> insetTextMap = TemplateToWord.getInsetTextMap(calInfor, calDataList, auxMessage);
                        //检定表模板
                        ValidationTableType tableType=ValidationTableType.QUALITY1;
                        if(((String) auxTypeSpinner.getSelectedItem()).equals(Constants.VOLUME_ARRAY[0]))
                            tableType=ValidationTableType.VOLUME1;
                        if(((String) auxTypeSpinner.getSelectedItem()).equals(Constants.QUALITY_ARRAY[0]))
                            tableType=ValidationTableType.QUALITY1;
                        TemplateToWord.generateWord(getContext(), insetTextMap, tableType, new WordResultCallBack() {
                            @Override
                            public void onError(Exception e) {
                                Log.e("生成word文档错误",e.toString());
                                controllerNewViewModel.setText("生成word文档错误!");
                            }

                            @Override
                            public void onSuccess(String filePath) {
                                Log.d("生成word文档成功","文件名："+filePath);
                                controllerNewViewModel.setText("生成word文档成功!");
                                //设置文件名称
//                                calInforFilepathContent.setText(filePath);
//                                controllerNewViewModel.setIsInLocalDataBase(true);
                                //插入本地数据库
                                FlowDocModel flowDocModel=new FlowDocModel(getContext());
                                //获取检定表模板名称
                                String tableTemplateName=(String) auxTypeSpinner.getSelectedItem();
                                FlowDoc flowDoc=new FlowDoc(calInfor.getDevNo(),calInfor.getDate(),tableTemplateName,auxMessage.getCustomer(),auxMessage.getManufactor(),filePath);
                                Handler handler=new Handler(Looper.getMainLooper()){
                                    @Override
                                    public void handleMessage(@NonNull Message msg) {
                                        super.handleMessage(msg);
                                        if (msg.what == Constants.INSERT_FLOW_DOC) {
                                            long id=(long)msg.obj;
                                            flowDoc.setId(id);
                                            controllerNewViewModel.setFlowDoc(flowDoc);
                                            Log.d("数据库插入","插入本地数据库记录成功");
                                        }
                                    }
                                };
                                flowDocModel.insertFlowDoc(flowDoc,handler);
                            }
                        });
                    }
                }
            }
        });


        //flowDoc 添加监听
        controllerNewViewModel.getFlowDoc().observe(getViewLifecycleOwner(), new Observer<FlowDoc>() {
            @Override
            public void onChanged(FlowDoc flowDoc) {
                if(flowDoc!=null){
                    calInforFilepathContent.setText(flowDoc.getFileName());   //文件名
                    calInforItemView.auxCustomerContent.setText(flowDoc.getCustomer());  //客户名称
                    calInforItemView.auxManufacturerContent.setText(flowDoc.getManufactor());  //厂家名称
                    //设定检定记录模板名称
                    for (int i = 0; i < Constants.VOLUME_ARRAY.length; i++) {
                        if(Constants.VOLUME_ARRAY[i].equals(flowDoc.getTableName()))
                            calInforItemView.auxTypeSpinner.setSelection(i);
                    }
                    for (int i = 0; i < Constants.QUALITY_ARRAY.length; i++) {
                        if(Constants.QUALITY_ARRAY[i].equals(flowDoc.getTableName()))
                            calInforItemView.auxTypeSpinner.setSelection(i);
                    }
                    calInforItemView.auxTypeSpinner.setEnabled(false);//禁止检定记录模板编辑
                    calInforItemView.auxCustomerContent.setEnabled(false); //禁止客户名称编辑
                    calInforItemView.auxManufacturerContent.setEnabled(false);//禁止厂家名称编辑
                    btnGetVerificationTable.setVisibility(View.GONE);
//                    btnCalinforOpenTable.setVisibility(View.VISIBLE);
                    calinforOpenExportLayout.setVisibility(View.VISIBLE);
                    calInforFilepathLayout.setVisibility(View.VISIBLE);
                }
                else{
                    calInforItemView.auxTypeSpinner.setEnabled(true);//允许检定记录模板编辑
                    calInforItemView.auxCustomerContent.setEnabled(true); //允许客户名称编辑
                    calInforItemView.auxManufacturerContent.setEnabled(true);//允许厂家名称编辑
                    calInforFilepathContent.setText("");   //文件名
                    calInforItemView.auxCustomerContent.setText("");  //客户名称
                    calInforItemView.auxManufacturerContent.setText("");  //厂家名称
                    btnGetVerificationTable.setVisibility(View.VISIBLE);
//                    btnCalinforOpenTable.setVisibility(View.GONE);
                    calinforOpenExportLayout.setVisibility(View.GONE);
                    calInforFilepathLayout.setVisibility(View.GONE);
//                    auxLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //自动显示隐藏生成检定表按钮、辅助信息（客户名称，制造厂家）,打开检定表按钮
/*        controllerNewViewModel.getIsInLocalDataBase().observe(getViewLifecycleOwner(),
                newValue->{
                    Toast.makeText(getContext(),newValue?"1":"0",Toast.LENGTH_SHORT).show();
            if(newValue){
//                btnGetVerificationTable.setVisibility(View.INVISIBLE);
                btnGetVerificationTable.setVisibility(View.GONE);
                btnCalinforOpenTable.setVisibility(View.VISIBLE);
                calInforFilepathLayout.setVisibility(View.VISIBLE);
                auxLayout.setVisibility(View.GONE);
            }
            else{
                btnGetVerificationTable.setVisibility(View.VISIBLE);
                btnCalinforOpenTable.setVisibility(View.GONE);
                calInforFilepathLayout.setVisibility(View.GONE);
                auxLayout.setVisibility(View.VISIBLE);
            }

        });*/
//        Toast.makeText(getContext(),controllerNewViewModel.getIsInLocalDataBase().getValue()?"隐藏":"显示",Toast.LENGTH_SHORT).show();
        //加载的时候，显示隐藏元素
/*        if(controllerNewViewModel.getIsInLocalDataBase().getValue()){
            btnGetVerificationTable.setVisibility(View.GONE);
            btnCalinforOpenTable.setVisibility(View.VISIBLE);
            calInforFilepathLayout.setVisibility(View.VISIBLE);
            auxLayout.setVisibility(View.GONE);
        }else{
            btnGetVerificationTable.setVisibility(View.VISIBLE);
            btnCalinforOpenTable.setVisibility(View.GONE);
            calInforFilepathLayout.setVisibility(View.GONE);
            auxLayout.setVisibility(View.VISIBLE);
        }*/
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    
}