package com.infomind.axboot.domain.sampleStudent;

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
@Table(name = "student_sample")
@Comment(value = "")
@Alias("sampleStudent")
public class SampleStudent extends SimpleJpaModel<Integer> {

	@Id
	@Column(name = "id", precision = 10, nullable = false)
	@Comment(value = "")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", length = 255, nullable = false)
	@Comment(value = "이름")
	private String name;

	@Column(name = "birthday", nullable = false)
	@Comment(value = "생년월일")
	private String birthday;

	@Column(name = "student_id", precision = 10, nullable = false)
	@Comment(value = "학번")
	private String studentId;

	@Column(name = "major", length = 255, nullable = false)
	@Comment(value = "전공")
	private String major;

	@Column(name = "admission_date", nullable = false)
	@Comment(value = "입학일")
	private String admissionDate;

	@Column(name = "graduation_date")
	@Comment(value = "졸업일")
	private String graduationDate;


    @Override
    public Integer getId() {
        return id;
    }
}