define('sealup_search',["component/header","ajaxhelper","utility"],function(e,t,n){var r={cities:n.cities,initialize:function(){this.mainBox=$("#i_mainbox"),this.tplfun=_.template($("#i_tpl").html()),this._sendRequest()},_sendRequest:function(){var e={};t.get("http://"+window.frontJSHost+"/ask/props",e,this,this._render,null)},_render:function(e){e.r.push(this.cities),this.mainBox.html(this.tplfun({result:e})),this._registEvent()},_registEvent:function(){$("#i_pay").off("click",this._pay).on("click",{ctx:this},this._pay)},_pay:function(e){var r={userId:n.getUserId(),productId:$("#i_product").data("id"),ownerName:$("#i_name").val(),ownerPhone:$("#i_phone").val(),cityId:$("#i_city").val(),address:$("#i_addr").val(),email:$("#i_email").val()};t.post("http://"+window.frontJSHost+"/order/icreate",r,this,function(e){var r={id:e.r.id};t.get("http://"+window.frontJSHost+"/order/pay",r,this,function(){n.showToast("支付成功"),window.location="question_complete2.html?id="+e.r.id})},null)}};return r});