package com.infomind.axboot.domain.classDtl;

import com.infomind.axboot.domain.classMst.ClassMst;
import com.infomind.axboot.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.chequer.axboot.core.parameter.RequestParams;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class ClassDtlService extends BaseService<ClassDtl, ClassDtl.ClassDtlId> {
    private ClassDtlRepository classDtlRepository;

    @Inject
    private ClassDtlMapper classDtlMapper;


    @Inject
    public ClassDtlService(ClassDtlRepository classDtlRepository) {
        super(classDtlRepository);
        this.classDtlRepository = classDtlRepository;
    }

    public List<ClassDtl> gets(RequestParams<ClassDtl> requestParams) {
        return findAll();
    }

    public List<ClassDtl> getClassStudentList(RequestParams<ClassDtl>  requestParams) {
        String schInput =requestParams.getString("schInput","");
        String clasSeq = requestParams.getString("clasSeq","");
        String stdtId = requestParams.getString("stdtId","");

        HashMap map = new HashMap<String, Object>();
        map.put("schInput",schInput);
        map.put("clasSeq",clasSeq);
        map.put("stdtId",stdtId);

        return classDtlMapper.getClassStudentList(map);
    }

    public List<ClassDtl> getAttendanceDtl(RequestParams<ClassDtl> requestParams) {
        String clasSeq =requestParams.getString("clasSeq","");
        String semeSeq =requestParams.getString("semeSeq","");
        String semeYear =requestParams.getString("semeYear","");
        String periodCd =requestParams.getString("periodCd","");
        String atdcSeq =requestParams.getString("atdcSeq","");
        String atdcDt =requestParams.getString("atdcDt","");


        HashMap map = new HashMap<String, Object>();
        map.put("clasSeq", clasSeq);
        map.put("semeSeq", semeSeq);
        map.put("semeYear", semeYear);
        map.put("periodCd", periodCd);
        map.put("atdcSeq", atdcSeq);
        map.put("atdcDt", atdcDt);

        return classDtlMapper.getAtdcDtl(map);
    }

    public void getClasStdtExcel(List<ClassMst> classMstList, HttpServletResponse response) throws IOException {
        String menuGrp = SessionUtils.getCurrentUser().getMenuGrpCd();

        SXSSFWorkbook wb = new SXSSFWorkbook();

        try {
            // title style
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 30);
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
            font = wb.createFont();
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
            headStyle.setFont(font);

            // body style
            CellStyle bodyStyle = wb.createCellStyle();
            bodyStyle.setBorderTop(CellStyle.BORDER_THIN);
            bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
            bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);
            bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
            bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
            font = wb.createFont();
            font.setFontHeightInPoints((short) 16);
            font.setBold(true);
            bodyStyle.setFont(font);

            for (ClassMst classMst : classMstList) {
                HashMap map = new HashMap<String, Object>();
                map.put("schClasSeq", classMst.getClasSeq());
                map.put("schSemeSeq", classMst.getSemeSeq());
                map.put("schSemeYear", classMst.getSemeYear());
                map.put("schPeriodCd", classMst.getPeriodCd());

                List<ClassDtl> classDtlList = classDtlMapper.getClasDtlList(map);

                Sheet sheet = wb.createSheet(classMst.getClasNm() + " " + classMst.getLctrRoom());

                Row row = null;
                Cell cell = null;
                int rowNo = 0;

                // 제목 생성
                row = sheet.createRow(rowNo++);
                cell = row.createCell(0);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(classMst.getClasNm() + "    " + classMst.getLctrRoom());
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 4));

                // 헤더 생성
                String[] header = {"연번", "국적", "한글명", "영문명", "생년월"};
                row = sheet.createRow(rowNo++);
                for (int i=0; i < header.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(headStyle);
                    cell.setCellValue(header[i]);
                }

                //데이터 생성
                int idx = 0;
                for (ClassDtl classDtl : classDtlList) {
                    String[] values = { String.valueOf(++idx), classDtl.getNatnNm(), classDtl.getStdtNmKor(), classDtl.getStdtNmEng(), menuGrp.equals("SYSTEM_MANAGER") ? classDtl.getBirthDt() : classDtl.getBirthMt() };
                    row = sheet.createRow(rowNo++);
                    for (int i=0; i < values.length; i++) {
                        cell = row.createCell(i);
                        cell.setCellStyle(bodyStyle);
                        cell.setCellValue(values[i]);
                    }
                }

                //컬럼 사이즈 설정
                int[] width = {3, 7, 15, 16, 8};
                for (int colNum = 0; colNum < width.length; colNum++) {
                    sheet.setColumnWidth(colNum, width[colNum] * 512);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
            String fileName = java.net.URLEncoder.encode("수강생현황_" + sdf.format(System.currentTimeMillis()), "UTF-8");
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
//        byte[] bytes = wb.getBytes();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        httpHeaders.setContentLength(bytes.length);
//        httpHeaders.setContentDispositionFormData("attachment", "test");
//
//        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }


    @Transactional
    public String saveExcel(List<ClassDtl> list) {
        List<String> strList = new ArrayList<>();
        String str ="";
        for(int i = list.size()-1 ; i>=0 ; i--){
            list.get(i).setFeeDt( list.get(i).getFeeDt() != null && !list.get(i).getFeeDt().equals("") ? list.get(i).getFeeDt() : null );

            HashMap map = new HashMap<String, Object>();
            map.put("stdtId", list.get(i).getStdtId());
            map.put("semeYear", list.get(i).getSemeYear());
            map.put("semeSeq", list.get(i).getSemeSeq());
            map.put("periodCd", list.get(i).getPeriodCd());
            map.put("clasSeq", list.get(i).getClasSeq());

            int cnt=classDtlMapper.isClasStdtExist(map);

            if(cnt == 0){
                list.remove(i);
                strList.add((i+1)+"");
            }
        }

        for(int i=strList.size()-1;i>=0;i--){
            if(i>0){
                str += strList.get(i)+", ";
            }else{
                str += strList.get(i)+ "번째 학생 학번이 존재하지 않거나 다른반에 등록 된 학번입니다.";
            }
        }

        if(strList.size() == 0){
            str = "저장되었습니다.";
        }
        save(list);

        return str;
    }

    public void getExcelClassStudentList(HashMap map, HttpServletResponse response) throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        log.info("excute =========" + map);
        log.info("response===========" +response);
        try {

            List<ClassDtl> excelClassStudentList = classDtlMapper.getExcelClassStudentList(map);
            Sheet sheet = wb.createSheet("학생 현황");
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

            CellStyle bodyLeftStyle = wb.createCellStyle();

            bodyLeftStyle.setBorderTop(CellStyle.BORDER_THIN);
            bodyLeftStyle.setBorderBottom(CellStyle.BORDER_THIN);
            bodyLeftStyle.setBorderLeft(CellStyle.BORDER_THIN);
            bodyLeftStyle.setBorderRight(CellStyle.BORDER_THIN);
            bodyLeftStyle.setAlignment(CellStyle.ALIGN_LEFT);
            bodyLeftStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            font = wb.createFont();
            font.setFontHeightInPoints((short) 11);
            font.setBold(true);
            bodyLeftStyle.setFont(font);

            // 제목 생성
            row = sheet.createRow(rowNo++);
            cell = row.createCell(0);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(map.get("schSemeYear") + "학년도 " + map.get("schSemeNm") + " " + map.get("schPeriNm") +" 학생 현황");
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 9));

            // 헤더 생성
            String[] header = {"수강반명", "학적상태", "학번", "이름", "영문명", "중국어명", "국적", "생년월일", "성별", "전화번호"};
            sheet.createRow(rowNo++);

            int[] headerSize = {5, 5, 10, 12, 12, 12, 8, 10, 5, 10};
            for (int i = 0; i < header.length; i++) {
                row = sheet.getRow(1);
                cell = row.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(header[i]);
                sheet.setColumnWidth(i, headerSize[i] * 512);
            }
            int cnt = 0;
            for(ClassDtl clasExcel : excelClassStudentList) {
                List<String> list = new ArrayList<>();

                list.add(clasExcel.getClasNm());
                list.add(clasExcel.getStatusNm());
                list.add(clasExcel.getStdtId());
                list.add(clasExcel.getStdtNm());
                list.add(clasExcel.getStdtNmEng());
                list.add(clasExcel.getStdtNmChn());
                list.add(clasExcel.getNatnNm());
                list.add(clasExcel.getBirthDt());
                list.add(clasExcel.getGenderNm());
                list.add(clasExcel.getHpNo());

                row = sheet.createRow(rowNo);
                rowNo++;
                for (int i = 0; i < list.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(list.get(i));
                }
            }



            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
            String fileName = java.net.URLEncoder.encode("학생명단_" + sdf.format(System.currentTimeMillis()), "UTF-8");
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