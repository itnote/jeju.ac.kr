package com.infomind.axboot.domain.scoreMst;


import com.chequer.axboot.core.annotations.ColumnPosition;
import com.infomind.axboot.domain.BaseJpaModel;
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
@Table(name = "jnu_scor_mst")
@Comment(value = "")
@IdClass(ScoreMst.ScoreMstId.class)
@Alias("scoreMst")
public class ScoreMst extends BaseJpaModel<ScoreMst.ScoreMstId> {

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

	@Column(name = "MID_SCOR", precision = 5, scale = 2)
	@Comment(value = "중간고사 총점")
	private BigDecimal midScor;

	@Column(name = "FIN_SCOR", precision = 5, scale = 2)
	@Comment(value = "기말고사 총점")
	private BigDecimal finScor;

	@Column(name = "TOT_SCOR", precision = 5, scale = 2)
	@Comment(value = "총점")
	private BigDecimal totScor;

	@Column(name = "SCOR_CD", length = 100)
	@Comment(value = "이수구분(이수_유급_과락)")
	private String scorCd;


	@Column(name = "FAIL_RESN", length = 100)
	@Comment(value = "유급 사유")
	private String failResn;


	@Transient
	private String scorNm;

	@Transient
	private String stdtNmKor;

	@Transient
	private String stdtNmEng;

	@Transient
	private String genderNm;

	@Transient
	private String natnNm;

	@Transient
	private String birthDt;

	@Transient
	private String tchrCd;

	@Transient
	private String subTchrCd;

	@Transient
	private String lv;




	@Override
public ScoreMstId getId() {
return ScoreMstId.of(semeYear, semeSeq, periodCd, clasSeq, stdtId);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class ScoreMstId implements Serializable {

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

}
}