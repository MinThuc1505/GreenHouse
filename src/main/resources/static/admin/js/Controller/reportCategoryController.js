app.controller('reportCategoryController', function ($scope, $http, urlReportCategory) {
    let host = urlReportCategory;
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
    $scope.load_all();
})

function formatDateToISOString(dateString) {
    var date = new Date(dateString);
    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString().padStart(2, '0');
    var day = date.getDate().toString().padStart(2, '0');
    var hours = date.getHours().toString().padStart(2, '0');
    var minutes = date.getMinutes().toString().padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  }