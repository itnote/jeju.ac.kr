package com.infomind.axboot.domain.excel;

import com.infomind.axboot.domain.student.Student;
import com.infomind.axboot.domain.lang.Lang;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExcelReadService {
//    @RequiredArgsConstructor
//    private enum CellHeader {
//        STDT_ID(0), LANG_KOR(1), LANG_ENG(2), LANG_CHN(3), NATN_CD(4), BIRTH_DT(5), GENDER(6), HP_NO(7), MSG_CD1(8), MSG_ID1(9),
//        MSG_CD2(10), MSG_ID2(11), EMAIL(12);
//
//        final Integer columnIndex;
//    }


    public List<Student> excelFileRead(File destFile) {
//        ExcelReadOption readOption = new ExcelReadOption();
//        readOption.setFilePath(destFile.getAbsolutePath());
//        readOption.setOutputColumns("A", "B", "C", "D", "E",
//                "F", "G", "H", "I", "J", "K", "L", "M");
//        readOption.setStartRow(2);
//        List<Map<String, String>> excelContent = ExcelRead.read(readOption);
//        Student jnuStudent = new Student();
//        List<Student> studentList = new ArrayList<Student>();
//        Lang lang = new Lang();
//        for(Map<String, String> student: excelContent) {
//            jnuStudent.setStdtId(student.get("A"));
//            lang.setKor(student.get("B"));
//            lang.setEng(student.get("C"));
//            lang.setChn(student.get("D"));
//            jnuStudent.setLang(lang);
//            jnuStudent.setNatnCd(student.get("E"));
//            jnuStudent.setBirthDt(student.get("F"));
//            jnuStudent.setGender(student.get("G"));
//            jnuStudent.setHpNo(student.get("H"));
//            jnuStudent.setMsgCd1(student.get("I"));
//            jnuStudent.setMsgId1(student.get("J"));
//            jnuStudent.setMsgCd2(student.get("K"));
//            jnuStudent.setMsgId2(student.get("L"));
//            jnuStudent.setEmail(student.get("M"));
//            studentList.add(jnuStudent);
//            log.info("import *******" + student.get("A"));
//            log.info("import *******" + student.get("B"));
//            log.info("import *******" + student.get("C"));
//            log.info("import *******" + student.get("D"));
//            log.info("import *******" + student.get("E"));
//            log.info("import *******" + student.get("F"));
//            log.info("import *******" + student.get("G"));
//            log.info("import *******" + student.get("H"));
//            log.info("import *******" + student.get("I"));
//            log.info("import *******" + student.get("J"));
//            log.info("import *******" + student.get("K"));
//            log.info("import *******" + student.get("L"));
//            log.info("import *******" + student.get("M"));
//
//        }
        return null;

    }
}
