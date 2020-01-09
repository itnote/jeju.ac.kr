package com.infomind.axboot.domain.scoreLog;

import com.chequer.axboot.core.annotations.ColumnPosition;
import com.infomind.axboot.domain.SimpleJpaModel;
import com.infomind.axboot.domain.user.User;
import com.infomind.axboot.utils.SessionUtils;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_scor_log")
@Comment(value = "")
@Alias("scoreLog")
public class ScoreLog extends SimpleJpaModel<Long> {

	@Id
	@Column(name = "LOG_ID", precision = 19, nullable = false)
	@Comment(value = "성적이력ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long logId;

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

	@Column(name = "SBJT_ID", precision = 19)
	@Comment(value = "과목ID")
	private String sbjtId;

	@Column(name = "EXAM_CD", length = 100)
	@Comment(value = "시험구분(M중간고사_F기말고사)")
	private String examCd;

	@Column(name = "OLD_SCOR", precision = 4, scale = 1)
	@Comment(value = "이전 성적")
	private BigDecimal oldScor;

	@Column(name = "NEW_SCOR", precision = 4, scale = 1)
	@Comment(value = "현재 성적")
	private BigDecimal newScor;

	@Column(name = "REASON", length = 100)
	@Comment(value = "변경 사유")
	private String reason;

	@Transient
	private String examNm;

	@Transient
	private String sbjtNmKor;

	@Transient
	private String createdByNm;

    @Override
    public Long getId() {
        return logId;
    }

	@Column(name = "CREATED_AT", updatable = false)
	@ColumnPosition(Integer.MAX_VALUE - 4)
	protected Instant createdAt;

	@Column(name = "CREATED_BY", updatable = false)
	@ColumnPosition(Integer.MAX_VALUE - 3)
	protected String createdBy;

	@PrePersist
	protected void onPersist() {
		this.createdBy = SessionUtils.getCurrentLoginUserCd();
		this.createdAt = Instant.now(Clock.systemUTC());
	}
}