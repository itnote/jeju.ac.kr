package com.infomind.axboot.domain.counseling;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import com.infomind.axboot.domain.classDtl.ClassDtl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface CounselingMapper extends MyBatisMapper {

    List<ClassDtl> getCounList(HashMap map);
    List<Counseling> getStudentCounList(HashMap map);
    void deleteCoun(Counseling counseling);
    List<CounselingExcel> tchrCounExcel(HashMap map);

}
