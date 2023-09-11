package com.yzh.ndlljdapp.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.config.ValidationTableType;
import com.yzh.ndlljdapp.entity.CalData;
import com.yzh.ndlljdapp.entity.CalInfor;
import com.yzh.ndlljdapp.entity.ValidationTableAuxMessage;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TemplateToWord {

    /**
     * @param wordValue ${...} 带${}的变量
     * @param map       存储需要替换的数据
     * @return java.lang.String
     * @Description 有${}的值匹配出替换的数据，没有${}就返回原来的数据
     * @author hacah
     * @Date 2021/6/15 16:02
     */
    private static String matchesValue(String wordValue, Map<String, String> map) {
        for (String s : map.keySet()) {
            String s1 = new StringBuilder("${").append(s).append("}").toString();
            if (s1.equals(wordValue)) {
                wordValue = map.get(s);
            }
        }
        return wordValue;
    }

    /**
     * @return boolean
     * @Description 测试是否包含需要替换的数据
     * @author hacah
     * @Date 2021/6/15 15:30
     */
    private static boolean isReplacement(String text) {
        boolean check = false;
        if (text.contains("$")) {
            check = true;
        }
        return check;
    }

    /**
     * @Description 处理所有文段数据，除了表格
     * @param xwpfDocument
     * @param insertTextMap
     * @author hacah
     * @Date 2021/6/17 10:04
     */
    private static void handleParagraphs(XWPFDocument xwpfDocument, Map<String, String> insertTextMap) {
        for (XWPFParagraph paragraph : xwpfDocument.getParagraphs()) {
            String text = paragraph.getText();
            if (isReplacement(text)) {
                for (XWPFRun run : paragraph.getRuns()) {
                    // 判断带有${}的run
                    // System.out.println(run);
//                    run.setText(matchesValue(run.text(), insertTextMap), 0);
                    run.setText(matchesValue(run.getText(0), insertTextMap), 0);
                }
            }
        }

    }


    /**
     * @Description 通过word模板生成word的主方法
     * @param inputStream
     * @param outputStream
     * @param insertTextMap
     * @author hacah
     * @Date 2021/6/17 10:03
     */
    public static void generateWord(InputStream inputStream, OutputStream outputStream, Map<String, String> insertTextMap) throws IOException {
        //获取docx解析对象
        XWPFDocument xwpfDocument = new XWPFDocument(inputStream);

        // 处理所有文段数据，除了表格
        handleParagraphs(xwpfDocument, insertTextMap);
        // 处理表格数据
        handleTable(xwpfDocument, insertTextMap);

        // 写出数据
        xwpfDocument.write(outputStream);
        outputStream.close();
    }


    /**
     * @Description 处理表格数据方法
     * @param xwpfDocument
     * @param insertTextMap
     * @author hacah
     * @Date 2021/6/17 10:04
     */
    private static void handleTable(XWPFDocument xwpfDocument, Map<String, String> insertTextMap) {
        List<XWPFTable> tables = xwpfDocument.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            if (rows.size() > 1) {
                if (isReplacement(table.getText())) {
                    // 替换数据
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> tableCells = row.getTableCells();
                        for (XWPFTableCell tableCell : tableCells) {
                            if (isReplacement(tableCell.getText())) {
                                // 替换数据
                                List<XWPFParagraph> paragraphs = tableCell.getParagraphs();
                                for (XWPFParagraph paragraph : paragraphs) {
                                    List<XWPFRun> runs = paragraph.getRuns();
                                    for (XWPFRun run : runs) {
                                        run.setText(matchesValue(tableCell.getText(), insertTextMap), 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
 /*   public static void main(String[] args) throws IOException {

        // 替换文本数据构建
        HashMap<String, String> insertTextMap = new HashMap<>(16);
        insertTextMap.put("CustomerName", "重庆月之恒科技");
        insertTextMap.put("devNo", "YZH123456");
        insertTextMap.put("Manufacturer", "重庆慧超环保科技");
        insertTextMap.put("Q", "1234.56");


        // 设置模板输入和结果输出
        String sourceDocPath = TemplateToWord.class.getClassLoader().getResource("").getPath()+ "source1.docx";
        String targetDocPath = TemplateToWord.class.getClassLoader().getResource("").getPath()+ "outPut1.docx";
        FileInputStream fileInputStream = new FileInputStream(sourceDocPath);
        FileOutputStream fileOutputStream = new FileOutputStream(targetDocPath);
        // 生成word
        generateWord(fileInputStream, fileOutputStream, insertTextMap);

    }*/

    /**
     * 返回要在word文档替换的信息,文件信息在“fileName”
     * @param calInfor
     * @param calDataList
     * @param auxMsg
     * @return
     */
    public static HashMap<String,String> getInsetTextMap(CalInfor calInfor, List<CalData> calDataList, ValidationTableAuxMessage auxMsg){
        //替换文本数据构建
        HashMap<String, String> insertTextMap = new HashMap<>();
        //客户名称
        insertTextMap.put("CustomerName", auxMsg.getCustomer());
        //出厂编号
        insertTextMap.put("devNo",calInfor.getDevNo());
        //制造厂
        insertTextMap.put("Manufacturer",auxMsg.getManufactor());
        //重复性
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        insertTextMap.put("Er",decimalFormat.format(calInfor.getUncertainty()));
        //检定日期
        DateFormat format=new SimpleDateFormat("yyyy.MM.dd");
        insertTextMap.put("DT1",format.format(new Date(calInfor.getDate())));

        //检定数据
        for (int i = 0; i < 5; i++) {
            if(calDataList!=null && i<calDataList.size()){
                //瞬时流量
                insertTextMap.put("Q"+(i+1),decimalFormat.format(calDataList.get(i).getInstantFlow()));
                //被校表单次累积值
                insertTextMap.put("X"+(i+1),decimalFormat.format(calDataList.get(i).getCalTotalFlow()));
                //标准表单次累积值
                insertTextMap.put("B"+(i+1),decimalFormat.format(calDataList.get(i).getStandardTotalFlow()));
                //温度
                insertTextMap.put("T"+(i+1),decimalFormat.format(calDataList.get(i).getTemp()));
                //基本误差
                insertTextMap.put("E"+(i+1),decimalFormat.format(calDataList.get(i).getE()));
            }
            else {
                //如果没有值，替换为空
                insertTextMap.put("Q"+(i+1),"");
                insertTextMap.put("X"+(i+1),"");
                insertTextMap.put("B"+(i+1),"");
                insertTextMap.put("T"+(i+1),"");
                insertTextMap.put("E"+(i+1),"");
            }
        }
        //输出文件命名 devNo-yyyyMMdd-HHmmss.docx
        DateFormat formatTime=new SimpleDateFormat("-yyyyMMdd-HHmmss");
        String targetFile=calInfor.getDevNo()+formatTime.format(new Date(calInfor.getDate()))+".docx";
        insertTextMap.put("fileName",targetFile);
        return insertTextMap;
    }


    /**
     *
     * @param context
     * @param insertTextMap word中要替换
     * @param type
     * @param wordResultCallBack
     */
    public static void generateWord(Context context, HashMap<String, String> insertTextMap, ValidationTableType type, WordResultCallBack wordResultCallBack) {
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 设置模板输入
                String sourceFileName = "";
                switch (type) {
                    case VOLUME1:
                        sourceFileName = "sourceV1.docx";
                        break;
                    case VOLUME2:
                        sourceFileName = "sourceV2.docx";
                        break;
                    case QUALITY1:
                        sourceFileName = "sourceQ1.docx";
                        break;
                    case QUALITY2:
                        sourceFileName = "sourceQ2.docx";
                        break;
                }
                try {
                    InputStream is = context.getAssets().open(sourceFileName);
                    String targetFile = insertTextMap.get("fileName");
                    File dir = new File(Constants.OUT_WORD_PATH);
                    //如果文件夹不存在，则创建指定的文件
                    if (!dir.exists() || !dir.isDirectory()) {
                        dir.mkdir();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(Constants.OUT_WORD_PATH + "/"+targetFile);
                    // 生成word
                    generateWord(is, fileOutputStream, insertTextMap);
                    //成功调用回调
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(wordResultCallBack!=null)
                                wordResultCallBack.onSuccess(targetFile);
                        }
                    });
                } catch (IOException e) {
                    //失败调用回调
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(wordResultCallBack!=null)
                                wordResultCallBack.onError(e);
                        }
                    });
                }
            }
        }).start();
    }
}

