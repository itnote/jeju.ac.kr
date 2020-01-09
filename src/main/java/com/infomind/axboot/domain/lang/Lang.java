package com.infomind.axboot.domain.lang;

import com.infomind.axboot.domain.BaseJpaModel;
import lombok.*;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_lang_pack")
@Comment(value = "")
@Alias("lang")
public class Lang extends BaseJpaModel<Long> {

	@Id
	@Column(name = "LANG_KEY", precision = 19, nullable = false)
	@Comment(value = "다국어키")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long langKey;

	@Column(name = "KOR", length = 65535, nullable = false)
	@Comment(value = "한국어")
	@NotEmpty(message = "이름을 입력하세요")
	private String kor;

	@Column(name = "ENG", length = 65535)
	@Comment(value = "영어")
	private String eng;

	@Column(name = "CHN", length = 65535)
	@Comment(value = "중국어")
	private String chn;




    @Override
    public Long getId() {
        return langKey;
    }
}