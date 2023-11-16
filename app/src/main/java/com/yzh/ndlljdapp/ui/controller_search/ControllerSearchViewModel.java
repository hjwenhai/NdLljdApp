package com.yzh.ndlljdapp.ui.controller_search;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yzh.ndlljdapp.R;
import com.yzh.ndlljdapp.config.AppConfig;
import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.config.ValidationTableType;
import com.yzh.ndlljdapp.entity.CalData;
import com.yzh.ndlljdapp.entity.CalInfor;
import com.yzh.ndlljdapp.entity.FlowDoc;
import com.yzh.ndlljdapp.entity.ResponseCalData;
import com.yzh.ndlljdapp.entity.ResponseCalInfor;
import com.yzh.ndlljdapp.entity.ResponseCalInforList;
import com.yzh.ndlljdapp.entity.ValidationTableAuxMessage;
import com.yzh.ndlljdapp.model.FlowDocModel;
import com.yzh.ndlljdapp.recyleviewitem.CalInforItem;
import com.yzh.ndlljdapp.recyleviewitem.CalinforAdpter;
import com.yzh.ndlljdapp.util.GsonUtil;
import com.yzh.ndlljdapp.util.OkHttpResultCallback;
import com.yzh.ndlljdapp.util.OkHttpUtil;
import com.yzh.ndlljdapp.util.TemplateToWord;
import com.yzh.ndlljdapp.util.WordResultCallBack;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/*我们在使用ViewModel的时候，千万不能从外面传入Activity，Fragment或者View之类的含有Context引用的东西，
否则系统会认为该ViewModel还在使用中，从而无法被系统销毁回收，导致内存泄漏的发生。
但如果你希望在ViewModel中使用Context怎么办呢？我们可以使用AndroidViewModel类，
它继承自ViewModel，并且接收Application作为Context，既然是Application作为Context，也就意味着，我们能够明确它的生命周期和Application是一样的，这就不算是一个内存泄露了。*/
public class ControllerSearchViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    //搜索框出厂编号数据
    private final MutableLiveData<String> mSearchDevNo;

    //开始时间
    private final MutableLiveData<Date> mSearchStartDate;
    //结束时间
    private final MutableLiveData<Date> mSearchEndDate;


    private final MutableLiveData<List<CalInforItem>> mCalInfortItems; //RecyclerView的数据源，包括calinfor,caldatas,flowDoc
    public ControllerSearchViewModel(Application app) {
        super(app);
        mText = new MutableLiveData<>();
        mText.setValue("控制器按条件搜索,上述选项至少选择一项！");
        mSearchDevNo=new MutableLiveData<>();
        mSearchDevNo.setValue("");
        final Date endDate = new Date();
        final Date startDate=new Date(endDate.getTime()- Constants.SEARCH_TIMESPAN);
        mSearchStartDate=new MutableLiveData<>();
        mSearchStartDate.setValue(startDate);
        mSearchEndDate=new MutableLiveData<>();
        mSearchEndDate.setValue(endDate);

        mCalInfortItems=new MutableLiveData<>();
        final ArrayList<CalInforItem> calInforItems = new ArrayList<>();
        mCalInfortItems.setValue(calInforItems);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public  MutableLiveData<String> getSearchDevNo(){return mSearchDevNo;}
    public MutableLiveData<Date> getSeartchStartDate(){return mSearchStartDate;}
    public MutableLiveData<Date> getSeartchEndDate(){return mSearchEndDate;}
    public MutableLiveData<List<CalInforItem>> getCalInfortItems(){return mCalInfortItems;}
    //从控制器搜索历史检定信息
    public void searchCalinforsFromController(CalinforAdpter calinforAdpter){
        //清空搜索结果
        mCalInfortItems.getValue().clear();
        calinforAdpter.notifyDataSetChanged();
//        String url  = Constants.BASE_URL + "v1/calinfo/querry?";
        String url  = AppConfig.getInstance().getBaseUrl() + "v1/calinfo/querry?";
        if(mSearchDevNo.getValue().trim().length()>0){
            //如果出厂编号不为空，添加出厂编号.
            url=url+"devno="+mSearchDevNo.getValue();
            if(mSearchStartDate.getValue()!=null && mSearchEndDate.getValue()!=null){
                url=url+"&startdate="+mSearchStartDate.getValue().getTime()+
                "&enddate="+mSearchEndDate.getValue().getTime();
            }
        }
        else{
            if(mSearchStartDate.getValue()!=null && mSearchEndDate.getValue()!=null){
                url=url+"startdate="+mSearchStartDate.getValue().getTime()+
                        "&enddate="+mSearchEndDate.getValue().getTime();
            }
        }
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            {
                mText.setValue("正在从控制器搜索检定信息...");
            }
            @Override
            public void onError(Call call, Exception e) {
                mText.setValue("控制器没有响应,请检查是否连接到控制器,控制器的IP是否设置正确！");
//                mCalInfortItems.getValue().clear();
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    mText.setValue("搜索检定信息,服务器有响应！");
                    String s = new String(bytes,"UTF-8");
                    ResponseCalInforList responseCalInforList = GsonUtil.gsonToBean(s, ResponseCalInforList.class);
                    switch (responseCalInforList.getError_code()){
                        case 0:
                            mText.setValue("搜索检定信息成功！");
                            Log.d("return CalInfor:",responseCalInforList.getData().toString());
                            //添加搜索结果到mCalInfortItems
//                            final ArrayList<CalInforItem> items = new ArrayList<>();
                            for (CalInfor calInfor : responseCalInforList.getData()) {
                                final CalInforItem calInforItem = new CalInforItem(calInfor);
                                mCalInfortItems.getValue().add(calInforItem);
//                                items.add(calInforItem);
                            }
//                            mCalInfortItems.setValue(items);
                            calinforAdpter.notifyDataSetChanged();
                            //查询flowdoc
                            queryFlowDocs(mCalInfortItems.getValue(),calinforAdpter);
                            break;
                        case 400:
                            mText.setValue("搜索检定信息的参数有误");
//                            mCalInfortItems.getValue().clear();
                            break;
                        case 500:
                            mText.setValue("控制器数据库中没有任何检定信息记录！");
//                            mCalInfortItems.getValue().clear();
                            break;
                        case 501:
                            mText.setValue("控制器数据库中没有符合查询条件的检定信息记录！");
//                            mCalInfortItems.getValue().clear();
                            break;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //查询本地数据库中是否已有记录
    public void queryFlowDocs(List<CalInforItem> calInforItems, CalinforAdpter calinforAdpter) {

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.QUERY_SEARCH_FLOW_DOCS) {
                    List<FlowDoc> list = (List<FlowDoc>) msg.obj;
                    if (list != null && list.size() > 0 && list.size() == calInforItems.size()) {
                        Log.d("查询本地数据库中的记录", list.toString());
                        for (int i = 0; i < calInforItems.size(); i++) {
                            calInforItems.get(i).setFlowDoc(list.get(i));
                        }
                    } else {
                        ;
                    }
                    calinforAdpter.notifyDataSetChanged();
                }
            }
        };
        Context context = getApplication();
        FlowDocModel flowDocModel = new FlowDocModel(context);
        flowDocModel.getFlowDocList(calInforItems, handler);
    }
    /**
     * 从控制器获取检定记录,并生成word文档
     */
    public void getCalDatasAndWord(CalInforItem calInforItem, Map<String,Object> inforMap,CalinforAdpter calinforAdpter){
        CalInfor calInfor=calInforItem.getCalInfor();
        if(calInfor!=null){
//            String url  = Constants.BASE_URL + "v1/caldata/querry?infoid="+ calInfor.getId();
            String url  = AppConfig.getInstance().getBaseUrl() + "v1/caldata/querry?infoid="+ calInfor.getId();
            OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
                {
                    mText.setValue("正在从控制器获取最新的检定数据...");
                }
                @Override
                public void onError(Call call, Exception e) {
                    mText.setValue("控制器没有响应,请检查是否连接到控制器,控制器的IP是否设置正确！");
                    calInforItem.setCalDataList(null);
                }

                @Override
                public void onResponse(byte[] bytes) {
                    try {
                        mText.setValue("获取检定数据成功！");
                        String s = new String(bytes,"UTF-8");
                        ResponseCalData responseCalData = GsonUtil.gsonToBean(s, ResponseCalData.class);
                        switch (responseCalData.getError_code()){
                            case 0:
                                Log.d("return CalData:",responseCalData.toString());
                                calInforItem.setCalDataList(responseCalData.getData());
                                //生成word
                                generateWord(calInforItem,inforMap,calinforAdpter);
                                break;
                            case 400:
                                mText.setValue("请求检定数据的参数有误");
                                calInforItem.setCalDataList(null);;
                                break;
                            case 500:
                                mText.setValue("控制器数据库中没有任何检定数据记录！");
                                calInforItem.setCalDataList(null);;
                                break;
                            case 501:
                                mText.setValue("控制器数据库中没有符合查询条件的检定数据记录！");
                                calInforItem.setCalDataList(null);;
                                break;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     *
     * @param calInforItem
     * @param inforMap
     * "auxMessage": ValidationTableAuxMessage (辅助信息，客户名称，制造厂家)
     * ”tableType“：ValidationTableType（生成检定表模板类型）
     * ”tableName“：String（生成检定表模板名称）
     * @param calinforAdpter
     */

    private void generateWord(CalInforItem calInforItem, Map<String,Object> inforMap,CalinforAdpter calinforAdpter){
        final List<CalData> calDataList = calInforItem.getCalDataList();
        final FlowDoc flowDoc = calInforItem.getFlowDoc();
        //有检定数据，且在本地数据库中没有记录
        if(calDataList!=null && calDataList.size()>0 && flowDoc==null){
            Log.d("检定数据变化",calDataList.toString());
            mText.setValue("正在生成word文档!");
            CalInfor calInfor = calInforItem.getCalInfor();
            ValidationTableAuxMessage auxMessage = (ValidationTableAuxMessage) inforMap.get("auxMessage");
            HashMap<String, String> insetTextMap = TemplateToWord.getInsetTextMap(calInfor, calDataList, auxMessage);
            //检定表模板
            ValidationTableType tableType=(ValidationTableType)inforMap.get("tableType");
            TemplateToWord.generateWord(getApplication(), insetTextMap, tableType, new WordResultCallBack() {
                @Override
                public void onError(Exception e) {
                    Log.e("生成word文档错误",e.toString());
                    mText.setValue("生成word文档错误!");
                }

                @Override
                public void onSuccess(String filePath) {
                    Log.d("生成word文档成功","文件名："+filePath);
                    mText.setValue("生成word文档成功!");

                    //插入本地数据库
                    FlowDocModel flowDocModel=new FlowDocModel(getApplication());
                    //获取检定表模板名称
                    String tableTemplateName=(String)inforMap.get("tableName");
                    FlowDoc flowDoc=new FlowDoc(calInfor.getDevNo(),calInfor.getDate(),tableTemplateName,auxMessage.getCustomer(),auxMessage.getManufactor(),filePath);
                    Handler handler=new Handler(Looper.getMainLooper()){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if (msg.what == Constants.INSERT_FLOW_DOC) {
                                long id=(long)msg.obj;
                                flowDoc.setId(id);
                                calInforItem.setFlowDoc(flowDoc);
                                calinforAdpter.notifyDataSetChanged();
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