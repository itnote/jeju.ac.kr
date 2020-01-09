package com.infomind.axboot.domain.certificate;

import lombok.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.List;

@Setter
@Getter
public class Certificate{

	private String semeYear;
	private String semeSeq;
	private String periodCd;
	private String clasSeq;
	private String stdtId;
	private String lv;

	private String docSeq;

	private String stdtNmKor;
	private String stdtNmEng;
	private String stdtNmChn;

	private String genderNm;
	private String natnNm;
	private String birthDt;
	private String birthMt;

	private String startDt;
	private String startDtKor;
	private String startDtEng;
	private String endDt;
	private String endDtKor;
	private String endDtEng;
	private String printDt;
	private String printDtKor;
	private String printDtEng;
	private String confEndDt;

	private String totHours;
	private String during;

	private String printYear;

	private List<Transcript> trans;

    private String freshYn;
    private String feeDt;
	private String paymentDt;
	private String paymentDtKor;
	private String paymentDtEng;
	private String paymentDtChn;

	private String dormAmt;
	private String insAmt;
	private String bedAmt;
	private String appAmt;
	private String regAmt;
	private String totalAmt;
	private String allAmt;

	private String semeNmKor;
	private String semeNmEng;

	private String printDtChn;
	private String natnCd;


	private String docCd;
	private String docCdNm;

	private String createdAt;
	private String createdBy;

	private String dormPayYn;
	private String appPayYn;
	private String insPayYn;
	private String bedPayYn;


	public JRBeanCollectionDataSource getTransDataSource() {
		return new JRBeanCollectionDataSource(trans, false);
	}
}