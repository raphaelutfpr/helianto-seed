(function() {
	app = angular.module('security', ['ui.utils','ui.bootstrap', 'app.services']);
	
	app.controller('SecurityController', ['$scope', '$window', '$http', '$resource' , 'genericServices', 'securityServices'
	                                  , function($scope, $window, $http, $resource, genericServices, securityServices) {
	
		$scope.baseName = "home";
		$scope.menuName = "home";
		$scope.email = email!='undefined'?email:'' ;
		$scope.logo='/images/logo.png';
		$scope.altLogo='Helianto Seed';
		
		/**
		 * Autorização
		 */
		$scope.authList =[];
		defineAuthorities();
		function defineAuthorities(){
			securityServices.getAuthorizedRoles(null).success(function(data, status, headers, config) {
				$scope.authList = data.content;
			});
		}
		$scope.isAuthorized =function(role, ext){
			return securityServices.isAuthorized($scope.authList, role, ext);
		}
		$scope.loginResource = $resource("/login/",{ username:"@username", password: "@password", rememberme:"@rememberme"},{
			login : { method:'POST',  headers : {'Content-Type': 'application/x-www-form-urlencoded'}}
		});
		$scope.signUpResource = $resource("/signup/");
		$scope.passwordResource = $resource("/signup/createPass");
		$scope.passwordRecoveryResource = $resource("/recovery/", { email:"@email"}, {
			update: { method: 'PUT' },
			create: { method: 'POST' }
		});
		
		$scope.login = function(usernameVal,passwordVal){
//			$scope.formLogin = {"username" :usernameVal, "password" :passwordVal };
			$scope.logged = $scope.loginResource.login($.param({username :usernameVal, password :passwordVal })); 
			$scope.logged.$promise.then(
					function(data, getReponseHeaders) {
						$window.location.href = "/home";
					}
			);
		}
		
		$scope.saveEmail = function(emailVal){
			$scope.signUpResource.get({tempEmail:emailVal});
		}
		
		//form initializer
		$scope.create = function(){
			$scope.form =  $scope.signUpResource.get({create:true});
			$scope.form.$promise.then(function(data) {
				data.email = $scope.email;
				$scope.form = data;
			});
		} 
		$scope.create();
		$scope.signUp = function(){
			$scope.form.password = 'save';
			$scope.returnCode = $scope.signUpResource.save($scope.form);
			$scope.returnCode.$promise.then(function(data) {
				console.log(data);
			});
		};
		
		$scope.updateUser = function(){
			$scope.form.email = $scope.email;
			$scope.passwordResource.save($scope.form);
		};
		
		$scope.passwordEmail = function(val){
			$scope.passwordRecoveryResource.save({email:val});
		}
		
		$scope.updatePassword = function(){
			$scope.form.email = $scope.email;
			$scope.passwordRecoveryResource.update($scope.form);
		}
		
		$scope.passwordMatches = function(){
			return $scope.cpassword === $scope.form.password;
		}
		
		$scope.cnpjOk=false;
		$scope.valideCNPJ = function(value){
			$scope.cnpjOk=validarCNPJ(value);
			console.log($scope.cnpjOk);
		};
		
		function validarCNPJ(cnpj) {
			if(cnpj == null || cnpj == 'undefined' || cnpj == '') return false;
		    cnpj = cnpj.replace(/[^\d]+/g,'');
		 
		    if (cnpj.length != 14) return false;
		 
		    // Elimina CNPJs invalidos conhecidos
		    if (cnpj == "00000000000000" || 
		        cnpj == "11111111111111" || 
		        cnpj == "22222222222222" || 
		        cnpj == "33333333333333" || 
		        cnpj == "44444444444444" || 
		        cnpj == "55555555555555" || 
		        cnpj == "66666666666666" || 
		        cnpj == "77777777777777" || 
		        cnpj == "88888888888888" || 
		        cnpj == "99999999999999")
		        return false;
		         
		    // Valida DVs
		    tamanho = cnpj.length - 2
		    numeros = cnpj.substring(0,tamanho);
		    digitos = cnpj.substring(tamanho);
		    soma = 0;
		    pos = tamanho - 7;
		    for (i = tamanho; i >= 1; i--) {
		      soma += numeros.charAt(tamanho - i) * pos--;
		      if (pos < 2)
		            pos = 9;
		    }
		    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
		    if (resultado != digitos.charAt(0))
		        return false;
		         
		    tamanho = tamanho + 1;
		    numeros = cnpj.substring(0,tamanho);
		    soma = 0;
		    pos = tamanho - 7;
		    for (i = tamanho; i >= 1; i--) {
		      soma += numeros.charAt(tamanho - i) * pos--;
		      if (pos < 2)
		            pos = 9;
		    }
		    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
		    if (resultado != digitos.charAt(1))
		          return false;
		           
		    return true;
		    
		}
		 
	}]); // SecurityController
	
	/**
	 * View Controller
	 */
	app.controller('ViewController', ['$scope', '$http', 'securityServices', function($scope, $http, securityServices) {
		
		/**
		 * Abas
		 */
		$scope.sectionTab = 1;
		$scope.setSectionTab = function(value) {
			this.sectionTab = value;
	    };
	    $scope.isSectionTabSet = function(value) {
	      return this.sectionTab === value;
	    };

		/**
		 * Autorização
		 */
		$scope.authList =[];
		defineAuthorities();
		function defineAuthorities(){
			securityServices.getAuthorizedRoles(null).success(function(data, status, headers, config) {
				$scope.authList = data.content;
			});
		}
		$scope.isAuthorized =function(role, ext){
			return securityServices.isAuthorized($scope.authList, role, ext);
		}
		 
	}]);

} )();
