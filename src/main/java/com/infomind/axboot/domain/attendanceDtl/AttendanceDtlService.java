package com.infomind.axboot.domain.attendanceDtl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;

import com.chequer.axboot.core.parameter.RequestParams;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AttendanceDtlService extends BaseService<AttendanceDtl, AttendanceDtl.AttendanceDtlId> {
    private AttendanceDtlRepository attendanceDtlRepository;

    Logger logger = LoggerFactory.getLogger(AttendanceDtlService.class);

    @Inject
    private AttendanceDtlMapper attendanceDtlMapper;

    @Inject
    public AttendanceDtlService(AttendanceDtlRepository attendanceDtlRepository) {
        super(attendanceDtlRepository);
        this.attendanceDtlRepository = attendanceDtlRepository;
    }

    public List<AttendanceDtl> gets(RequestParams<AttendanceDtl> requestParams) {
        String clasSeq =requestParams.getString("clasSeq","");
        String semeSeq =requestParams.getString("semeSeq","");
        String semeYear =requestParams.getString("semeYear","");
        String periodCd =requestParams.getString("periodCd","");
        String atdcSeq =requestParams.getString("atdcSeq","");

        HashMap map = new HashMap<String, Object>();
        map.put("clasSeq", clasSeq);
        map.put("semeSeq", semeSeq);
        map.put("semeYear", semeYear);
        map.put("periodCd", periodCd);
        map.put("atdcSeq", atdcSeq);

        return attendanceDtlMapper.getAtdcDtl(map);
    }

    public List<AttendanceView> viewList(RequestParams<Map> requestParams) {
        String semeYear =requestParams.getString("schSemeYear","");
        String semeSeq =requestParams.getString("schSemeSeq","");
        String periodCd =requestParams.getString("schPeriodCd","");
        String clasSeq =requestParams.getString("schClasSeq","");
        String date =requestParams.getString("schDate","");

        HashMap map = new HashMap<String, Object>();
        map.put("schSemeYear", semeYear);
        map.put("schSemeSeq", semeSeq);
        map.put("schPeriodCd", periodCd);
        map.put("schClasSeq", clasSeq);
        map.put("schDate", date);

        return attendanceDtlMapper.viewList(map);
    }

    public List<AttendanceView> dateList(RequestParams<Map> requestParams) {
        String date =requestParams.getString("schDate","");

        HashMap map = new HashMap<String, Object>();
        map.put("schDate", date);

        return attendanceDtlMapper.dateList(map);
    }


    public void missAtdtExcel(HashMap map, HttpServletResponse response) throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        logger.info("excute =========" + map);
        logger.info("response===========" +response);
        try {

            List<AttendanceMissExcel> atdtMissList = attendanceDtlMapper.getMissAtdtExcel(map);
            Sheet sheet = wb.createSheet(map.get("schSemeYear") + "학년도 출석입력 누락");
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
            cell.setCellValue(map.get("schSemeYear") + "학년도 출석입력 누락");
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));

            // 헤더 생성
            String[] header = {"강사ID", "강사명", "수강반명", "수업구분", "학번", "학생명", "누락 횟수", "누락 일자"};
            sheet.createRow(rowNo++);

            int[] headerSize = {5, 5, 5, 5, 8, 12, 5, 24};
            for (int i = 0; i < header.length; i++) {
                row = sheet.getRow(1);
                cell = row.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(header[i]);
                sheet.setColumnWidth(i, headerSize[i] * 512);
            }
            int cnt = 0;
            for(AttendanceMissExcel atdmExcel : atdtMissList) {
                List<String> list = new ArrayList<>();
                list.add(atdmExcel.getUserCd());
                list.add(atdmExcel.getUserNm());
                list.add(atdmExcel.getClasNm());
                list.add(atdmExcel.getTchrDiv());
                list.add(atdmExcel.getStdtId());
                list.add(atdmExcel.getStdtNmKor());
                list.add(atdmExcel.getAbscCnt());
                list.add(atdmExcel.getAbscDt());
                row = sheet.createRow(rowNo);
                rowNo++;
                for (int i = 0; i < list.size(); i++) {
                    cell = row.createCell(i);
                    if (i % 7 == 0 && i != 0) {
                        cell.setCellStyle(bodyLeftStyle);
                    } else {
                        cell.setCellStyle(bodyStyle);
                    }



                    cell.setCellValue(list.get(i));
                }
            }



            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
            String fileName = java.net.URLEncoder.encode("출석누락_" + sdf.format(System.currentTimeMillis()), "UTF-8");
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