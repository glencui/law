define(["component/nav_bar","component/header","ajaxhelper","utility"],function(e,t,n,r){var i={initialize:function(){e.initialize("i_navbar",3),t.initialize("i_header","员工管理"),this.mainBox=$("#i_mainbox"),this.tplFun=_.template($("#i_tpl").html()),this._sendRequest()},_sendRequest:function(){var e={};n.get("http://"+window.frontJSHost+"/api/ossuser/roles",e,this,this._getOssUsers,null)},_getOssUsers:function(e){var t={page:0,size:20};n.get("http://"+window.frontJSHost+"/api/ossuser/list",t,this,function(t){this._render({roles:e,result:t})},null)},_render:function(e){this.mainBox.html(this.tplFun(e)),this._registEvent()},_registEvent:function(){$(".j_selAuth li").off("click",this._selAuth).on("click",{ctx:this},this._selAuth),$(".j_status li").off("click",this._status).on("click",{ctx:this},this.j_status),$("#i_new").off("click",this._createEmployee).on("click",this._createEmployee)},_selAuth:function(e){var t=$(this).data("id"),n=$($(this).parents("tr")).data("id"),r=$(this).text();$(this).closest("ul").prev("button").data("id",t).find("span").eq(0).text(r)},_status:function(e){var t=$(this).data("id"),n=$(this).text(),r=$($(this).parents("tr")).data("id");$(this).closest("ul").prev("button").data("id",t).find("span").eq(0).text(n)},_createEmployee:function(e){window.location="new_employee.html"}};return i});