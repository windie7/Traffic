// JavaScript Document

var browser={   
	versions:function(){           
	var u = navigator.userAgent, app = navigator.appVersion;           
	return {               
	/*trident: u.indexOf('Trident') > -1, //IE内核               
	presto: u.indexOf('Presto') > -1, //opera内核               
	webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核               
	gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核*/
	mobile: !!u.match(/AppleWebKit.*Mobile/) || !!u.match(/Windows Phone/) || !!u.match(/Android/) || !!u.match(/MQQBrowser/),
	ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端               
	android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器               
	iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器               
	iPad: u.indexOf('iPad') > -1, //是否iPad               
	webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部           
	};
	}()
} 
   
if(browser.versions.mobile==false){
  document.write('<link rel="stylesheet" type="text/css" href="css/web_style.css" />');
}
 
 
 function scrollDoor(){
}
scrollDoor.prototype = {
	sd : function(menus,divs,openClass,closeClass){
		var _this = this;
		if(menus.length != divs.length)
		{
			alert("菜单层数量和内容层数量不一样!");
			return false;
		} 
		for(var i = 0 ; i < menus.length ; i++)
		{	
			_this.$(menus[i]).value = i;				
			_this.$(menus[i]).onclick = function(){
					
				for(var j = 0 ; j < menus.length ; j++)
				{						
					_this.$(menus[j]).className = closeClass;
					_this.$(divs[j]).style.display = "none";
				}
				_this.$(menus[this.value]).className = openClass;	
				_this.$(divs[this.value]).style.display = "block";				
			}
		}
		},
	$ : function(oid){
		if(typeof(oid) == "string")
		return document.getElementById(oid);
		return oid;
	}
}
window.onload = function(){
	var SDmodel = new scrollDoor();
	SDmodel.sd(["t11","t12","t13"],["lists11","lists12","lists13"],"now","normal"); 
	SDmodel.sd(["t21","t22","t23"],["lists21","lists22","lists23"],"now","normal"); 
	SDmodel.sd(["t31","t32","t33"],["lists31","lists32","lists33"],"now","normal"); 
	SDmodel.sd(["t41","t42","t43"],["lists41","lists42","lists43"],"now","normal"); 
	SDmodel.sd(["t51","t52","t53"],["lists51","lists52","lists53"],"now","normal"); 
} 
 