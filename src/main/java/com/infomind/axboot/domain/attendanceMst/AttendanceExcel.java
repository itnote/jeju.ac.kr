package com.infomind.axboot.domain.attendanceMst;


import lombok.*;
import java.io.Serializable;

@Setter
@Getter
public class AttendanceExcel {

	private String semeYear;
	private String semeSeq;
	private String periodCd;
	private String clasSeq;
	private String stdtId;

	private String stdtNmKor;
	private String stdtNmEng;

	private String genderNm;
	private String natnNm;
	private String birthMt;
	private String lvNm;
	private String clasNm;

	private int yyyy;
	private int weekOfYear;

	private int mAtdcCnt;
	private int mLateCnt;
	private int mAbscCnt;

	private int sAtdcCnt;
	private int sLateCnt;
	private int sAbscCnt;

	private String totRate;


	public StdtInfo getStdtInfo() {
		return StdtInfo.of(stdtId, stdtNmKor, stdtNmEng, genderNm, natnNm, birthMt);
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