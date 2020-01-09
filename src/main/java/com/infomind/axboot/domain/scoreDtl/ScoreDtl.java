package com.infomind.axboot.domain.scoreDtl;


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
import java.math.BigDecimal;


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_scor_dtl")
@Comment(value = "")
@IdClass(ScoreDtl.ScoreDtlId.class)
@Alias("scoreDtl")
public class ScoreDtl extends BaseJpaModel<ScoreDtl.ScoreDtlId> {

	@Id
	@Column(name = "SEME_YEAR", length = 4, nullable = false)
	@Comment(value = "학년도")
	private String semeYear;

	@Id
	@Column(name = "SEME_SEQ", precision = 19, nullable = false)
	@Comment(value = "학기시퀀스")
	private Long semeSeq;

	@Id
	@Column(name = "PERIOD_CD", length = 100, nullable = false)
	@Comment(value = "전후반기구분(F전반기_R후반기_N구분없음)")
	private String periodCd;

	@Id
	@Column(name = "CLAS_SEQ", precision = 19, nullable = false)
	@Comment(value = "수강반시퀀스")
	private Long clasSeq;

	@Id
	@Column(name = "STDT_ID", length = 10, nullable = false)
	@Comment(value = "학번")
	private String stdtId;

	@Id
	@Column(name = "SBJT_ID", precision = 19, nullable = false)
	@Comment(value = "과목ID")
	private String sbjtId;

	@Id
	@Column(name = "EXAM_CD", length = 100, nullable = false)
	@Comment(value = "시험구분(M중간고사_F기말고사)")
	private String examCd;

	@Column(name = "SCOR", precision = 4, scale = 1)
	@Comment(value = "성적")
	private BigDecimal scor;

	@Column(name = "PERF", precision = 4, scale = 1)
	@Comment(value = "수행평가")
	private BigDecimal perf;

	@Transient
	private String sbjtNmKor;

	@Transient
	private String examNm;

	@Transient
	private String updatedByNm;

	@Transient
	private BigDecimal oriScor;

	@Transient
	private BigDecimal oriPerf;

	@Transient
	private String reason;

	@Transient
	private BigDecimal totScor;

	@Transient
	private Integer midRatio;

	@Transient
	private Integer finalRatio;

	@Transient
	private Integer evalRatio;


@Override
public ScoreDtlId getId() {
return ScoreDtlId.of(semeYear, semeSeq, periodCd, clasSeq, stdtId, sbjtId, examCd);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class ScoreDtlId implements Serializable {

		@NonNull
		private String semeYear;

		@NonNull
		private Long semeSeq;

		@NonNull
		private String periodCd;

		@NonNull
		private Long clasSeq;

		@NonNull
		private String stdtId;

		@NonNull
		private String sbjtId;

		@NonNull
		private String examCd;

}
}