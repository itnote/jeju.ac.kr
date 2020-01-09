package com.infomind.axboot.domain.student;

import com.infomind.axboot.domain.BaseJpaModel;
import com.infomind.axboot.domain.lang.Lang;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "jnu_stdt")
@Comment(value = "")
@Alias("student")
@Slf4j
public class Student extends BaseJpaModel<String> {

	@Id
	@Column(name = "STDT_ID", length = 10, nullable = false)
	@Comment(value = "학번")
	@NotNull(message = "학번을 입력해주세요")
	private String stdtId;

	@Column(name = "STDT_NM", precision = 19)
	@Comment(value = "이름(LANG)")
	private Long stdtNm;

	@Column(name = "NATN_CD", length = 100)
	@Comment(value = "국적코드(중국_베트남_태국_몽골_홍콩_일본)")
	private String natnCd;

	@Column(name = "BIRTH_DT", length = 8)
	@Comment(value = "생년월일")
//	@Pattern(regexp = "^[0-9]*$")
	private String birthDt;

	@Column(name = "GENDER", length = 100)
	@Comment(value = "성별(M남자_F여자)")
	private String gender;

	@Column(name = "HP_NO", length = 128)
	@Comment(value = "휴대폰번호(암호화)")
	private String hpNo;

	@Column(name = "MSG_CD1", length = 100)
	@Comment(value = "메신저 종류1(WECHAT_KAKAO_FACEBOOK)")
	private String msgCd1;

	@Column(name = "MSG_ID1", length = 100)
	@Comment(value = "메신저 ID1")
	private String msgId1;

	@Column(name = "MSG_CD2", length = 100)
	@Comment(value = "메신저 종류2(WECHAT_KAKAO_FACEBOOK)")
	private String msgCd2;

	@Column(name = "MSG_ID2", length = 100)
	@Comment(value = "메신저 ID2")
	private String msgId2;

	@Column(name = "EMAIL", length = 50)
	@Comment(value = "이메일")
	private String email;

	@Column(name = "ADDR", length = 128)
	@Comment(value = "현주소(암호화)")
	private String addr;

	@Column(name = "NATN_ADDR", length = 128)
	@Comment(value = "본국주소(암호화)")
	private String natnAddr;

	@Column(name = "BANK_CD", length = 100)
	@Comment(value = "은행")
	private String bankCd;

	@Column(name = "ACCOUNT_NO", length = 128)
	@Comment(value = "계좌번호(암호화)")
	private String accountNo;

	@Column(name = "PHOTO_FILE_ID", precision = 19)
	@Comment(value = "증명사진")
	private Integer photoFileId;

	@Column(name = "USE_YN", length = 1)
	@Comment(value = "사용여부")
	private String useYn;

	@Column(name = "STUDY_PERIOD", length = 50)
	@Comment(value = "학습기간")
	private String studyPeriod;

	@Column(name = "TOPIK_CD", length = 100)
	@Comment(value = "토픽등급")
	private String topikCd;

	@Column(name="KOR_LV", length = 50)
	@Comment(value = "한국어레벨")
	private String korLv;

	@Column(name = "VISA_TYPE", length = 100)
	@Comment(value = "비자유형")
	private String visaType;

	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "STDT_NM", insertable = false, updatable = false, nullable = false)
	private Lang lang;

	@Transient
	private Long langKey;

	@Transient
	private String stdtNmKor;

	@Transient
	private String stdtNmEng;

	@Transient
	private String stdtNmChn;

	@Transient
	private String genderNm;

	@Transient
	private String natnNm;

	@Transient
	private String schStdt;

	@Transient
	private String schSemeSeq;

	@Transient
	private String schSemeYear;

	@Transient
	private String schPeriodCd;

	@Transient
	private String counDt;

	@Transient
	private String contents;

	@Transient
	private String counNm;

	@Transient
	private String createdByNm;
	// 임시 보류
//	@Transient
//	private String statusNm;
//
//	@Transient
//	private String clasNm;







    @Override
    public String getId() {
        return stdtId;
    }
}