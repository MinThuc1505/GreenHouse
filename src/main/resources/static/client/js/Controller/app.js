const appClient = angular.module('appClient',  ['ngRoute']);
appClient.constant('urlIndexClient', 'http://localhost:8081/client/rest/index'); 
appClient.constant('urlShopClient', 'http://localhost:8081/client/rest/shop'); 
appClient.constant('urlMenuClient', 'http://localhost:8081/client/rest/menu'); 

