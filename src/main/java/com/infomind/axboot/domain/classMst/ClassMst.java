package com.infomind.axboot.domain.classMst;


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


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_clas_mst")
@Comment(value = "")
@IdClass(ClassMst.ClassMstId.class)
@Alias("classMst")
public class ClassMst extends BaseJpaModel<ClassMst.ClassMstId> {

	@Id
	@Column(name = "CLAS_SEQ", precision = 19, nullable = false)
	@Comment(value = "수강반시퀀스")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long clasSeq;

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

	@Column(name = "CLAS_NM", length = 20)
	@Comment(value = "수강반명")
	private String clasNm;

	@Column(name = "CLAS_CD", length = 100)
	@Comment(value = "연수생구분(연수생_교류수학생)")
	private String clasCd;

	@Column(name = "LV", length = 100)
	@Comment(value = "LEVEL(정규1급_정규2급_정규3급_정규4급_정규5급_정규6급_교환초급_교환중급_교환고급)")
	private String lv;

	@Column(name = "TCHR_CD", length = 255)
	@Comment(value = "담임ID")
	private String tchrCd;

	@Column(name = "SUB_TCHR_CD", length = 255)
	@Comment(value = "부담임ID")
	private String subTchrCd;

	@Column(name = "LCTR_ROOM", length = 100)
	@Comment(value = "강의실")
	private String lctrRoom;

	@Transient
	private String semeNm;

	@Transient
	private String corsNm;

	@Transient
	private String periodNm;

	@Transient
	private String stdtCnt;

	@Transient
	private String tchrNm;

	@Transient
	private String subTchrNm;

	@Transient
	private String classNm;

	@Transient
	private String stdtList;



@Override
public ClassMstId getId() {
return ClassMstId.of(clasSeq, semeYear, semeSeq, periodCd);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class ClassMstId implements Serializable {

		@NonNull
		private Long clasSeq;

		@NonNull
		private String semeYear;

		@NonNull
		private Long semeSeq;

		@NonNull
		private String periodCd;

}
}