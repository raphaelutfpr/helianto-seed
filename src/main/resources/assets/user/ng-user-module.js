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
		$scope.itemsPerPage = 20;
		
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
		$scope.user = {};
		$scope.userId = 0;
		$scope.listUsers = function(value, pageNumber) {
			$scope.userList = resources.user.get(
				{userType: value, userStates: $scope.userStates, pageNumber: pageNumber, itemsPerPage: $scope.itemsPerPage})
			$scope.userList.$promise.then(function(data) {
				if (data.content.length>0) {
					if ($scope.userId === 0  && externalId==0) {
						$scope.user = data.content[0];
						$scope.userId = user.id;
					}
				}
			})
		}
		$scope.pageChanged = function() {
			var value = $scope.userList.number;
		    $scope.listUsers($scope.qualifierValue, value);
		}
		$scope.setItemsPerPage = function(value) {
			$scope.itemsPerPage = value;
		    $scope.listUsers($scope.qualifierValue, 1);
		}
		$scope.getUser = function(value) {
			$scope.user = resources.user.get(
				{userId: value});
			$scope.user.$promise.then(function(data) {
				if (data.length>0) {
					$scope.user = data;
					$scope.userId = user.id;
				}
			})
		}
		
	}]); // userController
	
} )();
