package com.infomind.axboot.domain.counseling;

import com.chequer.axboot.core.annotations.ColumnPosition;
import com.infomind.axboot.domain.BaseJpaModel;
import com.infomind.axboot.domain.SimpleJpaModel;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_coun")
@Comment(value = "")
@Alias("counseling")
public class Counseling extends BaseJpaModel<Long> {

	@Id
	@Column(name = "COUN_SEQ", precision = 19, nullable = false)
	@Comment(value = "면담_시퀀스")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long counSeq;

	@Column(name = "COUN_DT", nullable = false)
	@Comment(value = "면담일자")
	private LocalDate counDt;

	@Column(name = "SEME_YEAR", length = 4)
	@Comment(value = "학년도")
	private String semeYear;

	@Column(name = "SEME_SEQ", precision = 19)
	@Comment(value = "학기시퀀스")
	private Long semeSeq;

	@Column(name = "PERIOD_CD", length = 100)
	@Comment(value = "전후반기구분(F전반기_R후반기_N구분없음)")
	private String periodCd;

	@Column(name = "CLAS_SEQ", precision = 19)
	@Comment(value = "수강반시퀀스")
	private Long clasSeq;

	@Column(name = "STDT_ID", length = 10)
	@Comment(value = "학번")
	private String stdtId;

	@Column(name = "COUN_CD", length = 100)
	@Comment(value = "면담구분(A출결_I개별지도_G일반상담)")
	private String counCd;

	@Column(name = "CONTENTS", length = 65535)
	@Comment(value = "내용")
	private String contents;

	@Column(name = "OPEN_YN", length = 1)
	@Comment(value = "공개여부")
	private String openYn;

	@Column(name = "DEL_YN", length = 1)
	@Comment(value = "삭제여부")
	private String delYn;

	@Transient
	private String counNm;

	@Transient
	private String createdByNm;


    @Override
    public Long getId() {
        return counSeq;
    }
}