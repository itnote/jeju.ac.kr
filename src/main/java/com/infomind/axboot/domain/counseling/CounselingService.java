package com.infomind.axboot.domain.counseling;

import com.infomind.axboot.domain.classDtl.ClassDtl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CounselingService extends BaseService<Counseling, Long> {
    private CounselingRepository counselingRepository;

    @Inject
    private CounselingMapper counselingMapper;

    @Inject
    public CounselingService(CounselingRepository counselingRepository) {
        super(counselingRepository);
        this.counselingRepository = counselingRepository;
    }

    public List<Counseling> gets(RequestParams<Counseling> requestParams) {
        return findAll();
    }

    public List<ClassDtl> getCounList(RequestParams<Map> requestParams) {

        String semeYear =requestParams.getString("schSemeYear","");
        String semeSeq =requestParams.getString("schSemeSeq","");
        String periodCd =requestParams.getString("schPeriodCd","");
        String clasSeq =requestParams.getString("schClasSeq","");
        String stdtId =requestParams.getString("schStdtId","");

        HashMap map = new HashMap<String, Object>();
        map.put("schSemeYear", semeYear);
        map.put("schSemeSeq", semeSeq);
        map.put("schPeriodCd", periodCd);
        map.put("schClasSeq", clasSeq);
        map.put("schStdtId", stdtId);

        return counselingMapper.getCounList(map);
    }

    public List<Counseling> getStudentCounList(RequestParams<Counseling> requestParams) {

        String clasSeq =requestParams.getString("clasSeq","");
        String semeSeq =requestParams.getString("semeSeq","");
        String semeYear =requestParams.getString("semeYear","");
        String periodCd =requestParams.getString("periodCd","");
        String stdtId =requestParams.getString("stdtId","");
        String openYn =requestParams.getString("openYn","");

        HashMap map = new HashMap<String, Object>();
        map.put("clasSeq", clasSeq);
        map.put("semeSeq", semeSeq);
        map.put("semeYear", semeYear);
        map.put("periodCd", periodCd);
        map.put("stdtId", stdtId);
        map.put("openYn", openYn);

        return counselingMapper.getStudentCounList(map);
    }


    public void deleteCoun(Counseling counseling){
        counselingMapper.deleteCoun(counseling);
    }


    @SuppressWarnings("Duplicates")
    public void tchrCounExcel(HashMap map, HttpServletResponse response) throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook();

        try {
            // title style
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 16);
            font.setBold(true);
            titleStyle.setFont(font);

            // header style
            CellStyle headStyle = wb.createCellStyle();
            headStyle.setBorderTop(CellStyle.BORDER_THIN);
            headStyle.setBorderBottom(CellStyle.BORDER_THIN);
            headStyle.setBorderLeft(CellStyle.BORDER_THIN);
            headStyle.setBorderRight(CellStyle.BORDER_THIN);
            headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
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
            font.setFontHeightInPoints((short) 14);
            bodyStyle.setFont(font);

            List<CounselingExcel> counList = counselingMapper.tchrCounExcel(map);
            Map<String, List<CounselingExcel>> tchrMap = counList.stream().collect(groupingBy(CounselingExcel :: getUserNm));
            tchrMap.forEach((tchrNm, stdtList) -> {

                Sheet sheet = wb.createSheet(tchrNm);
                Row row;
                Cell cell;
                int rowNo = 0;

                // 제목 생성
                row = sheet.createRow(rowNo++);
                cell = row.createCell(0);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(map.get("schSemeYear") + " " + map.get("schSemeNm") + " " + map.get("schPeriNm") + tchrNm);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));

                // 헤더 생성
                String[] header = {"반", "담당", "학생명", "일반상담횟수", "개별지도횟수", "출결상담횟수", "출결상담누락건수", "출결상담누락일자"};
                row = sheet.createRow(rowNo++);
                for (int i = 0; i < header.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(headStyle);
                    cell.setCellValue(header[i]);
                }

                //데이터 생성
                int idx = 0;
                for (CounselingExcel data : stdtList) {
                    String[] values = {data.getClasNm(), data.getTchrDiv(), data.getStdtNmKor(), data.getGenCnt(), data.getIndiCnt(), data.getAtdcCnt(), data.getAbscCnt(), data.getAbscDt()};
                    row = sheet.createRow(rowNo++);
                    for (int i = 0; i < values.length; i++) {
                        cell = row.createCell(i);
                        cell.setCellStyle(bodyStyle);
                        cell.setCellValue(values[i]);
                    }
                }

                //컬럼 사이즈 설정
                int[] width = {5, 6, 15, 11, 11, 11, 12, 20};
                for (int colNum = 0; colNum < width.length; colNum++) {
                    sheet.setColumnWidth(colNum, width[colNum] * 512);
                }
            });

            String fileName = java.net.URLEncoder.encode(map.get("schSemeYear") + "" + map.get("schSemeNm") + "_" + map.get("schPeriNm") +"_강사별상담이력", "UTF-8");
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