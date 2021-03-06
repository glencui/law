define(['component/nav_bar','component/header', 'ajaxhelper', 'utility'], function (nav_bar, header, ajaxHelper, util) {
    var UserOrders = {
        initialize: function () {
            //nav_bar
            if(util.getQueryParameter("uid")){
                nav_bar.initialize("i_navbar", 1);
                header.initialize("i_header", "用户订单");
            }else{
                nav_bar.initialize("i_navbar", 4);
                if(util.getQueryParameter("status") == 4){
                    header.initialize("i_header", util.getQueryParameter("n")+"的待办任务");
                }else{
                    header.initialize("i_header", util.getQueryParameter("n")+"的已完成任务");
                }
                
            }

            this.mainBox1 = $('#i_mainbox1');
            this.tplFun1 = _.template($("#i_tpl1").html());

            this.mainBox2 = $('#i_mainbox2');
            this.tplFun2 = _.template($("#i_tpl2").html());

            this.modalBox = $('#i_modal_body');
            this.modalTplFun = _.template($("#i_modal_tpl").html());
            this._sendRequest();
        },
        _sendRequest: function () {
            if(util.getQueryParameter("uid")){
                var params = {
                    "page": 0,
                    "size":20,
                    "userId": util.getQueryParameter("uid")
                };
                ajaxHelper.get("http://" + window.frontJSHost + "/api/order/ilistbyuser",
                    params, this, this._render1, null);

                ajaxHelper.get("http://" + window.frontJSHost + "/api/order/slistbyuser",
                    params, this, this._render2, null);
            }else{
                var params = {
                    "page": 0,
                    "size":20,
                    "lawyerId": util.getQueryParameter("lid"),
                    "status": util.getQueryParameter("status")
                };
                ajaxHelper.get("http://" + window.frontJSHost + "/api/order/ilistbylawyer",
                    params, this, this._render1, null);

                ajaxHelper.get("http://" + window.frontJSHost + "/api/order/slistbylawyer",
                    params, this, this._render2, null);
            }
            
        },
        _render1: function (data) {
            var showLawyer = util.getQueryParameter("uid")?true:false;
            this.mainBox1.html(this.tplFun1({"result":data, "showLawyer": showLawyer, "start":1}));
            this._registEvent();
        },
        _render2: function (data) {
            this.mainBox2.html(this.tplFun2({"result":data, "showLawyer": showLawyer, "start":1}));
            this._registEvent();
        },
        _registEvent: function () {
            $('.j_assign').off("click", this._assign).on("click", {"ctx":this}, this._assign);
            $('.j_do_assign').off("click", this._doAssign).on("click", {"ctx":this}, this._doAssign);
        },
        _assign:function(e){
            var params = {
                cityId: $(e.currentTarget).data("city")
            };
            var oid = $(e.currentTarget).parents("tr").data("id");
            ajaxHelper.get("http://" + window.frontJSHost + "/api/lawyer/listbycity",
                params, e.data.ctx, function(data){
                    $("#i_modal").data({"id":oid}).modal("show");
                    this.modalBox.html(this.modalTplFun({"result":data}));
                    this._registEvent();
                }, null);
        },
        _doAssign:function(e){
            var oid = $("#i_modal").data("id");
            var lawyerId = $(e.currentTarget).parents("tr").data("id");
            var params = {
                opId : util.getUserId(),
                lawyerId : lawyerId,
                orderId : oid
            };
            $("#i_modal").modal("hide");
            ajaxHelper.get("http://" + window.frontJSHost + "/api/order/assignment",
                params, e.data.ctx, function(data){
                    util.showToast("指派成功");
                    this._sendRequest();
                }, null);
        }
    };
    return UserOrders;
});
