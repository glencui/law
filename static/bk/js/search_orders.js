define(["component/nav_bar","component/header","ajaxhelper","utility"],function(e,t,n,r){var i={initialize:function(){e.initialize("i_navbar",5),t.initialize("i_header","咨询订单管理"),this.mainBox=$("#i_mainbox"),this.tplFun=_.template($("#i_tpl").html()),this._sendRequest()},_sendRequest:function(){this._render()},_render:function(e){this.mainBox.html(this.tplFun()),this._registEvent()},_registEvent:function(){}};return i});