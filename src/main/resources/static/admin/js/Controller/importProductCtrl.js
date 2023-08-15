app.controller('importProductCtrl', function ($scope, $http, urlImportProduct) {
    let host = urlImportProduct;
    $scope.form = {};
    $scope.items = {};
      $scope.accounts = [];
    $scope.products = [];
    $scope.billImportProducts = [];
    $scope.selectedItemIndex = -1; // Biến lưu trạng thái sản phẩm đang được chỉnh sửa
    $scope.currentPage = 1; // Trang hiện tại
    $scope.itemsPerPage = 10; // Số lượng sản phẩm mỗi trang
    $scope.totalItems = 0; // Tổng số sản phẩm
    $scope.totalPages = 0; // Tổng số trang
    
    $scope.pageNumbers = []; // Danh sách số trang hiển thị
    
    $scope.searchText = ''; // Văn bản tìm kiếm
    
  
    $scope.selectedItemIndex = -1; // Biến lưu trạng thái sản phẩm đang được chỉnh sửa
  
    $scope.orderByField = ""; // Khởi tạo giá trị mặc định của cột sắp xếp
    $scope.reverseSort = true; // Khởi tạo giá trị mặc định của hướng sắp xếp

    
    $scope.setOrderByField = function (field) {
        if ($scope.orderByField === field) {
          $scope.reverseSort = !$scope.reverseSort; // Đảo ngược hướng sắp xếp nếu cùng một cột được nhấp liên tiếp
        } else {
          $scope.orderByField = field;
          $scope.reverseSort = true; // Đặt hướng sắp xếp mặc định khi chọn một cột mới
        }
      };
    

      $scope.$watch('searchText', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.load_all(); // Tải lại dữ liệu với bộ lọc tìm kiếm mới
        }
    });
    
      $scope.filterItems = function() {
        if (!$scope.searchText) {
            return $scope.items; // Trả về tất cả sản phẩm nếu không có văn bản tìm kiếm
        }
    
        const searchText = $scope.searchText.toLowerCase();
    
        return $scope.items.filter(item => {
            // Kiểm tra sự trùng khớp trong tất cả các thuộc tính của sản phẩm
            const propertiesToSearch = [ 'product.name', 'priceImport', 'quantityImport', 'amountImport',   'description'];
            for (const prop of propertiesToSearch) {
                if (item[prop] && item[prop].toString().toLowerCase().includes(searchText)) {
                    return true;
                }
            }
            return false; // Không có sự trùng khớp trong bất kỳ thuộc tính nào
        });
    };

    
    $scope.prevPage = function () {
        if ($scope.currentPage > 1) {
          $scope.currentPage--;
        }
      };
      
      $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPages) {
          $scope.currentPage++;
        }
      };
      
      $scope.goToPage = function (pageNumber) {
        if (pageNumber >= 1 && pageNumber <= $scope.totalPages) {
          $scope.currentPage = pageNumber;
        }
      };
      $scope.calculateTotalPages = function() {
        $scope.totalPages = Math.ceil($scope.totalItems / $scope.itemsPerPage);
      
        // Tạo danh sách số trang hiển thị
        $scope.pageNumbers = [];
        for (let i = 1; i <= $scope.totalPages; i++) {
            $scope.pageNumbers.push(i);
        }
      };
      


    $scope.load_all = function(){
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;
            $scope.totalItems = resp.data.length; // Cập nhật tổng số sản phẩm
            $scope.calculateTotalPages(); // Tính toán lại số trang và số lượng sản phẩm trên trang
            $scope.filterItems(); // Áp dụng bộ lọc tìm kiếm
 
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

        
    $scope.Reset();
    
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
    

    $scope.Reset = function () {
      $scope.product = "";
      $scope.billImportProduct = "";
      $scope.priceImport = "";
      $scope.quantityImport = "";
      $scope.amountImport = "";
      $scope.description = "";
      // Đặt lại các giá trị khác tùy theo yêu cầu của bạn
  };
    
    $scope.Update = function(key){
        
        var item = {
            id: $scope.form.id,
            product: $scope.form.product,
            billImportProduct:$scope.form.billImportProduct,
            priceImport: $scope.form.priceImport,
            quantityImport: $scope.form.quantityImport,
            amountImport: $scope.form.amountImport,
            createDate: new Date(),
            description: $scope.form.description
        };
        var url = `${host}/${key}`;
        $http.put(url, item).then(resp => {
           $scope.items[$scope.key] = resp.data;
           $scope.load_all();
           Swal.fire({
			    icon: 'success',
			    title: 'Thành công',
			    text: `Cập nhật nhập sản phẩm  ` + item.product.name + ` thành công`,
			});
        }).catch(Error =>{
			Swal.fire({
			    icon: 'error',
			    title: 'Thất bại',
			    text: `Cập nhật nhập thêm sản phẩm  ` + item.product.name + ` thất bại`,
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
                text: ` Đã xóa mã sản phẩm  ${key} thành công`,
            });
        }).catch(Error =>{
            Swal.fire({
                icon: 'error',
                title: 'Thất bại',
                text: `Xóa mã sản phẩm ${key} thất bại`,
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

$scope.prevPage = function () {
    if ($scope.currentPage > 1) {
      $scope.currentPage--;
    }
  };
  
  $scope.nextPage = function () {
    if ($scope.currentPage < $scope.totalPages) {
      $scope.currentPage++;
    }
  };
  
  $scope.goToPage = function (pageNumber) {
    if (pageNumber >= 1 && pageNumber <= $scope.totalPages) {
      $scope.currentPage = pageNumber;
    }
  };
  $scope.calculateTotalPages = function () {
    $scope.totalPages = Math.ceil($scope.totalItems / $scope.itemsPerPage);
  
    // Tạo danh sách số trang hiển thị
    $scope.pageNumbers = [];
    for (let i = 1; i <= $scope.totalPages; i++) {
      $scope.pageNumbers.push(i);
    }
  };
  