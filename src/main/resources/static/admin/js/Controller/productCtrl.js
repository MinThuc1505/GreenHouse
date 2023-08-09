app.controller('productCtrl', function($scope, $http, urlProduct) {
    let host = urlProduct;
    $scope.form = {};
    $scope.items = {};
    $scope.sizes = [];
    $scope.materials = [];
    $scope.selectedItemIndex = -1; // Biến lưu trạng thái sản phẩm đang được chỉnh sửa

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

   $scope.Edit = function(key, index) {
    var url = `${host}/${key}`;
    $http.get(url).then(resp => {
        $scope.form = resp.data;
        $scope.selectedItemIndex = index; // Lưu chỉ số sản phẩm đang được chỉnh sửa
        displayImages(resp.data.image); // Hiển thị ảnh tương ứng cho sản phẩm đang chỉnh sửa
    }).catch(Error => {
        console.log("Error", Error);
    });
};
    
    
    $scope.Create = function() {
        var formData = new FormData();
        formData.append("name", $scope.name);
        formData.append("price", $scope.price);
        formData.append("quantity", $scope.quantity);
        formData.append("status", $scope.status);
        formData.append("size", $scope.size);
        formData.append("material", $scope.material);
        formData.append("description", $scope.description);
        formData.append("file", document.getElementById('fileInputCreateProduct').files[0]);
    
        formData.append("productDTO", JSON.stringify({
            name: $scope.name,
            price: $scope.price,
            quantity: $scope.quantity,
            status: $scope.status,
            size: $scope.size,
            material: $scope.material,
            description: $scope.description
        }));
        formData.append("file", $scope.form.image);
        var url = `${host}`;
        $http.post(url, formData, {
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            console.log("Success", resp);
            $scope.load_all();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: `Đã thêm sản phẩm ` + $scope.name,
            });
        }).catch(Error => {
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Thêm sản phẩm ` + $scope.name + ` thất bại `,
            });
        });
    };
    $scope.Update = function(key) {
        var formData = new FormData();
        formData.append("name", $scope.form.name);
        formData.append("price", $scope.form.price);
        formData.append("quantity", $scope.form.quantity);
        formData.append("status", $scope.form.status);
        formData.append("size", $scope.form.size);
        formData.append("material", $scope.form.material);
        formData.append("description", $scope.form.description);
        formData.append("file", document.getElementById('fileInput22').files[0]);
    
        formData.append("productDTO", JSON.stringify({
            id: $scope.form.id,
            name: $scope.form.name,
            price: $scope.form.price,
            quantity: $scope.form.quantity,
            status: $scope.form.status,
            size: $scope.form.size,
            material: $scope.form.material,
            description: $scope.form.description
        }));
        formData.append("file", $scope.form.image);
    
        var url = `${host}/${key}`;
        $http.put(url, formData, {
            headers: { 'Content-Type': undefined }
        }).then(resp => {
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

function displayImage(event) {
    var input = event.target;
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            var imageContainer = document.getElementById('uploadedImage');
            imageContainer.src = e.target.result;
            imageContainer.style.display = "block";
        };

        reader.readAsDataURL(input.files[0]);
    }
}

function displayImages(event) {
    var input = event.target;
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            var imageContainer = document.getElementById('uploadedImages');
            imageContainer.src = e.target.result;
            imageContainer.style.display = "block";
        };

        reader.readAsDataURL(input.files[0]);
    }
}