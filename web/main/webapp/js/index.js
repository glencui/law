define(['component/header','ajaxhelper', 'utility'], function(header, ajaxHelper, util) {
    var Index = {
        initialize :function(){
			//request
			this._sendRequest();
		},
		_sendRequest :function(){
			this._render();
		},
		_render:function(){
			this._registEvent();
		},
		_registEvent:function(){
			$("#i_activities").off("click", this._goActivities).on("click", {ctx: this}, this._goActivities);
			$("#i_read").off("click", this._goRead).on("click", {ctx: this}, this._goRead);
			$("#i_consult").off("click", this._goConsult).on("click", {ctx: this}, this._goConsult);
			$("#i_invest").off("click", this._goInvest).on("click", {ctx: this}, this._goInvest);
			$("#i_ask").off("click", this._goAsk).on("click", {ctx: this}, this._goAsk);
			$("#i_my").off("click", this._goMy).on("click", {ctx: this}, this._goMy);
		},
		_goActivities:function(e){
			window.location = "https://mp.weixin.qq.com/mp/homepage?__biz=MzI1MjM5NjQxMQ==&hid=2&sn=c06582c69f67b164fe2d430ccda2f220&uin=MTEwMTQ3NjcwMg%3D%3D&key=2262e7a374f390cb7c3afe006611807c3a64ebfd2a6fdb6c55f26ded1c0162c3c32354cb3d2a903d87132d3a2d34d66f33d998f7bd8eff360b92fe2da4c8256dc0c23ab6d2b508c570a85069bce0a4f3&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.10.5+build(14F1808)&version=12020010&lang=zh_CN&nettype=WIFI&fontScale=100&pass_ticket=frESnNfvtzM2taC%2BagH61Y53hOKWUFfkVzm%2Fti4kWw72r2o%2FPiTvOQnBGtYdA9kG";
		},
		_goRead:function(e){
			window.location = "read_keys.html";
		},
		_goConsult:function(e){
			window.location = "lawyer_consult_info.html";
		},
		_goInvest:function(e){
			window.location = "lawyer_invest_info.html";
		},
		_goAsk:function(e){
			window.location = "answer_list.html";
		},
		_goMy:function(e){
			window.location = "my_question_list.html";
		},
    };
    return Index;
});