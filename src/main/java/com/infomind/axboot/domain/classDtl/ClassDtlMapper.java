package com.infomind.axboot.domain.classDtl;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import com.infomind.axboot.domain.classMst.ClassMst;
import com.infomind.axboot.domain.student.Student;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ClassDtlMapper extends MyBatisMapper {

    List<ClassDtl> getClassStudentList(HashMap map);
    List<ClassDtl> getClasDtlList(HashMap map);
    List<ClassDtl> getAtdcDtl(HashMap map);
    int isClasStdtExist(HashMap map);

    List<ClassDtl> getExcelClassStudentList(HashMap map);
}
