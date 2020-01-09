package com.infomind.axboot.domain.certificate;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CertificateMapper extends MyBatisMapper {

    List<Certificate> enrollmentList(Map map);
    List<Certificate> completionList(Map map);
    List<Transcript> transcriptList(Map map);
    List<Certificate> tuitionList(Map map);
    List<TuitionExcel> tuitionExcelList(Map map);
    List<Certificate> docLogList(Map map);


}
