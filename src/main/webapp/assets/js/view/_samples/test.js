var fnObj = {};

var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["users"],
            data: caller.searchView.getData(),
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.formView01.clear();
                ACTIONS.dispatch(ACTIONS.ROLE_GRID_DATA_INIT, {userCd: "", roleList: []});
            }
        });

        return false;
    },
    PAGE_SAVE: function (caller, act, data) {
        if (caller.gridView01.validate()) {
            axboot.ajax({
                type: "PUT",
                url: ["users"],
                data: JSON.stringify([caller.gridView01.getData()]),
                callback: function (res) {
                    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                }
            });
        }
    },
    FORM_CLEAR: function (caller, act, data) {
        axDialog.confirm({
            msg: LANG("ax.script.form.clearconfirm")
        }, function () {
            if (this.key == "ok") {
                caller.formView01.clear();
            }
        });
    },
    ITEM_CLICK: function (caller, act, data) {
        axboot.ajax({
            type: "GET",
            url: ["users"],
            data: {userCd: data.userCd},
            callback: function (res) {
                //ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                caller.formView01.setData(res);
            }
        });
    },
    ROLE_GRID_DATA_INIT: function (caller, act, data) {
        var list = [];
        CODE.userRole.forEach(function (n) {
            var item = {roleCd: n.roleCd, roleNm: n.roleNm, hasYn: "N", userCd: data.userCd};

            if (data && data.roleList) {
                data.roleList.forEach(function (r) {
                    if (item.roleCd == r.roleCd) {
                        item.hasYn = "Y";
                    }
                });
            }
            list.push(item);
        });

        caller.gridView02.setData(list);
    },
    ROLE_GRID_DATA_GET: function (caller, act, data) {
        return caller.gridView02.getData("Y");
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
                caller.formView01.setModalValue({
                    id: data.id,
                    name: data.name
                });
                this.close();
            }
        });
    }

});

var CODE = {};

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    var _this = this;
/*
    /////그리드 select 만들기
    axboot.call({
            type: "GET", url: "/api/v1/commonCodes", data: {groupCd: "USE_YN"},
            callback: function (res) {
                var useYnList = [];
                console.log(res);
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
        })*/


    axboot
        .call({
            type: "GET",
            url: ["commonCodes"],
            data: {groupCd: "USER_ROLE", useYn: "Y"},
            callback: function (res) {
                var userRole = [];
                res.list.forEach(function (n) {
                    userRole.push({
                        value: n.code, text: n.name + "(" + n.code + ")",
                        roleCd: n.code, roleNm: n.name,
                        data: n
                    });
                });
                this.userRole = userRole;
            }
        })
        .done(function () {

            CODE = this; // this는 call을 통해 수집된 데이터들.

            _this.pageButtonView.initView();
            _this.searchView.initView();
            _this.gridView01.initView();
            _this.gridView02.initView();
            _this.formView01.initView();

            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        });
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
        this.filter = $("#filter");
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            filter: this.filter.val()
        }
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


        this.target = axboot.gridBuilder({
            //showRowSelector: true,
            //frozenColumnIndex: 0,
            //sortable:true,
            //multipleSelect: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                {
                    key: "userCd",
                    label: COL("user.id"),
                    width: 120
                },
                {
                    key: "userNm",
                    label: COL("user.name"),
                    width: 120
                },
                {key: "locale", label:COL("user.language")},
                ////////////////////////////select 만들기
                {key: "useYn", label: "USE_YN", width: 150, align: "left", editor: {
                        type:"select",config:{
                            columnKeys:{
                                optionValue:"CD",optionText:"NM"
                            },
                            options:this.useYnList
                        }
                    }
                },
                ////////달력
                {key: "createdAt", label: "날짜", width: 150, align: "left", editor:{
                        type:"date",config:{}
                    }
                }
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex);
                    //this.self.select(this.dindex, {selectedClear: true});
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.list[this.dindex]);

                }
            }
        });
    },
    setData: function (_data) {
        this.target.setData(_data);
    },
    getData: function () {
        return this.target.getData();
    },
    align: function () {
        this.target.align();
    }
});


/**
 * formView01
 */
fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    getDefaultData: function () {
        return $.extend({}, axboot.formView.defaultData, {
            "compCd": "S0001",
            roleList: [],
            authList: []
        });
    },
    initView: function () {
        this.target = $("#formView01");
        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model); // 모델 포메터 시작
        this.initEvent();

        axboot.buttonClick(this, "data-form-view-01-btn", {
            "form-clear": function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);

            },
            "modal1": function () {
                ACTIONS.dispatch(ACTIONS.MODAL_OPEN)
            }
        });


        ACTIONS.dispatch(ACTIONS.ROLE_GRID_DATA_INIT, {});
    },
    initEvent: function () {
        var _this = this;
        this.model.onChange("password_change", function () {
            if (this.value == "Y") {
                _this.target.find('[data-ax-path="userPs"]').removeAttr("readonly");
                _this.target.find('[data-ax-path="userPs_chk"]').removeAttr("readonly");
                _this.target.find('[data-ax-path="userPs"]').attr("data-ax-validate", "required");
                _this.target.find('[data-ax-path="userPs_chk"]').attr("data-ax-validate", "required");
            } else {
                _this.target.find('[data-ax-path="userPs"]').attr("readonly", "readonly");
                _this.target.find('[data-ax-path="userPs_chk"]').attr("readonly", "readonly");
                _this.target.find('[data-ax-path="userPs"]').removeAttr("data-ax-validate");
                _this.target.find('[data-ax-path="userPs_chk"]').removeAttr("data-ax-validate");
            }
        });

        $('[data-ax5select]').ax5select({
            columnKeys: {
                optionValue: "optionValue", optionText: "optionText"
            },
            options: [
                {optionValue: 2, optionText: "Number"},
                {optionValue: 3, optionText: "substr"},
                {optionValue: 4, optionText: "substring"},
                {optionValue: 1, optionText: "String"},
                {optionValue: 5, optionText: "search"},
                {optionValue: 6, optionText: "parseInt"},
                {optionValue: 7, optionText: "toFixed"}
            ]
        });
    },
    getData: function () {
        var data = this.modelFormatter.getClearData(this.model.get()); // 모델의 값을 포멧팅 전 값으로 치환.

        data.authList = [];
        if (data.grpAuthCd) {
            data.grpAuthCd.forEach(function (n) {
                data.authList.push({
                    userCd: data.userCd,
                    grpAuthCd: n
                });
            });
        }

        data.roleList = ACTIONS.dispatch(ACTIONS.ROLE_GRID_DATA_GET);

        return $.extend({}, data);
    },
    setData: function (data) {

        if (typeof data === "undefined") data = this.getDefaultData();
        data = $.extend({}, data);

        if (data.authList) {
            data.grpAuthCd = [];
            data.authList.forEach(function (n) {
                data.grpAuthCd.push(n.grpAuthCd);
            });
        }
        ACTIONS.dispatch(ACTIONS.ROLE_GRID_DATA_INIT, {userCd: data.userCd, roleList: data.roleList});

        data.userPs = "";
        data.password_change = "";
        this.target.find('[data-ax-path="userPs"]').attr("readonly", "readonly");
        this.target.find('[data-ax-path="userPs_chk"]').attr("readonly", "readonly");
        this.target.find('[data-ax-path="userPs"]').removeAttr("data-ax-validate");
        this.target.find('[data-ax-path="userPs_chk"]').removeAttr("data-ax-validate");
        this.target.find('#wrap_pwCheck').css("display", "inline");
        this.model.setModel(data);
        this.modelFormatter.formatting(); // 입력된 값을 포메팅 된 값으로 변경


    },
    validate: function () {
        var rs = this.model.validate();
        if (rs.error) {
            alert(LANG("ax.script.form.validate", rs.error[0].jquery.attr("title")));
            rs.error[0].jquery.focus();
            return false;
        }
        return true;
    },
    clear: function () {
        this.model.setModel(this.getDefaultData());
        this.target.find('[data-ax-path="userPs"]').removeAttr("readonly");
        this.target.find('[data-ax-path="userPs_chk"]').removeAttr("readonly");
        this.target.find('[data-ax-path="userPs"]').attr("data-ax-validate", "required");
        this.target.find('[data-ax-path="userPs_chk"]').attr("data-ax-validate", "required");
        this.target.find('#wrap_pwCheck').css("display", "none");


    },
    setModalValue : function(data){
        this.model.set("modalCd",data.id);
        this.model.set("modalNm",data.name);
    }
});


/**
 * gridView
 */
fnObj.gridView02 = axboot.viewExtend(axboot.gridView, {
    initView: function () {

        var _this = this;
        this.target = axboot.gridBuilder({
            showLineNumber: false,
            target: $('[data-ax5grid="grid-view-02"]'),
            columns: [
                {key: "hasYn", label: COL("ax.admin.select"), width: 50, align: "center", editor: "checkYn"},
                {key: "roleCd", label: COL("ax.admin.user.role.code"), width: 150},
                {key: "roleNm", label: COL("ax.admin.user.role.name"), width: 180},
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex);
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.list[this.dindex]);
                }
            }
        });
    },
    setData: function (_data) {
        this.target.setData(_data);
    },
    getData: function (hasYn) {
        hasYn = hasYn || "Y";
        var list = ax5.util.filter(this.target.getList(), function () {
            return this.hasYn == hasYn;
        });
        return list;
    },
    align: function () {
        this.target.align();
    }
});