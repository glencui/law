define(["ajaxhelper","text!./up_xiniu.html?"+ +(new Date),"jquery","lib/MD5","lib/qiniu/plupload.full.min","lib/qiniu/qiniu"],function(e,t,n,r,i,s,o){window.newToken=!1;var u=function(e,t,r,i){this.root=n(e);if(this.root.length==1)return this.selector="j_qiniuContainer"+this.__proto__.count,this.upbtn="j_qiniuBtn"+this.__proto__.count,this.__proto__.count++,this.folderName=t,this.successFun=r,this.errFun=i,this.gettingToken=!1,this._sendRequest();var s=[];for(var o=0;o<this.root.length;o++)s.push(new u(this.root[o],t,r,i));return s};return n("body").append(t),u.prototype.xiniuTplFun=_.template(n("#i_xiniu_tpl").html()),n("#i_xiniu_tpl").remove(),u.prototype.count=0,u.prototype._sendRequest=function(){if(!window.localStorage.xiniuToken&&!this.gettingToken||!window.newToken){this.gettingToken=!0;var t={"backend.common.getCdnToken":{bucketName:""}};for(var n in t){var i=new Array;t[n].apiKey="android_client";for(var s in t[n])Object.prototype.toString.call(t[n][s])=="[object Object]"&&(t[n][s]=JSON.stringify(t[n][s])),!t[n][s]||i.push(s+"="+t[n][s]);var o=i.sort().join("&")+"&"+n+"&5b2e1c483b4cf67c87399e1de4554cf9",u=r(o);t[n].token=u}var a=this;e.get("http://api.weshare12.com/api/v1/exchange/activity/getCdnToken?paramStr="+JSON.stringify(t)+"&access_token=2hri2xzq4v11JI0gZinE6S",null,this,function(e){window.localStorage.xiniuToken=JSON.parse(e)["backend.common.getCdnToken"].results.uptoken,window.newToken=!0,a.gettingToken=!1},this.error,!1,!1)}return this._render()},u.prototype._render=function(e){var t={container:this.selector,upbtn:this.upbtn};this.root.html(this.__proto__.xiniuTplFun(t));var n=this,r=new QiniuJsSDK,i=r.uploader({runtimes:"html5,flash,html4",browse_button:this.upbtn,container:this.selector,drop_element:this.selector,max_file_size:"1000mb",flash_swf_url:"../../bower_components/plupload/js/Moxie.swf",dragdrop:!0,chunk_size:"4mb",uptoken:window.localStorage.xiniuToken,unique_names:!1,save_key:!1,domain:window.qiniuDomain,get_new_uptoken:!1,auto_start:!0,log_level:5,init:{FilesAdded:function(e,t){},BeforeUpload:function(e,t){},UploadProgress:function(e,t){},UploadComplete:function(){},FileUploaded:function(e,t,r){console.log(typeof n.successFun),typeof n.successFun=="function"&&n.successFun.apply(n.root[0],arguments)},Error:function(e,t,r){typeof n.errFun=="function"&&n.errFun.apply(n.root[0],arguments)},Key:function(e,t){var r=n.folderName;r.indexOf("/")==0&&(r=r.substr(1)),r[r.length-1]=="/"&&(r=r.substr(0,r.length-1));var i=new Date,s=[i.getFullYear(),i.getMonth()+1>10?i.getMonth()+1:"0"+(i.getMonth()+1),i.getDate()>10?i.getDate():"0"+i.getDate()],o=i.getTime().toString(),u=t.name.substr(t.name.lastIndexOf("."));return r+="/"+s.join("")+"/"+o.substr(4)+u,r}}});return this._registEvent(),i},u.prototype._registEvent=function(){},u.prototype.error=function(e){},u});