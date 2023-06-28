(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();


    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({ scrollTop: 0 }, 1500, 'easeInOutExpo');
        return false;
    });


    // Sidebar Toggler
    $('.sidebar-toggler').click(function () {
        $('.sidebar, .content').toggleClass("open");
        return false;
    });


    // Progress Bar
    $('.pg-bar').waypoint(function () {
        $('.progress .progress-bar').each(function () {
            $(this).css("width", $(this).attr("aria-valuenow") + '%');
        });
    }, { offset: '80%' });


    // Calender
    $('#calender').datetimepicker({
        inline: true,
        format: 'L'
    });


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        items: 1,
        dots: true,
        loop: true,
        nav: false
    });
    // thong bao
    function notification() {
        var message = $('.notification').text();
        if (message != "") {
            var type = $('.notification').data("type");
            var html = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          ${message}
        </div>`;
            var delay = 3000; // Thời gian 3 giây mặc định
            var $notification = $(html).appendTo('#notification-container');
            // Hiển thị thông báo và đặt timeout để tự động đóng sau 3s
            $notification.fadeIn('fast', function () {
                var timeout = setTimeout(function () {
                    $notification.fadeOut('slow', function () {
                        $(this).remove();
                    });
                }, delay);
                // Xóa timeout khi người dùng đóng thông báo
                $(".close").click(function () {
                    clearTimeout(timeout);
                });
            });
        }
    };


    function notificationDATA(message, type) {
        if (message != "") {
            var html = `<div class="alert alert-${type} alert-dismissible fade show" role="alert">
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          ${message}
        </div>`;
            var delay = 3000; // Thời gian 3 giây mặc định
            var $notification = $(html).appendTo('#notification-container');
            // Hiển thị thông báo và đặt timeout để tự động đóng sau 3s
            $notification.fadeIn('fast', function () {
                var timeout = setTimeout(function () {
                    $notification.fadeOut('slow', function () {
                        $(this).remove();
                    });
                }, delay);
                // Xóa timeout khi người dùng đóng thông báo
                $(".btn-close").click(function () {
                    clearTimeout(timeout);
                });
            });
        }
    };
    document.addEventListener('DOMContentLoaded', notification)


})(jQuery);

function displayImage(event) {
    var file = event.target.files[0];
    var reader = new FileReader();
    reader.onload = function (event) {
        var imageUrl = event.target.result;
        $('#image-container').html('<img src="' + imageUrl + '">');
    };
    reader.readAsDataURL(file);
    console.log("done");
}


function searchAccounts() {
    var keyword = $('#searchInput').val();
    var url = '/user/search?keyword=' + keyword;
    $.ajax({
        url: url,
        success: function (data) {
            // Cập nhật giá trị ô tìm kiếm
            $('#searchInput').val(keyword);

            // Xóa kết quả hiện tại (nếu có)
            $('#searchResults').empty();

            // Hiển thị kết quả tìm kiếm mới
            $.each(data.accounts, function (index, account) {
                var accountHtml = '<div>' + account.name + '</div>';
                $('#searchResults').append(accountHtml);
            });
        }
    });
}
