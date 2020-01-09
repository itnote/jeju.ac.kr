var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/attendancemst/tchrAtdcList"],
            data: caller.searchView.getData(),
            callback: function (res) {
                //console.log(caller.searchView.getData())

                caller.gridView01.setData(res);
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
            action: '/api/v1/attendancemst/tchrAtdcExcel',
            target: 'excelDownFrm',
            data: {
                schSemeYear : $("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val(),
                schPeriodCd : $("#schPeriodCd").val(),
                schBeginAtdcDt : $("#schBeginAtdcDt").val(),
                schEndAtdcDt : $("#schEndAtdcDt").val(),
                schTchrCd : $("#schTchrCd").val()
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
            _this.gridView01.initView();
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



    ///////////////강사조회 클릭이벤트
    $(document).on("click",".tchr",function(){

        //var index = this.getAttribute("data-custom-btn");

        axboot.modal.open({
            modalType: "TCHR-MODAL",
            param: {

            },
            sendData: function(){
                return {
                    "sendData": "AX5UI"
                };
            },
            callback: function (data) {
                $("#schTchrCd").val(data.userCd);
                $("#schTchrNm").val(data.userNm);
                //_this.searchView.setValue("schTchrCd",data.userCd);
                //_this.searchView.setValue("schTchrNm",data.userNm);
                this.close();
            }
        });
    })
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
        this.schBeginAtdcDt = $("#schBeginAtdcDt");
        this.schEndAtdcDt = $("#schEndAtdcDt");
        this.schTchrCd = $("#schTchrCd");

        $('.calendar').ax5picker({
            direction: "auto",
            content: {
                type: 'date'
            }
        });
        var date = new Date();

        $("#schBeginAtdcDt").val(ax5.util.date((new Date(date.getFullYear(), date.getMonth() - 1, 1)), {return:'yyyy-MM-dd'})); //출력일자에 전월 1일 날짜 셋팅
        $("#schEndAtdcDt").val(ax5.util.date((new Date(date.getFullYear(), date.getMonth(), 0)), {return:'yyyy-MM-dd'})); //출력일자에 전월 말일 날짜 셋팅
    },
    getData: function () {
        return {
            schSemeYear: this.schSemeYear.val(),
            schSemeSeq: this.schSemeSeq.val(),
            schPeriodCd: this.schPeriodCd.val(),
            schBeginAtdcDt: this.schBeginAtdcDt.val(),
            schEndAtdcDt: this.schEndAtdcDt.val(),
            schTchrCd: this.schTchrCd.val()
        }
    }
});


fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "tchrCd", label: '사번', width: 80, align: "center"},
                {key: "userNm", label: '강사명', width: 100, align: "center"},
                {key: "atdcDt", label: '일자', width: 120, align: "center"},
                {key: "dow", label: '요일', width: 80, align: "center"},
                {key: "hours", label: '수업시수', width: 80, align: "center"},
                {key: "absentHours", label: '결강시수', width: 80, align: "center"},
                {key: "subHours", label: '대강시수', width:80, align: "center"}

            ],
            body: {
                /*
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                },

                 */
                mergeCells: ["tchrCd", "userNm"],
                grouping: {
                    by: ["userNm"],
                    columns: [
                        {
                            label: function () {
                                return this.groupBy.labels.join(" ") + " 총계";
                            }, colspan: 4, align: "center"
                        },
                        {
                            key: "totHours", collector: function () {
                                var value = Number(0);
                                this.list.forEach(function (n) {
                                    if (!n.__isGrouping) {
                                        value += parseInt((Number(n.hours)));
                                    }
                                });
                                return parseInt(value);
                            }, align: "center"
                        }, {
                            key: "totAbsentHours", collector: function () {
                                var value = Number(0);
                                this.list.forEach(function (n) {
                                    if (!n.__isGrouping) {
                                        value += parseInt((Number(n.absentHours)));
                                    }
                                });
                                return parseInt(value);
                            }, align: "center"
                        }
                        , {
                            key: "totSubHours", collector: function () {
                                var value = Number(0);
                                this.list.forEach(function (n) {
                                    if (!n.__isGrouping) {
                                        value += parseInt((Number(n.subHours)));
                                    }
                                });
                                return parseInt(value);
                            }, align: "center"
                        }
                    ]
                }
            }

        });},
            getData: function (_type) {
                var list = [];
                var _list = this.target.getList(_type);

                if (_type == "modified" || _type == "deleted") {
                    list = ax5.util.filter(_list, function () {
                        delete this.deleted;
                        return this.key;
                    });
                } else {
                    list = _list;
                }
                return list;
            }
            });
