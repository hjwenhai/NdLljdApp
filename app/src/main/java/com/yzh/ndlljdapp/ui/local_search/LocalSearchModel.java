package com.yzh.ndlljdapp.ui.local_search;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.entity.FlowDoc;
import com.yzh.ndlljdapp.model.FlowDocModel;
import com.yzh.ndlljdapp.recyleviewitem.LocalSearchAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*我们在使用ViewModel的时候，千万不能从外面传入Activity，Fragment或者View之类的含有Context引用的东西，
否则系统会认为该ViewModel还在使用中，从而无法被系统销毁回收，导致内存泄漏的发生。
但如果你希望在ViewModel中使用Context怎么办呢？我们可以使用AndroidViewModel类，
它继承自ViewModel，并且接收Application作为Context，既然是Application作为Context，也就意味着，我们能够明确它的生命周期和Application是一样的，这就不算是一个内存泄露了。*/
public class LocalSearchModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    //搜索框出厂编号数据
    private final MutableLiveData<String> mSearchDevNo;

    //开始时间
    private final MutableLiveData<Date> mSearchStartDate;
    //结束时间
    private final MutableLiveData<Date> mSearchEndDate;
    //客户名称
    private final MutableLiveData<String> mCustomer;
    //客户名称
    private final MutableLiveData<String> mManufacturer;

    private final List<FlowDoc> flowDocList; //RecyclerView的数据源


    public LocalSearchModel(Application app) {
        super(app);
        mText = new MutableLiveData<>();
        mText.setValue("本地按条件搜索,上述选项至少选择一项！");
        mSearchDevNo=new MutableLiveData<>();
        mSearchDevNo.setValue("");
        final Date endDate = new Date();
        final Date startDate=new Date(endDate.getTime()- Constants.SEARCH_TIMESPAN);
        mSearchStartDate=new MutableLiveData<>();
        mSearchStartDate.setValue(startDate);
        mSearchEndDate=new MutableLiveData<>();
        mSearchEndDate.setValue(endDate);
        mCustomer=new MutableLiveData<>();
        mCustomer.setValue("");
        mManufacturer=new MutableLiveData<>();
        mManufacturer.setValue("");
        flowDocList=new ArrayList<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
    public  MutableLiveData<String> getSearchDevNo(){return mSearchDevNo;}
    public MutableLiveData<Date> getSeartchStartDate(){return mSearchStartDate;}
    public MutableLiveData<Date> getSeartchEndDate(){return mSearchEndDate;}
    public  MutableLiveData<String> getCustomer(){return mCustomer;}
    public  MutableLiveData<String> getManufactuer(){return mManufacturer;}
    public List<FlowDoc> getFlowDocs(){return flowDocList;}
    //从本地搜索符合条件的flowDoc
    public void searchFlowDocs(LocalSearchAdapter localSearchAdapter){
        flowDocList.clear();
        mText.setValue("正在从本地搜索检定信息...");
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constants.QUERY_FLOW_DOC) {
                    List<FlowDoc> list = (List<FlowDoc>) msg.obj;
                    if (list != null && list.size() > 0) {
                        Log.d("查询本地数据库中的记录", list.toString());
                        flowDocList.addAll(list);
                        mText.setValue("本地搜索检定信息成功！");
                    } else {
                        mText.setValue("本地没有符合条件的检定记录!");
                    }
                    localSearchAdapter.notifyDataSetChanged();
                }
            }
        };
        Context context = getApplication();
        FlowDocModel flowDocModel = new FlowDocModel(context);
        final String devNoValue = mSearchDevNo.getValue().length()==0 ? null : mSearchDevNo.getValue();
        final Long startDateValue=mSearchStartDate.getValue()==null ? null : mSearchStartDate.getValue().getTime();
        final Long endDateValue=mSearchEndDate.getValue()==null ? null : mSearchEndDate.getValue().getTime();
        final String customerValue = mCustomer.getValue().length()==0 ? null : mCustomer.getValue();
        final String manufacturerValue = mManufacturer.getValue().length()==0 ? null : mManufacturer.getValue();
        flowDocModel.getFlowDocList(devNoValue,startDateValue,endDateValue,customerValue,manufacturerValue,handler);
    }
}