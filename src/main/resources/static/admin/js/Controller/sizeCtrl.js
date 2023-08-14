app.controller('sizeCtrl', function ($scope, $http, urlSize) {
    let host = urlSize;
    $scope.form = {};
    $scope.sizes = [];

    $scope.loadAll = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.sizes = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });
    };

    $scope.createSize = function () {
        console.log("height:", $scope.height);
        console.log("width:", $scope.width);
        console.log("length:", $scope.length);
    
        var size = {
            id: $scope.id,
            height: $scope.height,
            width: $scope.width,
            length: $scope.length
        };
    
        var url = `${host}`;
        $http.post(url, size).then(resp => {
            console.log("Success", resp.data);
            $scope.loadAll();
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: 'Đã tạo kích thước mới.',
            });
        }).catch(error => {
            if (error.status === 409) {
                Swal.fire({
                    icon: 'error',
                    title: 'Thất bại',
                    text: 'Kích thước đã tồn tại.',
                });
            } 
        });
    };
    
    

    $scope.loadAll();
});
