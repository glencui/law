define(['component/nav_bar','component/header', 'ajaxhelper', 'utility'], function (nav_bar, header, ajaxHelper, util) {
    var UserManagement = {
        initialize: function () {
            //nav_bar
            nav_bar.initialize("i_navbar", 3);
            header.initialize("i_header", "员工管理");
            this.mainBox = $('#i_mainbox');
            this.tplFun = _.template($("#i_tpl").html());
            this._sendRequest();
        },
        _sendRequest: function () {
            /*var params = {
                type:2
            };
            ajaxHelper.post("http://" + window.frontJSHost + "/user/list",
                params, this, this._render, null);*/
            this._render();
        },
        _render: function (data) {
            this.mainBox.html(this.tplFun());
            this._registEvent();
        },
        _registEvent: function () {
            $('#i_new').off("click", this._createEmployee).on("click", this._createEmployee);
        },
        _createEmployee:function(e){
            window.location = "new_employee.html";
        }

    };
    return UserManagement;
});
