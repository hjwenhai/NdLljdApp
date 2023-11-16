package com.yzh.ndlljdapp.recyleviewitem;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yzh.ndlljdapp.R;
//该类是为控制器数据查询页面的Recycleview提供ViewHolder
public class CalInforViewHolder extends RecyclerView.ViewHolder{
    //出厂编号
    private final TextView devNoTv;
    //日期
    private final TextView dateTv;
    //流量计类型
    private final TextView typeTv;
    //示值误差
    private final TextView accuracyTv;
    //重复性
    private final TextView uncertaintyTv;
    //生成检定表模板类型
    private final Spinner tableTypeSp;
    //客户名称
    private final TextView customerTv;
    //制造厂名称
    private final TextView manufacturerTv;
    //生成检定表按钮
    private final Button getVerificationTableBtn;



    //文件名称布局，可隐藏。
    private final LinearLayout calInforFilepathLayout;
    //文件名
    private final TextView filepathTv;
    //打开检定表按钮
    private final Button openTableBtn;
    //导出检定表按钮
    private final Button exportTableBtn;

    //打开，导出检定表按钮布局名称,可隐藏
    private final LinearLayout calinforOpenExportLayout;

    public CalInforViewHolder(@NonNull View itemView) {
        super(itemView);
        devNoTv = itemView.findViewById(R.id.calInfor_devNo_content);
        dateTv = itemView.findViewById(R.id.calInfor_date_content);
        typeTv=itemView.findViewById(R.id.calInfor_type_content);
        accuracyTv=itemView.findViewById(R.id.calInfor_accuracy_content);
        uncertaintyTv=itemView.findViewById(R.id.calInfor_uncertainty_content);
        tableTypeSp=itemView.findViewById(R.id.aux_type_spinner);
        customerTv=itemView.findViewById(R.id.aux_customer_content);
        manufacturerTv=itemView.findViewById(R.id.aux_manufacturer_content);
        getVerificationTableBtn=itemView.findViewById(R.id.btn_getVerificationTable);
        filepathTv=itemView.findViewById(R.id.calInfor_filepath_content);
        openTableBtn=itemView.findViewById(R.id.btn_calinfor_openTable);
        exportTableBtn=itemView.findViewById(R.id.btn_exportTable);
        calinforOpenExportLayout = itemView.findViewById(R.id.calinfor_OpenExport_layout);
        calInforFilepathLayout = itemView.findViewById(R.id.calInfor_filepath_layout);
    }

    public TextView getDevNoTv() {
        return devNoTv;
    }

    public TextView getDateTv() {
        return dateTv;
    }

    public TextView getTypeTv() {
        return typeTv;
    }

    public TextView getAccuracyTv() {
        return accuracyTv;
    }

    public TextView getUncertaintyTv() {
        return uncertaintyTv;
    }

    public Spinner getTableTypeSp() {
        return tableTypeSp;
    }

    public TextView getCustomerTv() {
        return customerTv;
    }

    public TextView getManufacturerTv() {
        return manufacturerTv;
    }

    public Button getGetVerificationTableBtn() {
        return getVerificationTableBtn;
    }

    public TextView getFilepathTv() {
        return filepathTv;
    }

    public Button getOpenTableBtn() {
        return openTableBtn;
    }

    public Button getExportTableBtn() {
        return exportTableBtn;
    }
    public LinearLayout getCalInforFilepathLayout() {
        return calInforFilepathLayout;
    }

    public LinearLayout getCalinforOpenExportLayout() {
        return calinforOpenExportLayout;
    }
}
