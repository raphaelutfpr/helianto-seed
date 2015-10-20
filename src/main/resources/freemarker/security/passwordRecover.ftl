[#ftl]	
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
		
