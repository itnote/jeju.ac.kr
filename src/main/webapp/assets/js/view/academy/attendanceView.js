var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/attendancedtl/viewList"],
            data: caller.searchView.getData(),
            callback: function (res) {
                var dateList = {};

                axboot.promise()
                    .then(function (ok) {

                        axboot.ajax({
                            type: "GET",
                            url: ["/api/v1/attendancedtl/dateList"],
                            data: {
                                schDate : $("#schDate").val()
                            },
                            callback: function (result) {
                                dateList = result.list[0];
                                ok();
                            },
                            options: {
                                onError: function (err) {
                                }
                            }
                        });
                    })
                    .then(function (ok) {

                        var grid = new ax5.ui.grid({
                            frozenColumnIndex: 9,
                            target: $('[data-ax5grid="grid-view-01"]'),
                            columns: [
                                {key: "natnNm", label: '국적', width: 80, align: "center"},
                                {key: "stdtId", label: '학번', width: 80, align: "left"},
                                {key: "stdtNmKor", label: '학생명', width: 110, align: "left"},
                                {key: "stdtNmEng", label: '학생 영문명', width: 160, align: "left"},
                                {key: "genderNm", label: '성별', width: 40, align: "center"},
                                {key: "birthDt", label: '생년월일', width: 80, align: "center"},
                                {key: "fabsc", label: '결석(전반기)', width: 80, align: "center"},
                                {key: "rabsc", label: '결석(후반기)', width: 80, align: "center"},
                                {key: "tchrDivNm", label: '수업구분', width: 80, align: "center"},

                                {key: "d14", label: dateList.d14, width: 80, align: "center"},
                                {key: "d13", label: dateList.d13, width: 80, align: "center"},
                                {key: "d12", label: dateList.d12, width: 80, align: "center"},
                                {key: "d11", label: dateList.d11, width: 80, align: "center"},
                                {key: "d10", label: dateList.d10, width: 80, align: "center"},
                                {key: "d9", label: dateList.d9, width: 80, align: "center"},
                                {key: "d8", label: dateList.d8, width: 80, align: "center"},
                                {key: "d7", label: dateList.d7, width: 80, align: "center"},
                                {key: "d6", label: dateList.d6, width: 80, align: "center"},
                                {key: "d5", label: dateList.d5, width: 80, align: "center"},
                                {key: "d4", label: dateList.d4, width: 80, align: "center"},
                                {key: "d3", label: dateList.d3, width: 80, align: "center"},
                                {key: "d2", label: dateList.d2, width: 80, align: "center"},
                                {key: "d1", label: dateList.d1, width: 80, align: "center"},
                                {key: "d0", label: dateList.d0, width: 80, align: "center"}
                            ],
                            body: {
                                mergeCells: ["natnNm", "stdtId","stdtNmKor", "stdtNmEng", "genderNm", "birthDt", "fabsc", "rabsc"],
                            }
                        });
                        grid.setData(res);
                    })
                    .catch(function () {

                    });
            },
            options: {
                onError: function (err) {
                }
            }
        });
        return false;
    },
    EXCEL: function () {
        $.form({
            action: '/api/v1/attendancedtl/atdtMissExcel',
            target: 'excelDownFrm',
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val(),
                schPeriodCd : $("#schPeriodCd").val()
            }
        }).submit();
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
                data: {
                    schMyClasChk : $("#schMyClasChk").is(":checked")
                },
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
        })
        .catch(function () {

        });

    // 과정명 select
    $('[data-ax-path="schSemeYear"]').change(function(){
        $("#schSemeSeq option").remove();
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/semester/semeSeqCombo"],
            data: {
                schSemeYear : $(this).val(),
                schMyClasChk : $("#schMyClasChk").is(":checked")
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
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            }
        });
    });

    // 내수강반 click
    $("#schMyClasChk").click(function(){
        $('[data-ax-path="schSemeYear"]').change();
        //$('[data-ax-path="schPeriodCd"]').change();
    });
};


fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            "search": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
            "fn1": function () {
                ACTIONS.dispatch(ACTIONS.EXCEL);
            }
        });
    }
});

//== view 시작
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
        this.schDate = $("#schDate");

        $('#calendar').ax5picker({
            direction: "auto",
            content: {
                type: 'date'
            }
        });
        $("#schDate").val(ax5.util.date((new Date()), {return:'yyyy-MM-dd'})); //출력일자에 현재날짜 셋팅
    },
    getData: function () {
        return {
            schSemeYear: this.schSemeYear.val(),
            schSemeSeq: this.schSemeSeq.val(),
            schPeriodCd: this.schPeriodCd.val(),
            schClasSeq: this.schClasSeq.val(),
            schDate:this.schDate.val()
        }
    }
});
