package com.infomind.axboot.domain.scoreMst;

import com.infomind.axboot.domain.attendanceMst.AttendanceExcel;
import com.infomind.axboot.domain.attendanceMst.AttendanceMstMapper;
import com.infomind.axboot.domain.code.CommonCode;
import com.infomind.axboot.domain.scoreDtl.ScoreDtl;
import com.infomind.axboot.domain.scoreDtl.ScoreDtlService;
import com.infomind.axboot.utils.CommonCodeUtils;
import com.infomind.axboot.utils.SessionUtils;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ScoreMstService extends BaseService<ScoreMst, ScoreMst.ScoreMstId> {
    private ScoreMstRepository scoreMstRepository;

    @Inject
    private ScoreMstMapper scoreMstMapper;
    @Inject
    private ScoreDtlService scoreDtlService;
    @Inject
    private AttendanceMstMapper attendanceMstMapper;

    @Inject
    public ScoreMstService(ScoreMstRepository scoreMstRepository) {
        super(scoreMstRepository);
        this.scoreMstRepository = scoreMstRepository;
    }

    public List<ScoreMst> gets(RequestParams<ScoreMst> requestParams) {
        return findAll();
    }

    public List<ScoreMst> getStudentScoreList(RequestParams<Map> requestParams) {

        HashMap map = new HashMap<String, Object>();
        map.put("schClasSeq", requestParams.getString("schClasSeq",""));
        map.put("schSemeSeq", requestParams.getString("schSemeSeq",""));
        map.put("schSemeYear", requestParams.getString("schSemeYear",""));
        map.put("schPeriodCd", requestParams.getString("schPeriodCd",""));

        return scoreMstMapper.getStudentScoreList(map);
    }

    public String saveScore(List<ScoreMst> request) {
        String loginUser = SessionUtils.getCurrentLoginUserCd();
        String loginMenuGrp = SessionUtils.getCurrentUser().getMenuGrpCd();
        String message = "ax.script.calc.done";

        for (ScoreMst scoreMst : request) {
            //관리자, 담임, 부담임 모두 아니면
            if (!loginMenuGrp.equals("SYSTEM_MANAGER") && !scoreMst.getTchrCd().equals(loginUser) && !scoreMst.getSubTchrCd().equals(loginUser)) {
                return "ax.script.calc.auth";
            }

            scoreMst = scoreMstMapper.getEvalResult(scoreMst);
            this.save(scoreMst);
        }

        return message;
    }

    @SuppressWarnings("Duplicates")
    public void scoreExcel(HashMap map, HttpServletResponse response) throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        List<ScoreExcel> exceptionList = new ArrayList<>();

        try {
            List<Map> scorePeriodList = scoreMstMapper.getScorePeriod(map); //학기의 전후반기
            List<CommonCode> comExamList = CommonCodeUtils.get("EXAM_CD"); //시험 리스트
            List<CommonCode> comSbjtList = scoreMstMapper.getSemeSbjt(map); //과목 리스트
            List<ScoreExcel> scoreList = scoreMstMapper.getScoreExcel(map);
            List<AttendanceExcel> totAtdcRate = attendanceMstMapper.getTotalAtdcRate(map);

            SXSSFSheet sheet = wb.createSheet();

            SXSSFRow row;
            SXSSFCell cell;

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
            font.setFontHeightInPoints((short) 14);
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
            cell.setCellValue(map.get("schSemeYear") + "학년도 " + map.get("schSemeNm") + " 성적표");

            // 헤더 생성
            String[] header = {"연번", "학번", "국적", "한글명", "영문명", "성별", "생년월"};
            int[] headerSize = {3, 8, 6, 12, 16, 3, 5};
            sheet.createRow(1);
            sheet.createRow(2);
            sheet.createRow(3);
            sheet.createRow(4);
            for (int i=0; i < header.length; i++) {
                row = sheet.getRow(1);
                cell = row.createCell(i);
                cell.setCellStyle(headStyle);
                cell.setCellValue(header[i]);
                sheet.setColumnWidth(i, headerSize[i] * 512);
                sheet.addMergedRegion(new CellRangeAddress(1, 4, cell.getColumnIndex(), cell.getColumnIndex()));
            }
            sheet.createFreezePane(headerSize.length, 5);   //틀고정

            int startOfPeriod = header.length;

            //과목 헤더 생성
            for(Map periodMap : scorePeriodList) {
                String periodNm = (String)periodMap.get("PERIOD_NM");
                periodMap.put("PERIOD_IDX", startOfPeriod);

                row = sheet.getRow(1);
                cell = row.createCell(startOfPeriod);
                cell.setCellStyle(headStyle);
                cell.setCellValue(periodNm);

                int cellNo = startOfPeriod;
                for(int i=0; i < comExamList.size() + 1; i++) {

                    if (i == comExamList.size()) { //과목 표시 후 전후반기별 결산 헤더 출력
                        periodMap.put("SUM_IDX", cellNo);

                        row = sheet.getRow(1);

                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), startOfPeriod, cellNo-1));

                        cell = row.createCell(cellNo++);
                        cell.setCellStyle(headStyle4);
                        cell.setCellValue("반");
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 3, cell.getColumnIndex(), cell.getColumnIndex()));

                        cell = row.createCell(cellNo++);
                        cell.setCellStyle(headStyle4);
                        cell.setCellValue("총점");
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 3, cell.getColumnIndex(), cell.getColumnIndex()));

                        cell = row.createCell(cellNo++);
                        cell.setCellStyle(headStyle4);
                        cell.setCellValue("통과\n여부");
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 3, cell.getColumnIndex(), cell.getColumnIndex()));

                        cell = row.createCell(cellNo++);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("출석률");
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 3, cell.getColumnIndex(), cell.getColumnIndex()));

                        cell = row.createCell(cellNo++);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue("예정\n급수");
                        sheet.setColumnWidth(cell.getColumnIndex(), 4 * 512);
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 3, cell.getColumnIndex(), cell.getColumnIndex()));

                        startOfPeriod = cell.getColumnIndex() + 1;

                    } else {    //시험 표시
                        CommonCode exam = comExamList.get(i);
                        exam.setData5(cellNo - (int)periodMap.get("PERIOD_IDX"));

                        row = sheet.getRow(2);
                        cell = row.createCell(cellNo);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue(exam.getName());

                        int examIdx = cell.getColumnIndex();

                        //과목 표시
                        for(CommonCode sbjt : comSbjtList) {
                            String sbjtNm = sbjt.getName();
                            String isPerf = sbjt.getData1();

                            row = sheet.getRow(3);
                            cell = row.createCell(cellNo);
                            cell.setCellStyle(headStyle);
                            cell.setCellValue(sbjtNm);

                            if (isPerf != null && isPerf.equals("Y")) {
                                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 2));

                                row = sheet.getRow(4);
                                cell = row.createCell(cellNo++);
                                cell.setCellStyle(headStyle3);
                                cell.setCellValue("시험");

                                cell = row.createCell(cellNo++);
                                cell.setCellStyle(headStyle3);
                                cell.setCellValue("수행");

                                cell = row.createCell(cellNo++);
                                cell.setCellStyle(headStyle);
                                cell.setCellValue("총점");
                            } else {
                                row = sheet.getRow(3);
                                cell = row.createCell(cellNo++);
                                cell.setCellStyle(headStyle);
                                cell.setCellValue(sbjtNm);

                                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum()+1, cell.getColumnIndex(), cell.getColumnIndex()));
                            }
                        }

                        cell = row.createCell(cellNo++);
                        cell.setCellStyle(headStyle4);
                        cell.setCellValue("평균");
                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum()+1, cell.getColumnIndex(), cell.getColumnIndex()));

                        sheet.addMergedRegion(new CellRangeAddress(2, 2, examIdx, cell.getColumnIndex()));
                    }
                }
            }

            //헤더 border style 일괄 생성
            int lastIdx = sheet.getRow(1).getLastCellNum();
            for (int y = 1; y <= 4; y++) {
                SXSSFRow r1 = sheet.getRow(y);

                for (int x = 0; x < lastIdx; x++) {
                    if (r1.getCell(x) == null) {
                        SXSSFCell c1 = r1.createCell(x);
                        c1.setCellStyle(headStyle);
                    }
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastIdx - 1));

            /////////////////////// DATA ///////////////////////////

            for (ScoreExcel examMap : scoreList) {
                if (examMap.getExamCd() == null) { // 성적이 입력되지 않은 학생이 있는지 확인
                    exceptionList.add(examMap);
                }
            }
            if (exceptionList.size() > 0) {
                return; // 있으면 finally
            }

            //성적List를 학생별로 grouping
            Map<ScoreExcel.StdtInfo, List<ScoreExcel>> stdtMap = scoreList.stream().collect(groupingBy(ScoreExcel::getStdtInfo));

            //학생별 출석률 Map
            Map<String, List<AttendanceExcel>> totAtdcRateMap = totAtdcRate.stream().collect(groupingBy(AttendanceExcel::getStdtId));

            int[] idx = { 5, 0 }; //start of {row, cell}
            stdtMap.forEach((stdtInfo, clasList) -> { //학생별 성적 List loop

                SXSSFRow dataRow = sheet.createRow(idx[0]++);
                SXSSFCell dataCell;

                //학생정보 loop
                String[] stdtArr = {String.valueOf(idx[0]-5), stdtInfo.getStdtId(), stdtInfo.getNatnNm(), stdtInfo.getStdtNmKor(), stdtInfo.getStdtNmEng(), stdtInfo.getGenderNm(), stdtInfo.getBirthMt()};
                for (int i=0; i < stdtArr.length; i++) {
                    dataCell = dataRow.createCell(idx[1]++);
                    dataCell.setCellStyle(bodyStyle);
                    dataCell.setCellValue(StringUtils.defaultString(stdtArr[i]));
                }

                //학생별 출석List를 전후반기구분 으로 grouping
                Map<String, List<ScoreExcel>> stdtPeriodMap = clasList.stream()
                        .collect(groupingBy(ScoreExcel::getPeriodCd));

                for (Map periodMap : scorePeriodList) {
                    if (stdtPeriodMap.containsKey(periodMap.get("PERIOD_CD"))) {
                        int periodIdx = (int)periodMap.get("PERIOD_IDX");
                        int sumIdx  = (int)periodMap.get("SUM_IDX");

                        List<ScoreExcel> stdtPeriodList = stdtPeriodMap.get(periodMap.get("PERIOD_CD"));

                        //전후반기List를 시험별로 grouping
                        Map<String, List<ScoreExcel>> stdtExamMap = stdtPeriodList.stream()
                                .collect(groupingBy(ScoreExcel::getExamCd));

                        for (CommonCode exam : comExamList) {
                            if (stdtExamMap.containsKey(exam.getCode())) {
                                int examIdx = periodIdx + exam.getData5();

                                List<ScoreExcel> examList = stdtExamMap.get(exam.getCode());
                                //시험별List를 과목별로 grouping
                                Map<String, List<ScoreExcel>> sbjtMap = examList.stream()
                                        .collect(groupingBy(ScoreExcel::getSbjtId));

                                for (CommonCode comSbjt : comSbjtList) {
                                    if (sbjtMap.containsKey(comSbjt.getCode())) {
                                        List<ScoreExcel> sbjtList = sbjtMap.get(comSbjt.getCode());

                                        if(comSbjt.getData1() != null && comSbjt.getData1().equals("Y")) {
                                            dataCell = dataRow.createCell(examIdx++);
                                            dataCell.setCellStyle(bodyStyle);
                                            dataCell.setCellValue(sbjtList.get(0).getScor().doubleValue());

                                            dataCell = dataRow.createCell(examIdx++);
                                            dataCell.setCellStyle(bodyStyle);
                                            dataCell.setCellValue(sbjtList.get(0).getPerf().doubleValue());

                                            dataCell = dataRow.createCell(examIdx++);
                                            dataCell.setCellStyle(bodyStyle);
                                            dataCell.setCellValue(sbjtList.get(0).getScor().doubleValue() + sbjtList.get(0).getPerf().doubleValue());
                                        } else {
                                            dataCell = dataRow.createCell(examIdx++);
                                            dataCell.setCellStyle(bodyStyle);
                                            dataCell.setCellValue(sbjtList.get(0).getScor().doubleValue());
                                        }
                                    } else {
                                        if(comSbjt.getData1() != null && comSbjt.getData1().equals("Y")) {
                                            examIdx += 3;
                                        } else {
                                            examIdx += 1;
                                        }
                                    }
                                }

                                BigDecimal avgScor = exam.getCode().equals("M") ? examList.get(0).getMidScor() : examList.get(0).getFinScor();
                                dataCell = dataRow.createCell(examIdx++);
                                dataCell.setCellStyle(bodyStyle);
                                dataCell.setCellValue(avgScor.doubleValue());
                            }
                        }

                        dataCell = dataRow.createCell(sumIdx++);
                        dataCell.setCellStyle(bodyStyle);
                        dataCell.setCellValue(stdtPeriodList.get(0).getClasNm());

                        dataCell = dataRow.createCell(sumIdx++);
                        dataCell.setCellStyle(bodyStyle);
                        dataCell.setCellValue(stdtPeriodList.get(0).getTotScor().doubleValue());

                        dataCell = dataRow.createCell(sumIdx++);
                        dataCell.setCellStyle(bodyStyle);
                        dataCell.setCellValue(stdtPeriodList.get(0).getScorNm());

                        dataCell = dataRow.createCell(sumIdx++);
                        dataCell.setCellStyle(bodyStyle);
                        String atdcRate = "";
                        List<AttendanceExcel> atdcList = totAtdcRateMap.get(stdtInfo.getStdtId());
                        if (atdcList != null) {
                            for (AttendanceExcel atdc : atdcList) {
                                if (atdc.getPeriodCd().equals(periodMap.get("PERIOD_CD")))
                                    atdcRate = atdc.getTotRate();
                            }
                        }
                        dataCell.setCellValue(atdcRate);

                        dataCell = dataRow.createCell(sumIdx++);
                        dataCell.setCellStyle(bodyStyle);
                    }
                }

                idx[1] = 0;
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (exceptionList.size() > 0) {
                wb.removeSheetAt(0);
                SXSSFSheet sheet = wb.createSheet();
                SXSSFRow row = sheet.createRow(0);
                SXSSFCell cell = row.createCell(0);
                cell.setCellValue("성적이 입력되지 않은 학생이 존재합니다.");

                int idx = 2;
                for (ScoreExcel scoreExcel : exceptionList) {   // 성적 입력 누락 학생 list
                    row = sheet.createRow(idx++);

                    cell = row.createCell(0);
                    cell.setCellValue(scoreExcel.getClasNm());
                    cell = row.createCell(1);
                    cell.setCellValue(scoreExcel.getStdtId());
                    cell = row.createCell(2);
                    cell.setCellValue(scoreExcel.getStdtNmKor());
                    cell = row.createCell(3);
                    cell.setCellValue(scoreExcel.getStdtNmEng());
                }
            }

            String fileName = java.net.URLEncoder.encode(map.get("schSemeYear") + "학년도 " + map.get("schSemeNm") + " 성적표", "UTF-8");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);   //application/vnd.ms-excel
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

            wb.write(response.getOutputStream());

            wb.dispose();
            wb.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }


    @SuppressWarnings("Duplicates")
    public void scoreUpload(File file) {
        XSSFWorkbook wb;

        try {
            wb = (XSSFWorkbook) WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);


            for (int i=0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String semeYear = "2019";
                Long semeSeq = 2L;
                String periodCd = "N";
                Long clasSeq = Long.valueOf(row.getCell(0).getStringCellValue());
                String stdtId = row.getCell(1).getStringCellValue();

                ScoreMst mst = new ScoreMst();
                List<ScoreDtl> dtlList = new ArrayList();

                mst.setSemeYear(semeYear);
                mst.setSemeSeq(semeSeq);
                mst.setPeriodCd(periodCd);
                mst.setClasSeq(clasSeq);
                mst.setStdtId(stdtId);

                ScoreDtl dtl = new ScoreDtl();
                dtl.setSemeYear(semeYear);
                dtl.setSemeSeq(semeSeq);
                dtl.setPeriodCd(periodCd);
                dtl.setClasSeq(clasSeq);
                dtl.setStdtId(stdtId);
                dtl.setSbjtId("S");
                dtl.setExamCd("M");
                dtl.setScor(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));
                dtl.setPerf(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
                dtlList.add(dtl);

                dtl = new ScoreDtl();
                dtl.setSemeYear(semeYear);
                dtl.setSemeSeq(semeSeq);
                dtl.setPeriodCd(periodCd);
                dtl.setClasSeq(clasSeq);
                dtl.setStdtId(stdtId);
                dtl.setSbjtId("W");
                dtl.setExamCd("M");
                dtl.setScor(BigDecimal.valueOf(row.getCell(4).getNumericCellValue()));
                dtl.setPerf(BigDecimal.valueOf(row.getCell(5).getNumericCellValue()));
                dtlList.add(dtl);

                dtl = new ScoreDtl();
                dtl.setSemeYear(semeYear);
                dtl.setSemeSeq(semeSeq);
                dtl.setPeriodCd(periodCd);
                dtl.setClasSeq(clasSeq);
                dtl.setStdtId(stdtId);
                dtl.setSbjtId("L");
                dtl.setExamCd("M");
                dtl.setScor(BigDecimal.valueOf(row.getCell(6).getNumericCellValue()));
                dtl.setPerf(BigDecimal.valueOf(0));
                dtlList.add(dtl);

                dtl = new ScoreDtl();
                dtl.setSemeYear(semeYear);
                dtl.setSemeSeq(semeSeq);
                dtl.setPeriodCd(periodCd);
                dtl.setClasSeq(clasSeq);
                dtl.setStdtId(stdtId);
                dtl.setSbjtId("R");
                dtl.setExamCd("M");
                dtl.setScor(BigDecimal.valueOf(row.getCell(7).getNumericCellValue()));
                dtl.setPerf(BigDecimal.valueOf(0));
                dtlList.add(dtl);

//                dtl = new ScoreDtl();
//                dtl.setSemeYear(semeYear);
//                dtl.setSemeSeq(semeSeq);
//                dtl.setPeriodCd(periodCd);
//                dtl.setClasSeq(clasSeq);
//                dtl.setStdtId(stdtId);
//                dtl.setSbjtId("S");
//                dtl.setExamCd("F");
//                dtl.setScor(BigDecimal.valueOf(row.getCell(8).getNumericCellValue()));
//                dtl.setPerf(BigDecimal.valueOf(row.getCell(9).getNumericCellValue()));
//                dtlList.add(dtl);
//
//                dtl = new ScoreDtl();
//                dtl.setSemeYear(semeYear);
//                dtl.setSemeSeq(semeSeq);
//                dtl.setPeriodCd(periodCd);
//                dtl.setClasSeq(clasSeq);
//                dtl.setStdtId(stdtId);
//                dtl.setSbjtId("W");
//                dtl.setExamCd("F");
//                dtl.setScor(BigDecimal.valueOf(row.getCell(10).getNumericCellValue()));
//                dtl.setPerf(BigDecimal.valueOf(row.getCell(11).getNumericCellValue()));
//                dtlList.add(dtl);
//
//                dtl = new ScoreDtl();
//                dtl.setSemeYear(semeYear);
//                dtl.setSemeSeq(semeSeq);
//                dtl.setPeriodCd(periodCd);
//                dtl.setClasSeq(clasSeq);
//                dtl.setStdtId(stdtId);
//                dtl.setSbjtId("L");
//                dtl.setExamCd("F");
//                dtl.setScor(BigDecimal.valueOf(row.getCell(12).getNumericCellValue()));
//                dtl.setPerf(BigDecimal.valueOf(0));
//                dtlList.add(dtl);
//
//                dtl = new ScoreDtl();
//                dtl.setSemeYear(semeYear);
//                dtl.setSemeSeq(semeSeq);
//                dtl.setPeriodCd(periodCd);
//                dtl.setClasSeq(clasSeq);
//                dtl.setStdtId(stdtId);
//                dtl.setSbjtId("R");
//                dtl.setExamCd("F");
//                dtl.setScor(BigDecimal.valueOf(row.getCell(13).getNumericCellValue()));
//                dtl.setPerf(BigDecimal.valueOf(0));
//                dtlList.add(dtl);

                save(mst);
                scoreDtlService.save(dtlList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}