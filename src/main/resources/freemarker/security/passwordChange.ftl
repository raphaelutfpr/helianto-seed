[#ftl]
<!DOCTYPE html>
[#import "/spring.ftl" as spring/]
[#assign sec=JspTaglibs["http://www.springframework.org/security/tags"] ]
<html id="ng-app" xmlns:ng="http://angularjs.org" data-ng-app="security" >
<head>
	<link type="image/x-icon" href="/images/favicon.ico" rel="shortcut icon">
	<link type="image/x-icon" href="/images/favicon.ico" rel="icon">
    <title>Mudar senha</title>
    
	[#include "../frame-head.ftl" /]
   
</head>
<body style="background-color: #f3f3f4;">

	<div id="unauth">
	<div id="main" class="container" data-ng-controller="SecurityController as securityCtrl" >
	<div class="container-small" data-ng-cloak >

	[#--
	 # Logotipo
	 #--]
	<div class="clearfix text-center">
		<a href="#" class="text-center" target="_self"><img src="/images/logo.png" alt="" ></a>
	</div>

	[#--
	 # Título: Recuperação de senha
	 #--]
	<div class="heading" style="margin-bottom: 40px;">
		<h3 class="text-center">Recuperação de senha</h3>
	</div>

	<div class="heading">
		<h4 align="middle">Seu e-mail é [#if email??>${email}[/#if]!</h4>
	</div>

	[#if userExists??]<div class="alert alert-danger" role="alert"><p>Email já cadastrado" </p> </div>[/#if]
	
	[#if recoverFail?? && recoverFail="true" ]<div class="alert alert-danger" role="alert"><p>Falha ao mudar a senha</p> </div>[/#if]
	[#if recoverFail?? && recoverFail="false"]<div class="alert alert-success" role="alert"><p>Senha modificada com sucesso.</p> </div>[/#if]
	[#if recoverFailMsg?? ]<div class="alert alert-warning" role="alert"><@spring.message "${recoverFailMsg}" /> </div>[/#if]
			
    
    <div class="panel panel-default">
    <div class="panel-heading">
		<div class="row">
			<div class="col-md-12">
				<h3 class="panel-title"><span class="glyphicon glyphicon-wrench"></span> Senha</h3>
			</div>
		</div>
	</div>
	
	<div class="panel-body">
    
    	[#--
    	 # Pode se tratar de um usuário externo ou já vinculado a uma sessão.
    	 #--]
    	 
    	<#assign changeAction="/recovery/submit" />
    	<@sec.authorize access="isAuthenticated()">
    		<#assign changeAction="/recovery/submit" />
    	</@sec.authorize>

    
		<form method="POST" name="form"  id="change" action="/recovery/submit">
				
			[#--
			 # CSRF
			 #--]
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			
			[#--
			 # Campo de email (oculto), senha e confirmação
			 #--]
			<input type="hidden" name="email" id="email" value="[#if email??]${email}[/#if]" >
			<password-checker password="password" cpassword="cpassword" ></password-checker>	
			<div id="form-group-password" class="form-group">
				<input type="password"  required="" name="password"  id="password" data-ng-model="password" placeholder="Senha" class="form-control">
			</div>
			
			</br>
			
			<div id="form-group-passwordc" class="form-group">
				<input type="password"  required="" name="cpassword" id="cpassword" data-ng-model="cpassword" placeholder="Confirmação de senha" class="form-control">
			</div>
			
			</br>
			
			<button type="submit" class="btn btn-primary" style="width: 100%;" data-ng-disabled="cannotChangePassword"> Mudar Senha</button>
		
		</form>
		</div>

		
	<div class="panel-footer">
		<div class="row">
			<div class="col-md-6">
				<h5>Página Inicial</h5>

			</div>
			<div class="col-md-6">
				<h5><a class="pull-right" href="/app/home">Home</a></h5>
			</div>
		</div>

	</div>
	</div>
	

	
	

  	<footer class="footer">
		<hr>
        <p>© i-serv Consultoria Empresarial Ltda. 2015</p>
  
  		<div class="row">
    	<div class="col-md-12">
	 		<small> 
		 			<a class="text-muted" target="_new" data-ng-href="/signup/privacy/" href="/signup/privacy/"> Politica de Privacidade</a>  |
		    		<a target="_new" class="text-muted" data-ng-href="/signup/license/" href="/signup/license/"> Termos de Uso</a>
			</small>
     	</div>
		</div>
</footer>
	

	</div><!-- .container-small -->
	</div><!-- #main -->
	</div><!-- #unauth -->

	[#include "../frame-foot.ftl" /]

</body>
</html>
