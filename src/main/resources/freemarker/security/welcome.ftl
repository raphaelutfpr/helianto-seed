[#ftl]
<!DOCTYPE html>
<html id="ng-app" xmlns:ng="http://angularjs.org" data-ng-app="security" >
[#import "/spring.ftl" as spring/]
[#assign sec=JspTaglibs["http://www.springframework.org/security/tags"] /]
<head>
	<link type="image/x-icon" href="/images/favicon.ico" rel="shortcut icon">
	<link type="image/x-icon" href="/images/favicon.ico" rel="icon">
    <title>Login</title>
    
	[#include "../frame-head.ftl" /]

</head>
<body style="background-color: #f3f3f4;">

	<div id="unauth">
	<div id="main" class="container" data-ng-controller="SecurityController as securityCtrl" >
	<div class="container-small" data-ng-cloak >

	<!--
	 ! Logotipo
	 !-->
	<div class="clearfix text-center">
		<a href="#" class="text-center" target="_self"><img src="/images/logo.png" alt="iservport" ></a>
	</div>

	<div class="heading">
		<h2 align="middle">Verifique seu e-mail</h2>
	<div class="well well-lg">
		[#if emailSent?? && emailSent="false" ]
			<div class="alert alert-danger" role="alert">
			<@spring.message "label.user.email.sent.not" /> 
			</div>
		[/#if]
		
		[#--
		 # E-mail enviado
		 #--]
		[#if emailSent?? && emailSent="true"]

		<h4>Não recebeu o e-mail?</h4>
		<p>Verifique se a caixa de spam contém uma mensagem de ${sender!'alexandre@winbid.com.br'}.</p>
		[/#if]
		
		[#if passError?? && passError="true" ]
			<div class="alert alert-danger" role="alert">
			<@spring.message "label.user.password.create.not" /> 
			</div>
		[/#if]
			
		[#if passError?? && passError="false"]
			<div class="alert alert-success" role="alert">
			<@spring.message "label.user.password.create" /> 
			</div>
		[/#if]

		<h2><a class="form-control btn btn-primary full-width" href="/home/">Retornar ao login</a></h2>
		</div>
	</div>

    
	</div><!-- .container-small -->
	</div><!-- #main -->
	</div><!-- #unauth -->

   [#include "../frame-foot.ftl" /]
</body>
</html>
