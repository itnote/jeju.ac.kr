package com.infomind.axboot.domain.testM;

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
@Table(name = "test_m")
@Comment(value = "")
@Alias("testM")
public class TestM extends SimpleJpaModel<Integer> {

	@Id
	@Column(name = "ID", precision = 10, nullable = false)
	@Comment(value = "")
	private Integer id;

	@Column(name = "NAME", length = 255)
	@Comment(value = "")
	private String name;


    @Override
    public Integer getId() {
        return id;
    }
}