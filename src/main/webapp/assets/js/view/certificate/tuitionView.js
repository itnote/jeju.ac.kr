var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {

        axboot.ajax({
            type: "GET",
            url: ["/api/v1/certificate/tuitionList"],
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
    DOWN: function (caller, act, data) {
        var chkList = caller.gridView01.getData("selected");


        if (chkList.length == 0) {
            axToast.push("학생을 선택해주세요.");
            return;
        }


        for(var i=0;i<chkList.length;i++){
            // 납입내역 없어도 영수증 출력 가능하게 수정 요청
            // if(data=='receipt'&&chkList[i].feeDt == null){
            //     axToast.push("선택한 학생 중 납입내역이 없는 학생이 존재합니다.");
            //     return;
            // }
            chkList[i].printDt = $("#printDt").val();
            chkList[i].paymentDt = $("#paymentDt").val();
        }

        $.form({
            action: '/api/v1/certificate/'+ data +'Down',
            target: 'downFrm',
            data: chkList,
            schForm: caller.searchView.getData()
        }).submit();
    },
    EXCEL_MODAL: function (caller, act, data) {

        axboot.modal.open({
            modalType: "EXCEL-UPLOAD",
            param: {
                schSemeYear:$("#schSemeYear").val(),
                schSemeSeq : $("#schSemeSeq").val(),
                schPeriodCd : $("#schPeriodCd").val()
            },
            sendData: function(){
                return {
                    "sendData": "AX5UI"
                };
            },
            callback: function (data) {
                caller.gridView01.setData(data);
                this.close();
            }
        });
    }
});

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
                }
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
                        //ok();
                    }
                });
            });
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
                        ok();
                    }
                });
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
            "fn1": function () {
                ACTIONS.dispatch(ACTIONS.DOWN, "receipt");
            },
            "fn2": function () {
                ACTIONS.dispatch(ACTIONS.DOWN, "tuitionBill");
            },
            "fn3": function () {
                ACTIONS.dispatch(ACTIONS.DOWN, "tuitionBillChn");
            },
            "fn4": function () {
                ACTIONS.dispatch(ACTIONS.DOWN, "tuitionBillEng");
            },
            "fn5": function () {
                ACTIONS.dispatch(ACTIONS.EXCEL_MODAL);
            }
        });
    }
});

/**
 * 검색조건
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document["searchView0"]);
        this.target.attr("onsubmit", "return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);");
        this.schSemeYear = $("#schSemeYear");
        this.schSemeSeq = $("#schSemeSeq");
        this.schPeriodCd = $("#schPeriodCd");
        this.schStdt = $("#schStdt");
        this.schFeeYn = $("#schFeeYn");

        $('[data-ax5picker="date"]').ax5picker({
            direction: "auto",
            content: {
                type: 'date'
            }
        });
        $("#printDt").val(ax5.util.date((new Date()), {return:'yyyy-MM-dd'})); //출력일자에 현재날짜 셋팅
        $("#paymentDt").val(ax5.util.date((new Date()), {return:'yyyy-MM-dd'})); //납부일자에 현재날짜 셋팅
    },
    getData: function () {
        return {
            schSemeYear: this.schSemeYear.val(),
            schSemeSeq: this.schSemeSeq.val(),
            schStdt: this.schStdt.val(),
            schFeeYn: this.schFeeYn.val(),
            schPeriodCd : this.schPeriodCd.val(),
            schPrintDt: $("#printDt").val(),
            schPaymentDt: $("#paymentDt").val()
        }
    }
});


/**
 * 학생목록
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            showRowSelector: true,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "natnNm", label: '국적', width: 120, align: "center"},
                {key: "stdtId", label: '학번', width: 100, align: "center"},
                {key: "stdtNmKor", label: '학생명', width: 150, align: "left"},
                {key: "stdtNmEng", label: '학생 영문명', width: 250, align: "left"},
                {key: "genderNm", label: '성별', width: 70, align: "center"},
                {key: "birthDt", label: '생년월일', width: 100, align: "center"},
                {key: "freshYn", label: '신입생여부', width: 100, align: "center"},
                {key: "feeDt", label: '납입일', width: 100, align: "center"},
                {key: "semeYear", label: '학년도', hidden:"true"},
                {key: "semeSeq", label: '학기', hidden:"true"}
            ]
        });
    },
    getData: function (_type) {
        return this.target.getList(_type);
    }
});
