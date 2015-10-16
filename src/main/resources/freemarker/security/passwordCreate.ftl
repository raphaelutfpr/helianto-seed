[#ftl]
<!DOCTYPE html>
<html id="ng-app" xmlns:ng="http://angularjs.org" data-ng-app="security" >

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
		<a href="#" class="text-center" target="_self"><img src="/images/logo.png" alt="Ubivis" ></a>
	</div>


	<div class="heading">
		<h3 align="middle">Atualize sua senha</h3>
	</div>

	[#if userExists??]
		<div class="alert alert-danger" role="alert">
		<p>Senha modificada com sucesso.</p> 
		</div>
	[/#if]
	
	[#if recoverFail?? && recoverFail="true" ]
		<div class="alert alert-danger" role="alert">
		<p> Falha ao mudar a senha </p> 
		</div>
	[/#if]
	
	[#if recoverFail?? && recoverFail="false"]
		<div class="alert alert-success" role="alert">
		<p> Senha modificada com sucesso. </p> 
		</div>
	[/#if]
	
	[#if recoverFailMsg?? ]
		<div class="alert alert-warning" role="alert">
		<@spring.message "${recoverFailMsg}" />
		</div>
	[/#if]
			
    <div class="panel panel-default">
    <div class="panel-heading">
    	<h3 class="panel-title"><span class="glyphicon glyphicon-ok-sign"></span> Confirmação</h3>
    </div>
	<div class="panel-body">
			<form method="POST" name="form"  id="signup" action="/verify/createPass">
				
				<div id="form-group-email" class="form-group">
					<input type="email" required="" name="username" data-ng-model="username" placeholder="[#if email??]${email}[/#if]" class="form-control ng-dirty ng-valid ng-valid-required ng-touched" disabled>
					</br>
				</div>
			
				<input type="hidden" name="email" id="email" value="[#if email??]${email}[/#if]" >
				<password-checker password="password" cpassword="cpassword"> </password-checker>
				<div id="form-group-password" class="field">
				<input type="password"  required="" name="password"  id="password" placeholder="Senha" class="form-control" data-ng-model="password">
				</br>
				</div>
				<div id="form-group-passwordc" class="field">
					<input type="password"  required="" name="cpassword" id="cpassword" placeholder="Confirmação de senha" data-ng-model="cpassword" class="form-control">
					</br>
				</div>
					
				<button type="submit" class="btn btn-primary" style="width: 100%;" data-ng-disabled="cannotChangePassword"> Criar Senha </button>
				<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			</form>
		</div></div>
		
	 <footer class="footer">
		<hr>
        <p>© 2001-2015 Winbid Negócios Governamentais </p>
  
  <!--
  
  		<div class="row">
    	<div class="col-md-12">
	 		<small> 
		 			<a class="text-muted" target="_new" data-ng-href="/signup/privacy/" href="/signup/privacy/"> Politica de Privacidade</a>  |
		    		<a target="_new" class="text-muted" data-ng-href="/signup/license/" href="/signup/license/"> Termos de Uso</a>
			</small>
     	</div>
		</div>
 -->
</footer>
	
	</div><!-- .container-small -->
	</div><!-- #main -->
	</div><!-- #unauth -->

[#include "../frame-foot.ftl" /]
</body>
</html>
