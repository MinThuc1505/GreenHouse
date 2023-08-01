const appClient = angular.module('appClient', ['ngRoute', 'ngCookies']);
appClient.constant('urlIndexClient', 'http://localhost:8081/client/rest/index'); 
appClient.constant('urlShopClient', 'http://localhost:8081/client/rest/shop'); 
appClient.constant('urlMenuClient', 'http://localhost:8081/client/rest/menu'); 
appClient.constant('urlSignInClient', 'http://localhost:8081/client/rest/signIn'); 
appClient.constant('urlHeaderClient', 'http://localhost:8081/client/rest/header'); 