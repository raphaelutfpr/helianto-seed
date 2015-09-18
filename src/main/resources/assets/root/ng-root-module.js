(function() {
	app = angular.module('root', ['ui.bootstrap', 'app.layout', 'angular-loading-bar', 'app.services']);

	app.controller('RootController', ['$scope', '$window', '$http', '$resource', 'qualifierService', 'lang'
	                                 , function($scope, $window, $http, $resource, qualifierService, lang) {
		
		$scope.baseName = "root";
		
		var baseUrl = '/api/root/';

		/**
		 * Resources
		 */
		$scope.rootResource = $resource(baseUrl + "entity", {entityType: "@entityType", rootUserId: "@rootUserId"}, {
			save: { method: 'PUT' }
			, create: { method: 'POST' }
			, search : { method: 'POST', url: baseUrl+'search'}
			, authorize : { method: 'GET'}
		});

		$scope.root;

		/**
		 * Qualifier
		 */
		$scope.setQualifier = function(value, data) {
			if (Array.isArray(data)) {
				$scope.qualifiers = data;
			}
			$scope.qualifierValue = value;
			$scope.rootList = [];
			$scope.listRoots(value);
		}
		qualifierService.run($resource(baseUrl + "qualifier"), $scope.setQualifier, 0);

		/**
		 * Root entities
		 */
		$scope.rootValue = 0;
		// list
		$scope.listRoots = function(qualifierValue) {
			$scope.rootList = $scope.rootResource.get({entityType: qualifierValue});
			$scope.rootList.$promise.then(function(data) {
				if ($scope.rootValue === 0 && data.length>0) {
					$scope.rootValue = $scope.rootList[0].id;
				}
			})
		};
		
		/**
		 * Faz  a pesquisa
		 */
		$scope.search = function(page, searchUrl) {
			$scope.searchString = $("#searchString").val();
			search(page, $scope.searchString, searchUrl) ;
		}
		
		function search(page, searchString, searchUrl) {
			var dataObj = {	
					"searchString" : searchString ,
					"qualifierValue" : $scope.categoryId,
			};
			var res =  $http.post(searchUrl+'/'+$scope.sectionTab+'/'+page
					, dataObj
					, {});
			res.success(function(data, status, headers, config) {
				$scope.resultFromSearch = data;
				$scope.searchBool = true;
				$scope.page = page;
				$scope.nextAndPrevious = genericServices.getNextAndPreviousLinkByList(data);
				if(data.totalElements == 1){
					$scope.root = data.content[0];
				}
				else if(data.totalElements>1) {
					$scope.rootList = data;	
				}
				else {					
					$scope.rootList = [];
				}
			})    
		}
		// get
		$scope.getRoot = function(id) {
			console.log("USER ID = "+id);
			if (id==0) {
				$scope.root = $scope.rootResource.create({categoryId:$scope.qualifierValue});
			}
			else {
				$scope.root = $scope.rootResource.get({userId: id});
			}
			$scope.root.$promise.then(
				function(data, getReponseHeaders) {
					if (data.length>0) {
						$scope.rootValue = data.userId;
					}
				}
			);
		};
		// authorize
		$scope.authorize = function() {
			$scope.newUser = $scope.rootResource.authorize({rootUserId:$scope.root.userId});
			$scope.newUser.$promise.then(
				function(data, getReponseHeaders) {
					if (data.success==1) {
						$window.location = "/";
					}
				}
			);
		}
				
		/**
		 * Abre um modal.
		 * 
		 * @param formName Nome do Fragmento (form-YYYY)
		 * 
		 */
		$scope.openForm = function(formName){
			$scope.message =[];
			//inicialização em form-report
			//$scope.createPart = false;
			//$('#save-report input[type="text"]').removeAttr('readonly').val('');
			
			$scope.formUrl = '/ng/entity/'+formName+'.html';
			console.log($scope.formUrl);
			$("#modalBody").modal('show');
		}
		
		/**
		 * Retorna o form a ser mostrado no Modal
		 */
		$scope.getFormUrl = function(){
			return $scope.formUrl;
		} 
		
		$scope.open = function($event,value) {
			$event.preventDefault();
			$event.stopPropagation();
			$scope.datePicker = [];
			$scope.datePicker[value]=true;
		};
		//formato do combobox
		$scope.formats = ['ativo', 'inativo'];
		$scope.format = $scope.formats[0];
		
		  $scope.openForm = function(formName){
				openForm(formName);
			}
			
		  function openForm(formName){
				$scope.message = [];
				//inicialização em form
				$scope.formUrl = '/assets/'+$scope.baseName+'/'+formName+'.html';
				$("#modalBody").modal('show');
		
			}

	}])
	
	.directive('rootWrapper', function() {
		return {
			restrict: 'EA',
			scope: { root: '='},
			transclude: true,
			templateUrl: '/assets/root/ng-root-template-entity.html'
		}

	})
		
})();
