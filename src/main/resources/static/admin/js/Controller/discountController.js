app.controller('discountController', function($scope, $http, urlDiscount){
	let host = urlDiscount;
    $scope.form = {};
    $scope.items = {};
    $scope.load_all = function(){
        var url = `${host}`;
        $http.get(url).then(resp => {
        	$scope.items = resp.data;
        }).catch(Error =>{
            console.log("Error", Error);
        })
    }
    $scope.Edit = function(key){
        var url = `${host}/${key}`;
        $http.get(url).then(resp => {
            $scope.form = resp.data;
            $scope.key = key;
            
             
           $scope.form.startDate = formatDateToISOString($scope.form.startDate);
			$scope.form.endDate = formatDateToISOString($scope.form.endDate);
            
             $scope.form.status = ($scope.form.status === "1");
            
            
            
          /*   console.log("Success", resp);*/
        }).catch(Error =>{
            console.log("Error", Error);
        })
    }
    $scope.Update = function(key){
        
        var item = {
	        discountCode: $scope.form.discountCode,
	        startDate: $scope.form.startDate,
	        quantity: $scope.form.quantity,
	        endDate: $scope.form.endDate,
	        discountPercent: $scope.form.discountPercent,
	        status: $scope.form.status 
	    };
        var url = `${host}/${key}`;
         console.log(url);
        $http.put(url, item).then(resp => {
           $scope.items[$scope.key] = resp.data;
           $scope.load_all();
           Swal.fire({
			    icon: 'success',
			    title: 'Thành công',
			    text: `Cập nhật mã ${key}`,
			});
        }).catch(Error =>{
			Swal.fire({
			    icon: 'error',
			    title: 'Thất bại',
			    text: `Cập nhật mã ${key} thất bại`,
			});
        })
    }
    $scope.Create = function(){
        var item = {
	        discountCode: $scope.discountCode,
	        startDate: $scope.startDate,
	        quantity: $scope.quantity,
	        endDate: $scope.endDate,
	        discountPercent: $scope.discountPercent,
	        status: $scope.status 
	    };
        
        
        var url = `${host}`;
        $http.post(url, item).then(resp => {
            console.log("Success", resp);
            $scope.load_all();
            Swal.fire({
			    icon: 'success',
			    title: 'Thành công',
			    text: `Đã thêm mã ` + item.discountCode,
			});
        }).catch(Error =>{
           Swal.fire({
			    icon: 'error',
			    title: 'Thất bại',
			    text: `Thêm mã ` + item.discountCode + ` thất bại `,
			});
        })
    }
    $scope.Delete = function(key){
        var url = `${host}/${key}`;
        $http.delete(url).then(resp => {
            $scope.load_all();
	        Swal.fire({
				    icon: 'success',
				    title: 'Thành công',
				    text: `Đã xóa mã ${key}`,
				});
        }).catch(Error =>{
	          Swal.fire({
				    icon: 'error',
				    title: 'Thất bại',
				    text: `Xóa mã ${key} thất bại`,
				});
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