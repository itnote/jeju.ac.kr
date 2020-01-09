var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["users"],
            data: caller.searchView.getData(),
            callback: function (res) {
                caller.gridView01.setData(res);
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
    PAGE_SAVE: function (caller, act, data) {
        var saveList = [].concat(caller.gridView01.getData("modified"));
        saveList = saveList.concat(caller.gridView01.getData("deleted"));

        axboot.ajax({
            type: "PUT",
            url: ["users"],
            data: JSON.stringify(saveList),
            callback: function (res) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                axToast.push("저장 되었습니다");
            }
        });
    },
    ITEM_CLICK: function (caller, act, data) {

    },
    ITEM_ADD: function (caller, act, data) {
        caller.gridView01.addRow();
    },
    ITEM_DEL: function (caller, act, data) {
        caller.gridView01.delRow("selected");
    },
    dispatch: function (caller, act, data) {
        var result = ACTIONS.exec(caller, act, data);
        if (result != "error") {
            return result;
        } else {
            // 직접코딩
            return false;
        }
    },
    ///모달창
    MODAL_OPEN: function (caller, act, data) {
        axboot.modal.open({
            modalType: "TEST-MODAL",
            param: "",
            sendData: function () {
                return {
                    //"sendData": "AX5UI"
                };
            },
            callback: function (data) {
                //console.log(data);
                caller.searchView.setEtc3Value({
                    id: data.id,
                    name: data.name
                });
                this.close();
            }
        });
    }

});

var CODE = {};  //select 만들기 추가
// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    ////////////////////////////select 만들기
    var _this = this;

    console.log(parent.axboot.modal.getData());
    // 부모창에서 가져온 값
    axboot
        .call({
            type: "GET", url: "/api/v1/commonCodes", data: {groupCd: "USE_YN"},
            callback: function (res) {
                var useYnList = [];
                res.list.forEach(function (n) {
                    useYnList.push({
                        //여기서 CD, NM의 문자를 사용한 이유는 아래 gridView의 원산지의
                        // optionValue와 optionText를 CD와 NM으로 사용했기 때문이다.
                        CD: n.code, NM: n.name + "(" + n.code + ")"
                    });
                });
                this.useYnList = useYnList;
                //alert(JSON.stringify(this.useYnList));
            }
        })

        .done(function () {

            CODE = this; // this는 call을 통해 수집된 데이터들.

            _this.pageButtonView.initView(); // this --> _this 로 변경
            _this.searchView.initView();
            _this.gridView01.initView();

            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        });  //추가

    ////////////////////////////select 만들기
};

fnObj.pageResize = function () {

};


fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            "search": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
            "save": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SAVE);
            },
            "excel": function () {

            }
        });
    }
});

//== view 시작
/**
 * searchView
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    getDefaultData: function () {
        return $.extend({}, axboot.searchView.defaultData, {});
    },
    initEvent: function () {
        var _this = this;
    },
    initView: function () {
        this.target = $("#searchView0");
        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model); // 모델 포메터 시작

        this.initEvent();
    //////////////달력만들기
        this.target.find('[data-ax5picker="date"]').ax5picker({
            direction: "auto",
            content: {
                type: 'date'
            }
        });
        ///모달창
        axboot.buttonClick(this, "data-searchview-btn", {
            "modal": function () {
                ACTIONS.dispatch(ACTIONS.MODAL_OPEN)
            }
        });

    },

    getData: function () {
        var data = this.modelFormatter.getClearData(this.model.get()); // 모델의 값을 포멧팅 전 값으로 치환.
        return $.extend({}, data);
    },
    setData: function (data) {

        if (typeof data === "undefined") data = this.getDefaultData();
        data = $.extend({}, data);

        this.target.find('[data-ax-path="key"]').attr("readonly", "readonly");

        this.model.setModel(data);
        this.modelFormatter.formatting(); // 입력된 값을 포메팅 된 값으로 변경
    },
    ////모달 데이터 셋
    setEtc3Value: function (data) {
        this.model.set("etc3", data.id);
        this.model.set("etc4", data.name);

    }
});


/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;
        ////////////////////////////select 만들기
        this.useYnList = CODE.useYnList;
        ////////////////////////////select 만들기

        this.target = axboot.gridBuilder({
            showRowSelector: true,
            frozenColumnIndex: 0,
            sortable:true,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {key: "userCd", label: "USER_CD", width: 160, align: "left", editor: "text"},
                {key: "userNm", label: "USER_NM", width: 150, align: "left", editor: "text"},
                ////////달력
                {key: "createdAt", label: "날짜", width: 150, align: "left", editor:{
                    type:"date",config:{}
                    }
                    },
                ////////////////////////////select 만들기
                {key: "useYn", label: "USE_YN", width: 150, align: "left", editor: {
                    type:"select",config:{
                        columnKeys:{
                            optionValue:"CD",optionText:"NM"
                        },
                            options:this.useYnList
                        }
                    }
                }
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, {selectedClear: true});
                }
            }
        });

        axboot.buttonClick(this, "data-grid-view-01-btn", {
            "add": function () {
                ACTIONS.dispatch(ACTIONS.ITEM_ADD);
            },
            "delete": function () {
                ACTIONS.dispatch(ACTIONS.ITEM_DEL);
            }
        });
    },
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
    },
    addRow: function () {
        this.target.addRow({__created__: true}, "last");
    }
});