<%@ page import="com.infomind.axboot.utils.SessionUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${pageRemark}"/>
<ax:set key="page_auto_height" value="true"/>
<ax:set key="function1Label" value='<i class="cqc-download"></i> 학기일정표'/>
<ax:set key="menuGrpCd" value="${SessionUtils.getCurrentUser().getMenuGrpCd()}"/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" />
        <script type="text/javascript" src="<c:url value='/assets/js/view/academy/semesterMgmt.js?v=67' />"></script>
        <script type="text/javascript">
            var userMenuGrpCd = "${menuGrpCd}";
        </script>
        <style type="text/css">
            .calendar-item-day {
                font-size: 30px !important;
            }
        </style>

    </jsp:attribute>
    <jsp:body>

        <ax:page-buttons></ax:page-buttons>

        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='학년도' width="300px">
                            <select data-ax-path="schSemeYear" class="form-control W100"  id="schSemeYear">
                                <option value="">전체</option>

                            </select>
                            <div data-ax5select="schSemeYear" data-ax5select-config="{}" ></div>
                        </ax:td>
                        <ax:td label='학기구분' width="300px">
                            <ax:common-code groupCd="SEME_CD" id="schSemeCd" emptyText="전체" />
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>

        <ax:split-layout name="ax1" orientation="vertical">
            <ax:split-panel width="600" style="padding-right: 10px;">
                <!-- 목록 -->
                <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                    <div class="left">
                        <h2><i class="cqc-list"></i>
                            학기목록</h2>
                    </div>
                    <div class="right">

                    </div>
                </div>
                <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;" id="grid01"></div>
            </ax:split-panel>
            <ax:splitter></ax:splitter>
            <ax:split-panel width="*" style="padding-left: 10px;" scroll="scroll">
                <ax:tab-layout name="ax2" data_fit_height_content="layout-view-01" style="height:100%;">
                    <ax:tab-panel label="학기정보" scroll="scroll">
                        <!-- 폼 -->
                        <div class="ax-button-group" role="panel-header">
                            <div class="left">
                                <h2><i class="cqc-news"></i>
                                    학기정보
                                </h2>
                            </div>
                            <div class="right">
                                <button type="button" class="btn btn-default" data-form-view-01-btn="form-clear">
                                    <i class="cqc-erase"></i>
                                    <ax:lang id="ax.admin.clear"/>
                                </button>
                            </div>
                        </div>
                        <ax:form name="formView01">
                            <ax:tbl clazz="ax-form-tbl" minWidth="500px">
                                <ax:tr>
                                    <ax:td label="(*)학년도" width="250px">
                                        <select data-ax-path="semeYear" class="form-control W100"  data-ax-validate="required" title="학년도" id="semeYear">
                                            <option value="" selected disabled hidden>선택</option>
                                        </select>

                                    </ax:td>
                                    <ax:td label="(*)학기구분" width="250px">
                                        <ax:common-code groupCd="SEME_CD" dataPath="semeCd"/>
                                        <%--<input type="text" data-ax-path="semeCd" title="학기구분" class="form-control" data-ax-validate="required" />--%>
                                        <input type="hidden" data-ax-path="semeSeq" title="학기시퀀스" class="form-control" id="semeSeq"/>
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="(*)과정 한글명" width="300px" >
                                        <input type="text" data-ax-path="kor" class="form-control"  title="과정 한글명"  data-ax-validate="required"/>
                                        <input type="hidden" data-ax-path="corsNm" title="과정명" class="form-control" />
                                    </ax:td>

                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="과정 영문명" width="300px">
                                        <input type="text" data-ax-path="eng" class="form-control"  />
                                    </ax:td>

                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="과정 중문명" width="300px">
                                        <input type="text" data-ax-path="chn" class="form-control" />
                                    </ax:td>

                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="(*)시작일" width="250px">

                                        <div class="input-group" data-ax5picker="date">
                                            <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="startDt" title="시작일 " data-ax-validate="required" id="startDt"/>
                                            <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                                        </div>

                                    </ax:td>

                                    <ax:td label="(*)종료일" width="250px">

                                        <div class="input-group" data-ax5picker="date">
                                            <input type="text" class="form-control" placeholder="yyyy-mm-dd" data-ax-path="endDt"  title="종료일"  data-ax-validate="required" id="endDt"/>
                                            <span class="input-group-addon"><i class="cqc-calendar"></i></span>
                                        </div>

                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="(*)기준시수" width="250px">
                                        <input type="number" data-ax-path="totHours" class="form-control" title="기준시수"  data-ax-validate="required"  style="width:100px;"  placeholder="시간"/>
                                    </ax:td>
                                    <ax:td label="전반기시수" width="250px">
                                        <input type="number" data-ax-path="firstHalf" class="form-control" title="전반기시수"  style="width:100px;"  placeholder="시간"/>
                                    </ax:td>
                                    <ax:td label="후반기시수" width="250px">
                                        <input type="number" data-ax-path="secondHalf" class="form-control" title="후반기시수"  style="width:100px;"  placeholder="시간"/>
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="(*)월요일" width="250px">

                                        <ax:common-code groupCd="WEEK_CD" dataPath="monCd" id="monCd" />

                                    </ax:td>
                                    <ax:td label="(*)화요일" width="250px">

                                        <ax:common-code groupCd="WEEK_CD" dataPath="tueCd" id="tueCd"/>
                                    </ax:td>
                                    <ax:td label="(*)수요일" width="250px">

                                        <ax:common-code groupCd="WEEK_CD" dataPath="wedCd" id="wedCd" />
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="(*)목요일" width="250px">

                                        <ax:common-code groupCd="WEEK_CD" dataPath="thuCd" id="thuCd" />
                                    </ax:td>
                                    <ax:td label="(*)금요일" width="250px">

                                        <ax:common-code groupCd="WEEK_CD" dataPath="friCd" id="friCd" />
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="(*)재학생등록금" width="250px">
                                        <input type="number" data-ax-path="oldRegStdAmt" class="form-control" title="재학생등록금"  data-ax-validate="required"  style="width:100px;" placeholder="원"/>
                                    </ax:td>
                                    <ax:td label="(*)신입생등록금" width="250px">
                                        <input type="number" data-ax-path="newRegStdAmt" class="form-control" title="신입생등록금"  data-ax-validate="required" style="width:100px;" placeholder="원"/>
                                    </ax:td>
                                    <ax:td label="(*)신입생기숙사" width="250px">
                                        <input type="number" data-ax-path="newDormStdAmt" class="form-control" title="신입생기숙사"  data-ax-validate="required" style="width:100px;" placeholder="원"/>
                                    </ax:td>
                                </ax:tr>
                                <ax:tr>
                                    <ax:td label="(*)신입생전형료" width="250px">
                                        <input type="number" data-ax-path="newAppStdAmt" class="form-control" title="재학생등록금"  data-ax-validate="required"  style="width:100px;" placeholder="원"/>
                                    </ax:td>
                                    <ax:td label="(*)신입생의료보험료" width="250px">
                                        <input type="number" data-ax-path="newInsStdAmt" class="form-control" title="신입생등록금"  data-ax-validate="required" style="width:100px;" placeholder="원"/>
                                    </ax:td>
                                    <ax:td label="(*)신입생침구" width="250px">
                                        <input type="number" data-ax-path="newBedStdAmt" class="form-control" title="신입생기숙사"  data-ax-validate="required" style="width:100px;" placeholder="원"/>
                                    </ax:td>
                                </ax:tr>
                            </ax:tbl>

                            <div class="H5"></div>
                            <div class="ax-button-group">
                                <div class="left">
                                    <h3>
                                        <i class="cqc-list"></i>
                                        과목정보
                                    </h3>
                                </div>
                                <div class="right">
                                    <button type="button" class="btn btn-default" data-grid-view-02-btn="item-add">
                                        <i class="cqc-plus"></i>
                                        <ax:lang id="ax.admin.add"/>
                                    </button>
                                    <button type="button" class="btn btn-default" data-grid-view-02-btn="item-remove">
                                        <i class="cqc-minus"></i>
                                        <ax:lang id="ax.admin.delete"/>
                                    </button>
                                </div>
                            </div>

                            <div data-ax5grid="grid-view-02" style="height: 200px;"></div>

                        </ax:form>
                    </ax:tab-panel>
                    <ax:tab-panel label="일정관리" scroll="scroll" >
                        <div class="H10"></div>
                        <div id="calendar-target" style="width:640px;border:1px solid #ccc;border-radius: 5px;padding: 5px;overflow: hidden;">
                        </div>
                    </ax:tab-panel>
                </ax:tab-layout>

            </ax:split-panel>
        </ax:split-layout>
        <iframe name="excelDownFrm" style="height:0;width:0;visibility:hidden;"></iframe>
    </jsp:body>
</ax:layout>