package com.infomind.axboot.domain.attendanceMst;

import com.chequer.axboot.core.mybatis.MyBatisMapper;
import com.infomind.axboot.domain.classDtl.ClassDtl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface AttendanceMstMapper extends MyBatisMapper {

    List<AttendanceMst> getAtdcMst(HashMap map);

    Map getTchrDiv(HashMap map);

    List<Map> getAtdcPeriod(HashMap map);

    List<AttendanceExcel> getAtdcExcel(HashMap map);

    void deleteAtdcMst(AttendanceMst attendanceMst);

    List<Map> getAtdcDate();

    List<AttendanceExcel> getTotalAtdcRate(HashMap map);

    List<AttendanceTchrView> getTchrAtdcList(HashMap map);

    List<AttendanceTchrView> getTchrAtdcExcel(HashMap map);

    List<AttendanceTchrView> getTchrAtdcDate(HashMap map);

    String getTchrClasNm(HashMap map);
}
