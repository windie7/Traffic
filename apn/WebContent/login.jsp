<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>
<html>
<head>
<title>登录页面</title>
<META content=text/html;charset=UTF-8 http-equiv=Content-Type />
<META content=max-age=3600 http-equiv=Cache-Control />
<META name=MobileOptimized content=240 />
<META name=apple-touch-fullscreen content=YES />
<META name=apple-mobile-web-app-capable content=yes />
<META name=viewport
	content="width=device-width, initial-scale=1.0,  minimum-scale=1.0, maximum-scale=1.0" />
<META name=keywords content="" />
<META name=description content="" />
<META name=GENERATOR content="MSHTML 8.00.6001.23501" />
<link type="text/css" rel="stylesheet" href="css/login.css" />
<script type="text/javascript" src="js/check.js"></script>
<script src="js/jquery-1.7.1.min.js"></script>

<script type="text/javascript">
	var context = "/apn";

	var pMobile = /^1[0-9]{10}$/;
	var pCode = /^[0-9]{6}$/;

	$(document).ready(function() {
		$("#i_code").click(function() {

			var m = $("#u3").val();

			if (!pMobile.test(m)) {
				$("#tip_code").text("请输入有效手机号码");
				
				return;
			}

			var ret = $.ajax({
				url : context + "/login?op=code&mobile=" + m,
				async : false
			});

			var c = ret.responseText;
			if (c == '0') {
				$("#i_code").attr("disabled", true);
				refreshCode(600);
				return;
			} else if (c == '1') {
				$("#tip_code").text("请输入有效手机号码");
				return;
			} else if (c == '2') {
				$("#tip_code").text("频繁请求验证码,请稍候再试");
				return;
			}
		});

		$("#i_login").click(function() {
			var m = $("#u3").val();
			var c = $("#u4").val();

			if (!pMobile.test(m)) {
				$("#tips").text("请输入有效手机号码");
				return;
			}
			if (!pCode.test(c)) {
				$("#tips").text("请输入有效验证码");
				return;
			}

			$("#form_login").submit();

		});

		$("#u3").click(function() {
			clearTips();
		});

		$("#u4").click(function() {
			clearTips();
		});
	});

	function refreshCode(s) {
		if (s == 0) {
			$("#i_code").removeAttr("disabled");
			$("#tip_code").text("");
			return;
		}
		$("#tip_code").text("验证码已发送，" + s + "秒后失效");
		s = s - 1;
		setTimeout("refreshCode(" + s + ")", 1000);
	}

	function clearTips() {
		$("#tip_code").text("");
		$("#tips").text("");
	}
</script>
</head>
<body>
	<div class="contain">
		<div class="safe_logo">
			<a href=""><img src="images/login_logo.png" alt="" /></a>
		</div>

		<form id="form_login" action="<%=request.getContextPath()%>/login"
			method="post">
			<input type="hidden" name="op" value="login" />
			<div class="margin">
				<div class="input">
					<div class="left">请输入手机号</div>
					<div class="right">
						<input id="u3" type="text" name="mobile" value="" />
					</div>
				</div>
				<div class="clear"></div>
				<div class="getcode">
					<div class="left">
						<div class="border">
							<img src="images/title_left.png" alt="" />
						</div>
						<div class="words">
							<a id="i_code" href="javascript:void(0)">获取短信验证码</a>
						</div>
						<div class="border">
							<img src="images/title_right.png" alt="" />
						</div>
					</div>
					<div id="tip_code" class="right"></div>
				</div>
				<div class="clear"></div>
				<div class="input">
					<div class="left">请输入验证码</div>
					<div class="right">
						<input id="u4" type="text" name="code" value="" />
					</div>
				</div>
				<div class="clear"></div>
				<div id="tips" class="notice">${msg}</div>
				<div class="getcode">
					<div class="border">
						<img src="images/title_left.png" alt="" />
					</div>
					<div class="words wid">
						<a id="i_login" href="javascript:void(0)">访问</a>
					</div>
					<div class="border">
						<img src="images/title_right.png" alt="" />
					</div>
				</div>
				<div class="clear"></div>

			</div>
		</form>
		<div class="ad">
			<img src="images/down_ad.png" alt=" " />
		</div>
	</div>
</body>
</html>
