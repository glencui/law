define('read_list',["component/header","ajaxhelper","utility"],function(e,t,n){var r={initialize:function(){this._sendRequest()},_sendRequest:function(){this._render()},_render:function(){this._registEvent()},_registEvent:function(){$(".read_list_box").off("click",this._go).on("click",{ctx:this},this._go)},_go:function(e){window.location="read_detail.html"}};return r});