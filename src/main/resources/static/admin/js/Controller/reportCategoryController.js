app.controller('reportCategoryController', function ($scope, $http, urlReportCategory) {
    let host = urlReportCategory;
    $scope.form = {};
    $scope.items = [];
    $scope.currentPage = 1; // Trang hiện tại
    $scope.pageSize = 5; // Số mục trên mỗi trang
    $scope.pagedItems = []; // Dữ liệu được phân trang hiển thị trên trang hiện tại
    $scope.pageNumbers = []; // Số trang
    $scope.totalPages = 0; // Tổng số trang

    $scope.load_all = function () {
        var url = `${host}`;
        $http.get(url).then(resp => {
            $scope.items = resp.data;

            $scope.totalItems = resp.data.length;
            $scope.totalPages = Math.ceil($scope.totalItems / $scope.pageSize); // Tổng số trang

            $scope.updatePageNumbers();
            $scope.loadPage();
        }).catch(Error => {
            console.log("Error", Error);
        });
    };

    $scope.loadPage = function () {
        var startIndex = ($scope.currentPage - 1) * $scope.pageSize;
        var endIndex = startIndex + $scope.pageSize;
        $scope.pagedItems = $scope.items.slice(startIndex, endIndex);
    };

    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPages) {
            $scope.currentPage++;
            $scope.updatePageNumbers();
            $scope.loadPage();
        }
    };

    $scope.prevPage = function () {
        if ($scope.currentPage > 1) {
            $scope.currentPage--;
            $scope.updatePageNumbers();
            $scope.loadPage();
        }
    };

    $scope.updatePageNumbers = function () {
        $scope.pageNumbers = [];

        var startPage = Math.max(1, $scope.currentPage - 2);
        var endPage = Math.min($scope.totalPages, $scope.currentPage + 2);

        for (var i = startPage; i <= endPage; i++) {
            $scope.pageNumbers.push(i);
        }

        if (startPage > 1) {
            $scope.pageNumbers.unshift('...');
        }
        if (endPage < $scope.totalPages) {
            $scope.pageNumbers.push('...');
        }
    };

    $scope.goToPage = function (pageNumber) {
        if (pageNumber === '...' || $scope.currentPage === pageNumber) {
            return;
        }
        $scope.currentPage = pageNumber;
        $scope.updatePageNumbers();
        $scope.loadPage();
    };

    $scope.load_all();
});