app.controller('materialCtrl', function($scope, $http, urlMaterial) {
    let host = urlMaterial;
    $scope.form = {};
    $scope.materials = [];
 

    $scope.load_allMaterial= function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.materials = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });
    };
    $scope.createMaterial = function () {
      var item = {
          id: $scope.id,
          material: $scope.material
      };
      console.log(item);
  
      var url = `${host}`;
      $http.post(url, item).then(resp => {
          console.log("Success", resp.data);
          $scope.load_allMaterial();
          Swal.fire({
              icon: 'success',
              title: 'Thành công',
              text: 'Đã tạo chất liệu mới.',
          });
      }).catch(error => {
          console.error("Error", error); {
            Swal.fire({
              icon: 'error',
              title: 'Thất bại',
              text: 'Tên chất liệu đã tồn tại.',
          });
          }
      });
  };
  
      
    $scope.load_allMaterial();
});

