<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>客运大巴无线接入登陆</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="imagetoolbar" content="no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<link href="css/apn.css" rel="stylesheet" type="text/css">

<script src="scripts/jquery-1.7.1.min.js"></script>
<script src="scripts/jquery-ui-1.8.10.custom.min.js"></script>

<script type="text/javascript">
	var context = "/TrafficApn";

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
				url : context + "/Login?op=code&mobile=" + m,
				async : false
			});

			var c = ret.responseText;
			if (c == '0') {
				$("#i_code").attr("disabled",true);
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
			$("#i_code").removeAttr("disabled") ;
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
	<div id="main_container">

		<div id="u0" class="u0_container">
			<div id="u0_img" class="u0_normal detectCanvas"></div>
			<div id="u1" class="u1" style="visibility: hidden;">
				<div id="u1_rtf"></div>
			</div>
		</div>

		<form id="form_login" action="<%=request.getContextPath()%>/Login"
			method="post">
			<input type="hidden" name="op" value="login">
			<div id="u2" class="u2">
				<div id="u2_rtf">
					<p style="text-align: left;">
						<span
							style="font-family: 微软雅黑; font-size: 48px; font-weight: normal; font-style: normal; text-decoration: none; color: #333333;">请输入手机号</span>
					</p>
				</div>
			</div>
			<input id="u3" type="text" name="mobile" value="" class="u3" /> <input
				id="u4" type="text" name="code" value="" class="u4" />

			<div id="u5" class="u5">
				<div id="u5_rtf">
					<p style="text-align: left;">
						<span
							style="font-family: 微软雅黑; font-size: 48px; font-weight: normal; font-style: normal; text-decoration: none; color: #333333;">请输入验证码</span>
					</p>
				</div>
			</div>
			<div id="u6" class="u6_container">
				<div id="u6_img" class="detectCanvas">
					<input id="i_code" type="button" value="获取短信验证码" class="u6_normal" />
				</div>
				<div id="u7" class="u7" style="visibility: hidden;">
					<div id="u7_rtf"></div>
				</div>
			</div>
			<!-- 		<div id="u8" class="u8">
			<div id="u8_rtf">

			</div>
		</div> -->
			<div id="u9" class="u9_container">
				<div id="u9_img" class="u9_normal detectCanvas"></div>
				<div id="u10" class="u10" style="visibility: hidden;">
					<div id="u10_rtf"></div>
				</div>
			</div>
			<div id="u11" class="u11">
				<div id="u11_rtf">
					<p style="text-align: left;">
						<span
							style="font-family: 微软雅黑; font-size: 36px; font-weight: normal; font-style: normal; text-decoration: none; color: #333333;">客运大巴无线接入</span><span
							style="font-family: 微软雅黑; font-size: 36px; font-weight: normal; font-style: normal; text-decoration: none; color: #333333;">登陆</span>
					</p>
				</div>
			</div>
			<div id="u12" class="u12">
				<div id="u12_rtf">
					<p style="text-align: left;">
						<span id="tip_code"
							style="font-family: 微软雅黑; font-size: 28px; font-weight: normal; font-style: normal; text-decoration: none; color: #333333;"></span>
					</p>
				</div>
			</div>
			<div id="u13" class="u13_container">
				<div id="u13_img" class="detectCanvas">
					<input id="i_login" type="button" value="访问" class="u13_normal" />
				</div>
				<div id="u14" class="u14" style="visibility: hidden;">
					<div id="u14_rtf"></div>
				</div>
			</div>
			<!-- 		<div id="u15" class="u15">
			<div id="u15_rtf">
				
			</div>
		</div> -->
			<div id="u16" class="u16">
				<div id="u16_rtf">
					<p style="text-align: left;">
						<span id="tips"
							style="font-family: 微软雅黑; font-size: 28px; font-weight: normal; font-style: normal; text-decoration: none; color: #FF0000;">${msg}</span>
					</p>
				</div>
			</div>

		</form>
	</div>
</body>