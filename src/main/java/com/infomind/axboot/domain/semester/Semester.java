package com.infomind.axboot.domain.semester;


import com.infomind.axboot.domain.BaseJpaModel;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_seme")
@Comment(value = "")
@IdClass(Semester.SemesterId.class)
@Alias("semester")
public class Semester extends BaseJpaModel<Semester.SemesterId> {

	@Id
	@Column(name = "SEME_YEAR", length = 4, nullable = false)
	@Comment(value = "학년도")
	private String semeYear;

	@Id
	@Column(name = "SEME_SEQ", precision = 19, nullable = false)
	@Comment(value = "학기시퀀스")
	private Long semeSeq;

	@Column(name = "CORS_NM", precision = 19, nullable = false)
	@Comment(value = "과정명(LANG)")
	private Long corsNm;

	@Column(name = "SEME_CD", length = 100, nullable = false)
	@Comment(value = "학기코드(R정규학기_S특별학기)")
	private String semeCd;

	@Column(name = "TOT_HOURS", precision = 10)
	@Comment(value = "기준시수")
	private Integer totHours;

	@Column(name = "FIRST_HALF", precision = 10)
	@Comment(value = "전반기시수")
	private Integer firstHalf;

	@Column(name = "SECOND_HALF", precision = 10)
	@Comment(value = "후반기시수")
	private Integer secondHalf;

	@Column(name = "START_DT", length = 8)
	@Comment(value = "시작일자")
	private String startDt;

	@Column(name = "END_DT", length = 8)
	@Comment(value = "종료일자")
	private String endDt;

	@Column(name = "MON_CD", length = 100)
	@Comment(value = "월요일_수업구분(수업없음_담임4시간_정부각각2시간씩_담임2시간)")
	private String monCd;

	@Column(name = "TUE_CD", length = 100)
	@Comment(value = "화요일_수업여부(수업없음_담임4시간_정부각각2시간씩_담임2시간)")
	private String tueCd;

	@Column(name = "WED_CD", length = 100)
	@Comment(value = "수요일_수업여부(수업없음_담임4시간_정부각각2시간씩_담임2시간)")
	private String wedCd;

	@Column(name = "THU_CD", length = 100)
	@Comment(value = "목요일_수업여부(수업없음_담임4시간_정부각각2시간씩_담임2시간)")
	private String thuCd;

	@Column(name = "FRI_CD", length = 100)
	@Comment(value = "금요일_수업여부(수업없음_담임4시간_정부각각2시간씩_담임2시간)")
	private String friCd;

	@Column(name = "NEW_REG_STD_AMT", precision = 10)
	@Comment(value = "신입생 등록금 납부 기준금액")
	private Integer newRegStdAmt;

	@Column(name = "NEW_DORM_STD_AMT", precision = 10)
	@Comment(value = "신입생 기숙사 납부 기준금액")
	private Integer newDormStdAmt;

	@Column(name = "OLD_REG_STD_AMT", precision = 10)
	@Comment(value = "재학생 등록금 납부 기준금액")
	private Integer oldRegStdAmt;

	@Column(name = "NEW_APP_STD_AMT", precision = 10)
	@Comment(value = "신입생 전형료 납부 기준금액")
	private Integer newAppStdAmt;

	@Column(name = "NEW_INS_STD_AMT", precision = 10)
	@Comment(value = "신입생 의료보험 납부 기준금액")
	private Integer newInsStdAmt;

	@Column(name = "NEW_BED_STD_AMT", precision = 10)
	@Comment(value = "재학생 침구 납부 기준금액")
	private Integer newBedStdAmt;

	@Transient
	private String semeNm;

	@Transient
	private String kor;
	@Transient
	private String eng;
	@Transient
	private String chn;

	@Transient
	private String monHours;
	@Transient
	private String tueHours;
	@Transient
	private String wedHours;
	@Transient
	private String thuHours;
	@Transient
	private String friHours;

	@Transient
	private String schMyClasChk;



@Override
public SemesterId getId() {
return SemesterId.of(semeYear, semeSeq);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class SemesterId implements Serializable {

		@NonNull
		private String semeYear;

		@NonNull
		private Long semeSeq;

}
}