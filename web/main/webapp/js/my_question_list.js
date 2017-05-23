define(['component/header','ajaxhelper', 'utility', 'scroll'], function(header, ajaxHelper, util, listScroll) {
    var MyQuestionList = {
        //初始化标志
		initialized : false,
		pageNo : 0,
		pageSize: 5,
		initialize :function(){
			//body
			this.mainBox = $('#i_mainbox');
			this.tplfun = _.template($("#i_tpl").html());
			//request
			this._sendRequest(false);
		},
		_sendRequest :function(isPaging){
			var params = {"userId": util.getUserId(), "page": this.pageNo, "size":this.pageSize};
			ajaxHelper.get("http://" + window.frontJSHost + "/order/listbyuser",
                params, this, this._render, null, isPaging);
		},
		_render:function(data){
			this.pageNo++;
			if(!this.initialized){
				//第一次，初始化
				this.mainBox.html(this.tplfun({"result": data}));
				this._registEvent();
				this.initialized = true;
				if(data.r.length == this.pageSize){
					listScroll.initialize(this);
				}else{
					$("#i_refresh").css("display", "none");
				}
			}else{
				var a = $(this.tplfun({"result":data})).find("#i_order_list .invest_result_box");
				a.insertBefore('#i_refresh');
				if(data.r.length<this.pageSize){
					$("#i_refresh").css("display", "none");
					listScroll.setDisabled();
				}else{
					listScroll.setEnabled();
				}
				listScroll.refresh();
				this._registEvent();
			}
		},
		_registEvent:function(){
			$(".j_cancel").off("click", this._cancel).on("click", this._cancel);
			$(".j_pay").off("click", this._pay).on("click", this._pay);
			$(".j_complete").off("click", this._complete).on("click", this._complete);
			$(".j_view").off("click", this._view).on("click", this._view);
			$(".j_view_law").off("click", this._viewLayer).on("click", this._viewLayer);
		},
		//下拉刷新回调函数
		dragFresh:function(){
        	//防止重复下拉刷新
			listScroll.setDisabled();
			this._sendRequest(true);
		},
		_cancel:function(e){
			var oid = $(e.target).parents(".j_result_item").data("id");
			var productId = $(e.target).parents(".j_result_item").data("pid");
			var params = {"id": oid};
			ajaxHelper.get("http://" + window.frontJSHost + "/order/cancel",
                params, this, function(){
                	util.showToast("取消成功", function(){
                		window.location = "my_question_list.html";
                	})
                });
		},
		_pay:function(e){
			var oid = $(e.target).parents(".j_result_item").data("id");
			var productId = $(e.target).parents(".j_result_item").data("pid");
			var params = {"id": oid};
			ajaxHelper.get("http://" + window.frontJSHost + "/order/pay",
                params, this, function(){
                	util.showToast("支付成功", function(){
                		if(productId == 1 || productId == 2){
                			window.location = "question_complete.html";
                		}else{
                			window.location = "my_question_list.html";
                		}
                	})
                });
		},
		_complete:function(e){
			var oid = $(e.target).parents(".j_result_item").data("id");
			var productId = $(e.target).parents(".j_result_item").data("pid");
			window.location = "question_complete.html?id=" + oid;
		},
		_refund:function(e){

		},
		_view:function(e){
			var oid = $(e.target).parents(".j_result_item").data("id");
			var productId = $(e.target).parents(".j_result_item").data("pid");
			window.location = "invest_result.html?id=" + oid + "&pid=" + productId;
		},
		_viewLayer:function(e){
			var oid = $(e.target).parents(".j_result_item").data("id");
			var productId = $(e.target).parents(".j_result_item").data("pid");
			window.location = "lawyer_reply.html?id=" + oid + "&pid=" + productId;
		},
    };
    return MyQuestionList;
});