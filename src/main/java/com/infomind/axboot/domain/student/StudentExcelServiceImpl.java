package com.infomind.axboot.domain.student;

import com.infomind.axboot.domain.excel.CommonExcelService;
import com.infomind.axboot.domain.excel.ExcelRead;
import com.infomind.axboot.domain.excel.ExcelReadOption;
import com.infomind.axboot.domain.lang.Lang;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StudentExcelServiceImpl implements CommonExcelService {

    @Override
    public List<Student> excelFileRead(File destFile) {
        List<Student> studentList = new ArrayList<>();

        for(Map<String, String> student: setReadOption(destFile)) {
            Student jnuStudent = new Student();
//            Lang lang = new Lang();
            log.info("test");
//            lang.setKor(student.get("B"));
//            lang.setEng(student.get("C"));
//            lang.setChn(student.get("D"));
//            jnuStudent.setLang(lang);
            jnuStudent.setStdtId(student.get("A"));
            jnuStudent.setStdtNmKor(student.get("B"));
            jnuStudent.setStdtNmEng(student.get("C"));
            jnuStudent.setStdtNmChn(student.get("D"));
            jnuStudent.setNatnCd(student.get("E"));
            jnuStudent.setBirthDt(student.get("F"));
            jnuStudent.setGender(student.get("G"));
            jnuStudent.setHpNo(student.get("H"));
            jnuStudent.setMsgCd1(student.get("I"));
            jnuStudent.setMsgId1(student.get("J"));
            jnuStudent.setMsgCd2(student.get("K"));
            jnuStudent.setMsgId2(student.get("L"));
            jnuStudent.setEmail(student.get("M"));
            jnuStudent.setAddr(student.get("N"));
            jnuStudent.setNatnAddr(student.get("O"));
            jnuStudent.setBankCd(student.get("P"));
            jnuStudent.setAccountNo(student.get("Q"));
            jnuStudent.setStudyPeriod(student.get("R"));
            jnuStudent.setTopikCd(student.get("S"));
            jnuStudent.setKorLv(student.get("T"));
            jnuStudent.setVisaType(student.get("U"));
            jnuStudent.setUseYn("Y");
            studentList.add(jnuStudent);
        }

        return studentList;
    }

    @Override
    public List<Map<String, String>> setReadOption(File destFile) {
        ExcelReadOption readOption = new ExcelReadOption();
        readOption.setFilePath(destFile.getAbsolutePath());

        readOption.setOutputColumns("A", "B", "C", "D", "E",
                "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U");
        readOption.setStartRow(2);
        List<Map<String, String>> excelContent = ExcelRead.read(readOption);
        return excelContent;
    }
}
