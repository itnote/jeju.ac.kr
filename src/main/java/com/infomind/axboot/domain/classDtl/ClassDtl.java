package com.infomind.axboot.domain.classDtl;


import com.chequer.axboot.core.annotations.ColumnPosition;
import com.infomind.axboot.domain.BaseJpaModel;
import com.infomind.axboot.domain.SimpleJpaModel;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_clas_dtl")
@Comment(value = "")
@IdClass(ClassDtl.ClassDtlId.class)
@Alias("classDtl")
public class ClassDtl extends BaseJpaModel<ClassDtl.ClassDtlId> {

	@Id
	@Column(name = "SEME_YEAR", length = 4, nullable = false)
	@Comment(value = "학년도")
	private String semeYear;

	@Id
	@Column(name = "SEME_SEQ", precision = 19, nullable = false)
	@Comment(value = "학기시퀀스")
	private String semeSeq;

	@Id
	@Column(name = "PERIOD_CD", length = 100, nullable = false)
	@Comment(value = "전후반기구분(F전반기_R후반기_N구분없음)")
	private String periodCd;

	@Id
	@Column(name = "CLAS_SEQ", precision = 19, nullable = false)
	@Comment(value = "수강반시퀀스")
	private String clasSeq;

	@Id
	@Column(name = "STDT_ID", length = 10, nullable = false)
	@Comment(value = "학번")
	private String stdtId;

	@Column(name = "STATUS_CD")
	@Comment(value = "학적상태")
	private String statusCd;

	@Column(name = "FEE_DT")
	@Comment(value = "등록금 납부일자")
	private String feeDt;

	@Column(name = "FEE_AMT", precision = 10)
	@Comment(value = "등록금 납부금액")
	private Integer feeAmt;

	@Column(name = "DORM_AMT", precision = 10)
	@Comment(value = "기숙사비 납부금액")
	private Integer dormAmt;

	@Column(name = "GENERAL_REVIEW", length = 65535)
	@Comment(value = "총평")
	private String generalReview;

	@Column(name = "FRESH_YN")
	@Comment(value = "학적상태")
	private String freshYn;

	@Column(name = "DORM_PAY_YN")
	@Comment(value = "학적상태")
	private String dormPayYn;

	@Column(name = "APP_PAY_YN")
	@Comment(value = "학적상태")
	private String appPayYn;

	@Column(name = "INS_PAY_YN")
	@Comment(value = "학적상태")
	private String insPayYn;

	@Column(name = "BED_PAY_YN")
	@Comment(value = "학적상태")
	private String bedPayYn;

	@Column(name = "PRE_PAY_YN")
	@Comment(value = "학적상태")
	private String prePayYn;

	@Column(name = "DISC_CD")
	@Comment(value = "학적상태")
	private String discCd;


	@Transient
	private String stdtNm;

	@Transient
	private String schInput;

	@Transient
	private String statusNm;

	@Transient
	private String stdtNmKor;

	@Transient
	private String stdtNmEng;

	@Transient
	private String gender;

	@Transient
	private String genderNm;

	@Transient
	private String natnCd;

	@Transient
	private String natnNm;

	@Transient
	private String birthDt;

	@Transient
	private String birthMt;

	@Transient
	private String schStdt;

	@Transient
	private String schSemeSeq;

	@Transient
	private String schSemeYear;

	@Transient
	private String schPeriodCd;

	@Transient
	private String atdcCd;

	@Transient
	private String atdcDt;

	@Transient
	private String atdcSeq;

	@Transient
	private String atdcNm;

	@Transient
	private String atdcReason;

	@Transient
	private String atdcReasonNm;

	@Transient
	private String clasNm;

	@Transient
	private String lv;

	@Transient
	private String periodNm;

	@Transient
	private String tchrNm;

	@Transient
	private String subTchrNm;

	@Transient
	private String corsNm;

	@Transient
	private int mCnt;

	@Transient
	private int sCnt;

	@Transient
	private String stdtNmChn;

	@Transient
	private String hpNo;


	@Override
public ClassDtlId getId() {
return ClassDtlId.of(semeYear, semeSeq, periodCd, clasSeq, stdtId);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class ClassDtlId implements Serializable {

		@NonNull
		private String semeYear;

		@NonNull
		private String semeSeq;

		@NonNull
		private String periodCd;

		@NonNull
		private String clasSeq;

		@NonNull
		private String stdtId;

}
}