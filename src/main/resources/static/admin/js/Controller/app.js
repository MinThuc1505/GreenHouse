const app = angular.module('app', []);
app.constant('urlDiscount', 'http://localhost:8081/rest/discounts');

app.constant('urlAccount', 'http://localhost:8081/rest/user');

app.constant('urlProvider', 'http://localhost:8081/rest/provider');

app.constant('urlProduct', 'http://localhost:8081/rest/products');
app.constant('urlSize', 'http://localhost:8081/rest/sizes');