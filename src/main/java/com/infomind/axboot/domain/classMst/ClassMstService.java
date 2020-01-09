package com.infomind.axboot.domain.classMst;

import com.infomind.axboot.utils.SessionUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.chequer.axboot.core.parameter.RequestParams;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ClassMstService extends BaseService<ClassMst, ClassMst.ClassMstId> {
    private ClassMstRepository classMstRepository;

    @Inject
    private ClassMstMapper classMstMapper;

    @Inject
    public ClassMstService(ClassMstRepository classMstRepository) {
        super(classMstRepository);
        this.classMstRepository = classMstRepository;
    }

    public List<ClassMst> gets(RequestParams<ClassMst> requestParams) {
        return findAll();
    }


    public List<ClassMst> getClassList(RequestParams<ClassMst> requestParams) {
        String semeYear =requestParams.getString("semeYear","");
        String periodCd = requestParams.getString("periodCd","");
        String semeSeq = requestParams.getString("semeSeq","");

        HashMap map = new HashMap<String, Object>();
        map.put("semeYear", semeYear);
        map.put("periodCd", periodCd);
        map.put("semeSeq", semeSeq);

        return classMstMapper.getClassList(map);
    }

    public void classMstSave(ClassMst classMst) {

        if(classMst.getClasSeq() == null || classMst.getClasSeq().equals("")){
            classMstMapper.insertClassMst(classMst);
        }else{
            classMstMapper.updateClassMst(classMst);
        }

    }

    public void deleteClassMst(ClassMst classMst) {

        classMstMapper.deleteClassMst(classMst);
    }

    public List<Map> getPeriodCombo(RequestParams<Map> requestParams) {
        String userCd = SessionUtils.getCurrentLoginUserCd();
        String semeYear =requestParams.getString("schSemeYear","");
        String semeSeq = requestParams.getString("schSemeSeq","");
        String myClasChk = requestParams.getString("schMyClasChk","");

        HashMap map = new HashMap<String, Object>();
        map.put("schSemeYear", semeYear);
        map.put("schSemeSeq", semeSeq);
        map.put("schMyClasChk", myClasChk);
        map.put("userCd", userCd);

        return classMstMapper.getPeriodCombo(map);
    }

    public List<Map> getClasSeqCombo(RequestParams<Map> requestParams) {
        String userCd = SessionUtils.getCurrentLoginUserCd();
        String semeYear =requestParams.getString("schSemeYear","");
        String semeSeq = requestParams.getString("schSemeSeq","");
        String periodCd = requestParams.getString("schPeriodCd","");
        String myClasChk = requestParams.getString("schMyClasChk","");

        HashMap map = new HashMap<String, Object>();
        map.put("schSemeYear", semeYear);
        map.put("schSemeSeq", semeSeq);
        map.put("schPeriodCd", periodCd);
        map.put("schMyClasChk", myClasChk);
        map.put("userCd", userCd);

        return classMstMapper.getClasSeqCombo(map);
    }

    public void getClasExcel(HashMap map, HttpServletResponse response) throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        try {
            List<ClassMst> classMstResult = classMstMapper.getClassGrouping(map);

            Sheet sheet = wb.createSheet(map.get("semeYear") + "학년도 " + map.get("schSemeNm") +" "+map.get("schPeriNm")+" 반편성");
            Row row = null;
            Cell cell = null;
            int rowNo = 0;

            // title style
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 20);
            font.setBold(true);
            titleStyle.setFont(font);

            // header style
            CellStyle headStyle = wb.createCellStyle();

            headStyle.setBorderTop(CellStyle.BORDER_THIN);
            headStyle.setBorderBottom(CellStyle.BORDER_THIN);
            headStyle.setBorderLeft(CellStyle.BORDER_THIN);
            headStyle.setBorderRight(CellStyle.BORDER_THIN);
            headStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
            headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headStyle.setAlignment(CellStyle.ALIGN_CENTER);
            headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            font = wb.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setBold(true);
            headStyle.setFont(font);

            // body style
            CellStyle bodyStyle = wb.createCellStyle();

            bodyStyle.setBorderTop(CellStyle.BORDER_THIN);
            bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
            bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);
            bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
            bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
            bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            font = wb.createFont();
            font.setFontHeightInPoints((short) 11);
            font.setBold(true);
            bodyStyle.setFont(font);




            // 제목 생성
            row = sheet.createRow(rowNo++);
            cell = row.createCell(0);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(map.get("semeYear") + "학년도 " + map.get("schSemeNm") +" "+map.get("schPeriNm")+" 반편성");
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 8));

            // 헤더 생성
            String[] header = {"구분", "학급", "강의실", "담임", "부담임"};
            int max = classMstMapper.getClassMaxCount(map); // class 최대 학생수

            int num=1;
            for(int i=0;i<max+7;i++){
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellStyle(headStyle);
                if(i < header.length){
                    cell.setCellValue(header[i]);
                }else if(i == max+5){
                    cell.setCellValue("인원");
                }else{
                    cell.setCellValue(num++);
                }

            }

            int cnt =0;
            HashMap<String,Integer> countLv = new HashMap<>();
            HashMap<String,Integer> sumMap = new HashMap<>();

            for(ClassMst classMst : classMstResult){
                List<String> list = new ArrayList<>();

                list.add(++cnt+"");
                list.add(classMst.getClasNm());
                list.add(classMst.getLctrRoom());
                list.add(classMst.getTchrNm());
                list.add(classMst.getSubTchrNm());

                String[] stdtList = classMst.getStdtList().split("\\|");

                sheet.setColumnWidth(cnt,  10 * 512);

                for(String str : stdtList){
                    list.add(str);
                }
                //list.add(classMst.getStdtCnt());

                for(int j=0;j<max+7;j++){
                    row = sheet.getRow(j+1);
                    cell = row.createCell(cnt);
                    cell.setCellStyle(bodyStyle);
                    if(j < list.size()) {
                        cell.setCellValue(list.get(j));
                    }

                }

                row = sheet.getRow(max+6);
                cell = row.createCell(cnt);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(classMst.getStdtCnt());

                //lv별 인원합계

                if(sumMap.get(classMst.getLv()) == null){
                    sumMap.put(classMst.getLv(),0);
                    countLv.put(classMst.getLv(),0);
                }
                countLv.put(classMst.getLv(),countLv.get(classMst.getLv())+1);
                sumMap.put(classMst.getLv(),sumMap.get(classMst.getLv())+Integer.parseInt(classMst.getStdtCnt()));

            }
            sheet.addMergedRegion(new CellRangeAddress(max+6, max+7, 0,0)); //인원 병합

            //row = sheet.getRow(max+7);

            int firstCol = 1;
            Set set = countLv.keySet();
            Iterator iterator = set.iterator();

            for(int i=0;i<countLv.size();i++){
                String key = (String)iterator.next();

                row = sheet.getRow(max+7);
                cell = row.createCell(firstCol);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(sumMap.get(key));

                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), firstCol, firstCol+countLv.get(key)-1)); //인원 병합
                firstCol +=  countLv.get(key);
            }

            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
            String fileName = java.net.URLEncoder.encode("반편성_" + sdf.format(System.currentTimeMillis()), "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");   //application/vnd.ms-excel
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName +".xlsx");

            wb.write(response.getOutputStream());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            wb.dispose();
            wb.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

}