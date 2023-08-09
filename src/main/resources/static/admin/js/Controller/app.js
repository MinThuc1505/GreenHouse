const app = angular.module('app', []);
app.constant('urlDiscount', 'http://localhost:8081/rest/discounts');

app.constant('urlAccount', 'http://localhost:8081/rest/user');

app.constant('urlProvider', 'http://localhost:8081/rest/provider');

app.constant('urlReportBill', 'http://localhost:8081/rest/reportBill')

app.constant('urlReportCategory', 'http://localhost:8081/rest/reportCategory')

app.constant('urlReportStatic', 'http://localhost:8081/rest/MonthlyStatistic')

app.constant('urlProduct', 'http://localhost:8081/rest/products');

