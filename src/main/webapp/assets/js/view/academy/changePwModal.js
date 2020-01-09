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


            if(formView01Data.userPs != formView01Data.newPw){
                axToast.push('비밀번호와 비밀번호 확인 내용이 같지 않습니다.');
                return;
            }
            var regExpPw = /(?=.*\d{1,50})(?=.*[a-zA-Z]{1,50}).{8,50}$/;

            if(!regExpPw.exec(formView01Data.userPs)){
                axToast.push('영문과 숫자를 조합한 8자리 이상 비밀번호를 입력해주세요');
                return;
            }

            axboot.ajax({
                type: "PUT", url: ["/api/v1/users/changePw"],
                data: JSON.stringify(formView01Data),
                callback: function (res) {
                    if(res.message == "SUCCESS") {
                        //parent.axboot.modal.callback("변경 되었습니다");
                        ACTIONS.dispatch(ACTIONS.PAGE_CLOSE);
                    }
                }
            });
        }
    }
});

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


