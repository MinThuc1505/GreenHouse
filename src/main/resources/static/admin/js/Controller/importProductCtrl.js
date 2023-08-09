app.controller('importProductCtrl', function ($scope, $http, urlImportProduct) {
    let host = urlImportProduct;
    $scope.form = {};
    $scope.items = {};
      $scope.accounts = [];
    $scope.products = [];
    $scope.billImportProducts = [];
    $scope.selectedItemIndex = -1; // Biến lưu trạng thái sản phẩm đang được chỉnh sửa
    $scope.load_all = function(){
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
        }).catch(Error =>{
            console.log("Error", Error);
        })
    }

    // Lấy dữ liệu kích tên sản phẩm
    $http.get('/rest/products').then(resp => {
        $scope.products = resp.data;
    }).catch(Error => {
        console.log("Error", Error);
    });

    // Lấy dữ liệu kích thước
    $http.get('/rest/user').then(resp => {
        $scope.accounts = resp.data;
    }).catch(Error => {
        console.log("Error", Error);
    });
      // Lấy dữ liệu kích thước
      $http.get('/rest/billImportProduct').then(resp => {
        $scope.billImportProducts = resp.data;
    }).catch(Error => {
        console.log("Error", Error);
    });

    $scope.Edit = function(key, index){
        var url = `${host}/${key}`;
        $http.get(url).then(resp => {
            $scope.form = resp.data;
            $scope.selectedItemIndex = index; // Lưu chỉ số sản phẩm đang được chỉnh sửa
            $scope.form.createDate = formatDateToISOString($scope.form.createDate);
        }).catch(Error =>{
            console.log("Error", Error);
        })
    }

    $scope.Create = function() {
       
        var item = {
            product: $scope.product,
            billImportProduct:$scope.billImportProduct,
            priceImport: $scope.priceImport,
            quantityImport: $scope.quantityImport,
            amountImport: $scope.amountImport,
            createDate: new Date(),
            description: $scope.description
        };
    
        console.log(item);
        var url = `${host}`;
        $http.post(url, item).then(resp => {
            console.log("Success", resp);
            $scope.load_all();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: 'Đã thêm tài khoản ' + item.product.name,
            });
        }).catch(error => {
            console.error("Error", error);
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: 'Thêm tài khoản ' + item.product.name + ' thất bại',
            });
        });
    };
    

    
    // Cập nhật sản phẩm
    $scope.Update = function() {
        
        var url = `${host}/${$scope.form.id}`;

        $http.put(url, $scope.form).then(resp => {
            $scope.items[$scope.selectedItemIndex] = resp.data;
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: 'Cập nhật sản phẩm thành công',
            });
            $scope.form = {}; // Reset form
            $scope.selectedItemIndex = -1; // Reset chỉ số sản phẩm đang được chỉnh sửa
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: 'Cập nhật sản phẩm thất bại',
            });
        });
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
