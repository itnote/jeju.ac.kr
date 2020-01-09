package com.infomind.axboot.domain.certificate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transcript {

	private String semeYear;
	private String semeSeq;
	private String periodCd;
	private String clasSeq;
	private String stdtId;

	private String lv;

	private String examScorS;
	private String examScorW;
	private String examScorL;
	private String examScorR;

	private String totHours;
	private String atdcRate;
	private String atdc;

	private String scoreTitleKor;
	private String scoreTitleEng;


}