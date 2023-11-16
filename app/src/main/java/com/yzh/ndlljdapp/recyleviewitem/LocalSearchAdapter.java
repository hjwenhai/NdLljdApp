package com.yzh.ndlljdapp.recyleviewitem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yzh.ndlljdapp.R;
import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.entity.FlowDoc;
import com.yzh.ndlljdapp.model.FlowDocModel;
import com.yzh.ndlljdapp.util.WpsUtil;
import com.yzh.ndlljdapp.util.usbHelper.ExportFileToUsb;

import java.io.File;
import java.util.List;

//该类是为本地查询页面的Recycleview提供Adpter
public class LocalSearchAdapter extends RecyclerView.Adapter<LocalSeachViewHolder> {
    private final List<FlowDoc> flowDocList;
    private final ExportFileToUsb exportFileToUsb;

    public LocalSearchAdapter(List<FlowDoc> flowDocList,ExportFileToUsb exportFileToUsb) {
        this.flowDocList = flowDocList;
        this.exportFileToUsb=exportFileToUsb;
    }

    @NonNull
    @Override
    public LocalSeachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_search_item, parent, false);
        return new LocalSeachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalSeachViewHolder holder, int position) {
        final FlowDoc flowDoc = flowDocList.get(position);
        //更新数据
        holder.getDevNoTv().setText(flowDoc.getDevNo());
        holder.getDateTv().setText(Constants.DATE_FORMAT.format(flowDoc.getDate()));
        holder.getTemplateTv().setText(flowDoc.getTableName());
        holder.getCustomerTv().setText(flowDoc.getCustomer());
        holder.getManufacturerTv().setText(flowDoc.getManufactor());
        holder.getFileTv().setText(flowDoc.getFileName());
        //打开检定表按钮
        holder.getOpenTableBtn().setOnClickListener(v -> {
            if(holder.getFileTv().getText()!=null && holder.getFileTv().getText().length()>0)// 打开文档
            {
                WpsUtil.openFile(v.getContext(),new File(Constants.OUT_WORD_PATH + "/"+holder.getFileTv().getText()));
            }
        });
        //导出检定表按钮
        holder.getExportTableBtn().setOnClickListener(v->{
            if(holder.getFileTv().getText()!=null && holder.getFileTv().getText().length()>0)// 导出文档
            {
                if(exportFileToUsb!=null){
                    //要复制本地的文件路径
                    final File file = new File(Constants.OUT_WORD_PATH + "/" + holder.getFileTv().getText().toString());
                    exportFileToUsb.copyLocalFileToUsb(file);
                }
            }
        });
        //删除检定表按钮
        holder.getDeleteTableBtn().setOnClickListener(v->{
            if(holder.getFileTv().getText()!=null && holder.getFileTv().getText().length()>0)// 删除文档
            {
                final LocalSearchAdapter localSearchAdapter = this;
                //确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("提示");
                builder.setMessage("确定要删除吗？");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //从数据库删除记录
                                FlowDocModel flowDocModel=new FlowDocModel(v.getContext());
                                Handler handler=new Handler(Looper.getMainLooper()){
                                    @Override
                                    public void handleMessage(@NonNull Message msg) {
                                        super.handleMessage(msg);
                                        if (msg.what == Constants.Delet_FLOW_DOC) {
                                            int num=(int)msg.obj;
                                            if(num>0) {
                                                Toast.makeText(v.getContext(), "本地删除检定表成功！", Toast.LENGTH_SHORT).show();
                                                //删除文件
                                                final File file = new File(Constants.OUT_WORD_PATH + "/" + holder.getFileTv().getText().toString());
                                                if (file.exists() && file.isFile()){
                                                    file.delete();
                                                }
                                                //从列表中删除，更新显示
                                                flowDocList.remove(flowDoc);
                                                localSearchAdapter.notifyDataSetChanged();
                                            }
                                            else
                                                Toast.makeText(v.getContext(),"本地删除检定表失败！",Toast.LENGTH_SHORT);
                                        }
                                    }
                                };
                                flowDocModel.deleteFlowDoc(flowDoc,handler);
                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return flowDocList.size();
    }
}
