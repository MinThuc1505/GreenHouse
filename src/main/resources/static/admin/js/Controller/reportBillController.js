app.controller('reportBillController', function ($scope, $http, urlReportBill) {
    let host = urlReportBill;
    $scope.form = {};
    $scope.items = {};
    $scope.load_all = function(){
        var url = `${host}`;
        $http.get(url).then(resp => {
        	$scope.items = resp.data;
            console.log("Success", items);
        }).catch(Error =>{
            console.log("Error", Error);
        })
    }
    $scope.formatDate = function (dateString) {
        var date = new Date(dateString);
        var day = date.getDate().toString().padStart(2, '0');
        var month = (date.getMonth() + 1).toString().padStart(2, '0');
        var year = date.getFullYear();
        var hours = date.getHours().toString().padStart(2, '0');
        var minutes = date.getMinutes().toString().padStart(2, '0');
        var seconds = date.getSeconds().toString().padStart(2, '0');
        return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    };

    $scope.load_all();
})

