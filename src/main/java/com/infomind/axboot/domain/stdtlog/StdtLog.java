package com.infomind.axboot.domain.stdtlog;

import com.infomind.axboot.domain.TranJpaModel;
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
@Table(name = "jnu_stdt_log")
@Comment(value = "")
@Alias("stdtLog")
public class StdtLog extends TranJpaModel<Long> {

	@Id
	@Column(name = "LOG_ID", precision = 19, nullable = false)
	@Comment(value = "학적변동ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long logId;

	@Column(name = "STDT_ID", length = 10, nullable = false)
	@Comment(value = "학번")
	private String stdtId;

	@Column(name = "STDT_LOG_CD", length = 100)
	@Comment(value = "변동구분(A주소_T연락처)")
	private String stdtLogCd;

	@Column(name = "LOG_DATA", length = 128)
	@Comment(value = "데이터")
	private String logData;




//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "STDT_ID", insertable = false, updatable = false)
//	@JsonBackReference
//	private Student jnuStudent;






    @Override
    public Long getId() {
        return logId;
    }
}