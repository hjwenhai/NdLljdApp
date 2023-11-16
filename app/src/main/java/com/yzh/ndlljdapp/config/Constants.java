package com.yzh.ndlljdapp.config;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Constants {
    public final static int LOGIN_CHECK_USER = 1;
    public final static int LOGIN_REGISTER_USER = 2;

    public final static int LOGIN_UPDATE_USER = 3;
    public final static int LOGIN_DELETE_USER = 4;
    public final static int QUERY_FLOW_DOC = 5; //查询flowdoc;
    public final static int INSERT_FLOW_DOC = 6;
    public final static int QUERY_SEARCH_FLOW_DOCS = 7; //在控制器查询数据时，获取flowdocs;
    public final static int Delet_FLOW_DOC = 9;

    //时间格式
    public final static DateFormat DATE_FORMAT=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.000");;

    //搜索默认时间跨度,默认3天,单位ms
    public final static  long SEARCH_TIMESPAN=3*24*3600*1000;
    //控制器的ip地址
//    public  static String BASE_URL = "http://192.168.0.108:65000/";
    public  final static String BASE_URL = "http://192.168.137.1:65000/";

   //文档输出文件地址
    public final static String OUT_WORD_PATH="/mnt/sdcard/Reports";
    //生成报表类型
    public final static String[] VOLUME_ARRAY ={"容积式流量计检定原始记录"};

    public final static String[] QUALITY_ARRAY ={"质量流量计检定原始记录"};
    //导出到U盘的路径




}
