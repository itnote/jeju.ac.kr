package com.infomind.axboot.domain.report;

import com.chequer.axboot.core.annotations.ColumnPosition;
import com.infomind.axboot.domain.SimpleJpaModel;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import javax.persistence.*;


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "report_sample")
@Comment(value = "")
@Alias("reportSample")
public class ReportSample extends SimpleJpaModel<Integer> {

	@Id
	@Column(name = "id", precision = 10, nullable = false)
	@Comment(value = "")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_id", precision = 10)
	@Comment(value = "사용자 ID")
	private Integer userId;

	@Column(name = "student_id", precision = 10, nullable = false)
	@Comment(value = "출력 대상(학생)ID")
	private Integer studentId;


    @Override
    public Integer getId() {
        return id;
    }
}