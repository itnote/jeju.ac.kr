<%@ page import="com.chequer.axboot.core.utils.RequestUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>
<ax:set key="pageName" value="File Browser"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="axbody_class" value="baseStyle"/>
<%
    RequestUtils req = RequestUtils.of(request);

    String schSemeYear =req.getString("schSemeYear");
    request.setAttribute("schSemeYear",schSemeYear);

    String schSemeSeq =req.getString("schSemeSeq");
    request.setAttribute("schSemeSeq",schSemeSeq);

    String schPeriodCd =req.getString("schPeriodCd");
    request.setAttribute("schPeriodCd",schPeriodCd);


%>
<ax:layout name="modal">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/excelUpload.js?ver=8' />"></script>
    </jsp:attribute>
    <jsp:attribute name="header">
        <h1 class="title">
            <i class="cqc-browser"></i>
            학생목록
        </h1>
    </jsp:attribute>
    <jsp:body>
        <ax:page-buttons>
            <button type="button" class="btn btn-info" data-page-btn="down">
                    업로드양식 다운
            </button>
            <!--
            <a  href="/assets/excel/tuition_student.xlsx" class="btn btn-info" download>업로드양식 다운</a>
            -->
            <button type="button" class="btn btn-info" data-page-btn="excel">
                <i class="cqc-file-excel-o">
                    엑셀업로드
                </i>
            </button>
            <button type="button" class="btn btn-default" data-page-btn="close"><ax:lang id="ax.admin.sample.modal.button.close"/></button>
        </ax:page-buttons>

        <div role="page-header">
            <ax:form id="excelUploadForm" name="excelUploadForm">
                <input type="hidden" id="schSemeYear" value="${schSemeYear}"/>
                <input type="hidden" id="schSemeSeq" value="${schSemeSeq}"/>
                <input type="hidden" id="schPeriodCd" value="${schPeriodCd}"/>
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='첨부파일' width="100%">
                                <div class="contents">
                                    <div>첨부파일은 한개만 등록 가능합니다.</div>
                                    <br/>
                                    <dl class="vm_name">
                                        <dd>
                                            <input id="excelFile" type="file" name="excelFile" />
                                        </dd>
                                    </dl>
                                </div>
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>


        <iframe name="excelDownFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>