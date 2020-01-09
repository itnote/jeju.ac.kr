<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>
<ax:set key="pageName" value="File Browser"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="axbody_class" value="baseStyle"/>

<ax:layout name="modal">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/excelModal.js?v=2' />"></script>
    </jsp:attribute>
    <jsp:attribute name="header">
        <h1 class="title">
            <i class="cqc-browser"></i>
            학생목록
        </h1>
    </jsp:attribute>
    <jsp:body>
        <ax:page-buttons>
            <a  href="/assets/excel/student.xlsx" class="btn btn-info" download>업로드양식 다운</a>
            <button type="button" class="btn btn-info" data-page-btn="excel">
                <i class="cqc-file-excel-o">
                    엑셀업로드
                </i>
            </button>
            <button type="button" class="btn btn-fn1" data-page-btn="save">
                <i class="cqc-save">
                    저장
                </i>
            </button>
            <button type="button" class="btn btn-default" data-page-btn="close"><ax:lang id="ax.admin.sample.modal.button.close"/></button>
        </ax:page-buttons>

        <div role="page-header">
            <ax:form id="excelUploadForm" name="excelUploadForm">
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

        <ax:split-layout name="ax1" orientation="vertical">
            <ax:split-panel width="*" style="padding-right: 0px;">
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01"></div>
            </ax:split-panel>
        </ax:split-layout>

    </jsp:body>
</ax:layout>