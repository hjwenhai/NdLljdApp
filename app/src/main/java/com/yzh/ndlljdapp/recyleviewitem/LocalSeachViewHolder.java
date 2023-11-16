package com.yzh.ndlljdapp.recyleviewitem;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yzh.ndlljdapp.R;

//该类是为本地查询页面的Recycleview提供ViewHolder
public class LocalSeachViewHolder extends RecyclerView.ViewHolder{
    //出厂编号
    private final TextView devNoTv;
    //日期
    private final TextView dateTv;
    //记录模板
    private final TextView templateTv;
    //客户名称
    private final TextView customerTv;
    //制造厂名称
    private final TextView manufacturerTv;
    //文件名
    private final TextView fileTv;
    //打开检定表按钮
    private final Button openTableBtn;
    //导出检定表按钮
    private final Button exportTableBtn;
    //导出检定表按钮
    private final Button deleteTableBtn;
    public LocalSeachViewHolder(@NonNull View itemView) {
        super(itemView);
        devNoTv = itemView.findViewById(R.id.local_search_devNo_content);
        dateTv = itemView.findViewById(R.id.local_search_date_content);
        templateTv=itemView.findViewById(R.id.local_search_template_content);
        customerTv=itemView.findViewById(R.id.local_search_customer_content);
        manufacturerTv=itemView.findViewById(R.id.local_search_manufacturer_content);
        fileTv=itemView.findViewById(R.id.local_search_file_content);
        openTableBtn=itemView.findViewById(R.id.btn_local_search_openTable);
        exportTableBtn=itemView.findViewById(R.id.btn_local_search_exportTable);
        deleteTableBtn=itemView.findViewById(R.id.btn_local_search_delete);
    }

    public TextView getDevNoTv() {
        return devNoTv;
    }

    public TextView getDateTv() {
        return dateTv;
    }

    public TextView getTemplateTv() {
        return templateTv;
    }

    public TextView getCustomerTv() {
        return customerTv;
    }

    public TextView getManufacturerTv() {
        return manufacturerTv;
    }

    public TextView getFileTv() {
        return fileTv;
    }

    public Button getOpenTableBtn() {
        return openTableBtn;
    }

    public Button getExportTableBtn() {
        return exportTableBtn;
    }

    public Button getDeleteTableBtn() {
        return deleteTableBtn;
    }
}
