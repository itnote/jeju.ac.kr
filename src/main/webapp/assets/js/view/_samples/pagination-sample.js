var fnObj = {};
var vPageSize = 5;

var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/student/counStudentList"],
            // data: $.extend({}, caller.searchView.getData(), caller.gridView01.getPageData()),
            data: caller.searchView.getData(),
            callback: function (res) {
                // caller.gridView01.setData(res.list, 0);
                gridView.setData(res.list, 0);
            },
            options: {
                // axboot.ajax 함수에 2번째 인자는 필수가 아닙니다. ajax의 옵션을 전달하고자 할때 사용합니다.
                onError: function (err) {
                    console.log(err);
                }
            }
        });

        return false;
    },
    EXCEL_EXPORT:function(caller, act, data){
        gridView.target.exportExcel("counseling.xls");
    }
});

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    var _this = this;

    axboot.promise()
        .then(function (ok, fail, data) {
            //  학년도 select
            axboot.ajax({
                type: "GET",
                url: ["/api/v1/semester/yearCombo"],
                callback: function (res) {
                    for(var i=0;i<res.list.length;i++){
                        $("#schSemeYear").append($('<option>',{
                            value:res.list[i].SEME_YEAR,
                            text:res.list[i].SEME_YEAR
                        }));
                    }
                    $('[data-ax-path="schSemeYear"]').change();
                    ok();
                }
            });
        })
        .then(function (ok) {
            _this.pageButtonView.initView();
            _this.searchView.initView();
            gridView.initView();

            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        })
        .catch(function (err) {
            console.log(err);
        });

    // 과정명 select
    $('[data-ax-path="schSemeYear"]').change(function(){
        $("#schSemeSeq option").remove();
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/semester/semeSeqCombo"],
            data: {
                schSemeYear : $(this).val()
            },
            callback: function (res) {
                for(var i=0;i<res.list.length;i++){
                    $("#schSemeSeq").append($('<option>',{
                        value:res.list[i].SEME_SEQ,
                        text:res.list[i].KOR
                    }));
                }
                $('[data-ax-path="schSemeSeq"]').change();
            }
        });
    });

    // 전후반기구분 select
    $('[data-ax-path="schSemeSeq"]').change(function(){
        $("#schPeriodCd option").remove();
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/classmst/periodCombo"],
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $(this).val()
            },
            callback: function (res) {
                for(var i=0;i<res.list.length;i++){
                    $("#schPeriodCd").append($('<option>',{
                        value:res.list[i].PERIOD_CD,
                        text:res.list[i].NM
                    }));
                }
                $('[data-ax-path="schPeriodCd"]').change();
            }
        });
    });

    // 수강반 select
    $('[data-ax-path="schPeriodCd"]').change(function(){
        $("#schClasSeq option").remove();
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/classmst/clasSeqCombo"],
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val(),
                schPeriodCd : $(this).val(),
                schMyClasChk : $("#schMyClasChk").is(":checked")
            },
            callback: function (res) {
                for(var i=0;i<res.list.length;i++){
                    $("#schClasSeq").append($('<option>',{
                        value:res.list[i].CLAS_SEQ,
                        text:res.list[i].CLAS_NM
                    }));
                }
            }
        });
    });

};

fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            "search": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
            "excel": function () {
                ACTIONS.dispatch(ACTIONS.EXCEL_EXPORT);
            }
        });
    }
});

/**
 * searchView
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document["searchView0"]);
        this.target.attr("onsubmit", "return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);");
        this.schSemeYear = $("#schSemeYear");
        this.schSemeSeq = $("#schSemeSeq");
        this.schPeriodCd = $("#schPeriodCd");
        this.schClasSeq = $("#schClasSeq");
        this.schStdt = $("#schStdt");
        this.openYn = $("#openYn");
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            schSemeYear: this.schSemeYear.val(),
            schSemeSeq: this.schSemeSeq.val(),
            schPeriodCd: this.schPeriodCd.val(),
            schClasSeq: this.schClasSeq.val(),
            schStdt: this.schStdt.val(),
            openYn: this.openYn.val()
        }
    }
});


/**
 * gridView
 */
// fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
//     initView: function () {
//         var _this = this;
//
//         this.target = axboot.gridBuilder({
//             showRowSelector: true,
//             frozenColumnIndex: 0,
//             target: $('[data-ax5grid="grid-view-01"]'),
//             columns: [
//                 {key: "natnNm", label: '국적', width: 120, align: "left"},
//                 {key: "stdtId", label: '학번', width: 100, align: "left",hidden:"true"},
//                 {key: "semeYear", label: '학년도', width: 100, align: "left",hidden:"true"},
//                 {key: "semeSeq", label: '학기', width: 100, align: "left",hidden:"true"},
//                 {key: "clasSeq", label: '수강반', width: 100, align: "left",hidden:"true"},
//                 {key: "periodCd", label: '전후반기', width: 100, align: "left",hidden:"true"},
//                 {key: "stdtNmKor", label: '학생명', width: 100, align: "left"},
//                 {key: "stdtNmEng", label: '학생 영문명', width: 100, align: "left"},
//                 {key: "natnCd", label: '국적', width: 120, align: "left",hidden:"true"},
//                 {key: "gender", label: '성별', width: 60, align: "center",hidden:"true"},
//                 {key: "genderNm", label: '성별', width: 80, align: "center"},
//                 {key: "birthDt", label: '생년월일', width: 120, align: "center"},
//                 {key: "counDt", label: "면담일자", width: 130, align: "center"},
//                 {key: "counNm", label: "면담구분", width: 100, align: "left"},
//                 {key: "createdBy", label: "작성자", width: 100, align: "left"},
//                 {key: "contents", label: "면담내용", width: 400, align: "left"}
//             ],
//             body: {
//                 onClick: function () {
//                     this.self.select(this.dindex, {selectedClear: true});
//                 }
//             },
//             onPageChange: function (pageNumber) {
//                 console.log(_this);
//             }
//         });
//
//     },
//     setData: function (_list, _pageNo) {
//         this.target.setData({
//             list: _list,
//             page: {
//                 currentPage: _pageNo,
//                 pageSize: vPageSize,
//                 totalElements: _list.length,
//                 totalPages: (_list.length % vPageSize == 0 ? Math.floor(_list.length / vPageSize) : Math.ceil(_list.length / vPageSize))
//             }
//         });
//         return this;
//     }
// });

var gridView = {
    initView: function () {
        this.target = new ax5.ui.grid();
        this.target.setConfig({
            target: $('[data-ax5grid="grid-view-01"]'),
            frozenRowIndex: 0,
            showLineNumber: true,
            body: {
                align: "center",
                columnHeight: 28,
                onClick: function () {
                    this.self.select(this.dindex);
                }
            },
            columns: [
                {key: "natnNm", label: '국적', width: 120, align: "left"},
                {key: "stdtId", label: '학번', width: 100, align: "left",hidden:"true"},
                {key: "semeYear", label: '학년도', width: 100, align: "left",hidden:"true"},
                {key: "semeSeq", label: '학기', width: 100, align: "left",hidden:"true"},
                {key: "clasSeq", label: '수강반', width: 100, align: "left",hidden:"true"},
                {key: "periodCd", label: '전후반기', width: 100, align: "left",hidden:"true"},
                {key: "stdtNmKor", label: '학생명', width: 100, align: "left"},
                {key: "stdtNmEng", label: '학생 영문명', width: 100, align: "left"},
                {key: "natnCd", label: '국적', width: 120, align: "left",hidden:"true"},
                {key: "gender", label: '성별', width: 60, align: "center",hidden:"true"},
                {key: "genderNm", label: '성별', width: 80, align: "center"},
                {key: "birthDt", label: '생년월일', width: 120, align: "center"},
                {key: "counDt", label: "면담일자", width: 130, align: "center"},
                {key: "counNm", label: "면담구분", width: 100, align: "left"},
                {key: "createdBy", label: "작성자", width: 100, align: "left"},
                {key: "contents", label: "면담내용", width: 400, align: "left"}
            ],
            page: {
                navigationItemCount: 9,
                height: 30,
                display: true,
                firstIcon: '<i class="fa fa-step-backward" aria-hidden="true"></i>',
                prevIcon: '<i class="fa fa-caret-left" aria-hidden="true"></i>',
                nextIcon: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                lastIcon: '<i class="fa fa-step-forward" aria-hidden="true"></i>',
                onChange: function () {
                    gridView.setData(gridView.getData(), this.page.selectPage);
                }
            }
        });
        return this;
    },
    setData: function (_list, _pageNo) {
        // console.log(this.target);
        this.target.setData({
            list: _list,
            page: {
                currentPage: _pageNo,
                pageSize: vPageSize,
                totalElements: _list.length,
                totalPages: (_list.length % vPageSize == 0 ? Math.floor(_list.length / vPageSize) : Math.ceil(_list.length / vPageSize))
            }
        });
        return this;
    },
    getData: function (_type) {
        var _list = this.target.getList(_type);

        return _list;
    }
};