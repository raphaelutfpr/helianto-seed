[#ftl]
<!DOCTYPE html>

<html id="ng-app" xmlns:ng="http://angularjs.org" data-ng-app="security" >
<head>
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
		<a href="#" class="text-center" target="_self"><img src="/images/logo.png" alt="Iservport" ></a>
	</div>

	[#--
	 # Título: Recuperação de senha
	 #--]
	<div class="heading" style="margin-bottom: 40px;">
		<h3 class="text-center">Recuperação de senha</h3>
	</div>

    <div class="panel panel-default">
    <div class="panel-heading">
		<div class="row">
			<div class="col-md-12">
				<h3 class="panel-title"><span class="glyphicon glyphicon-wrench"></span> Senha</h3>
			</div>
		</div>
	</div>
	
	<div class="panel-body">
		<form class="m-t" role="form"  method="POST" name="form"  id="recovery" action="/recovery/send">
		
			[#--
			 # CSRF
			 #--]
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			
			[#--
			 # Campo de email
			 #--]
			<div id="form-group-email" class="form-group">
			<label for="firstName">Email</label>
				<i class="fa fa-check" style="color:green;" data-ng-show="!emailNotOk"></i>
				<i class="fa fa-close" style="color:red;" data-ng-show="emailNotOk"></i>

				<input type="email" name="principal" id="principal" required="true" placeholder="E-mail" data-ng-blur="verifyEmail(form.principal)" 
					class="form-control" data-ng-model="form.principal">
			</div>
				
			<button type="submit" class="btn btn-primary" style="width: 100%;" data-ng-disabled="emailNotOk">Envie-me um e-mail de recuperação de senha</button>
		</div>
		</form>
		
		[#--
		 # Novo usuário
		 #--]
		<div class="panel-footer">
		
			<div class="row">
				<div class="col-md-6">
					<h5>Não possui acesso ainda?</h5>
				</div>
				<div class="col-md-6">
					<h5><a class="pull-right" href="/signup/">Criar conta de usuário</a></h5>
				</div>
			</div>
		</div>
	</div>
		

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
