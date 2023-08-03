app.controller('productCtrl', function($scope, $http, urlProduct) {
    let host = urlProduct;
    $scope.form = {};
    $scope.items = {};
    $scope.sizes = [];
    $scope.materials = [];

    
    $scope.load_all = function() {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
        }).catch(Error => {
            console.log("Error", Error);
        });
    };
       // Lấy dữ liệu kích thước
       $http.get('/rest/sizes').then(resp => {
        $scope.sizes = resp.data;
    }).catch(Error => {
        console.log("Error", Error);
    });

    // Lấy dữ liệu vật liệu
    $http.get('/rest/materials').then(resp => {
        $scope.materials = resp.data;
    }).catch(Error => {
        console.log("Error", Error);
    });
    
    $scope.Edit = function(key) {
        var url = `${host}/${key}`;
        $http.get(url).then(resp => {
            $scope.form = resp.data;
            $scope.key = key;
              }).catch(Error => {
            console.log("Error", Error);
        });
    };
    
    $scope.Update = function(key) {
        var item = {
            id: $scope.form.id,
            name: $scope.form.name,
            price: $scope.form.price,
            quantity: $scope.form.quantity,
            status: $scope.form.status,
            size: $scope.form.size,
            material: $scope.form.material,
            image: $scope.form.image, 
            description: $scope.form.description
        };

        var url = `${host}/${key}`;
        $http.put(url, item).then(resp => {
            $scope.items[$scope.key] = resp.data;
            $scope.load_all();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Cập nhật sản phẩm ${key}`,
            });
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Cập nhật sản phẩm ${key} thất bại`,
            });
        });
    };
    
    $scope.Create = function() {
        var files = document.getElementById('fileInputCreateProduct').files;
        console.log(files[0].name);
        var item = {
            name: $scope.name,
            price: $scope.price,
            quantity: $scope.quantity,
            status: $scope.status,
            size: $scope.size,
            material: $scope.material,
            image: files[0].name, 
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
                text: `Đã thêm sản phẩm ` + item.name,
            });
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Thêm sản phẩm ` + item.name + ` thất bại `,
            });
        });
    };
    
    $scope.Delete = function(key) {
        var url = `${host}/${key}`;
        $http.delete(url).then(resp => {
            $scope.load_all();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Đã xóa sản phẩm ${key}`,
            });
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Xóa sản phẩm ${key} thất bại`,
            });
        });
    };
    
    $scope.load_all();
});

function displayImageProduct(event, idImg) {
    var image = document.getElementById(idImg);
    image.src = URL.createObjectURL(event.target.files[0]);
    image.style.display = "block";
}