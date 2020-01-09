<%@ page import="com.infomind.axboot.utils.SessionUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>




<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="menuGrpCd" value="${SessionUtils.getCurrentUser().getMenuGrpCd()}"/>
<ax:set key="function1Label" value='<i class="cqc-download"></i> 강사별상담이력'/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/counselMgmt.js?v=30'/>"></script>
    </jsp:attribute>
    <jsp:body>
        <%--<input type="text" value="${menuGrpCd}" id="menuGrpCd"/>--%>
        <input type="hidden" id="schOpenYn" value="<c:if test='${menuGrpCd eq "USER"}'>Y</c:if>">


        <ax:page-buttons></ax:page-buttons>

        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='학년도' width="250px">
                            <select data-ax-path="schSemeYear" class="form-control W100"  id="schSemeYear">
                            </select>
                        </ax:td>
                        <ax:td label='과정명' width="400px">
                            <select data-ax-path="schSemeSeq" class="form-control W250"  id="schSemeSeq">
                            </select>

                        </ax:td>
                        <ax:td label='전후반기구분' width="250px">
                            <select data-ax-path="schPeriodCd" class="form-control W100"  id="schPeriodCd">
                            </select>
                        </ax:td>
                        <ax:td label='수강반명' width="300px">
                            <select data-ax-path="schClasSeq" class="form-control W150"  id="schClasSeq">
                            </select>
                        </ax:td>
                        <ax:td label='내수강반' width="300px">
                            <input type="checkbox" class="checkbox-inline" id="schMyClasChk" <c:if test='${menuGrpCd eq "USER"}'>checked</c:if>>
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="vertical">
            <ax:split-panel width="782" style="padding-right: 10px;">
                <!-- 목록 -->
                <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                    <div class="left">
                        <h2><i class="cqc-list"></i>
                          학생 목록 </h2>
                    </div>

                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>
            </ax:split-panel>
            <ax:splitter></ax:splitter>
            <ax:split-panel width="*" style="padding-left: 10px;" scroll="scroll">
                <ax:split-layout name="ax2" orientation="horizontal">
                    <ax:split-panel height="*" style="padding-bottom: 10px;">
                        <!-- 목록 -->
                        <div class="ax-button-group" data-fit-height-aside="grid-view-02">
                            <div class="left">
                                <h2><i class="cqc-news"></i>
                                    면담이력 </h2>
                            </div>
                            <div class="right">
                                <button type="button" class="btn btn-default" data-grid-view-02-btn="delete">
                                    <i class="cqc-minus"></i>
                                    <ax:lang id="ax.admin.delete"/>
                                </button>
                            </div>

                        </div>
                        <div data-ax5grid="grid-view-02" data-fit-height-content="grid-view-02" style="height:300px;"></div>
                    </ax:split-panel>
                    <ax:splitter></ax:splitter>
                    <ax:split-panel height="*" style="padding-top: 10px;">

                        <!-- 폼 -->
                        <div class="ax-button-group" role="panel-header">
                            <div class="left">
                                <h2><i class="cqc-news"></i>
                                    면담내용
                                </h2>
                            </div>
                            <div class="right">
                                <button type="button" class="btn btn-default" data-form-view-01-btn="item-add">
                                    <i class="cqc-plus"></i>
                                    신규
                                </button>
                            </div>
                        </div>
                        <ax:form name="formView01">
                            <ax:tbl clazz="ax-form-tbl" minWidth="500px">
                                <ax:tr>
                                    <ax:td label="(*)면담일자" width="220px">
                                        <div class="input-group" data-ax5picker="date">
                                            <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="counDt" title="면담일자 " data-ax-validate="required" id="counDt"/>
                                            <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                                        </div>

                                    </ax:td>
                                    <ax:td label="(*)면담구분" width="200px">
                                        <ax:common-code groupCd="COUN_CD" dataPath="counCd" id="counCd" />
                                    </ax:td>
                                    <ax:td label="공개여부" width="200px" >
                                        <ax:common-code groupCd="USE_YN" dataPath="openYn" id="openYn" />


                                        <input type="hidden" class="form-control" data-ax-path="semeYear"/>
                                        <input type="hidden" class="form-control" data-ax-path="semeSeq"/>
                                        <input type="hidden" class="form-control" data-ax-path="periodCd"/>
                                        <input type="hidden" class="form-control" data-ax-path="stdtId"/>
                                        <input type="hidden" class="form-control" data-ax-path="clasSeq"/>
                                        <input type="hidden" class="form-control" data-ax-path="counSeq"/>
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="면담내용" width="500px">
                                        <textarea style="width:100%;" rows="10"  data-ax-path="contents" class="form-control" id="contents">

                                        </textarea>
                                    </ax:td>

                                </ax:tr>
                            </ax:tbl>
                        </ax:form>

                    </ax:split-panel>
                </ax:split-layout>

            </ax:split-panel>
        </ax:split-layout>
        <iframe name="excelDownFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>