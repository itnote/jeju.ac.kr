var fnObj = {};

var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {

        if($("#schStdt").val() == '' && $("#schTchr").val() == '') {
            alert("조회조건을 하나 이상 입력해주세요.");
            return;
        }

        axboot.ajax({
            type: "GET",
            url: ["/api/v1/student/counStudentList"],
            data: caller.searchView.getData(),
            callback: function (res) {
                caller.gridView01.setData(res);
            },
            options: {
                onError: function (err) {

                }
            }
        });

        return false;
    },
    EXCEL_EXPORT:function(caller, act, data){
        caller.gridView01.target.exportExcel("counseling.xls");
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
            _this.gridView01.initView();


            // ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        })
        .catch(function (err) {

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
        this.schTchr = $("#schTchr");
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
            openYn: this.openYn.val(),
            schTchr: this.schTchr.val()
        }
    }
});
/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            frozenRowIndex: 0,
            showLineNumber: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "stdtId", label: '학번', width: 110, align: "center"},
                {key: "natnNm", label: '국적', width: 100, align: "center"},
                {key: "semeYear", label: '학년도', width: 100, align: "left",hidden:"true"},
                {key: "semeSeq", label: '학기', width: 100, align: "left",hidden:"true"},
                {key: "clasSeq", label: '수강반', width: 100, align: "left",hidden:"true"},
                {key: "periodCd", label: '전후반기', width: 100, align: "left",hidden:"true"},
                {key: "stdtNmKor", label: '학생명', width: 130, align: "left"},
                {key: "stdtNmEng", label: '학생 영문명', width: 200, align: "left"},
                {key: "natnCd", label: '국적', width: 120, align: "left",hidden:"true"},
                {key: "gender", label: '성별', width: 60, align: "center",hidden:"true"},
                {key: "genderNm", label: '성별', width: 80, align: "center"},
                {key: "birthDt", label: '생년월일', width: 100, align: "center"},
                {key: "counDt", label: "면담일자", width: 100, align: "center"},
                {key: "counNm", label: "면담구분", width: 100, align: "center"},
                {key: "createdBy", label: "작성자", width: 130, align: "left",hidden:"true"},
                {key: "createdByNm", label: "작성자", width: 130, align: "left"},
                {key: "contents", label: "면담내용", width: 400, align: "left", multiLine: "true"}
            ],
            body: {
                align: "center",
                columnHeight: 60,
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                },
            }
        });

    }

});


