package com.infomind.axboot.domain.attendanceMst;



import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AttendanceTchrView {
    private String tchrCd;
    private String userNm;
    private String tchrDiv;
    private String tchrDivNm;
    private String atdcDt;
    private String dow;
    private String weeks;
    private String hours;
    private String isYours;
    private String absentHours;
    private String subHours;
//    private String groupNm;
    private String clasNm;
    private String scheNm;
    private String holyYn;

    public AtdcInfo getAtdcInfo() {
        return AtdcInfo.of(dow, weeks, hours, absentHours, subHours, atdcDt);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class AtdcInfo implements Serializable {
        private String dow;
        private String weeks;
        private String hours;
        private String absentHours;
        private String subHours;
        private String atdcDt;
    }
}
