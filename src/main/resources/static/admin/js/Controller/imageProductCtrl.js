app.controller('imageProductCtrl', function($scope, $http, urlImageProduct) {
    let host = urlImageProduct;
    $scope.form = {};
    $scope.items = {};
    $scope.products = [];
    $scope.selectedItemIndex = -1; // Biến lưu trạng thái sản phẩm đang được chỉnh sửa
 
    
      // Lấy dữ liệu kích tên sản phẩm
      $http.get('/rest/products').then(resp => {
        $scope.products = resp.data;
    }).catch(Error => {
        console.log("Error", Error);
    });

    $scope.load_allImage = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
        }).catch(error => {

            console.log("Error", error);
        });
    };

    $scope.Edit = function (key,index) {
        var url = `${host}/${key}`;
        $http.get(url).then(resp => {
            $scope.form = resp.data;
            $scope.key = key;
            $scope.selectedItemIndex = index; // Lưu chỉ số sản phẩm đang được chỉnh sửa
   
            }).catch(Error => {
            console.log("Error", Error);
        })
    }
    $scope.Create = function () {
        var item = {
            id: $scope.id,
            product: $scope.product,
            image: $scope.image
            
        };
        var url = `${host}`;
        $http.post(url, item).then(resp => {
            console.log("Success", resp);
            $scope.load_allImage();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Đã thêm mã ` + item.product.name,
            });
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Thêm mã ` + item.product.name + ` thất bại `,
            });
        })
    }
    $scope.Update = function (key) {

        var item = {
            id: $scope.form.id,
            product: $scope.form.product,
            image: $scope.form.image
        };
        var url = `${host}/${key}`;
        $http.put(url, item).then(resp => {
            $scope.items[$scope.key] = resp.data;
            $scope.load_allImage();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Cập nhật mã ${key}`,
            });
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Cập nhật mã ${key} thất bại`,
            });
        })
    }
    $scope.Delete = function(key){
        var url = `${host}/${key}`;
        $http.delete(url).then(resp => {
            $scope.load_allImage();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: ` Đã xóa sản phẩm  ${key} thành công`,
            });
        }).catch(Error =>{
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Xóa sản phẩm ${key} thất bại`,
            });
        })
    }
    $scope.load_allImage();
});
