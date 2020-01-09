package com.infomind.axboot.domain.docLog;

import com.infomind.axboot.domain.BaseJpaModel;
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
@Table(name = "jnu_doc_log")
@Comment(value = "")
@Alias("docLog")
public class DocLog extends BaseJpaModel {

	@Id
	@Column(name = "DOC_SEQ", precision = 19, nullable = false)
	@Comment(value = "문서번호")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long docSeq;

	@Column(name = "SEME_YEAR", length = 4)
	@Comment(value = "학년도")
	private String semeYear;

	@Column(name = "SEME_SEQ", precision = 19)
	@Comment(value = "학기시퀀스")
	private String semeSeq;

	@Column(name = "STDT_ID", length = 10)
	@Comment(value = "학번")
	private String stdtId;

	@Column(name = "DOC_CD", length = 100)
	@Comment(value = "문서코드")
	private String docCd;

	@Column(name = "PRINT_DT")
	@Comment(value = "출력일자")
	private String printDt;

    @Override
    public Long getId() {
        return docSeq;
    }
}