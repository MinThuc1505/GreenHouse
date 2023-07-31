app.controller('userController', function($scope, $http, urlAccount){
	let host = urlAccount;
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
           $scope.form.createDate = formatDateToISOString($scope.form.createDate);
        }).catch(Error =>{
            console.log("Error", Error);
        })
    }
    $scope.Update = function(key){var item = {
        username: $scope.form.username,
        password: $scope.form.password,
        fullName: $scope.form.fullName,
        email: $scope.form.email,
        phone: $scope.form.phone,
        gender: $scope.form.gender,
        address: $scope.form.address,
        image: $scope.form.image,
        role_id: { id: $scope.form.role_id === 'admin' ? 'admin' : 'user', name: $scope.form.role_id === 'admin' ? 'Admin' : 'User' },
        createDate: $scope.form.createDate
    };

    var url = `${host}/${key}`;
    $http.put(url, item).then(resp => {
        $scope.items[$scope.key] = resp.data;
        $scope.load_all();
        Swal.fire({
            icon: 'success',
            title: 'Thành công',
            text: `Cập nhật ${key}`,
        });
    }).catch(Error => {
        Swal.fire({
            icon: 'error',
            title: 'Thất bại',
            text: `Cập nhật ${key} thất bại`,
        });
    });
    }
    
   
    
    $scope.Create = function(){
		
		var files = document.getElementById('fileInputCreateUser').files;
		console.log(files[0].name);
        var item = {
            username: $scope.username,
            password: $scope.password,
	        fullName: $scope.fullName,
	        email: $scope.email,
	        phone: $scope.phone,
	        gender: $scope.gender,
	        address: $scope.address,
            image: files[0].name,
            role_id: { id: $scope.form.role_id === 'admin' ? 'admin' : 'user', name: $scope.form.role_id === 'admin' ? 'Admin' : 'User' },
            createDate: new Date()
	    };
        
        
        
        
         console.log(item);
        
        var url = `${host}`;
        $http.post(url, item).then(resp => {
            console.log("Success", resp);
            $scope.load_all();
            Swal.fire({
			    icon: 'success',
			    title: 'Thành công',
			    text: `Đã thêm tài khoản ` + item.username,
			});
        }).catch(Error =>{
           Swal.fire({
			    icon: 'error',
			    title: 'Thất bại',
			    text: `Thêm tài khoản ` + item.username + ` thất bại `,
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
				    text: `Đã xóa tài khoản ${key}`,
				});
        }).catch(Error =>{
	          Swal.fire({
				    icon: 'error',
				    title: 'Thất bại',
				    text: `Xóa tài khoản ${key} thất bại`,
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