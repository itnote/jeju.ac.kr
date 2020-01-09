package com.infomind.axboot.domain.schedule;


import com.chequer.axboot.core.annotations.ColumnPosition;
import com.infomind.axboot.domain.BaseJpaModel;
import com.infomind.axboot.domain.SimpleJpaModel;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_sche")
@Comment(value = "")
@IdClass(Schedule.ScheduleId.class)
@Alias("schedule")
public class Schedule extends BaseJpaModel<Schedule.ScheduleId> {

	@Id
	@Column(name = "SEME_YEAR", length = 4, nullable = false)
	@Comment(value = "학년도")
	private String semeYear;

	@Id
	@Column(name = "SEME_SEQ", precision = 19, nullable = false)
	@Comment(value = "학기시퀀스")
	private Long semeSeq;

	@Id
	@Column(name = "SCHE_DT", nullable = false)
	@Comment(value = "일자")
	private LocalDate scheDt;

	@Column(name = "SCHE_NM", length = 100)
	@Comment(value = "일정명")
	private String scheNm;

	@Column(name = "HOLI_YN", length = 1, nullable = false)
	@Comment(value = "휴일여부")
	private String holiYn;

	@Column(name = "COUN_YN", length = 1)
	@Comment(value = "출결면담여부")
	private String counYn;

	@Column(name = "WEEK_CD", length = 100)
	@Comment(value = "수업구분코드")
	private String weekCd;

	@Transient
	private String startDt;

	@Transient
	private String endDt;

    @Transient
    private String monCd;

    @Transient
    private String tueCd;

    @Transient
    private String wedCd;

    @Transient
    private String thuCd;

    @Transient
    private String friCd;

	@Transient
	private String weekHours;




@Override
public ScheduleId getId() {
return ScheduleId.of(semeYear, semeSeq, scheDt);
}

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public static class ScheduleId implements Serializable {

		@NonNull
		private String semeYear;

		@NonNull
		private Long semeSeq;

		@NonNull
		private LocalDate scheDt;

}
}