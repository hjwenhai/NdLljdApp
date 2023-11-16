package com.yzh.ndlljdapp.recyleviewitem;

import com.yzh.ndlljdapp.entity.CalData;
import com.yzh.ndlljdapp.entity.CalInfor;
import com.yzh.ndlljdapp.entity.FlowDoc;

import java.util.List;

//该类是calinfor_item_view配套的实体类,为控制器数据查询页面的Recycleview提供数据项
public class CalInforItem {
    private CalInfor calInfor;
    private List<CalData> calDataList;
    private FlowDoc flowDoc;

    public CalInforItem(CalInfor calInfor) {
        this.calInfor = calInfor;
    }

    public CalInfor getCalInfor() {
        return calInfor;
    }

    public void setCalInfor(CalInfor calInfor) {
        this.calInfor = calInfor;
    }

    public List<CalData> getCalDataList() {
        return calDataList;
    }

    public void setCalDataList(List<CalData> calDataList) {
        this.calDataList = calDataList;
    }

    public FlowDoc getFlowDoc() {
        return flowDoc;
    }

    public void setFlowDoc(FlowDoc flowDoc) {
        this.flowDoc = flowDoc;
    }

    @Override
    public String toString() {
        return "CalInforItem{" +
                "calInfor=" + calInfor +
                ", calDataList=" + calDataList +
                ", flowDoc=" + flowDoc +
                '}';
    }
}
