package com.infomind.axboot.domain.classDtl;

import com.infomind.axboot.domain.excel.CommonExcelService;
import com.infomind.axboot.domain.excel.ExcelRead;
import com.infomind.axboot.domain.excel.ExcelReadOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ClassDtlExcelServiceImpl implements CommonExcelService {

    @Override
    public List<ClassDtl> excelFileRead(File destFile) {
        List<ClassDtl> classDtlList = new ArrayList<>();

        for(Map<String, String> classDtl: setReadOption(destFile)) {
            ClassDtl jnuClassDtl = new ClassDtl();
            jnuClassDtl.setSemeYear("");
            jnuClassDtl.setSemeSeq("");
            jnuClassDtl.setPeriodCd("");
            jnuClassDtl.setClasSeq("");

            jnuClassDtl.setStdtId(classDtl.get("A"));
            jnuClassDtl.setStdtNm(classDtl.get("B"));
            jnuClassDtl.setStatusCd(classDtl.get("C"));
            jnuClassDtl.setFreshYn(classDtl.get("D"));
            jnuClassDtl.setDormPayYn(classDtl.get("E"));
            jnuClassDtl.setAppPayYn(classDtl.get("F"));
            jnuClassDtl.setInsPayYn(classDtl.get("G"));
            jnuClassDtl.setBedPayYn(classDtl.get("H"));
            jnuClassDtl.setPrePayYn(classDtl.get("I"));
            jnuClassDtl.setFeeDt(classDtl.get("J"));
            jnuClassDtl.setDiscCd(classDtl.get("K"));
            if (classDtl.get("L") != null && !classDtl.get("L").equals("")) {
                jnuClassDtl.setFeeAmt(Integer.valueOf(classDtl.get("L")));
            }
            if (classDtl.get("M") != null && !classDtl.get("M").equals("")) {
                jnuClassDtl.setDormAmt(Integer.valueOf(classDtl.get("M")));
            }
            jnuClassDtl.setGeneralReview(classDtl.get("N"));

            classDtlList.add(jnuClassDtl);
        }

        return classDtlList;
    }

    @Override
    public List<Map<String, String>> setReadOption(File destFile) {
        ExcelReadOption readOption = new ExcelReadOption();
        readOption.setFilePath(destFile.getAbsolutePath());
        readOption.setOutputColumns("A", "B", "C", "D", "E",
                "F","G","H","I","J","K","L","M","N");
        readOption.setStartRow(2);
        List<Map<String, String>> excelContent = ExcelRead.read(readOption);
        return excelContent;
    }
}
