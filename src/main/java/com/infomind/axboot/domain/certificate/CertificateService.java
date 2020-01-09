package com.infomind.axboot.domain.certificate;

import com.chequer.axboot.core.parameter.RequestParams;
import com.infomind.axboot.domain.docLog.DocLog;
import com.infomind.axboot.domain.docLog.DocLogMapper;
import com.infomind.axboot.utils.SessionUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;


@Service
public class CertificateService {

    @Inject
    private CertificateMapper certificateMapper;

    @Inject
    private DocLogMapper docLogMapper;

    public byte[] generatedToPdf(String inputFileName, Map<String, Object> params, JRBeanCollectionDataSource dataSource) throws IOException {
        byte[] pdfReport = null;
        InputStream inputStream = this.getClass().getResourceAsStream("/reports/" + inputFileName +".jrxml");

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            pdfReport = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
        return pdfReport;
    }


    public static JRBeanCollectionDataSource getDataSource(Collection dataSource) {
        return new JRBeanCollectionDataSource(dataSource);
    }

    public void insertDocLog(List<Certificate> list, String docCd) {
        for (Certificate cert : list) {
            //문서대장 insert and get DOC_SEQ
            DocLog docLog = new DocLog();
            docLog.setSemeYear(cert.getSemeYear());
            docLog.setSemeSeq(cert.getSemeSeq());
            docLog.setStdtId(cert.getStdtId());
            docLog.setDocCd(docCd);
            docLog.setPrintDt(cert.getPrintDt());
            docLog.setCreatedBy(SessionUtils.getCurrentUser().getUserCd());

            docLogMapper.insertDocLog(docLog);
            cert.setDocSeq(docLog.getDocSeq()+"");
            cert.setPrintYear(cert.getPrintDt().substring(0,4));
        }
    }

    public List<Certificate> enrollmentList(RequestParams<Map> requestParams) {

        Map map = new HashMap<String, Object>();
        map.put("schSemeYear", requestParams.getString("schSemeYear",""));
        map.put("schSemeSeq", requestParams.getString("schSemeSeq",""));
        map.put("schPrintDt", requestParams.getString("schPrintDt",""));
        map.put("schStdt", requestParams.getString("schStdt",""));

        return certificateMapper.enrollmentList(map);
    }

    public List<Certificate> completionList(RequestParams<Map> requestParams) {

        Map map = new HashMap<String, Object>();
        map.put("schSemeYear", requestParams.getString("schSemeYear",""));
        map.put("schSemeSeq", requestParams.getString("schSemeSeq",""));
        map.put("schPrintDt", requestParams.getString("schPrintDt",""));
        map.put("schStdt", requestParams.getString("schStdt",""));

        return certificateMapper.completionList(map);
    }

    public List<Certificate> tuitionList(RequestParams<Map> requestParams) {

        Map map = new HashMap<String, Object>();
        map.put("schSemeYear", requestParams.getString("schSemeYear",""));
        map.put("schSemeSeq", requestParams.getString("schSemeSeq",""));
        map.put("schPrintDt", requestParams.getString("schPrintDt",""));
        map.put("schStdt", requestParams.getString("schStdt",""));
        map.put("schFeeYn", requestParams.getString("schFeeYn",""));
        map.put("schPeriodCd", requestParams.getString("schPeriodCd",""));
        map.put("schPaymentDt", requestParams.getString("schPaymentDt",""));


        return certificateMapper.tuitionList(map);
    }

    public List<Certificate> docLogList(RequestParams<Map> requestParams) {

        Map map = new HashMap<String, Object>();
        map.put("schSemeYear", requestParams.getString("schSemeYear",""));
        map.put("schDocSeq", requestParams.getString("schDocSeq",""));
        map.put("schCreatedAt", requestParams.getString("schCreatedAt",""));

        return certificateMapper.docLogList(map);
    }

    public byte[] enrollmentDown(List<Certificate> list, Map map) {
        byte[] bytes = null;
        for(Certificate certificate : list){
            certificate.setPrintDtKor(formatDt(certificate.getPrintDt(),"yyyy년 MM월 dd일"));
        }

        try {
            insertDocLog(list, "I");

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("Enrollment", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] confirmationDown(List<Certificate> list, Map map) {
        byte[] bytes = null;
        for(Certificate certificate : list){
            certificate.setPrintDtKor(formatDt(certificate.getPrintDt(),"yyyy년 MM월 dd일"));
            certificate.setConfEndDt(formatDt(certificate.getConfEndDt(),"yyyy. MM. dd"));
        }

        try {
            insertDocLog(list, "A");

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("Confirmation", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] studyDown(List<Certificate> list, Map map) {
        byte[] bytes = null;
        for(Certificate certificate : list){
            certificate.setPrintDtKor(formatDt(certificate.getPrintDt(),"yyyy년 MM월 dd일"));
        }

        try {
            insertDocLog(list, "B");

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("StudyCompletion", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] completionDown(List<Certificate> list, Map map) {
        byte[] bytes = null;
        for(Certificate certificate : list){
            certificate.setPrintDtKor(formatDt(certificate.getPrintDt(),"yyyy년 MM월 dd일"));
            certificate.setPrintDtEng(formatDt(certificate.getPrintDt(),"MMMMM dd, yyyy"));
        }

        try {
            insertDocLog(list, "C");

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("Completion", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] scoreDown(List<Certificate> list, Map map) {
        byte[] bytes = null;
        for(Certificate certificate : list){
            certificate.setPrintDtKor(formatDt(certificate.getPrintDt(),"yyyy년 MM월 dd일"));

        }

        try {
            insertDocLog(list, "D");

            List<Transcript> trans = certificateMapper.transcriptList(map);
            Map<String, List<Transcript>> transMap = trans.stream()
                    .collect(groupingBy(Transcript::getStdtId));
            for(Certificate cert : list) {
                if (transMap.containsKey(cert.getStdtId())) {
                    cert.setTrans(transMap.get(cert.getStdtId()));
                }
            }

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("ScoreCompletion", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }


    public byte[] receiptDown(List<Certificate> list, Map map) {
        byte[] bytes = null;

        try {

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("Receipt", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] tuitionBillDown(List<Certificate> list, Map map) {
        byte[] bytes = null;
        for(Certificate certificate : list){
            certificate.setPrintDtKor(formatDt(certificate.getPrintDt(),"yyyy년 MM월 dd일"));
            certificate.setPaymentDtKor(formatDt(certificate.getPaymentDt(),"yyyy. MM. dd."));
            certificate.setPrintDtEng(formatDt(certificate.getPrintDt(),"MMMMM dd, yyyy"));
            certificate.setPaymentDtEng(formatDt(certificate.getPaymentDt(),"MMMMM dd, yyyy"));
        }

        try {

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("TuitionBill", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] tuitionBillChnDown(List<Certificate> list, Map map) {
        byte[] bytes = null;
        for(Certificate certificate : list){
            certificate.setPrintDtChn(formatDt(certificate.getPrintDt(),"yyyy年 MM月 dd日"));
            certificate.setPaymentDtChn(formatDt(certificate.getPaymentDt(),"yyyy年 MM月 dd日"));

        }

        try {

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("TuitionBillChn", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] tuitionBillEngDown(List<Certificate> list, Map map) {
        byte[] bytes = null;
        for(Certificate certificate : list){
            certificate.setPrintDtEng(formatDt(certificate.getPrintDt(),"MMMMM dd, yyyy"));
            certificate.setPaymentDtEng(formatDt(certificate.getPaymentDt(),"MMMMM dd, yyyy"));

        }

        try {

            JRBeanCollectionDataSource dataSource = getDataSource(list);
            bytes = generatedToPdf("TuitionBillEng", map, dataSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private String formatDt(String dt,String pattern){
        SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern,new Locale("en","US"));
        String formatDate ="";
        try {
            Date date=stringToDate.parse(dt);
            formatDate = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }

    public void tuitionExcelFormDown(HashMap map, HttpServletResponse response) throws IOException {
        XSSFWorkbook wb = null;

        File file = new File(map.get("path") + "/assets/excel/tuition_student.xlsx");
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString());
        Files.copy(file.toPath(), tmpFile.toPath());
        try {
            wb = (XSSFWorkbook) WorkbookFactory.create(tmpFile);
            List<TuitionExcel> tuitionList = certificateMapper.tuitionExcelList(map);
            Sheet sheet = wb.getSheetAt(0);
            Row row;
            Cell cell;
            int rowNo = 1;

            for (TuitionExcel tuition : tuitionList) {
                String[] values = {
                        tuition.getStdtId(), tuition.getStdtNm(), tuition.getStdtNmEng(), tuition.getStdtNmChn(),
                        tuition.getNatnNm(), tuition.getBirthDt(), tuition.getGenderNm(), tuition.getFreshYn(),
                        tuition.getFeeDt(), tuition.getDormPayYn(), tuition.getAppPayYn(), tuition.getInsPayYn(),
                        tuition.getBedPayYn()
                };
                row = sheet.createRow(rowNo++);
                for (int cellNo = 0; cellNo < values.length; cellNo++) {
                    cell = row.createCell(cellNo);
                    cell.setCellValue(values[cellNo]);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
            String fileName = java.net.URLEncoder.encode("등록금_" + sdf.format(System.currentTimeMillis()), "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");   //application/vnd.ms-excel
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName +".xlsx");

            wb.write(response.getOutputStream());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tmpFile.delete();
            wb.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

}