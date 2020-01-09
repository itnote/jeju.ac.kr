var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_CLOSE: function (caller, act, data) {
        if (parent) {
            parent.axboot.modal.close();
        }
    },
    PAGE_SAVE: function (caller, act, data) {
        if (caller.formView01.validate()) {
            var formView01Data = caller.formView01.getData();
            console.log(formView01Data);
            axboot.ajax({
                type: "PUT", url: ["/api/v1/schedule"],
                data: JSON.stringify(formView01Data),

                callback: function (res) {
                    if(res.message == "SUCCESS") {
                        parent.axboot.modal.callback("저장 되었습니다");
                        ACTIONS.dispatch(ACTIONS.PAGE_CLOSE);
                    }

                }
            });
        }
    },
    PAGE_CHOICE: function (caller, act, data) {
        var list = caller.gridView01.getData("selected");
        if (list.length > 0) {
            if (parent && parent.axboot && parent.axboot.modal) {
                parent.axboot.modal.callback(list[0]);
            }
        } else {
            alert(LANG("ax.script.requireselect"));
        }
    },
    PAGE_DEL: function (caller, act, data) {

        if (!confirm(LANG("ax.script.deleteconfirm"))) return;
        
        var formView01Data = caller.formView01.getData();

        axboot.ajax({
            type: "DELETE",
            url: "/api/v1/schedule",
            data: JSON.stringify(formView01Data),
            callback: function (res) {
                parent.axboot.modal.callback("삭제 되었습니다");
                ACTIONS.dispatch(ACTIONS.PAGE_CLOSE);
            }
        });
    },
    ITEM_CLICK: function (caller, act, data) {
        ACTIONS.dispatch(ACTIONS.PAGE_CHOICE);
    }
});

var CODE = {};

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    var _this = this;
    _this.pageButtonView.initView();
    _this.formView01.initView();
};


fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            "save": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SAVE);
            },
            "delete": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_DEL);
            },
            "close": function () {
                ACTIONS.dispatch(ACTIONS.PAGE_CLOSE);
            }
        });
    }
});

/**
 * formView01
 */
fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    getDefaultData: function () {
        return $.extend({}, axboot.formView.defaultData, {
            scheDt : $("[data-ax-path='scheDt']").val(),
            semeYear : $("[data-ax-path='semeYear']").val(),
            semeSeq : $("[data-ax-path='semeSeq']").val(),
            scheNm : $("[data-ax-path='scheNm']").val(),
            holiYn : $("#holiYn").val(),
            counYn : $("#counYn").val(),
            weekCd : $("#weekCd").val()
        });
    },
    initView: function () {
        this.target = $("#formView01");
        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model); // 모델 포메터 시작
        this.initEvent();

    },
    initEvent: function () {
        var _this = this;
    },
    getData: function () {
        var data = this.modelFormatter.getClearData(this.model.get()); // 모델의 값을 포멧팅 전 값으로 치환.
        return $.extend({}, data);
    },
    setData: function (data) {

        if (typeof data === "undefined") data = this.getDefaultData();
        data = $.extend({}, data);
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
    }
});