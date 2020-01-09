package com.infomind.axboot.domain.student;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import com.infomind.axboot.domain.classMst.ClassMst;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface StudentMapper extends MyBatisMapper {

    List<Student> getStudentList(HashMap map);
    List<Student> getCounStudentList(HashMap map);

}
