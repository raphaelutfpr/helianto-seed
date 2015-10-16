// From Slim
angular.module('app.services')
.controller("AppCtrl",["$scope","$rootScope","$route","$document"
                       ,function($scope,$rootScope,$route,$document){
	var $window;
	return $window=$(window)
	,$scope.main={brand:"Slim",name:"Lisa Doe"}
	,$scope.pageTransitionOpts=[
	      {name:"Fade up","class":"animate-fade-up"}
	      ,{name:"Scale up","class":"ainmate-scale-up"}
	      ,{name:"Slide in from right","class":"ainmate-slide-in-right"}
	      ,{name:"Flip Y","class":"animate-flip-y"}
	 ],$scope.admin={
		layout:"wide"
		,menu:"vertical"
		,fixedHeader:!0
		,fixedSidebar:!0
		,pageTransition:$scope.pageTransitionOpts[0],skin:"11"
	},$scope.$watch("admin",function(newVal,oldVal){
		return"horizontal"===newVal.menu&&"vertical"===oldVal.menu?void $rootScope.$broadcast("nav:reset"):newVal.fixedHeader===!1&&newVal.fixedSidebar===!0?(oldVal.fixedHeader===!1&&oldVal.fixedSidebar===!1&&($scope.admin.fixedHeader=!0,$scope.admin.fixedSidebar=!0),void(oldVal.fixedHeader===!0&&oldVal.fixedSidebar===!0&&($scope.admin.fixedHeader=!1,$scope.admin.fixedSidebar=!1))):(newVal.fixedSidebar===!0&&($scope.admin.fixedHeader=!0),void(newVal.fixedHeader===!1&&($scope.admin.fixedSidebar=!1)))},!0),$scope.color={primary:"#5B90BF",success:"#A3BE8C",info:"#7FABD2",infoAlt:"#B48EAD",warning:"#EBCB8B",danger:"#BF616A",gray:"#DCDCDC"},$rootScope.$on("$routeChangeSuccess",function(){return $document.scrollTo(0,0)})}])

.directive("toggleNavCollapsedMin",["$rootScope",function($rootScope){
	return{
		restrict:"A"
		,link:function(scope,ele){
			var app;
			return app=$("#app")
			,ele.on("click",function(e){
				return app.hasClass("nav-collapsed-min")?app.removeClass("nav-collapsed-min"):(app.addClass("nav-collapsed-min")
						,$rootScope.$broadcast("nav:reset"))
						,e.preventDefault()}
			)
		}
	}
}])
.directive("collapseNav",[function(){
	return{
		restrict:"A"
		,link:function(scope,ele){var $a,$aRest,$app,$lists,$listsRest,$nav,$window,Timer,prevWidth,updateClass;return $window=$(window),$lists=ele.find("ul").parent("li"),$lists.append('<i class="ti-angle-down icon-has-ul-h"></i><i class="ti-angle-double-right icon-has-ul"></i>'),$a=$lists.children("a"),$listsRest=ele.children("li").not($lists),$aRest=$listsRest.children("a"),$app=$("#app"),$nav=$("#nav-container"),$a.on("click",function(event){var $parent,$this;return $app.hasClass("nav-collapsed-min")||$nav.hasClass("nav-horizontal")&&$window.width()>=768?!1:($this=$(this),$parent=$this.parent("li"),$lists.not($parent).removeClass("open").find("ul").slideUp(),$parent.toggleClass("open").find("ul").stop().slideToggle(),event.preventDefault())}),$aRest.on("click",function(){return $lists.removeClass("open").find("ul").slideUp()}),scope.$on("nav:reset",function(){return $lists.removeClass("open").find("ul").slideUp()}),Timer=void 0,prevWidth=$window.width(),updateClass=function(){var currentWidth;return currentWidth=$window.width(),768>currentWidth&&$app.removeClass("nav-collapsed-min"),768>prevWidth&&currentWidth>=768&&$nav.hasClass("nav-horizontal")&&$lists.removeClass("open").find("ul").slideUp(),prevWidth=currentWidth},$window.resize(function(){var t;return clearTimeout(t),t=setTimeout(updateClass,300)})}}}])
//.directive("highlightActive",[function(){
//	return{
//		restrict:"A"
//		,controller:["$scope","$element","$attrs","$location",function($scope,$element,$attrs,$location){
//			var highlightActive,links,path;
//			return links=$element.find("a")
//			,path=function(){
//				return $location.path()
//			}
//			,highlightActive=function(links,path){
//				return path=path
//				,angular.forEach(
//						links
//						,function(link){
//							var $li,$link,href;
//							return $link=angular.element(link)
//							,$li=$link.parent("li")
//							,href=$link.attr("href")
//							,$li.hasClass("active")&&$li.removeClass("active")
//							,0===path.indexOf(href)?$li.addClass("active"):void 0
//				})}
//			,highlightActive(links,$location.path())
//			,$scope.$watch(path,function(newVal,oldVal){
//				return newVal!==oldVal?highlightActive(links,$location.path()):void 0})}]}}
//])
.directive("toggleOffCanvas",[function(){
	return{
		restrict:"A"
		,link:function(scope,ele){return ele.on("click",function(){return $("#app").toggleClass("on-canvas")})}}}
])
