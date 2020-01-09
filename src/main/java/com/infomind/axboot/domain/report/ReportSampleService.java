package com.infomind.axboot.domain.report;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.stereotype.Service;
import com.infomind.axboot.domain.BaseService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.chequer.axboot.core.parameter.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ReportSampleService extends BaseService<ReportSample, Integer> {
    private ReportSampleRepository reportSampleRepository;

    @Inject
    public ReportSampleService(ReportSampleRepository reportSampleRepository) {
        super(reportSampleRepository);
        this.reportSampleRepository = reportSampleRepository;
    }

    public List<ReportSample> gets(RequestParams<ReportSample> requestParams) {
        return findAll();
    }

    public void exportReportToHtml(HttpServletResponse response, String inputFileName, JRBeanCollectionDataSource dataSource) throws Exception {
        InputStream inputStream = this.getClass().getResourceAsStream("/reports/" + inputFileName +".jrxml");
        try {
            response.setContentType("text/html");
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
            exporter.exportReport();
        } catch (JRException | IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }


    public byte[] generatedToPdf(String inputFileName, Map<String, Object> params, JRBeanCollectionDataSource dataSource) throws Exception{
        log.info("****************generate PDF report****************");
        byte[] pdfReport = null;
        InputStream inputStream = this.getClass().getResourceAsStream("/reports/" + inputFileName +".jrxml");
        try {
            log.info("***infomind*** Start Compiling!!!!");
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            log.info("***infomind*** Done Compiling!!! ...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                    params, dataSource);
            if (jasperPrint != null) {
                 pdfReport = JasperExportManager
                        .exportReportToPdf(jasperPrint);
                log.info("******* inputStream Close *******");
                inputStream.close();
            }
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        return pdfReport;
    }




    public static JRBeanCollectionDataSource getDataSource(Collection dataSource) {
        return new JRBeanCollectionDataSource(dataSource);
    }


}