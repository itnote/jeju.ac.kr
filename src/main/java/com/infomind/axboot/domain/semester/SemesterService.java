package com.infomind.axboot.domain.semester;

import com.infomind.axboot.domain.lang.Lang;
import com.infomind.axboot.domain.lang.LangService;
import com.infomind.axboot.utils.SessionUtils;
import com.querydsl.core.BooleanBuilder;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SemesterService extends BaseService<Semester, Semester.SemesterId> {
    private SemesterRepository semesterRepository;

    @Inject
    private LangService langService;

    @Inject
    private SemesterMapper semeMapper;

    @Inject
    public SemesterService(SemesterRepository semesterRepository) {
        super(semesterRepository);
        this.semesterRepository = semesterRepository;
    }

    public List<Semester> gets(RequestParams<Semester> requestParams) {
        String semeYear =requestParams.getString("semeYear","");
        String semeCd = requestParams.getString("semeCd","");
       // String filter = requestParams.getString("filter");

        BooleanBuilder builder = new BooleanBuilder();

        // 필터 매개변수가 없으면 각 매개변수가 있는지 확인하고, 전달된 매개변수가 있으면 검색조건에 추가.
        if (isNotEmpty(semeYear)) {
            builder.and(qSemester.semeYear.eq(semeYear));
        }
        if (isNotEmpty(semeCd)) {
            builder.and(qSemester.semeCd.eq(semeCd));
        }

        List<Semester> list = select()
                .from(qSemester)
                .where(builder)
                .orderBy(qSemester.semeYear.asc(), qSemester.semeCd.asc())
                .fetch();

   /*     if (isNotEmpty(filter)) {
            list = filter(list, filter);
        }*/


        return  list;
    }

    public List<Semester> getSemesterMgmtList(RequestParams<Semester> requestParams) {
        String semeYear =requestParams.getString("semeYear","");
        String semeCd = requestParams.getString("semeCd","");
        String semeSeq = requestParams.getString("semeSeq","");

        HashMap map = new HashMap<String, Object>();
        map.put("semeYear", semeYear);
        map.put("semeCd", semeCd);
        map.put("semeSeq", semeSeq);

        return semeMapper.getSemesterMgmtList(map);
    }

    public List<Map> getSemeYearCombo(RequestParams<Map> requestParams) {
        String userCd = SessionUtils.getCurrentLoginUserCd();
        String myClasChk = requestParams.getString("schMyClasChk","");
        HashMap map = new HashMap<String, Object>();
        map.put("userCd", userCd);
        map.put("schMyClasChk", myClasChk);
        return semeMapper.getSemeYearCombo(map);
    }

    public List<Map> getSemeSeqCombo(RequestParams<Map> requestParams) {
;       String userCd = SessionUtils.getCurrentLoginUserCd();
        String semeYear =requestParams.getString("schSemeYear","");

        String myClasChk = requestParams.getString("schMyClasChk","");

        HashMap map = new HashMap<String, Object>();
        map.put("schSemeYear", semeYear);
        map.put("userCd", userCd);
        map.put("schMyClasChk", myClasChk);

        return semeMapper.getSemeSeqCombo(map);
    }

    public List<Semester> getSeq(String semeYear) {

        return select()
                .from(qSemester)
                .where(qSemester.semeYear.eq(semeYear))
                .orderBy(qSemester.semeSeq.desc())
                .limit(1)
                .fetch();

    }


    @Transactional
    public Long semeSave(List<Semester> list) {
        Semester semester = list.get(0);
        semester.setEndDt(semester.getEndDt().replaceAll("-",""));
        semester.setStartDt(semester.getStartDt().replaceAll("-",""));
        List<Semester> semesterSeqList  = getSeq(semester.getSemeYear());


        if(semesterSeqList.size() == 0){
            semester.setSemeSeq(Long.valueOf(1));
        }

        if(semester.getSemeSeq() == null){
                semester.setSemeSeq(semesterSeqList.get(0).getSemeSeq()+1);
        }

        Long corsNm = semester.getCorsNm();
        Lang lang = new Lang();
        lang.setKor(semester.getKor());
        lang.setEng(semester.getEng());
        lang.setChn(semester.getChn());


        if (corsNm != null) {
            lang.setLangKey(corsNm);
            langService.save(lang);
        } else {
            langService.save(lang);
            semester.setCorsNm(lang.getLangKey());
        }
        save(list);

        return semester.getSemeSeq();
    }


    @SuppressWarnings("Duplicates")
    public void scheduleExcel(Semester seme, HttpServletResponse response) throws IOException {
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
            font.setFontHeightInPoints((short) 20);
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
            font.setFontHeightInPoints((short) 14);
            bodyStyle.setFont(font);

            // body green style
            CellStyle bodyStyleG = wb.createCellStyle();
            bodyStyleG.setBorderTop(CellStyle.BORDER_THIN);
            bodyStyleG.setBorderBottom(CellStyle.BORDER_THIN);
            bodyStyleG.setBorderLeft(CellStyle.BORDER_THIN);
            bodyStyleG.setBorderRight(CellStyle.BORDER_THIN);
            bodyStyleG.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
            bodyStyleG.setFillPattern(CellStyle.SOLID_FOREGROUND);
            bodyStyleG.setAlignment(CellStyle.ALIGN_CENTER);
            bodyStyleG.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            font = wb.createFont();
            font.setFontHeightInPoints((short) 14);
            bodyStyleG.setFont(font);

            // body red style
            CellStyle bodyStyleR = wb.createCellStyle();
            bodyStyleR.setBorderTop(CellStyle.BORDER_THIN);
            bodyStyleR.setBorderBottom(CellStyle.BORDER_THIN);
            bodyStyleR.setBorderLeft(CellStyle.BORDER_THIN);
            bodyStyleR.setBorderRight(CellStyle.BORDER_THIN);
            bodyStyleR.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
            bodyStyleR.setFillPattern(CellStyle.SOLID_FOREGROUND);
            bodyStyleR.setAlignment(CellStyle.ALIGN_CENTER);
            bodyStyleR.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            font = wb.createFont();
            font.setFontHeightInPoints((short) 14);
            bodyStyleR.setFont(font);

            // body holiday style
            CellStyle bodyStyleHoli = wb.createCellStyle();
            bodyStyleHoli.setBorderTop(CellStyle.BORDER_THIN);
            bodyStyleHoli.setBorderBottom(CellStyle.BORDER_THIN);
            bodyStyleHoli.setBorderLeft(CellStyle.BORDER_THIN);
            bodyStyleHoli.setBorderRight(CellStyle.BORDER_THIN);
            bodyStyleHoli.setFillForegroundColor(HSSFColor.WHITE.index);
            bodyStyleHoli.setFillPattern(CellStyle.SOLID_FOREGROUND);
            bodyStyleHoli.setAlignment(CellStyle.ALIGN_CENTER);
            bodyStyleHoli.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            font = wb.createFont();
            font.setFontHeightInPoints((short) 14);
            font.setColor(Font.COLOR_RED);
            bodyStyleHoli.setFont(font);

            List<SemesterScheduleExcel> scheList = semeMapper.scheduleExcel(seme);
            List<SemesterScheduleExcel> fList = new ArrayList<>();  //전반기 리스트
            List<SemesterScheduleExcel> rList = new ArrayList<>();  //후반기 리스트
            List<List<SemesterScheduleExcel>> periodList = new ArrayList<>();

            for (SemesterScheduleExcel sche : scheList) {
                if (sche.getQuarterDiv().contains("F")) {
                    fList.add(sche);
                }
                if (sche.getQuarterDiv().contains("R")) {
                    rList.add(sche);
                }
            }
            periodList.add(fList);
            periodList.add(rList);

            int periodIdx = 0;
            String[] periodNm = {"전반기", "후반기"};
            for (List<SemesterScheduleExcel> list : periodList) {
                if (list.size() > 0) {
                    Sheet sheet = wb.createSheet(seme.getSemeYear() + " " + seme.getKor() + " " + periodNm[periodIdx++]);

                    Row row;
                    Cell cell;
                    int rowNo = 0;

                    // 제목 생성
                    row = sheet.createRow(rowNo++);
                    cell = row.createCell(0);
                    cell.setCellStyle(titleStyle);
                    cell.setCellValue(seme.getSemeYear() + " " + seme.getKor() + " 전반기 일정표");
                    sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));

                    // 헤더 생성
                    String[] header = {"주", "일", "월", "화", "수", "목", "금", "토"};
                    row = sheet.createRow(rowNo++);
                    for (int i = 0; i < header.length; i++) {
                        cell = row.createCell(i);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue(header[i]);
                    }

                    //데이터 생성
                    int idx = 1;
                    int rIdx = 0;
                    for (SemesterScheduleExcel data : list) {
                        rowNo = 2 + rIdx++ * 3;
                        row = sheet.createRow(rowNo);

                        cell = row.createCell(0);
                        cell.setCellStyle(bodyStyle);
                        cell.setCellValue(idx++);

                        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, 0, 0));

                        // [ "날짜", "수업구분", "스케쥴명", "휴일여부" ]
                        String[] sun = data.getSun() != null ? data.getSun().split("\\|") : null;
                        String[] mon = data.getMon() != null ? data.getMon().split("\\|") : null;
                        String[] tue = data.getTue() != null ? data.getTue().split("\\|") : null;
                        String[] wed = data.getWed() != null ? data.getWed().split("\\|") : null;
                        String[] thu = data.getThu() != null ? data.getThu().split("\\|") : null;
                        String[] fri = data.getFri() != null ? data.getFri().split("\\|") : null;
                        String[] sat = data.getSat() != null ? data.getSat().split("\\|") : null;

                        List<String[]> weekList = new ArrayList<>();
                        weekList.add(sun);
                        weekList.add(mon);
                        weekList.add(tue);
                        weekList.add(wed);
                        weekList.add(thu);
                        weekList.add(fri);
                        weekList.add(sat);

                        Row row1 = sheet.createRow(rowNo + 1);
                        Row row2 = sheet.createRow(rowNo + 2);
                        Cell cell1;
                        Cell cell2;

                        int weekIdx = 1;
                        for (String[] arr : weekList) {
                            if (arr != null) {
                                cell = row.createCell(weekIdx);
                                cell.setCellStyle(bodyStyle);
                                cell.setCellValue(arr[0]);

                                cell1 = row1.createCell(weekIdx);
                                cell1.setCellStyle(bodyStyleG);

                                cell2 = row2.createCell(weekIdx);
                                if (arr[1].equals("A2")) {
                                    cell2.setCellStyle(bodyStyleR);
                                } else if (arr[1].equals("N0")) {
                                    cell1.setCellValue("");
                                    cell.setCellStyle(bodyStyleHoli);
                                    cell1.setCellStyle(bodyStyleHoli);
                                    cell2.setCellStyle(bodyStyleHoli);
                                    sheet.addMergedRegion(new CellRangeAddress(row1.getRowNum(), row1.getRowNum() + 1, weekIdx, weekIdx));
                                } else {
                                    cell2.setCellStyle(bodyStyle);
                                    sheet.addMergedRegion(new CellRangeAddress(row1.getRowNum(), row1.getRowNum() + 1, weekIdx, weekIdx));
                                }

                                if (arr.length > 2) {   // 스케쥴이 있으면 표시
                                    cell1.setCellValue(arr[2]);

                                    if (arr[3].equals("Y")) {   // 공휴일이면 스타일 변경
                                        cell.setCellStyle(bodyStyleHoli);
                                        cell1.setCellStyle(bodyStyleHoli);
                                        if (arr[1].equals("A2")) {
                                            sheet.addMergedRegion(new CellRangeAddress(row1.getRowNum(), row1.getRowNum() + 1, weekIdx, weekIdx));
                                        }
                                    }
                                }
                            } else {
                                cell = row.createCell(weekIdx);
                                cell.setCellStyle(bodyStyle);
                                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, weekIdx, weekIdx));
                            }
                            weekIdx++;
                        }
                    }

                    //컬럼 사이즈 설정
                    int[] width = {4, 13, 13, 13, 13, 13, 13, 13};
                    for (int colNum = 0; colNum < width.length; colNum++) {
                        sheet.setColumnWidth(colNum, width[colNum] * 512);
                    }

                    //border style 누락 cell 일괄 생성
                    int lastIdx = sheet.getRow(1).getLastCellNum();
                    for (int y = 2; y <= sheet.getLastRowNum(); y++) {
                        Row r1 = sheet.getRow(y);

                        for (int x = 0; x < lastIdx; x++) {
                            if (r1.getCell(x) == null) {
                                Cell c1 = r1.createCell(x);
                                c1.setCellStyle(bodyStyle);
                            }
                        }
                    }
                }
            }

            String fileName = java.net.URLEncoder.encode(seme.getSemeNm() + "_" + seme.getKor() + "_" + "_일정표", "UTF-8");
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

    public Semester getSemesterAmt(String semeYear, String semeSeq) {

        HashMap map = new HashMap<String, Object>();
        map.put("schSemeYear", semeYear);
        map.put("schSemeSeq", semeSeq);

        return semeMapper.getSemesterAmt(map);
    }
}