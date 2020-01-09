package com.infomind.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.infomind.axboot.domain.certificate.Certificate;
import com.infomind.axboot.domain.certificate.CertificateExcelServiceImpl;
import com.infomind.axboot.domain.certificate.CertificateService;
import com.infomind.axboot.domain.file.CommonFileService;
import com.infomind.axboot.domain.semester.Semester;
import com.infomind.axboot.domain.semester.SemesterService;
import com.infomind.axboot.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/certificate")
@Slf4j
public class CertificateController extends BaseController {

    @Inject
    private CertificateService certificateService;
    @Inject
    private CommonFileService commonFileService;
    @Inject
    private CertificateExcelServiceImpl certificateExcelService;
    @Inject
    private SemesterService semesterService;

    private final ResponseEntity<byte[]> fileDownload(byte[] bytes, String prefix) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
        String fileName = java.net.URLEncoder.encode(prefix + "_" + sdf.format(System.currentTimeMillis()), "UTF-8");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName + ".pdf");

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/enrollmentList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse enrollmentList(RequestParams<Map> requestParams) {
        List<Certificate> list = certificateService.enrollmentList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/completionList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse completionList(RequestParams<Map> requestParams) {
        List<Certificate> list = certificateService.completionList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/tuitionList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse tuitionList(RequestParams<Map> requestParams) {
        List<Certificate> list = certificateService.tuitionList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/docLogList", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse docLogList(RequestParams<Map> requestParams) {
        List<Certificate> list = certificateService.docLogList(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/enrollmentDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> enrollmentDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());

        Map map = new HashMap();
        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.enrollmentDown(list, map);

        return fileDownload(bytes, "재학증명서");
    }

    @RequestMapping(value = "/confirmationDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> confirmationDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());

        Map map = new HashMap();
        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.confirmationDown(list, map);

        return fileDownload(bytes, "확인서");
    }

    @RequestMapping(value = "/studyDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> studyDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());

        Map map = new HashMap();
        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.studyDown(list, map);

        return fileDownload(bytes, "연수증명서");
    }

    @RequestMapping(value = "/completionDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> completionDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());

        Map map = new HashMap();
        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.completionDown(list, map);

        return fileDownload(bytes, "수료증명서");
    }

    @RequestMapping(value = "/scoreDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> scoreDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        String schForm = request.getParameter("schForm");

        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());
        Map map = JsonUtils.jsonToAny(schForm, new TypeToken<Map>() {}.getType());

        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.scoreDown(list, map);

        return fileDownload(bytes, "성적증명서");
    }

    @RequestMapping(value = "/receiptDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> receiptDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());

        Map map = new HashMap();
        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.receiptDown(list, map);

        return fileDownload(bytes, "영수증");
    }

    @RequestMapping(value = "/tuitionBillDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> tuitionBillDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());

        Map map = new HashMap();
        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.tuitionBillDown(list, map);

        return fileDownload(bytes, "납부고지서(한영)");
    }

    @RequestMapping(value = "/tuitionBillChnDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> tuitionBillChnDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());

        Map map = new HashMap();
        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.tuitionBillChnDown(list, map);

        return fileDownload(bytes, "납부고지서(중문)");
    }

    @RequestMapping(value = "/tuitionBillEngDown", method = RequestMethod.POST)
    public ResponseEntity<byte[]> tuitionBillEngDown(HttpServletRequest request) throws Exception {
        String data = request.getParameter("data");
        List<Certificate> list = JsonUtils.jsonToAny(data, new TypeToken<List<Certificate>>() {}.getType());

        Map map = new HashMap();
        String imagePath = request.getSession().getServletContext().getRealPath("/") + "assets/images/";
        map.put("imagePath", imagePath);

        byte[] bytes = certificateService.tuitionBillEngDown(list, map);

        return fileDownload(bytes, "납부고지서(영문)");
    }

    @ResponseBody
    @RequestMapping(value = "/excelUpload", method = {RequestMethod.POST},  produces = APPLICATION_JSON)
    public Responses.ListResponse excelUpload(@RequestParam(value = "excelFile") MultipartFile excelFile, HttpServletRequest request) throws Exception {
        if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        File destFile = commonFileService.multiPartFileToFile(excelFile);


        List<Certificate> certicicateList = certificateExcelService.excelFileRead(destFile);
        destFile.delete();

        Semester semester=semesterService.getSemesterAmt(request.getParameter("schSemeYear"), request.getParameter("schSemeSeq"));
        for(Certificate certificate:certicicateList){
            certificate.setSemeYear(semester.getSemeYear());
            certificate.setSemeNmKor(semester.getKor());
            certificate.setSemeNmEng(semester.getEng());

            int regAmt= 0;
            int dormAmt=0;
            int insAmt=0;
            int appAmt=0;
            int bedAmt=0;

            DecimalFormat decimalFormat = new DecimalFormat("###,###");

            if(certificate.getFreshYn().equals("Y")){
                regAmt = semester.getNewRegStdAmt();
            }else{
                regAmt = semester.getOldRegStdAmt();
            }

            if(certificate.getDormPayYn().equals("Y")){
                dormAmt=semester.getNewDormStdAmt();

            }
            if(certificate.getAppPayYn().equals("Y")){
                appAmt=semester.getNewAppStdAmt();

            }
            if(certificate.getInsPayYn().equals("Y")){
                insAmt=semester.getNewInsStdAmt();

            }
            if(certificate.getBedPayYn().equals("Y")){
                bedAmt=semester.getNewBedStdAmt();

            }
            certificate.setBedAmt(String.valueOf(decimalFormat.format(bedAmt)));
            certificate.setInsAmt(String.valueOf(decimalFormat.format(insAmt)));
            certificate.setAppAmt(String.valueOf(decimalFormat.format(appAmt)));
            certificate.setDormAmt(String.valueOf(decimalFormat.format(dormAmt)));
            certificate.setRegAmt(String.valueOf(decimalFormat.format(regAmt)));
            certificate.setTotalAmt(String.valueOf(decimalFormat.format(regAmt+insAmt+dormAmt)));
            certificate.setAllAmt(String.valueOf(decimalFormat.format(regAmt+insAmt+dormAmt+appAmt+bedAmt)));
        }


        return Responses.ListResponse.of(certicicateList);
    }


    @RequestMapping(value = "/tuitionExcelFormDown", method = RequestMethod.POST)
    public void tuitionExcelFormDown(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String data = request.getParameter("data");
        HashMap map = JsonUtils.jsonToAny(data, new TypeToken<HashMap>(){}.getType());
        String path = request.getSession().getServletContext().getRealPath("/");
        map.put("path", path);
        certificateService.tuitionExcelFormDown(map, response);

    }




}