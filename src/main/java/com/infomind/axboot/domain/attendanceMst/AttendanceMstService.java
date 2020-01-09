package com.infomind.axboot.domain.attendanceMst;

import com.infomind.axboot.domain.attendanceDtl.AttendanceDtl;
import com.infomind.axboot.domain.attendanceDtl.AttendanceDtlMapper;
import com.infomind.axboot.domain.attendanceDtl.AttendanceDtlRepository;
import com.infomind.axboot.domain.attendanceDtl.AttendanceDtlService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.chequer.axboot.core.parameter.RequestParams;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
public class AttendanceMstService extends BaseService<AttendanceMst, AttendanceMst.AttendanceMstId> {
    private AttendanceMstRepository attendanceMstRepository;

    @Inject
    private AttendanceMstMapper attendanceMstMapper;

    @Inject
    private AttendanceDtlMapper attendanceDtlMapper;

    @Inject
    private AttendanceDtlService attendanceDtlService;

    @Inject
    public AttendanceMstService(AttendanceMstRepository attendanceMstRepository) {
        super(attendanceMstRepository);
        this.attendanceMstRepository = attendanceMstRepository;
    }

    public List<AttendanceMst> gets(RequestParams<AttendanceMst> requestParams) {
        String schClasSeq =requestParams.getString("schClasSeq","");
        String schSemeSeq =requestParams.getString("schSemeSeq","");
        String schSemeYear =requestParams.getString("schSemeYear","");
        String schPeriodCd =requestParams.getString("schPeriodCd","");
        String schAtdcDt =requestParams.getString("schAtdcDt","");

        HashMap map = new HashMap<String, Object>();
        map.put("schClasSeq", schClasSeq);
        map.put("schSemeSeq", schSemeSeq);
        map.put("schSemeYear", schSemeYear);
        map.put("schPeriodCd", schPeriodCd);
        map.put("schAtdcDt", schAtdcDt);
        return attendanceMstMapper.getAtdcMst(map);
    }

    public Map getTchrDiv(RequestParams<AttendanceMst> requestParams) {
        String schSemeYear =requestParams.getString("schSemeYear","");
        String schSemeSeq =requestParams.getString("schSemeSeq","");
        String schPeriodCd =requestParams.getString("schPeriodCd","");
        String schClasSeq =requestParams.getString("schClasSeq","");

        HashMap map = new HashMap<String, Object>();
        map.put("schSemeYear", schSemeYear);
        map.put("schSemeSeq", schSemeSeq);
        map.put("schPeriodCd", schPeriodCd);
        map.put("schClasSeq", schClasSeq);
        return attendanceMstMapper.getTchrDiv(map);
    }


    @SuppressWarnings("Duplicates")
    public void atdcExcel(HashMap map, HttpServletResponse response) throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook();

        try {
            DecimalFormat df=new DecimalFormat("#.#");
            List<Map> atdcPeriodList = attendanceMstMapper.getAtdcPeriod(map); //학기의 전후반기별 주차 정보
            List<AttendanceExcel> atdcList = attendanceMstMapper.getAtdcExcel(map); //출석 현황

            SXSSFSheet sheet = wb.createSheet();

            SXSSFRow row = null;
            SXSSFCell cell = null;
            int rowNo = 0;

            // title style
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            Font font = wb.createFont();
            font.setFontName("맑은 고딕");
            font.setFontHeightInPoints((short) 48);
            font.setBold(true);
            titleStyle.setFont(font);

            // 메인헤더
            CellStyle headStyle = wb.createCellStyle();

            headStyle.setBorderTop(CellStyle.BORDER_THIN);
            headStyle.setBorderBottom(CellStyle.BORDER_THIN);
            headStyle.setBorderLeft(CellStyle.BORDER_THIN);
            headStyle.setBorderRight(CellStyle.BORDER_THIN);
            headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headStyle.setAlignment(CellStyle.ALIGN_CENTER);
            headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            headStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
            headStyle.setWrapText(true);
            font = wb.createFont();
            font.setFontName("맑은 고딕");
            font.setFontHeightInPoints((short) 14);
            font.setColor(HSSFColor.WHITE.index);
            font.setBold(true);
            headStyle.setFont(font);

            // 헤더 상세 (담임)
            CellStyle headStyle2 = wb.createCellStyle();

            headStyle2.setBorderTop(CellStyle.BORDER_THIN);
            headStyle2.setBorderBottom(CellStyle.BORDER_THIN);
            headStyle2.setBorderLeft(CellStyle.BORDER_THIN);
            headStyle2.setBorderRight(CellStyle.BORDER_THIN);
            headStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headStyle2.setAlignment(CellStyle.ALIGN_CENTER);
            headStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            headStyle2.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
            headStyle2.setWrapText(true);
            font = wb.createFont();
            font.setFontName("맑은 고딕");
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
            headStyle2.setFont(font);

            // 헤더 상세 (부담임)
            CellStyle headStyle3 = wb.createCellStyle();

            headStyle3.setBorderTop(CellStyle.BORDER_THIN);
            headStyle3.setBorderBottom(CellStyle.BORDER_THIN);
            headStyle3.setBorderLeft(CellStyle.BORDER_THIN);
            headStyle3.setBorderRight(CellStyle.BORDER_THIN);
            headStyle3.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headStyle3.setAlignment(CellStyle.ALIGN_CENTER);
            headStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            headStyle3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            headStyle3.setWrapText(true);
            font = wb.createFont();
            font.setFontName("맑은 고딕");
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
            headStyle3.setFont(font);

            // 헤더 총결과
            CellStyle headStyle4 = wb.createCellStyle();

            headStyle4.setBorderTop(CellStyle.BORDER_THIN);
            headStyle4.setBorderBottom(CellStyle.BORDER_THIN);
            headStyle4.setBorderLeft(CellStyle.BORDER_THIN);
            headStyle4.setBorderRight(CellStyle.BORDER_THIN);
            headStyle4.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headStyle4.setAlignment(CellStyle.ALIGN_CENTER);
            headStyle4.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            headStyle4.setFillForegroundColor(HSSFColor.YELLOW.index);
            headStyle4.setWrapText(true);
            font = wb.createFont();
            font.setFontName("맑은 고딕");
            font.setFontHeightInPoints((short) 15);
            font.setBold(true);
            headStyle4.setFont(font);

            // body style
            CellStyle bodyStyle = wb.createCellStyle();

            bodyStyle.setBorderTop(CellStyle.BORDER_THIN);
            bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
            bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);
            bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
            bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
            font = wb.createFont();
            font.setFontName("맑은 고딕");
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
            bodyStyle.setFont(font);

            // 제목 생성
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(map.get("schSemeYear") + "학년도 " + map.get("schSemeNm") + " 출석부");

            // 헤더 생성
            String[] header = {"연번", "학번", "국적", "한글명", "영문명", "성별", "생년월"};
            int[] headerSize = {3, 8, 6, 12, 16, 3, 5};
            sheet.createRow(1);
            sheet.createRow(2);
            sheet.createRow(3);
            for (int i=0; i < header.length; i++) {
                row = sheet.getRow(1);
                cell = row.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(header[i]);
                sheet.setColumnWidth(i, headerSize[i] * 512);
                sheet.addMergedRegion(new CellRangeAddress(1, 3, cell.getColumnIndex(), cell.getColumnIndex()));
            }
            sheet.createFreezePane(header.length, 4);   //틀고정

            int startOfPeriod = header.length;
            //전후반기 헤더 생성
            for(Map periodMap : atdcPeriodList) {
                int cnt = (int)(long)periodMap.get("CNT");
                String periodNm = (String)periodMap.get("PERIOD_NM");
                periodMap.put("ATDC_IDX", startOfPeriod);

                for(int i=0; i < cnt + 1; i++) {
                    int cellNo = startOfPeriod + (i * 4);

                    if (i == cnt) { //주차 표시 후 전후반기 결산 헤더 출력
                        periodMap.put("SUM_IDX", cellNo);

                        row = sheet.getRow(1);

                        cell = row.createCell(cellNo);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue(periodNm);
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 8));

                        row = sheet.getRow(2);

                        cell = row.createCell(cellNo);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("결석");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 2));

                        cell = row.createCell(cellNo + 3);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("출석");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 2));

                        cell = row.createCell(cellNo + 6);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("반");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cell.getColumnIndex(), cell.getColumnIndex()));
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);

                        cell = row.createCell(cellNo + 7);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("%");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cell.getColumnIndex(), cell.getColumnIndex()));
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);

                        cell = row.createCell(cellNo + 8);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("수료\n여부");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, cell.getColumnIndex(), cell.getColumnIndex()));
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);

                        startOfPeriod = cell.getColumnIndex() + 1;

                        row = sheet.getRow(3);

                        cell = row.createCell(cellNo);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("담임");
                        sheet.setColumnWidth(cell.getColumnIndex(), 3 * 512);

                        cell = row.createCell(cellNo + 1);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("부");
                        sheet.setColumnWidth(cell.getColumnIndex(), 3 * 512);

                        cell = row.createCell(cellNo + 2);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("총결");
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);

                        cell = row.createCell(cellNo + 3);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("담임");
                        sheet.setColumnWidth(cell.getColumnIndex(), 3 * 512);

                        cell = row.createCell(cellNo + 4);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("부");
                        sheet.setColumnWidth(cell.getColumnIndex(), 3 * 512);

                        cell = row.createCell(cellNo + 5);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("총출");
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);

                    } else {    //주차 표시
                        row = sheet.getRow(1);

                        cell = row.createCell(cellNo);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue(i+1 + "주");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 3));

                        row = sheet.getRow(2);

                        cell = row.createCell(cellNo);
                        cell.setCellStyle(headStyle2);
                        cell.setCellValue("담임");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 1));

                        cell = row.createCell(cellNo + 2);
                        cell.setCellStyle(headStyle3);
                        cell.setCellValue("부");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 1));

                        row = sheet.getRow(3);

                        cell = row.createCell(cellNo);
                        cell.setCellStyle(headStyle2);
                        cell.setCellValue("지\n각");
                        sheet.setColumnWidth(cell.getColumnIndex(), 2 * 512);

                        cell = row.createCell(cellNo + 1);
                        cell.setCellStyle(headStyle2);
                        cell.setCellValue("결\n석");
                        sheet.setColumnWidth(cell.getColumnIndex(), 2 * 512);

                        cell = row.createCell(cellNo + 2);
                        cell.setCellStyle(headStyle3);
                        cell.setCellValue("지\n각");
                        sheet.setColumnWidth(cell.getColumnIndex(), 2 * 512);

                        cell = row.createCell(cellNo + 3);
                        cell.setCellStyle(headStyle3);
                        cell.setCellValue("결\n석");
                        sheet.setColumnWidth(cell.getColumnIndex(), 2 * 512);
                    }
                }
            }

            row = sheet.getRow(1);

            int summaryIdx = sheet.getRow(2).getLastCellNum();
            cell = row.createCell(summaryIdx);
            cell.setCellStyle(headStyle4);
            cell.setCellValue(map.get("schSemeYear").toString() + " " + map.get("schSemeNm").toString());
            sheet.addMergedRegion(new CellRangeAddress(1, 2, cell.getColumnIndex(), cell.getColumnIndex() + 4));

            row = sheet.getRow(3);

            String[] summary = {"결", "출", "%", "수료\n여부", "비고(개근상 등)"};
            int[] summarySize = {4, 4, 4, 4, 14};
            int lastCellNo = cell.getColumnIndex();
            for (int i = 0; i < summary.length; i++) {
                int idx = lastCellNo + i;
                cell = row.createCell(idx);
                cell.setCellStyle(headStyle4);
                cell.setCellValue(summary[i]);
                sheet.setColumnWidth(cell.getColumnIndex(), summarySize[i] * 512);
            }

            //헤더 border style 일괄 생성
            for (int y = 1; y <= 3; y++) {
                SXSSFRow r1 = sheet.getRow(y);
                int cellIdx = sheet.getRow(3).getLastCellNum();
                for (int x = 0; x < cellIdx; x++) {
                    if (r1.getCell(x) == null) {
                        SXSSFCell c1 = r1.createCell(x);
                        c1.setCellStyle(headStyle);
                    }
                }
            }

            /////////////////////// DATA ///////////////////////////

            //출석List를 학생별로 grouping
            Map<AttendanceExcel.StdtInfo, List<AttendanceExcel>> stdtMap = atdcList.stream()
                    .collect(groupingBy(AttendanceExcel::getStdtInfo));

            int[] idx = { 4, 0 }; //start of {row, cell}
            stdtMap.forEach((stdtInfo, clasList) -> { //학생별 출석 List loop

                SXSSFRow dataRow = sheet.createRow(idx[0]++);
                SXSSFCell dataCell;

                //학생정보 loop
                String[] stdtArr = {String.valueOf(idx[0]-4), stdtInfo.getStdtId(), stdtInfo.getNatnNm(), stdtInfo.getStdtNmKor(), stdtInfo.getStdtNmEng(), stdtInfo.getGenderNm(), stdtInfo.getBirthMt()};
                for (int i=0; i < stdtArr.length; i++) {
                    dataCell = dataRow.createCell(idx[1]++);
                    dataCell.setCellStyle(bodyStyle);
                    dataCell.setCellValue(StringUtils.defaultString(stdtArr[i]));
                }

                //학생별 출석List를 전후반기구분 으로 grouping
                Map<String, List<AttendanceExcel>> stdtPeriodMap = clasList.stream()
                        .collect(groupingBy(AttendanceExcel::getPeriodCd));

                String[] summaryData = {"0", "0", "0", "", ""};

                for (Map periodMap : atdcPeriodList) {
                    if (stdtPeriodMap.containsKey(periodMap.get("PERIOD_CD"))) {
                        int startWeek = (int)periodMap.get("START_OF_PERIOD");
                        int cntWeek = (int)(long)periodMap.get("CNT");
                        int[] periodWeekList = Stream.of(periodMap.get("WEEK_LIST").toString().split("\\|")).mapToInt(Integer::parseInt).toArray();
                        int atdcIdx = (int)periodMap.get("ATDC_IDX");
                        int sumIdx  = (int)periodMap.get("SUM_IDX");
                        int totHours  = Integer.parseInt(periodMap.get("TOT_HOURS").toString());

                        List<AttendanceExcel> stdtWeekList = stdtPeriodMap.get(periodMap.get("PERIOD_CD"));

                        //전후반기List를 weekOfYear로 grouping
                        Map<Integer, List<AttendanceExcel>> stdtWeekMap = stdtWeekList.stream()
                                .collect(groupingBy(AttendanceExcel::getWeekOfYear));

                        for (int i = 0; i < periodWeekList.length; i++) {
                            int colNo = atdcIdx + (i * 4);
                            if (stdtWeekMap.containsKey(periodWeekList[i])) {
                                List<AttendanceExcel> week = stdtWeekMap.get(periodWeekList[i]);

                                //주차 출석 정보
                                int[] atdcArr = {week.get(0).getMLateCnt(), week.get(0).getMAbscCnt(), week.get(0).getSLateCnt(), week.get(0).getSAbscCnt()};
                                for (int k=0; k<atdcArr.length; k++) {
                                    dataCell = dataRow.createCell(colNo + k);
                                    dataCell.setCellStyle(bodyStyle);
                                    dataCell.setCellValue(atdcArr[k]);
                                }
                            }
                        }

                        //출결 합산
                        double[] pSummaryCnt = {0, 0, 0, 0, 0, 0};
                        for (AttendanceExcel weekMap : stdtWeekList) {
                            pSummaryCnt[0] += weekMap.getMAbscCnt(); //담임 결석
                            pSummaryCnt[1] += weekMap.getMLateCnt() / 3.0; //담임 지각
                            pSummaryCnt[2] += weekMap.getSAbscCnt(); //부 결석
                            pSummaryCnt[3] += weekMap.getSLateCnt() / 3.0; //부 지각
                            pSummaryCnt[4] += weekMap.getMAtdcCnt() + weekMap.getMLateCnt(); //담임 출석 + 지각
                            pSummaryCnt[5] += weekMap.getSAtdcCnt() + weekMap.getSLateCnt(); //부 출석 + 지각
                        }

                        //전후반기별 summary 출력
                        double[] pSummaryValue = {0, 0, 0, 0, 0, 0};
                        pSummaryValue[0] = pSummaryCnt[0] + (Math.round(pSummaryCnt[1] * 100) / 100.0);
                        pSummaryValue[1] = pSummaryCnt[2] + (Math.round(pSummaryCnt[3] * 100) / 100.0);
                        pSummaryValue[2] = pSummaryValue[0] + pSummaryValue[1];
                        pSummaryValue[3] = pSummaryCnt[4] - Math.floor(pSummaryCnt[1]);
                        pSummaryValue[4] = pSummaryCnt[5] - Math.floor(pSummaryCnt[3]);
                        pSummaryValue[5] = pSummaryValue[3] + pSummaryValue[4];
                        for (int i=0; i<pSummaryValue.length; i++) {
                            dataCell = dataRow.createCell(sumIdx + i);
                            dataCell.setCellStyle(bodyStyle);
                            dataCell.setCellValue(pSummaryValue[i]);
                        }

                        summaryData[0] = String.valueOf(Integer.parseInt(summaryData[0]) + (int)pSummaryValue[2]);
                        summaryData[1] = String.valueOf(Integer.parseInt(summaryData[1]) + (int)pSummaryValue[5]);
                        summaryData[2] = String.valueOf(Double.parseDouble(summaryData[2]) + Math.floor(pSummaryValue[5] / totHours * 100 * 10) / 10);

                        dataCell = dataRow.createCell(sumIdx + 6);
                        dataCell.setCellStyle(bodyStyle);
                        dataCell.setCellValue(stdtWeekList.get(0).getClasNm());

                        dataCell = dataRow.createCell(sumIdx + 7);
                        dataCell.setCellStyle(bodyStyle);
                        dataCell.setCellValue(df.format(Math.floor(pSummaryValue[5] / totHours * 100 * 10) / 10) + "%");

                        dataCell = dataRow.createCell(sumIdx + 8);
                        dataCell.setCellStyle(bodyStyle);
                        dataCell.setCellValue(Math.floor(pSummaryValue[5] / totHours * 100 * 10) / 10 >= 80 ? "수료" : "불가");
                    }
                }

                Double sRate = Math.floor(Double.parseDouble(summaryData[2]) / atdcPeriodList.size() * 10) / 10;
                summaryData[2] = df.format(sRate) + "%";
                summaryData[3] = sRate >= 80 ? "수료" : "불가";
                for (int i=0; i < summaryData.length; i++) {
                    dataCell = dataRow.createCell(summaryIdx + i);
                    dataCell.setCellStyle(bodyStyle);
                    dataCell.setCellValue(summaryData[i]);
                }

                idx[1] = 0;
            });

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, summaryIdx + 4));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = java.net.URLEncoder.encode(map.get("schSemeYear") + "학년도 " + map.get("schSemeNm") + " 출석부", "UTF-8");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);   //application/vnd.ms-excel
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

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

    public void deleteAtdtMst(AttendanceMst requestParams){

        attendanceMstMapper.deleteAtdcMst(requestParams);
        AttendanceDtl attendanceDtl = new AttendanceDtl();

        attendanceDtl.setSemeYear(requestParams.getSemeYear());
        attendanceDtl.setSemeSeq(requestParams.getSemeSeq());
        attendanceDtl.setPeriodCd(requestParams.getPeriodCd());
        attendanceDtl.setClasSeq(requestParams.getClasSeq());
        attendanceDtl.setAtdcDt(requestParams.getAtdcDt());
        attendanceDtl.setAtdcSeq(requestParams.getAtdcSeq());

        attendanceDtlMapper.deleteAtdcDtl(attendanceDtl);
    }

    @SuppressWarnings("Duplicates")
    public void atdcUpload(File file) {
        XSSFWorkbook wb = null;

        try {
            wb = (XSSFWorkbook) WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);

            List<Map> dateList = attendanceMstMapper.getAtdcDate();

            String clas = "0";
            for (int i=0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String semeYear = "2019";
                String semeSeq = "2";
                String periodCd = "N";
                String clasSeq = row.getCell(0).getStringCellValue();
                String tchr = row.getCell(1).getStringCellValue();
//                String subTchr = row.getCell(2).getStringCellValue();
                String stdtId = row.getCell(3).getStringCellValue();

                boolean initClas = false;
                if (!clas.equals(clasSeq)) {
                    initClas = true;
                }
                clas = clasSeq;

                List<Map> atdcList = new ArrayList();

                Map atdcMap = new HashMap();
                atdcMap.put("week", 1);
                atdcMap.put("ml", (int)row.getCell(4).getNumericCellValue());
                atdcMap.put("ma", (int)row.getCell(5).getNumericCellValue());
//                atdcMap.put("sl", (int)row.getCell(6).getNumericCellValue());
//                atdcMap.put("sa", (int)row.getCell(7).getNumericCellValue());
                atdcList.add(atdcMap);
                atdcMap = new HashMap();
                atdcMap.put("week", 2);
                atdcMap.put("ml", (int)row.getCell(8).getNumericCellValue());
                atdcMap.put("ma", (int)row.getCell(9).getNumericCellValue());
//                atdcMap.put("sl", (int)row.getCell(10).getNumericCellValue());
//                atdcMap.put("sa", (int)row.getCell(11).getNumericCellValue());
                atdcList.add(atdcMap);
                atdcMap = new HashMap();
                atdcMap.put("week", 3);
                atdcMap.put("ml", (int)row.getCell(12).getNumericCellValue());
                atdcMap.put("ma", (int)row.getCell(13).getNumericCellValue());
//                atdcMap.put("sl", (int)row.getCell(14).getNumericCellValue());
//                atdcMap.put("sa", (int)row.getCell(15).getNumericCellValue());
                atdcList.add(atdcMap);
                atdcMap = new HashMap();
                atdcMap.put("week", 4);
                atdcMap.put("ml", (int)row.getCell(16).getNumericCellValue());
                atdcMap.put("ma", (int)row.getCell(17).getNumericCellValue());
//                atdcMap.put("sl", (int)row.getCell(18).getNumericCellValue());
//                atdcMap.put("sa", (int)row.getCell(19).getNumericCellValue());
                atdcList.add(atdcMap);
                if (row.getCell(20) != null) {
                    atdcMap = new HashMap();
                    atdcMap.put("week", 5);
                    atdcMap.put("ml", (int) row.getCell(20).getNumericCellValue());
                    atdcMap.put("ma", (int) row.getCell(21).getNumericCellValue());
//                    atdcMap.put("sl", (int) row.getCell(22).getNumericCellValue());
//                    atdcMap.put("sa", (int) row.getCell(23).getNumericCellValue());
                    atdcList.add(atdcMap);
                }
                if (row.getCell(24) != null) {
                    atdcMap = new HashMap();
                    atdcMap.put("week", 6);
                    atdcMap.put("ml", (int) row.getCell(24).getNumericCellValue());
                    atdcMap.put("ma", (int) row.getCell(25).getNumericCellValue());
//                    atdcMap.put("sl", (int) row.getCell(26).getNumericCellValue());
//                    atdcMap.put("sa", (int) row.getCell(27).getNumericCellValue());
                    atdcList.add(atdcMap);
                }
                if (row.getCell(28) != null) {
                    atdcMap = new HashMap();
                    atdcMap.put("week", 7);
                    atdcMap.put("ml", (int) row.getCell(28).getNumericCellValue());
                    atdcMap.put("ma", (int) row.getCell(29).getNumericCellValue());
//                    atdcMap.put("sl", (int) row.getCell(30).getNumericCellValue());
//                    atdcMap.put("sa", (int) row.getCell(31).getNumericCellValue());
                    atdcList.add(atdcMap);
                }
                if (row.getCell(32) != null) {
                    atdcMap = new HashMap();
                    atdcMap.put("week", 8);
                    atdcMap.put("ml", (int) row.getCell(32).getNumericCellValue());
                    atdcMap.put("ma", (int) row.getCell(33).getNumericCellValue());
//                    atdcMap.put("sl", (int) row.getCell(34).getNumericCellValue());
//                    atdcMap.put("sa", (int) row.getCell(35).getNumericCellValue());
                    atdcList.add(atdcMap);
                }
                if (row.getCell(36) != null) {
                    atdcMap = new HashMap();
                    atdcMap.put("week", 9);
                    atdcMap.put("ml", (int) row.getCell(36).getNumericCellValue());
                    atdcMap.put("ma", (int) row.getCell(37).getNumericCellValue());
//                    atdcMap.put("sl", (int) row.getCell(38).getNumericCellValue());
//                    atdcMap.put("sa", (int) row.getCell(39).getNumericCellValue());
                    atdcList.add(atdcMap);
                }
                if (row.getCell(40) != null) {
                    atdcMap = new HashMap();
                    atdcMap.put("week", 10);
                    atdcMap.put("ml", (int) row.getCell(40).getNumericCellValue());
                    atdcMap.put("ma", (int) row.getCell(41).getNumericCellValue());
//                    atdcMap.put("sl", (int) row.getCell(42).getNumericCellValue());
//                    atdcMap.put("sa", (int) row.getCell(43).getNumericCellValue());
                    atdcList.add(atdcMap);
                }
                if (row.getCell(44) != null) {
                    atdcMap = new HashMap();
                    atdcMap.put("week", 11);
                    atdcMap.put("ml", (int) row.getCell(44).getNumericCellValue());
                    atdcMap.put("ma", (int) row.getCell(45).getNumericCellValue());
//                    atdcMap.put("sl", (int) row.getCell(46).getNumericCellValue());
//                    atdcMap.put("sa", (int) row.getCell(47).getNumericCellValue());
                    atdcList.add(atdcMap);
                }
                if (row.getCell(48) != null) {
                    atdcMap = new HashMap();
                    atdcMap.put("week", 12);
                    atdcMap.put("ml", (int) row.getCell(48).getNumericCellValue());
                    atdcMap.put("ma", (int) row.getCell(49).getNumericCellValue());
//                    atdcMap.put("sl", (int) row.getCell(50).getNumericCellValue());
//                    atdcMap.put("sa", (int) row.getCell(51).getNumericCellValue());
                    atdcList.add(atdcMap);
                }

                int mL = 0;
                int mA = 0;
                int sL = 0;
                int sA = 0;
                int prevWeek = 0;

                for (Map dateMap : dateList) {
                    String date = dateMap.get("DT").toString();
                    int mCnt = Integer.parseInt(dateMap.get("DATA1").toString());
                    int sCnt = Integer.parseInt(dateMap.get("DATA2").toString());
                    int week = Integer.parseInt(dateMap.get("RNUM").toString());

                    if (atdcList.size() < week) {
                        break;
                    }

                    for (Map map : atdcList) {
                        int w = (int)map.get("week");
                        if (w == week && prevWeek != week) {
                            mL = (int)map.get("ml");
                            mA = (int)map.get("ma");
//                            sL = (int)map.get("sl");
//                            sA = (int)map.get("sa");
                        }
                    }
                    prevWeek = week;

                    AttendanceMst am = new AttendanceMst();
                    am.setSemeYear(semeYear);
                    am.setSemeSeq(semeSeq);
                    am.setPeriodCd(periodCd);
                    am.setClasSeq(clasSeq);
                    am.setAtdcDt(date);

                    AttendanceDtl ad = new AttendanceDtl();
                    ad.setSemeYear(semeYear);
                    ad.setSemeSeq(semeSeq);
                    ad.setPeriodCd(periodCd);
                    ad.setClasSeq(clasSeq);
                    ad.setAtdcDt(date);
                    ad.setStdtId(stdtId);

                    int atdcSeq = 1;
                    for (int m=0; m < mCnt; m++) {
                        am.setAtdcSeq(String.valueOf(atdcSeq));
                        ad.setAtdcSeq(String.valueOf(atdcSeq++));
                        am.setTchrDiv("M");
                        am.setTchrCd(tchr);

                        if (mL > 0) {
                            ad.setAtdcCd("1");
                            mL--;
                        } else if (mA > 0) {
                            ad.setAtdcCd("2");
                            mA--;
                        } else {
                            ad.setAtdcCd("0");
                        }

                        if (initClas) {
                            save(am);
                        }
                        attendanceDtlService.save(ad);
                    }
//                    for (int s=0; s < sCnt; s++) {
//                        am.setAtdcSeq(String.valueOf(atdcSeq));
//                        ad.setAtdcSeq(String.valueOf(atdcSeq++));
//                        am.setTchrDiv("S");
//                        am.setTchrCd(subTchr);
//
//                        if (sL > 0) {
//                            ad.setAtdcCd("1");
//                            sL--;
//                        } else if (sA > 0) {
//                            ad.setAtdcCd("2");
//                            sA--;
//                        } else {
//                            ad.setAtdcCd("0");
//                        }
//
//                        if (initClas) {
//                            save(am);
//                        }
//                        attendanceDtlService.save(ad);
//                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AttendanceTchrView> getTchrAtdcList(RequestParams<AttendanceTchrView> requestParams) {
        String schSemeSeq =requestParams.getString("schSemeSeq","");
        String schSemeYear =requestParams.getString("schSemeYear","");
        String schPeriodCd =requestParams.getString("schPeriodCd","");
        String schBeginAtdcDt =requestParams.getString("schBeginAtdcDt","");
        String schEndAtdcDt =requestParams.getString("schEndAtdcDt","");
        String schTchrCd =requestParams.getString("schTchrCd","");

        HashMap map = new HashMap<String, Object>();
        map.put("schSemeSeq", schSemeSeq);
        map.put("schSemeYear", schSemeYear);
        map.put("schPeriodCd", schPeriodCd);
        map.put("schBeginAtdcDt", schBeginAtdcDt);
        map.put("schEndAtdcDt", schEndAtdcDt);
        map.put("schTchrCd", schTchrCd);
        return attendanceMstMapper.getTchrAtdcList(map);
    }

    @SuppressWarnings("Duplicates")
    public void getTchrAtdcExcel(HashMap map, HttpServletResponse response) throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        try {

            List<AttendanceTchrView> attendanceTchrViewList = attendanceMstMapper.getTchrAtdcExcel(map);
            List<AttendanceTchrView> dateList = attendanceMstMapper.getTchrAtdcDate(map);

            Map<String, List<AttendanceTchrView>> tchrMap = attendanceTchrViewList.stream().collect(groupingBy(AttendanceTchrView :: getUserNm));
            tchrMap.forEach((tchrNm, attendanceTchrList) -> {
                map.put("schTchrCd", attendanceTchrList.get(0).getTchrCd());
                String clasNm = attendanceMstMapper.getTchrClasNm(map);
                Map<String, AttendanceTchrView.AtdcInfo> atdcDateMap = attendanceTchrList.stream().collect(Collectors.toMap(AttendanceTchrView::getAtdcDt, AttendanceTchrView::getAtdcInfo));

                Sheet sheet = wb.createSheet(tchrNm);
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

                        CellStyle borderlessStyle = wb.createCellStyle();

                        borderlessStyle.setAlignment(CellStyle.ALIGN_CENTER);
                        borderlessStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                        font = wb.createFont();
                        font.setFontHeightInPoints((short) 11);
                        font.setBold(true);
                        borderlessStyle.setFont(font);

                        CellStyle borderlessLeftStyle = wb.createCellStyle();

                        borderlessLeftStyle.setAlignment(CellStyle.ALIGN_LEFT);
                        borderlessLeftStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                        font = wb.createFont();
                        font.setFontHeightInPoints((short) 11);
                        font.setBold(true);
                        borderlessLeftStyle.setFont(font);

                        // 제목 생성
                        row = sheet.createRow(rowNo++);
                        cell = row.createCell(0);
                        cell.setCellStyle(titleStyle);

                        cell.setCellValue(map.get("schSemeYear") + "학년도 출강부");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));

                        row = sheet.createRow(rowNo++);
                        cell = row.createCell(0);
                        cell.setCellStyle(borderlessLeftStyle);
                        cell.setCellValue("□ 강사 : " + tchrNm);


                        row = sheet.createRow(rowNo++);
                        cell = row.createCell(0);
                        cell.setCellStyle(borderlessLeftStyle);
                        cell.setCellValue("□ 수강반 : " + clasNm);



                        // 헤더 생성
                        String[] header = {"기간", "수업시수", "일", "월", "화", "수", "목", "금", "토"};
                        sheet.createRow(rowNo++);
                        sheet.createRow(rowNo);

                        int[] headerSize = {15, 5, 5, 5, 5, 5, 5, 5, 5};
                        for (int i = 0; i < header.length; i++) {
                            if (i == 1) {
                                row = sheet.getRow(3);
                                for (int j = 1; j < 8; j++) {
                                    cell = row.createCell(j);
                                    cell.setCellStyle(headStyle);
                                }
                                cell = row.getCell(1);
                                cell.setCellValue(header[i]);
                                sheet.setColumnWidth(i, headerSize[i] * 512);
                                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 7));
                            } else if (i > 1) {
                                row = sheet.getRow(4);
                                cell = row.createCell(i - 1);
                                cell.setCellStyle(headStyle);
                                cell.setCellValue(header[i]);
                                sheet.setColumnWidth(i, headerSize[i] * 512);
                            } else if (i == 0) {
                                row = sheet.getRow(3);
                                cell = row.createCell(i);
                                cell.setCellStyle(headStyle);
                                cell.setCellValue(header[i]);
                                sheet.setColumnWidth(i, headerSize[i] * 512);
                                sheet.addMergedRegion(new CellRangeAddress(3, 4, cell.getColumnIndex(), cell.getColumnIndex()));
                            }
                        }

                        String firstWeek = "";
                        int totalHours = 0;
                        int totalAbsentHours = 0;
                        int totalSubHours = 0;
                        int i = 1;
                        List<String> absentDays = new ArrayList<>();
                        List<String> subDays = new ArrayList<>();

                        for (AttendanceTchrView date : dateList) {
                            if (!firstWeek.equals(date.getWeeks())) {
                                row = sheet.createRow(++rowNo);

                                firstWeek = date.getWeeks();

                                cell = row.createCell(0);
                                cell.setCellStyle(bodyStyle);
                                cell.setCellValue(date.getAtdcDt() + " ~ ");
                            }

                            cell = row.createCell(Integer.parseInt(date.getDow()));
                            cell.setCellStyle(bodyStyle);
                            cell.setCellValue(date.getScheNm());

                            AttendanceTchrView.AtdcInfo atdcInfo = atdcDateMap.get(date.getAtdcDt());

                            if (atdcInfo != null) {
                                int realHours = Integer.parseInt(atdcInfo.getHours()) - Integer.parseInt(atdcInfo.getAbsentHours()) + Integer.parseInt(atdcInfo.getSubHours());
                                if (realHours != 0) {
                                    cell.setCellValue(realHours);
                                }
                                int hours = Integer.parseInt(atdcInfo.getHours());
                                totalHours += hours;
                                if (!atdcInfo.getAbsentHours().equals("0")) {
                                    int absentHours = Integer.parseInt(atdcInfo.getAbsentHours());
                                    totalAbsentHours += absentHours;
                                    absentDays.add(atdcInfo.getAtdcDt());
                                }
                                if (!atdcInfo.getSubHours().equals("0")) {
                                    int subHours = Integer.parseInt(atdcInfo.getSubHours());
                                    totalSubHours += subHours;
                                    subDays.add(atdcInfo.getAtdcDt());
                                }
                            }

                            if (i < dateList.size()) {
                                if (!firstWeek.equals(dateList.get(i).getWeeks())) {
                                    cell = row.getCell(0);
                                    cell.setCellValue(cell.getStringCellValue() + date.getAtdcDt());
                                }
                            } else {
                                cell = row.getCell(0);
                                cell.setCellValue(cell.getStringCellValue() + date.getAtdcDt());
                            }

                            i++;

                        }
                        rowNo += 2;

                        sheet.createRow(rowNo++);
                        row = sheet.createRow(rowNo++);

                        cell = row.createCell(0);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue("시수 계 : ");
                        cell = row.createCell(1);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue(String.valueOf(totalHours));

                        row = sheet.createRow(rowNo++);
                        cell = row.createCell(0);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue("결강 계 : ");
                        cell = row.createCell(1);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue(String.valueOf(totalAbsentHours));
                        cell = row.createCell(2);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue("날짜 : ");
                        cell = row.createCell(3);
                        cell.setCellStyle(borderlessLeftStyle);
                        cell.setCellValue(String.join(", ", absentDays));
                        cell = row.createCell(4);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue("사유 : ");

                        row = sheet.createRow(rowNo++);
                        cell = row.createCell(0);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue("대강 계 : ");
                        cell = row.createCell(1);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue(String.valueOf(totalSubHours));
                        cell = row.createCell(2);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue("날짜 : ");
                        cell = row.createCell(3);
                        cell.setCellStyle(borderlessLeftStyle);
                        cell.setCellValue(String.join(", ", subDays));
                        cell = row.createCell(4);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue("사유 : ");


                        row = sheet.createRow(rowNo++);
                        cell = row.createCell(0);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue("총 수업시수 : ");
                        cell = row.createCell(1);
                        cell.setCellStyle(borderlessStyle);
                        cell.setCellValue(String.valueOf(totalHours + totalSubHours - totalAbsentHours));

                        sheet.createRow(rowNo++);
                        row = sheet.createRow(rowNo++);
                        for (int a = 4; a < 8; a++) {
                            cell = row.createCell(a);
                            cell.setCellStyle(bodyLeftStyle);
                        }
                        cell = row.getCell(4);
                        cell.setCellValue("서 명 : ");
                        row = sheet.createRow(rowNo++);
                        for (int a = 4; a < 8; a++) {
                            cell = row.createCell(a);
                            cell.setCellStyle(bodyLeftStyle);
                        }
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(), 4, 7));
                    });
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
            String fileName = java.net.URLEncoder.encode("출강부_" + sdf.format(System.currentTimeMillis()), "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");   //application/vnd.ms-excel
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName +".xlsx");

            wb.write(response.getOutputStream());




        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {
            wb.dispose();
            wb.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }
}