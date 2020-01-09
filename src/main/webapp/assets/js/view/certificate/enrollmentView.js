var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["/api/v1/certificate/enrollmentList"],
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
            chkList[i].printDt = $("#printDt").val();
            chkList[i].confEndDt = $("#confEndDt").val();
        }

        $.form({
            action: '/api/v1/certificate/'+data+'Down',
            target: 'downFrm',
            data: chkList
        }).submit();
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
                        ok();
                    }
                });
            });

        })
        .then(function (ok) {
            _this.pageButtonView.initView();
            _this.searchView.initView();
            _this.gridView01.initView();

            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
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
                ACTIONS.dispatch(ACTIONS.DOWN,"confirmation");
            },
            "fn2": function () {
                ACTIONS.dispatch(ACTIONS.DOWN, "enrollment");
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
        this.schStdt = $("#schStdt");

        $('#calendar2').ax5picker({
            direction: "auto",
            content: {
                type: 'date'
            }
        });
        $('#calendar').ax5picker({
            direction: "auto",
            content: {
                type: 'date'
            }
        });
        $("#printDt").val(ax5.util.date((new Date()), {return:'yyyy-MM-dd'})); //출력일자에 현재날짜 셋팅
        $("#confEndDt").val(ax5.util.date((new Date()), {return:'yyyy-MM-dd'})); //종료일자에 현재날짜 셋팅
    },
getData: function () {
        return {
            schSemeYear: this.schSemeYear.val(),
            schSemeSeq: this.schSemeSeq.val(),
            schStdt: this.schStdt.val(),
            schPrintDt: $("#printDt").val()
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
                {key: "semeYear", label: '학년도', hidden:"true"},
                {key: "semeSeq", label: '학기', hidden:"true"}
            ]
        });
    },
    getData: function (_type) {
        return this.target.getList(_type);
    }
});
