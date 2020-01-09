package com.infomind.axboot.domain.image;

import com.chequer.axboot.core.annotations.ColumnPosition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.infomind.axboot.domain.BaseJpaModel;
import com.infomind.axboot.domain.SimpleJpaModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.chequer.axboot.core.annotations.Comment;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;


@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "file_l")
@Comment(value = "")
@Alias("image")
@Slf4j
public class Image extends BaseJpaModel<Long> {

	@Id
	@Column(name = "ID", precision = 19, nullable = false)
	@Comment(value = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TARGET_TYPE", length = 50)
	@Comment(value = "타겟구분")
	private String targetType;

	@Column(name = "TARGET_ID", length = 100)
	@Comment(value = "타겟ID")
	private String targetId;

	@Column(name = "FILE_NM", length = 65535)
	@Comment(value = "파일명")
	private String fileNm;

	@Column(name = "SAVE_NM", length = 65535)
	@Comment(value = "저장명")
	private String saveNm;

	@Column(name = "FILE_TYPE", length = 30)
	@Comment(value = "파일구분")
	private String fileType;

	@Column(name = "EXTENSION", length = 10)
	@Comment(value = "확장자")
	private String extension;

	@Column(name = "FILE_SIZE", precision = 19)
	@Comment(value = "파일사이즈")
	private Long fileSize;

	@Column(name = "DEL_YN", length = 1)
	@Comment(value = "삭제여부")
	private String delYn;

	@Column(name = "FILE_DESC", length = 65535)
	@Comment(value = "파일설명")
	private String fileDesc;

	@Column(name = "SORT", precision = 10)
	@Comment(value = "정렬순서")
	private Integer sort;

	@JsonIgnore
	@Transient
	private String _preview;

	@JsonIgnore
	@Transient
	private String _thumbnail;

	@JsonIgnore
	@Transient
	private String _download;


	@JsonIgnore
	@Transient
	private MultipartFile multipartFile;

	@JsonIgnore
	@Transient
	private File file;


//	public static Image of(MultipartFile multipartFile) {
//		Image image = new Image();
//		log.info("***********" + FilenameUtils.getName(multipartFile.getOriginalFilename()));
//		log.info("*********** extension :" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
//		image.setFileNm(FilenameUtils.getName(multipartFile.getOriginalFilename()));
//		image.setExtension(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
//		image.setFileSize(multipartFile.getSize());
//		image.setMultipartFile(multipartFile);
//
//		return image;
//	}
//
	@JsonProperty("preview")
	public String preview() {
		if (StringUtils.isEmpty(_preview)) {
			return "/api/v1/image/preview?id=" + getId();
		}
		return _preview;
	}

	@JsonProperty("thumbnail")
	public String thumbnail() {
		if (StringUtils.isEmpty(_thumbnail)) {
			return "/api/v1/image/thumbnail?id=" + getId();
		}
		return _thumbnail;
	}

	@JsonProperty("download")
	public String download() {
		if (StringUtils.isEmpty(_download)) {
			return "/api/v1/image/download?id=" + getId();
		}
		return _download;
	}


	public String getThumbnailSaveName() {
		String originName = fileNm.replaceFirst("[.][^.]+$", "");
//		return FilenameUtils.getBaseName(getSaveNm()) + "-thumbnail" + "." + FilenameUtils.getExtension(getSaveNm());
		return String.format("%s-thumbnail.%s", originName, extension);
	}



    @Override
    public Long getId() {
        return id;
    }
}