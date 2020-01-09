package com.infomind.axboot.domain.student;

import com.infomind.axboot.domain.image.ImageService;
import com.infomind.axboot.domain.lang.Lang;
import com.infomind.axboot.domain.lang.LangService;
import com.infomind.axboot.domain.stdtlog.StdtLog;
import com.infomind.axboot.domain.stdtlog.StdtLogService;
import com.infomind.axboot.utils.ExistsUtils;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.chequer.axboot.core.parameter.RequestParams;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Slf4j
public class StudentService extends BaseService<Student, String> {
    private StudentRepository studentRepository;

    @Inject
    private LangService langService;
    @Inject
    private StdtLogService stdtLogService;
    @Inject
    private ImageService imageService;

    @Inject
    private StudentMapper studentMapper;

    @Inject
    public StudentService(StudentRepository studentRepository) {
        super(studentRepository);
        this.studentRepository = studentRepository;
    }


    public List<Student> get(RequestParams requestParams) {
        String stdtId = requestParams.getString("stdtId", "");
        String name = requestParams.getString("lang.kor", "");
        String engName = requestParams.getString("lang.eng", "");
        String filter = requestParams.getString("filter");
        BooleanBuilder builder = new BooleanBuilder();

        if (isNotEmpty(stdtId)) {
            builder.and(qStudent.stdtId.eq(stdtId));
        }

//        if (isNotEmpty(name)) {
//            builder.and(qLang.kor.eq(name));
//        }
//
//        if (isNotEmpty(engName)) {
//            builder.and(qLang.eng.eq(engName));
//        }

          List<Student> list = select().from(qStudent).where(builder).orderBy(qStudent.stdtId.asc()).fetch();
        if (isNotEmpty(filter)) {
            list = filter(list, filter);
        }

        return list;
    }



    @Transactional
    public void saveStudent(List<Student> students) throws IOException {
        StdtLog stdtLog = new StdtLog();
        for (Student student : students) {
            String stdtId = student.getStdtId();
            Long stdtNm = student.getStdtNm();
            Lang lang = new Lang();

            Student stdtOld = findOne(stdtId);
            if (stdtOld != null) {
                stdtNm = stdtOld.getStdtNm();
                String oldAddr = ExistsUtils.isExistsString(stdtOld.getAddr());
                String oldHpno = ExistsUtils.isExistsString(stdtOld.getHpNo());

                if (!oldAddr.equals(student.getAddr()) && !oldAddr.equals("")) {
                    stdtLog.setStdtId(stdtId);
                    stdtLog.setLogData(oldAddr);
                    stdtLog.setStdtLogCd("A");
                    stdtLogService.save(stdtLog);
                }
                if (!oldHpno.equals(student.getHpNo()) && !oldHpno.equals("")) {
                    stdtLog.setStdtId(stdtId);
                    stdtLog.setLogData(oldHpno);
                    stdtLog.setStdtLogCd("T");
                    stdtLogService.save(stdtLog);
                }
            }

            // DATE컬럼에 ""들어올 시 에러 방지
            student.setBirthDt( student.getBirthDt() != null && !student.getBirthDt().equals("") ? student.getBirthDt() : null );

            lang.setLangKey(stdtNm);
            lang.setKor(student.getStdtNmKor());
            lang.setEng(student.getStdtNmEng());
            lang.setChn(student.getStdtNmChn());
            langService.save(lang);

            student.setLang(lang);
            student.setStdtNm(lang.getLangKey());
            save(student);
        }
    }

    public List<Student> getStudentList(RequestParams<Student> requestParams) {
        String stdtId =requestParams.getString("stdtId","");
        String schStdt =requestParams.getString("schStdt","");
        String useYn =requestParams.getString("useYn","");
        String schSemeSeq =requestParams.getString("schSemeSeq","");
        String schSemeYear =requestParams.getString("schSemeYear","");
        String schPeriodCd =requestParams.getString("schPeriodCd","");
        String isClassMgmt = requestParams.getString("isClassMgmt", "");

        HashMap map = new HashMap<String, Object>();
        map.put("stdtId", stdtId);
        map.put("schStdt", schStdt);
        map.put("useYn", useYn);
        map.put("schSemeSeq", schSemeSeq);
        map.put("schSemeYear", schSemeYear);
        map.put("schPeriodCd", schPeriodCd);
        map.put("isClassMgmt", isClassMgmt);
        return studentMapper.getStudentList(map);
    }

    public List<Student> getCounStudentList(RequestParams<Student> requestParams) {
        String schStdt =requestParams.getString("schStdt","");
        String schTchr =requestParams.getString("schTchr","");
        String openYn =requestParams.getString("openYn","");
        String schSemeSeq =requestParams.getString("schSemeSeq","");
        String schSemeYear =requestParams.getString("schSemeYear","");
        String schPeriodCd =requestParams.getString("schPeriodCd","");
        String schClasSeq =requestParams.getString("schClasSeq","");

        HashMap map = new HashMap<String, Object>();

        map.put("schStdt", schStdt);
        map.put("openYn", openYn);
        map.put("schSemeSeq", schSemeSeq);
        map.put("schSemeYear", schSemeYear);
        map.put("schPeriodCd", schPeriodCd);
        map.put("schClasSeq", schClasSeq);
        map.put("schTchr", schTchr);

        return studentMapper.getCounStudentList(map);
    }


    public void studentExcel(Map sch, HttpServletResponse response) throws IOException {
        XSSFWorkbook wb = null;

        File file = new File(sch.get("path") + "/assets/excel/student.xlsx");
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString());
        Files.copy(file.toPath(), tmpFile.toPath());

        try {
            wb = (XSSFWorkbook) WorkbookFactory.create(tmpFile);

            RequestParams<Student> schMap = new RequestParams<>();

            schMap.put("schSemeYear", sch.get("schSemeYear"));
            schMap.put("schStdt", sch.get("schStdt"));
            schMap.put("schSemeSeq", sch.get("schSemeSeq"));
            schMap.put("schPeriodCd", sch.get("schPeriodCd"));
            schMap.put("isClassMgmt", sch.get("isClassMgmt"));

            List<Student> studentList = getStudentList(schMap); //전체 학생 조회


            Sheet sheet = wb.getSheetAt(0);
            Row row;
            Cell cell;
            int rowNo = 1;

            for (Student stdt : studentList) {
                //데이터 생성
                String[] values = { stdt.getStdtId(), stdt.getStdtNmKor(), stdt.getStdtNmEng(), stdt.getStdtNmChn(), stdt.getNatnCd(), stdt.getBirthDt()
                                    , stdt.getGender(), stdt.getHpNo(), stdt.getMsgCd1(), stdt.getMsgId1(), stdt.getMsgCd2(), stdt.getMsgId2(), stdt.getEmail()
                                    , stdt.getAddr(), stdt.getNatnAddr(), stdt.getBankCd(), stdt.getAccountNo(), stdt.getStudyPeriod(), stdt.getTopikCd(), stdt.getKorLv(), stdt.getVisaType() };
                row = sheet.createRow(rowNo++);
                for (int i=0; i < values.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(values[i]);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
            String fileName = java.net.URLEncoder.encode("학생현황_" + sdf.format(System.currentTimeMillis()), "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");   //application/vnd.ms-excel
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName +".xlsx");

            wb.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tmpFile.delete();
            wb.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

}