package com.infomind.axboot.domain.attendanceDtl;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
public class AttendanceMissExcel {
    private String semeYear;
    private String semeSeq;
    private String periodCd;
    private String clasSeq;
    private String stdtId;

    private String stdtNmKor;
    private String stdtNmEng;
    private String userCd;
    private String userNm;
    private String tchrDiv;
    private String genderNm;
    private String natnNm;
    private String birthMt;
    private String lvNm;
    private String clasNm;
    private String abscDt;
    private String abscCnt;

    private int yyyy;
    private int weekOfYear;

    private int mAtdcCnt;
    private int mLateCnt;
    private int mAbscCnt;

    private int sAtdcCnt;
    private int sLateCnt;
    private int sAbscCnt;


    public AttendanceMissExcel.StdtInfo getStdtInfo() {
        return AttendanceMissExcel.StdtInfo.of(stdtId, stdtNmKor, stdtNmEng, genderNm, natnNm, birthMt);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class StdtInfo implements Serializable {

        private String stdtId;
        private String stdtNmKor;
        private String stdtNmEng;
        private String genderNm;
        private String natnNm;
        private String birthMt;
    }
}
