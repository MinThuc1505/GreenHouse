const app = angular.module('app', []);
app.constant('urlDiscount', 'http://localhost:8081/rest/discounts');

app.constant('urlAccount', 'http://localhost:8081/rest/user');

app.constant('urlProvider', 'http://localhost:8081/rest/provider');

app.constant('urlProduct', 'http://localhost:8081/rest/products');

app.constant('urlReportBill', 'http://localhost:8081/rest/reportBill')

app.constant('urlReportCategory', 'http://localhost:8081/rest/reportCategory')

// app.constant('urlReportStatic', 'http://localhost:8081/rest/MonthlyStatistic')
app.constant('urlCategory', 'http://localhost:8081/rest/category');
app.constant('urlSize', 'http://localhost:8081/rest/sizes');
app.constant('urlMaterial', 'http://localhost:8081/rest/materials');
app.constant('urlImportProduct', 'http://localhost:8081/rest/importProduct');
app.constant('urlPriceHistoryCtrl', 'http://localhost:8081/rest/priceHistorys');