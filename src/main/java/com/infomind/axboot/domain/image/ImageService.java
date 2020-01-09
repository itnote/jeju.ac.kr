package com.infomind.axboot.domain.image;

import com.chequer.axboot.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.chequer.axboot.core.parameter.RequestParams;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ImageService extends BaseService<Image, Long> {
    private ImageRepository imageRepository;

    @Value("${axboot.upload.repository}")
    public String uploadRepository;


    @Inject
    public ImageService(ImageRepository imageRepository) {
        super(imageRepository);
        this.imageRepository = imageRepository;
    }

    public List<Image> gets(RequestParams requestParams) {
        return findAll();
    }

    public void createBaseDirectory() {
        try {
            FileUtils.forceMkdir(new File(uploadRepository));
        } catch (IOException e) {
        }
    }


    public String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }


    public File multiPartFileToFile(MultipartFile multipartFile) throws IOException {
        String baseDir = getTempDir() + "/" + UUID.randomUUID().toString();
        FileUtils.forceMkdir(new File(baseDir));

        String tmpFileName = baseDir + "/" + FilenameUtils.getName(multipartFile.getOriginalFilename());

        File file = new File(tmpFileName);

        multipartFile.transferTo(file);
        return file;
    }

    @Transactional
    public Image upload(MultipartFile multipartFile, String fileType) throws IOException {
        return upload(multiPartFileToFile(multipartFile), fileType);
    }

    @Transactional
    public Image upload(File file, String fileType) throws IOException {
        Image image = new Image();
        image.setFile(file);
        image.setFileType(fileType);

        return upload(image);
    }

    @Transactional
    public Image upload(Image image) throws IOException {


        if(!image.getMultipartFile().getOriginalFilename().equals("") && image.getMultipartFile().getOriginalFilename() != null) {

            File uploadFile = image.getFile();

            if (uploadFile == null && image.getMultipartFile() != null) {
                uploadFile = multiPartFileToFile(image.getMultipartFile());
            }

            String fileName = FilenameUtils.getName(uploadFile.getName());
            String extension = FilenameUtils.getExtension(fileName);
            String fileType = getFileType(extension);

            String baseName = UUID.randomUUID().toString();
            String originName = fileName.replaceFirst("[.][^.]+$", "");
            String saveName = originName + "-"+System.currentTimeMillis() + "." + extension;
            String savePath = getSavePath(saveName);

            File file = new File(savePath);
            FileUtils.copyFile(uploadFile, file);


            image.setFileNm(fileName);
            image.setSaveNm(saveName);
            image.setFileType(fileType);
            image.setExtension(FilenameUtils.getExtension(fileName).toUpperCase());
            image.setFileSize(file.length());

            FileUtils.deleteQuietly(uploadFile);

            save(image);

        }

        return image;
    }


    @Transactional
    public Map<String, Object> uploadFiles(MultipartHttpServletRequest mRequest, Image image) throws IOException {

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, MultipartFile> files = mRequest.getFileMap();

        if(!files.isEmpty()) {  // 파일이 존재하는지 체크

            // 파일이 존재함
            Iterator<Map.Entry<String, MultipartFile>> itr = files.entrySet().iterator();
            MultipartFile mFile;

            while (itr.hasNext()) {

                Map.Entry<String, MultipartFile> entry = itr.next();
                mFile = entry.getValue();

                File uploadFile = multiPartFileToFile(mFile);

                String fileName = FilenameUtils.getName(uploadFile.getName());
                String extension = FilenameUtils.getExtension(fileName);
                String fileType = getFileType(extension);

                String baseName = UUID.randomUUID().toString();
                String originName = fileName.replaceFirst("[.][^.]+$", "");
                String saveName = originName + "-"+System.currentTimeMillis() + "." + extension;
                String savePath = getSavePath(saveName);

                File file = new File(savePath);
                FileUtils.copyFile(uploadFile, file);


                image.setFileNm(fileName);
                image.setSaveNm(saveName);
                image.setFileType(fileType);
                image.setExtension(FilenameUtils.getExtension(fileName).toUpperCase());
                image.setFileSize(file.length());

                FileUtils.deleteQuietly(uploadFile);

                save(image);
            }
        }
        return resultMap;
    }



    private String getFileType(String extension) {
        switch (extension.toUpperCase()) {
            case Types.FileExtensions.PNG:
            case Types.FileExtensions.JPG:
            case Types.FileExtensions.JPEG:
            case Types.FileExtensions.GIF:
            case Types.FileExtensions.BMP:
            case Types.FileExtensions.TIFF:
            case Types.FileExtensions.TIF:
                return Types.FileType.IMAGE;

            case Types.FileExtensions.PDF:
                return Types.FileType.PDF;

            default:
                return Types.FileType.ETC;
        }
    }

    public String getBasePath() {
            return uploadRepository;
    }

    public String getSavePath(String saveName) {
        return getBasePath() + "/" + saveName;
    }

    public byte[] getFileBytes(String saveName) throws IOException {
        return FileUtils.readFileToByteArray(new File(getSavePath(saveName)));
    }


    public void preview(HttpServletResponse response, Long id, String type) throws IOException {
        Image image = findOne(id);

        if (image == null)
            return;

        MediaType mediaType = null;
        String imagePath = "";

        switch (image.getExtension()) {
            case Types.FileExtensions.JPEG:
            case Types.FileExtensions.JPG:
                mediaType = MediaType.IMAGE_JPEG;
                break;

            case Types.FileExtensions.PNG:
                mediaType = MediaType.IMAGE_PNG;
                break;

            case Types.FileExtensions.GIF:
                mediaType = MediaType.IMAGE_GIF;
                break;

            default:
        }

        switch (type) {
            case Types.ImagePreviewType.ORIGIN:
                imagePath = getSavePath(image.getSaveNm());
                break;

            case Types.ImagePreviewType.THUMBNAIL:
                imagePath = getSavePath(image.getThumbnailSaveName());
                break;
        }

        if (mediaType != null) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                byte[] bytes = FileUtils.readFileToByteArray(imageFile);

                response.setContentType(mediaType.toString());
                response.setContentLength(bytes.length);
                IOUtils.copy(FileUtils.openInputStream(new File(imagePath)), response.getOutputStream());
            }
        }
    }

    public void preview(HttpServletResponse response, Long id) throws IOException {
        preview(response, id, Types.ImagePreviewType.ORIGIN);
    }

    public void thumbnail(HttpServletResponse response, Long id) throws IOException {
        preview(response, id, com.chequer.axboot.core.code.Types.ImagePreviewType.THUMBNAIL);
    }


    public void deleteById(Long id) {
        imageRepository.deleteById(id);
//        FileUtils.deleteQuietly(uploadFile);
    }

}