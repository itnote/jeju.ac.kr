package com.infomind.axboot.domain.subject;


import com.chequer.axboot.core.annotations.ColumnPosition;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.infomind.axboot.domain.BaseJpaModel;
import com.infomind.axboot.domain.SimpleJpaModel;
import com.infomind.axboot.domain.lang.Lang;
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
@Table(name = "jnu_sbjt")
@Comment(value = "")
@IdClass(Subject.SubjectId.class)
@Alias("subject")
public class Subject extends BaseJpaModel<Subject.SubjectId> {

	@Id
	@Column(name = "SEME_YEAR", length = 4, nullable = false)
	@Comment(value = "학년도")
	private String semeYear;

	@Id
	@Column(name = "SEME_SEQ", precision = 19, nullable = false)
	@Comment(value = "학기시퀀스")
	private Long semeSeq;

	@Id
	@Column(name = "SBJT_ID", length = 10, nullable = false)
	@Comment(value = "과목ID")
	private String sbjtId;

	@Column(name = "SBJT_NM", precision = 19, nullable = false)
	@Comment(value = "과목명(LANG)")
	private Long sbjtNm;

	@Column(name = "PRINT_YN", length = 1)
	@Comment(value = "성적증명서 표시여부")
	private String printYn;

	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "SBJT_NM", insertable = false, updatable = false, nullable = false)
	private Lang lang;


@Override
public SubjectId getId() {
return SubjectId.of(semeYear, semeSeq, sbjtId);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class SubjectId implements Serializable {

		@NonNull
		private String semeYear;

		@NonNull
		private Long semeSeq;

		@NonNull
		private String sbjtId;

}
}