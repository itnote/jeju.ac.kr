package com.infomind.axboot.domain.docLog;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import com.infomind.axboot.domain.certificate.Certificate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DocLogMapper extends MyBatisMapper {

    void insertDocLog(DocLog docLog);

}
