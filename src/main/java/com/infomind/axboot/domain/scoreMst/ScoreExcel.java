package com.infomind.axboot.domain.scoreMst;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
public class ScoreExcel {

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

	private BigDecimal midScor;
	private BigDecimal finScor;
	private BigDecimal totScor;
	private String scorNm;

	private String examCd;
	private String sbjtId;
	private BigDecimal scor;
	private BigDecimal perf;




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