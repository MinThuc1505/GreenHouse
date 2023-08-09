app.controller('materialCtrl', function($scope, $http, urlMaterial) {
    let host = urlMaterial;
    $scope.form = {};
    $scope.item = {};
    $scope.materials = [];
 

    $scope.load_all = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.materials = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });
    };
    $scope.createMaterial = function () {
        var item = {
          material: $scope.form.material,
        };
        console.log(item);
      
        var url = `${host}`;
        $http.post(url, item).then(
          function (resp) {
            console.log("Success", resp);
            $scope.loadAll(); // Gọi lại hàm loadAll() để load lại dữ liệu
            Swal.fire({
              icon: 'success',
              title: 'Thành công',
              text: `Đã thêm tài khoản ` + item.material,
            });
          },
          function (Error) {
            Swal.fire({
              icon: 'error',
              title: 'Thất bại',
              text: `Thêm tài khoản ` + item.material + ` thất bại `,
            });
          }
        );
      };
      
    $scope.load_all();
});

