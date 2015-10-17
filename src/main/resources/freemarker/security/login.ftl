[#ftl]
<script >
	var email = [#if email??]'${email}' [#else]''[/#if];
</script>
[#include "../config/login-splash.html" /]
<div class="panel panel-default">

	<div class="panel-heading">
		<div class="row">
			<div class="col-md-12">
				<h3 class="panel-title"><span class="glyphicon  glyphicon-lock"></span> Entrar</h3>
			</div>
		</div>
	</div>
	
	<div class="panel-body">
	<form  name="form" data-ng-submit="login(username,password)" method="POST">
	
	
		[#--
		 # CSRF
		 #--]
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		
		[#--
		 # Context id
		 #--]
		<input type="hidden" name="contextId" value="${contextId!1}" />
			
		[#if error?? ]
			[#if user?? && !user.accountNonExpired  ]		
				<!--
				 ! Inactive user
				 !-->
				<div class="alert alert-danger" role="alert">
					<i class="fa fa-lock"></i> Usuário inativo 
				</div>
			[#else]	
				<!--
				 ! Login error
				 !-->
				<div class="alert alert-danger" role="alert">
					<i class="fa fa-lock"></i> Erro ao fazer login
				</div>		
			[/#if]
		[/#if]
		[#if userConfirmed?? ]
				<div class="alert alert-success" role="alert"><i class="fa fa-unlock"></i> Seu usuário foi ativado, faça o login para entrar.</div>		
		[/#if]
		[#if emailRecoverySent?? ]
				<div class="alert alert-warning" role="alert">Consulte seu email para recuperar sua senha. </div>		
		[/#if]
		[#if emailRecoveryFailed?? ]
				<div class="alert alert-warning" role="alert">Falha ao enviar o email para recuperar sua senha. </div>		
		[/#if]
		[#if recoverFail?? && recoverFail="true" ]<div class="alert alert-danger" role="alert">Falha ao mudar a senha. </div>[/#if]
		
		[#if recoverFail?? && recoverFail="false"]<div class="alert alert-success" role="alert">Senha modificada com sucesso. </div>[/#if]

		<div id="form-group-email" class="form-group">
			<input type="email"  required="" name="username"  data-ng-model="username" placeholder="E-mail" class="form-control">
		</div>
		<div id="form-group-password" class="form-group">
			<input type="password"  required="" name="password" data-ng-model="password" placeholder="Senha" class="form-control">
		</div>
		
		<!--
		 ! Forgot password
		 !-->
		<div class="row">
			<div class="col-md-6">
				<p><input type="checkbox" name="remember-me" > Lembrar senha</p>
			</div>
			<div class="col-md-6">
				<a target="_self" class="pull-right" data-ng-href="/recovery/">Esqueceu sua senha?</a>
			</div>
		</div>
		
		<!--
		 ! Submit
		 !-->
		<hr/>
		<button type="submit" class="btn btn-primary" style="width: 100%" >Continue</button>
		
		
	</form>
	</div><!-- panel body -->
	<div class="panel-footer">
		<!--
		 ! New user
		 !-->
		<div class="row">
			<div class="col-md-6">
				<h5>Não possui acesso ainda?</h5>
			</div>
			<div class="col-md-6">
				<h5><a class="pull-right" href="/signup/">Criar conta de usuário</a></h5>
			</div>
		</div>
	</div><!-- panel footer -->
</div>
