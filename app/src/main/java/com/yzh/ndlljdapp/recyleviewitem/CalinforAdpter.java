package com.yzh.ndlljdapp.recyleviewitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yzh.ndlljdapp.R;
import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.config.ValidationTableType;
import com.yzh.ndlljdapp.entity.CalInfor;
import com.yzh.ndlljdapp.entity.FlowDoc;
import com.yzh.ndlljdapp.entity.ValidationTableAuxMessage;
import com.yzh.ndlljdapp.ui.controller_search.ControllerSearchViewModel;
import com.yzh.ndlljdapp.util.WpsUtil;
import com.yzh.ndlljdapp.util.usbHelper.ExportFileToUsb;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//该类是为控制器数据查询页面的Recycleview提供Adpter
public class CalinforAdpter extends RecyclerView.Adapter<CalInforViewHolder>{
    private final List<CalInforItem> calInforItemList;
    private final ControllerSearchViewModel controllerSearchViewModel;
    private final ExportFileToUsb exportFileToUsb;

    public CalinforAdpter(List<CalInforItem> calInforItemList, ControllerSearchViewModel controllerSearchViewModel, ExportFileToUsb exportFileToUsb) {
        this.calInforItemList = calInforItemList;
        this.controllerSearchViewModel=controllerSearchViewModel;
        this.exportFileToUsb=exportFileToUsb;
    }

    @NonNull
    @Override
    public CalInforViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calinfor_item_view, parent, false);
        return new CalInforViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalInforViewHolder holder, int position) {
        CalInfor calInfor = calInforItemList.get(position).getCalInfor();
        FlowDoc flowDoc=calInforItemList.get(position).getFlowDoc();
        holder.getDevNoTv().setText(calInfor.getDevNo());
        holder.getDateTv().setText(Constants.DATE_FORMAT.format(calInfor.getDate()));
        holder.getTypeTv().setText(calInfor.getType()==0?"质量":"体积");
        holder.getAccuracyTv().setText(Constants.DECIMAL_FORMAT.format(calInfor.getAccuracy()));
        holder.getUncertaintyTv().setText(Constants.DECIMAL_FORMAT.format(calInfor.getUncertainty()));
        holder.getUncertaintyTv().setText(Constants.DECIMAL_FORMAT.format(calInfor.getUncertainty()));
        //根据流量计类型,表格选项选择不同的报表选项
        String[] rType;
        if (calInfor.getType() == 0) {
            rType = Constants.QUALITY_ARRAY;
        } else {
            rType = Constants.VOLUME_ARRAY;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(holder.getTableTypeSp().getContext(), android.R.layout.simple_spinner_item,rType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.getTableTypeSp().setAdapter(adapter);
        if(flowDoc!=null){
            holder.getCustomerTv().setText(flowDoc.getCustomer());
            holder.getManufacturerTv().setText(flowDoc.getManufactor());
            holder.getFilepathTv().setText(flowDoc.getFileName());
            //设定检定记录模板名称
            for (int i = 0; i < Constants.VOLUME_ARRAY.length; i++) {
                if(Constants.VOLUME_ARRAY[i].equals(flowDoc.getTableName()))
                    holder.getTableTypeSp().setSelection(i);
            }
            for (int i = 0; i < Constants.QUALITY_ARRAY.length; i++) {
                if(Constants.QUALITY_ARRAY[i].equals(flowDoc.getTableName()))
                    holder.getTableTypeSp().setSelection(i);
            }
            holder.getTableTypeSp().setEnabled(false);//禁止检定记录模板编辑
            holder.getCustomerTv().setEnabled(false); //禁止客户名称编辑
            holder.getManufacturerTv().setEnabled(false);//禁止厂家名称编辑
            holder.getGetVerificationTableBtn().setVisibility(View.GONE);
//            holder.getOpenTableBtn().setVisibility(View.VISIBLE);
            holder.getCalinforOpenExportLayout().setVisibility(View.VISIBLE);
            holder.getCalInforFilepathLayout().setVisibility(View.VISIBLE);
        }
        else{
            holder.getCustomerTv().setText("");
            holder.getManufacturerTv().setText("");
            holder.getFilepathTv().setText("");
            holder.getTableTypeSp().setEnabled(true);//允许检定记录模板编辑
            holder.getCustomerTv().setEnabled(true); //允许客户名称编辑
            holder.getManufacturerTv().setEnabled(true);//允许厂家名称编辑
            holder.getGetVerificationTableBtn().setVisibility(View.VISIBLE);
//            holder.getOpenTableBtn().setVisibility(View.GONE);
            holder.getCalinforOpenExportLayout().setVisibility(View.GONE);
            holder.getCalInforFilepathLayout().setVisibility(View.GONE);

        }

        //生成检定表按钮
        holder.getGetVerificationTableBtn().setOnClickListener(v -> {
            //检定表模板必须选择
            if(holder.getTableTypeSp().getSelectedItemPosition()==-1) {
               Toast.makeText(v.getContext(), R.string.toast_tableTemplateName,Toast.LENGTH_SHORT).show();
                return;
            }
            //获取辅助信息
            String customerContent = holder.getCustomerTv().getText().toString().trim();
            String manufacturerContent = holder.getManufacturerTv().getText().toString().trim();
            if(customerContent.length()==0 || manufacturerContent.length()==0){
                Toast.makeText(v.getContext(),R.string.toast_customer_manufacturer,Toast.LENGTH_SHORT).show();
                return;
            }
            //必要信息放入map<string,object>
            // "auxMessage": ValidationTableAuxMessage (客户名称，制造厂家)
            // ”tableType“：ValidationTableType（生成检定表模板类型）
            //  ”tableName“：String（生成检定表模板名称）
            final ValidationTableAuxMessage auxMessage = new ValidationTableAuxMessage(customerContent, manufacturerContent);
            //获取检定表模板名称
            String tableTemplateName=(String) holder.getTableTypeSp().getSelectedItem();
            //检定表模板类型
            ValidationTableType tableType=ValidationTableType.QUALITY1;
            if(tableTemplateName.equals(Constants.VOLUME_ARRAY[0]))
                tableType=ValidationTableType.VOLUME1;
            if(tableTemplateName.equals(Constants.QUALITY_ARRAY[0]))
                tableType=ValidationTableType.QUALITY1;
            final Map<String, Object> map = new HashMap<>();
            map.put("auxMessage",auxMessage);
            map.put("tableType",tableType);
            map.put("tableName",tableTemplateName);
            controllerSearchViewModel.getCalDatasAndWord(calInforItemList.get(position),map,this);
        });

        //打开检定表按钮
        holder.getOpenTableBtn().setOnClickListener(v -> {
            if(holder.getFilepathTv().getText()!=null && holder.getFilepathTv().getText().length()>0)// 打开文档
            {
                WpsUtil.openFile(v.getContext(),new File(Constants.OUT_WORD_PATH + "/"+holder.getFilepathTv().getText()));
            }
        });
        //导出检定表按钮
        holder.getExportTableBtn().setOnClickListener(v->{
            if(holder.getFilepathTv().getText()!=null && holder.getFilepathTv().getText().length()>0)// 导出文档
            {
                if(exportFileToUsb!=null){
                    //要复制本地的文件路径
                    final File file = new File(Constants.OUT_WORD_PATH + "/" + holder.getFilepathTv().getText().toString());
                    exportFileToUsb.copyLocalFileToUsb(file);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return calInforItemList.size();
    }
}
