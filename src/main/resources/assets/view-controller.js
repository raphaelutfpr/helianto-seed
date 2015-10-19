angular.module('app.services')

/**
 * Language filter.
 */
.filter('i18n', ['lang', function (lang) {
	return function (key, p) {
		if (typeof lang[key] != 'undefined' && lang[key] != '') {
			return (typeof p === "undefined") ? lang[key] : lang[key].replace('@{}@', p);
		}
		return key;
	}
}])
.directive("slimScroll",[function(){
	return{
		restrict:"A"
		,link:function(scope,ele,attrs) {
			return ele.slimScroll({height:attrs.scrollHeight||"100%"})
		}
	}
}])

/**
 * View controller
 */
.controller('ViewController', ['$rootScope', '$http', 'lang'
                               , function($rootScope, $http, lang) {
		
	$rootScope.logout = function() {
		return $http.post('/logout');
    }
	/**
	 * Tabs
	 */
	$rootScope.sectionTab = 1;
	$rootScope.setSectionTab = function(value) {
		$rootScope.sectionTab = value;
    };
    $rootScope.isSectionTabSet = function(value) {
        return $rootScope.sectionTab === value;
    };

	/**
	 * Authorization
	 */
    $rootScope.getAuthorizedRoles = function(userId) {
		return $http.get('/api/entity/auth'+((userId!=null && userId!='null')?'?userId='+userId:''))
		.success(function(data, status, headers, config) {
			roleList = data.content;
		});
	}
    $rootScope.roleList = $rootScope.getAuthorizedRoles();
	$rootScope.isAuthorized = function(role, ext){
		return false;
	}
		 
}]);
