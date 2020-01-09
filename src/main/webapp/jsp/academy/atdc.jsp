<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${PAGE_REMARK}"/>
<ax:set key="page_auto_height" value="true"/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/atdc.js' />"></script>
    </jsp:attribute>
    <jsp:body>

        <ax:page-buttons></ax:page-buttons>


        <div role="page-header">
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="horizontal">
            <ax:split-panel width="*" style="">

                <!-- 목록 -->
                <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                    <div class="left">
                        <h2><i class="cqc-list"></i>
                            프로그램 목록 </h2>
                    </div>
                    <div class="right">
                        <button type="button" class="btn btn-info" data-page-btn="atdc">
                            <i class="cqc-file-excel-o">
                                출석업로드
                            </i>
                        </button>
                        <button type="button" class="btn btn-info" data-page-btn="score">
                            <i class="cqc-file-excel-o">
                                성적업로드
                            </i>
                        </button>
                    </div>
                </div>
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
            </ax:split-panel>
        </ax:split-layout>

    </jsp:body>
</ax:layout>