(function() {
	/**
	 * Angular user module.
	 */
	app = angular.module('user', ['ui.bootstrap', 'app.services', 'angular-redactor'])
	/**
	 * Resources service.
	 */
	.factory('resources', ['$resource', function($resource) {
		var service = {};
		service.qualifier = $resource("/api/user/qualifier");
		service.user = $resource("/api/user/");
		return service;
	}]) 
	/**
	 * Angular user controller
	 */
	.controller('UserController', ['$scope', '$window', 'resources', 'qualifierService', 'genericServices', 'securityServices'
	                                  , function($scope, $window, resources, qualifierService, genericServices, securityServices) {
	
		$scope.baseName = "user";
		$scope.userStates = "A";
		
		/**
		 * Qualifiers
		 */
		$scope.setQualifier = function(value, data) {
			if (Array.isArray(data)) {
				$scope.qualifiers = data;
			}
			$scope.qualifierValue = value;
			$scope.userList = {};
			$scope.listUsers(value);
		}
		qualifierService.run(resources.qualifier, $scope.setQualifier, 0);
		
		/**
		 * Users
		 */
		$scope.listUsers = function(value) {
			$scope.userList = resources.user.get({userType: value, userStates: $scope.userStates})
		}
		
	}]); // userController
	
} )();
