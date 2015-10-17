[#ftl]
<!DOCTYPE html>
<html id="ng-app" xmlns:ng="http://angularjs.org" data-ng-app="security" >
<head>
    <title>Login</title>
	[#include "/frame-head.ftl" /]
</head>
<body style="background-color: #f3f3f4;">

	<div id="unauth">
	<div id="main" class="container" data-ng-controller="SecurityController as securityCtrl" >
	<div class="container-small" data-ng-cloak >

	[#include "/${main!'login'}.ftl" /]
	
	<footer class="footer">
		<hr>
        <p>${copyright!''}</p>
  
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

	[#include "/_js.html" /]

</body>
</html>