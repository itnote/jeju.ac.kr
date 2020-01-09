var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        if(caller.searchView.getData().schDocSeq ==''&&caller.searchView.getData().schCreatedAt ==''){
            alert('문서번호 또는 발급일를 입력해주세요.');
            return false;
        }

        axboot.ajax({
            type: "GET",
            url: ["/api/v1/certificate/docLogList"],
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

        })
        .catch(function (err) {
        });

};

fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            "search": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
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
        this.schDocSeq = $("#schDocSeq");
        this.schCreatedAt = $("#schCreatedAt");
        this.target.find('[data-ax5picker="date"]').ax5picker({
            direction: "auto",
            content: {
                type: 'date'
            }

        });
        $("#schCreatedAt").val(ax5.util.date((new Date()), {return:'yyyy-MM-dd'})); //출력일자에 현재날짜 셋팅

    },
    getData: function () {
        return {
            schSemeYear: this.schSemeYear.val(),
            schDocSeq: this.schDocSeq.val(),
            schCreatedAt: this.schCreatedAt.val()
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
            showRowSelector: false,
            multipleSelect: false,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "printDt", label: '출력기준일', width: 100, align: "center"},
                {key: "semeYear", label: '학년도', width: 100, align: "center"},
                {key: "semeNmKor", label: '학기명', width: 150, align: "center"},
                {key: "docSeq", label: '문서번호', width: 120, align: "center"},
                {key: "stdtId", label: '학번', width: 150, align: "center"},
                {key: "stdtNmKor", label: '학생명(한)', width: 150, align: "center"},
                {key: "stdtNmEng", label: '학생명(영)', width: 200, align: "center"},
                {key: "docCd", label: '문서코드', width: 70, align: "center",hidden:"true"},
                {key: "semeSeq", label: '학기시퀀스', width: 150, align: "left",hidden:"true"},
                {key: "docCdNm", label: '문서구분', width: 100, align: "center"},
                {key: "createdAt", label: '발급일', width: 150, align: "center"},
                {key: "createdBy", label: '발급자', width: 120, align: "center"}


            ]
        });
    },
    getData: function (_type) {
        return this.target.getList(_type);
    }
});
