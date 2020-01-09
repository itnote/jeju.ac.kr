package com.infomind.axboot.domain.classMst;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import com.infomind.axboot.domain.schedule.Schedule;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface ClassMstMapper extends MyBatisMapper {

    List<ClassMst> getClassList(HashMap map);
    void insertClassMst(ClassMst classMst);
    void updateClassMst(ClassMst classMst);
    List<Map> getPeriodCombo(HashMap map);
    List<Map> getClasSeqCombo(HashMap map);
    List<ClassMst> getClassGrouping(HashMap map);
    int getClassMaxCount(HashMap map);
    void deleteClassMst(ClassMst classMst);
}
