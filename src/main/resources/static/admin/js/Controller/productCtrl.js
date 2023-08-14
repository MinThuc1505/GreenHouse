app.controller("productCtrl", function ($scope, $http, urlProduct) {
  let host = urlProduct;
  $scope.form = {};
  $scope.items = {};
  $scope.sizes = [];
  $scope.materials = [];
  $scope.searchText = ""; // Văn bản tìm kiếm
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

  $scope.$watch("searchText", function (newVal, oldVal) {
    if (newVal !== oldVal) {
      $scope.load_all(); // Tải lại dữ liệu với bộ lọc tìm kiếm mới
    }
  });

  $scope.filterItems = function () {
    if (!$scope.searchText) {
      return $scope.items; // Trả về tất cả sản phẩm nếu không có văn bản tìm kiếm
    }

    const searchText = $scope.searchText.toLowerCase();

    return $scope.items.filter((item) => {
      // Kiểm tra sự trùng khớp trong tất cả các thuộc tính của sản phẩm
      const propertiesToSearch = ["name", "price", "quantity", "description"];
      for (const prop of propertiesToSearch) {
        if (
          item[prop] &&
          item[prop].toString().toLowerCase().includes(searchText)
        ) {
          return true;
        }
      }
      return false; // Không có sự trùng khớp trong bất kỳ thuộc tính nào
    });
  };

  $scope.exportToExcel = function () {
    const wb = XLSX.utils.book_new();
    
    // Chuẩn bị dữ liệu để in ra tệp Excel
    const formattedData = $scope.items.map(item => {
      console.log(item);
        return {
            'STT': item.id,
            'Tên Sản Phẩm': item.name,
            'Giá Sản Phẩm': item.price + ' VND',
            'Số Lượng': item.quantity + ' cái',
            'Trạng Thái': item.status ? 'Kinh Doanh' : 'Ngừng Kinh doanh',
            'Chất Liệu': item.material.material || "",
            'Kích Thước': item.size.size || "",
            'Mô Tả': item.description || ""
        };
    });

    const ws = XLSX.utils.json_to_sheet(formattedData);
    
    // Định dạng cột Giá Sản Phẩm và Số Lượng
    ws['!cols'] = [
        { wch: 5 },   // STT
        { wch: 50 },  // Tên Sản Phẩm
        { wch: 15 },  // Giá Sản Phẩm
        { wch: 15 },  // Số Lượng
        { wch: 20 },  // Trạng Thái
        { wch: 15 },  // Chất Liệu
        { wch: 15 },  // Kích Thước
        { wch: 40 }   // Mô Tả
    ];

    XLSX.utils.book_append_sheet(wb, ws, "Danh sách sản phẩm");
    XLSX.writeFile(wb, "danh-sach-san-pham.xlsx");
};


  $scope.load_all = function () {
    var url = `${host}`;
    $http
      .get(url)
      .then((resp) => {
        $scope.items = resp.data;
        $scope.filterItems(); // Áp dụng bộ lọc tìm kiếm

        
        $scope.pagination = {
          currentPage: 1,
          pageSize: 10,
          totalPages: 1,
          visiblePages: 5,
          pageNumbers: [],
        };
        
        $scope.updatePagination = function () {
          $scope.pagination.totalItems = resp.data.length;
          $scope.pagination.totalPages = Math.ceil($scope.pagination.totalItems / $scope.pagination.pageSize);
        
          $scope.pagination.pageNumbers = calculateVisiblePageNumbers($scope.pagination.currentPage);
        };
        
        function calculateVisiblePageNumbers(currentPage) {
          const visiblePages = $scope.pagination.visiblePages;
          const totalPages = $scope.pagination.totalPages;
        
          let startPage = Math.max(1, currentPage - Math.floor(visiblePages / 2));
          let endPage = Math.min(totalPages, startPage + visiblePages - 1);
        
          if (endPage - startPage + 1 < visiblePages) {
            startPage = Math.max(1, endPage - visiblePages + 1);
          }
        
          return Array.from({ length: endPage - startPage + 1 }, (_, index) => startPage + index);
        }
        
        $scope.loadPage = function () {
          $http.get(host + "/page", {
            params: {
              page: $scope.pagination.currentPage - 1,
              size: $scope.pagination.pageSize,
            },
          })
          .then(function (response) {
            $scope.items = response.data.content;
          });
        };
        
        $scope.nextPage = function () {
          if ($scope.pagination.currentPage < $scope.pagination.totalPages) {
            $scope.pagination.currentPage++;
            $scope.updatePagination();
            $scope.loadPage();
          }
        };
        
        $scope.prevPage = function () {
          if ($scope.pagination.currentPage > 1) {
            $scope.pagination.currentPage--;
            $scope.updatePagination();
            $scope.loadPage();
          }
        };
        
        $scope.goToPage = function (pageNumber) {
          if (pageNumber >= 1 && pageNumber <= $scope.pagination.totalPages) {
            $scope.pagination.currentPage = pageNumber;
            $scope.updatePagination();
            $scope.loadPage();
          }
        };
        
        $scope.updatePagination();
        $scope.loadPage();
        
      })
      .catch((Error) => {
        console.log("Error", Error);
      });
  };

  // Lấy dữ liệu kích thước
  $http
    .get("/rest/sizes")
    .then((resp) => {
      $scope.sizes = resp.data;
    })
    .catch((Error) => {
      console.log("Error", Error);
    });

  // Lấy dữ liệu vật liệu
  $http
    .get("/rest/materials")
    .then((resp) => {
      $scope.materials = resp.data;
    })
    .catch((Error) => {
      console.log("Error", Error);
    });

  $scope.Edit = function (key, index) {
    var url = `${host}/${key}`;
    $http
      .get(url)
      .then((resp) => {
        $scope.form = resp.data;
        $scope.selectedItemIndex = index; // Lưu chỉ số sản phẩm đang được chỉnh sửa
        displayImages(resp.data.image); // Hiển thị ảnh tương ứng cho sản phẩm đang chỉnh sửa
      })
      .catch((Error) => {
        console.log("Error", Error);
      });
  };

  $scope.Create = function () {
    $scope.errorMessages = {};

    var files = document.getElementById("fileInputCreateProduct").files;
    var item = {
      name: $scope.name || "",
      price: $scope.price || "",
      quantity: $scope.quantity || "",
      status: $scope.status || "",
      size: $scope.size,
      material: $scope.material,
      image: files[0].name || "",
      description: $scope.description,
    };

    var url = `${host}`;
    $http
      .post(url, item)
      .then((resp) => {
        console.log("Success", resp);
        $scope.load_all();
        Swal.fire({
          icon: "success",
          title: "Thành công",
          text: `Đã thêm sản phẩm ` + $scope.name,
        });
      })
      .catch((error) => {
        console.error("Error", error);
        Swal.fire({
          icon: "error",
          title: "Thất bại",
          text: `Thêm sản phẩm ` + $scope.name + ` thất bại`,
        });

        if (error.data) {
          $scope.errorMessages = error.data;
          if ($scope.errorMessages.nameExists) {
            Swal.fire({
              icon: "info",
              title: "Thông tin",
              text: $scope.errorMessages.nameExists,
            });
          }
        }
      });
  };

  $scope.Update = function (key) {
    $scope.errorMessages = {};
    var formData = new FormData();
    formData.append("image", document.getElementById("fileInput22").files[0]);
    formData.append(
      "productDTO",
      JSON.stringify({
        id: $scope.form.id,
        name: $scope.form.name,
        price: $scope.form.price || "",
        quantity: $scope.form.quantity || "",
        status: $scope.form.status || "",
        size: $scope.form.size,
        material: $scope.form.material,
        image: $scope.form.image,
        description: $scope.form.description,
      })
    );

    var url = `${host}/${key}`;
    $http
      .put(url, formData, {
        headers: { "Content-Type": undefined },
      })
      .then((resp) => {
        $scope.items[$scope.key] = resp.data;
        $scope.load_all();
        Swal.fire({
          icon: "success",
          title: "Thành công",
          text: `Cập nhật sản phẩm ${key}`,
        });
      })
      .catch((Error) => {
        Swal.fire({
          icon: "error",
          title: "Thất bại",
          text: `Cập nhật sản phẩm ${key} thất bại`,
        });
      });
  };

  $scope.Delete = function (key) {
    var url = `${host}/${key}`;
    $http
      .delete(url)
      .then((resp) => {
        $scope.load_all();
        Swal.fire({
          icon: "success",
          title: "Thành công",
          text: `Đã xóa sản phẩm ${key}`,
        });
      })
      .catch((Error) => {
        if (Error.status === 409) {
          // Kiểm tra mã trạng thái lỗi
          Swal.fire({
            icon: "error",
            title: "Thất bại",
            text: `Sản phẩm ${key} đang được sử dụng và không thể xóa.`,
          });
        } else {
          Swal.fire({
            icon: "error",
            title: "Thất bại",
            text: `Xóa sản phẩm ${key} thất bại`,
          });
        }
      });
  };

  $scope.load_all();
});

function displayImage(event) {
  var input = event.target;
  if (input.files && input.files[0]) {
    var reader = new FileReader();

    reader.onload = function (e) {
      var imageContainer = document.getElementById("uploadedImage");
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
      var imageContainer = document.getElementById("uploadedImages");
      imageContainer.src = e.target.result;
      imageContainer.style.display = "block";
    };

    reader.readAsDataURL(input.files[0]);
  }
}
