package com.infomind.axboot.domain.attendanceMst;


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
@Table(name = "jnu_atdc_mst")
@Comment(value = "")
@IdClass(AttendanceMst.AttendanceMstId.class)
@Alias("attendanceMst")
public class AttendanceMst extends BaseJpaModel<AttendanceMst.AttendanceMstId> {

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

	@Column(name = "TCHR_DIV", length = 100)
	@Comment(value = "수업구분(M담임_S부담임)")
	private String tchrDiv;

	@Column(name = "TCHR_CD", length = 255)
	@Comment(value = "강사ID")
	private String tchrCd;

	@Transient
	private String tchrDivNm;

	@Transient
	private String schSemeYear;

	@Transient
	private String schSemeSeq;

	@Transient
	private String schPeriodCd;

	@Transient
	private String schClasSeq;

	@Transient
	private String schAtdcDt;

	@Transient
	private String tchrNm;

	@Transient
	private String schBeginAtdcDt;

	@Transient
	private String schEndAtdcDt;

	@Transient
	private String schTchrCd;


@Override
public AttendanceMstId getId() {
return AttendanceMstId.of(semeYear, semeSeq, periodCd, clasSeq, atdcDt, atdcSeq);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class AttendanceMstId implements Serializable {

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



}
}