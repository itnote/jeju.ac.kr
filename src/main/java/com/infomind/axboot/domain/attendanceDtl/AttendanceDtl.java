package com.infomind.axboot.domain.attendanceDtl;


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
@Table(name = "jnu_atdc_dtl")
@Comment(value = "")
@IdClass(AttendanceDtl.AttendanceDtlId.class)
@Alias("attendanceDtl")
public class AttendanceDtl extends BaseJpaModel<AttendanceDtl.AttendanceDtlId> {

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
	@Column(name = "ATDC_DT", nullable = false)
	@Comment(value = "출결일자")
	private String atdcDt;

	@Id
	@Column(name = "ATDC_SEQ", precision = 10, nullable = false)
	@Comment(value = "교시")
	private String atdcSeq;

	@Id
	@Column(name = "STDT_ID", length = 10, nullable = false)
	@Comment(value = "학번")
	private String stdtId;

	@Column(name = "ATDC_CD", length = 100)
	@Comment(value = "출결구분(0출석,1지각,2결석,3휴강)")
	private String atdcCd;

	@Column(name = "ATDC_REASON", length = 100)
	@Comment(value = "결석사유(20무단_21병가_22개인사정_23기타)")
	private String atdcReason;

	@Transient
	private String atdcNm;

	@Transient
	private String atdcReasonNm;


	@Transient
	private String stdtNmKor;

	@Transient
	private String stdtNmEng;

	@Transient
	private String natnCd;

	@Transient
	private String birthDt;

	@Transient
	private String gender;

	@Transient
	private String hpNo;



@Override
public AttendanceDtlId getId() {
return AttendanceDtlId.of(semeYear, semeSeq, periodCd, clasSeq, atdcDt, atdcSeq, stdtId);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class AttendanceDtlId implements Serializable {

		@NonNull
		private String semeYear;

		@NonNull
		private String semeSeq;

		@NonNull
		private String periodCd;

		@NonNull
		private String clasSeq;

		@NonNull
		private String atdcDt;

		@NonNull
		private String atdcSeq;

		@NonNull
		private String stdtId;

}
}