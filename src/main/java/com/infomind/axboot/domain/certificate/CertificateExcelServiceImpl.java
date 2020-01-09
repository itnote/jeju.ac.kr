package com.infomind.axboot.domain.certificate;

import com.infomind.axboot.domain.excel.CommonExcelService;
import com.infomind.axboot.domain.excel.ExcelRead;
import com.infomind.axboot.domain.excel.ExcelReadOption;
import com.infomind.axboot.domain.semester.SemesterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CertificateExcelServiceImpl implements CommonExcelService {

    @Override
    public List<Certificate> excelFileRead(File destFile) {
        List<Certificate> certificateList = new ArrayList<>();

        for(Map<String, String> certificate: setReadOption(destFile)) {
            Certificate jnuCertifivate = new Certificate();

            jnuCertifivate.setStdtId(certificate.get("A"));
            jnuCertifivate.setStdtNmKor(certificate.get("B"));
            jnuCertifivate.setStdtNmEng(certificate.get("C"));
            jnuCertifivate.setStdtNmChn(certificate.get("D"));
            jnuCertifivate.setNatnNm(certificate.get("E"));
            jnuCertifivate.setBirthDt(certificate.get("F"));
            jnuCertifivate.setGenderNm(certificate.get("G"));
            jnuCertifivate.setFreshYn(certificate.get("H"));
            jnuCertifivate.setFeeDt(certificate.get("I"));
            jnuCertifivate.setDormPayYn(certificate.get("J"));
            jnuCertifivate.setAppPayYn(certificate.get("K"));
            jnuCertifivate.setInsPayYn(certificate.get("L"));
            jnuCertifivate.setBedPayYn(certificate.get("M"));


            certificateList.add(jnuCertifivate);
        }

        return certificateList;
    }

    @Override
    public List<Map<String, String>> setReadOption(File destFile) {
        ExcelReadOption readOption = new ExcelReadOption();
        readOption.setFilePath(destFile.getAbsolutePath());
        // 엑셀 파일 .. Cell...-_-
        readOption.setOutputColumns("A", "B", "C", "D", "E",
                "F", "G", "H", "I","J","K","L","M");
        readOption.setStartRow(2);
        List<Map<String, String>> excelContent = ExcelRead.read(readOption);
        return excelContent;
    }
}

