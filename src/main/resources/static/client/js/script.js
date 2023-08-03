/**
 * WEBSITE: https://themefisher.com
 * TWITTER: https://twitter.com/themefisher
 * FACEBOOK: https://www.facebook.com/themefisher
 * GITHUB: https://github.com/themefisher/
 */

(function ($) {
  'use strict';

  // Preloader
  $(window).on('load', function () {
    $('#preloader').fadeOut('slow', function () {
      $(this).remove();
    });
  });


  // Instagram Feed
  if (($('#instafeed').length) !== 0) {
    var accessToken = $('#instafeed').attr('data-accessToken');
    var userFeed = new Instafeed({
      get: 'user',
      resolution: 'low_resolution',
      accessToken: accessToken,
      template: '<a href="{{link}}"><img src="{{image}}" alt="instagram-image"></a>'
    });
    userFeed.run();
  }

  setTimeout(function () {
    $('.instagram-slider').slick({
      dots: false,
      speed: 300,
      // autoplay: true,
      arrows: false,
      slidesToShow: 6,
      slidesToScroll: 1,
      responsive: [{
        breakpoint: 1024,
        settings: {
          slidesToShow: 4
        }
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 3
        }
      },
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 2
        }
      }
      ]
    });
  }, 1500);


  // e-commerce touchspin
  $('input[name=\'product-quantity\']').TouchSpin();


  // Video Lightbox
  $(document).on('click', '[data-toggle="lightbox"]', function (event) {
    event.preventDefault();
    $(this).ekkoLightbox();
  });


  // Count Down JS
  $('#simple-timer').syotimer({
    year: 2022,
    month: 5,
    day: 9,
    hour: 20,
    minute: 30
  });

  //Hero Slider
  $('.hero-slider').slick({
    // autoplay: true,
    infinite: true,
    arrows: true,
    prevArrow: '<button type=\'button\' class=\'heroSliderArrow prevArrow tf-ion-chevron-left\'></button>',
    nextArrow: '<button type=\'button\' class=\'heroSliderArrow nextArrow tf-ion-chevron-right\'></button>',
    dots: true,
    autoplaySpeed: 7000,
    pauseOnFocus: false,
    pauseOnHover: false
  });
  $('.hero-slider').slickAnimation();


})(jQuery);

window.addEventListener('DOMContentLoaded', function () {
  var menu = document.querySelector('.menu');
  var top_header = document.querySelector('.top-header');
  var scrollPosition = window.pageYOffset || document.documentElement.scrollTop;

  if (scrollPosition > 300) {
    menu.classList.add('sticky');
    top_header.classList.add('hidden-for-top');
  } else {
    menu.classList.remove('sticky');
    top_header.classList.remove('hidden-for-top');
  }

  window.addEventListener('scroll', function () {
    var scrollPosition = window.pageYOffset || document.documentElement.scrollTop;

    if (scrollPosition > 300) {
      menu.classList.add('sticky');
      top_header.classList.add('hidden-for-top');
    } else {
      menu.classList.remove('sticky');
      top_header.classList.remove('hidden-for-top');
    }
  });
});

// Back to top button
$(window).scroll(function () {
  if ($(this).scrollTop() > 300) {
    $('.back-to-top').fadeIn('slow');
  } else {
    $('.back-to-top').fadeOut('slow');
  }
});
$('.back-to-top').click(function () {
  $('html, body').animate({ scrollTop: 0 }, 1500, 'swing');
  return false;
});
// thong bao
function notificationDATA(message, type) {
  if (message != "") {
    var html = '<div class="notification notification-' + type + '">' +
      '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>' +
      message +
      '</div>';
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

// lazy load
function load(img) {
  const url = img.getAttribute('lazy-src');
  img.setAttribute('src', url);
}

function lazyLoad() {
  if ('IntersectionObserver' in window) {
    var lazyImgs = document.querySelectorAll('[lazy-src]')

    let observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          load(entry.target)
        }
      })
    });

    lazyImgs.forEach(img => {
      observer.observe(img)
    })
  }
}

document.addEventListener('DOMContentLoaded', lazyLoad)

function showProductDetails(id, name, price, image, description) {
  $('#product-img').attr('src', '/images/' + image);
  $('#product-title').text(name);
  var formattedPrice = price.toLocaleString('vi', { style: 'currency', currency: 'VND' });
  $('#product-price').text(formattedPrice);
  $('#product-description').text(description);
  $('#modal-idProduct').text(id);
  // Hiển thị modal chi tiết sản phẩm
  $('#product-modal').modal('show');
}

function previewImage(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();

    reader.onload = function (e) {
      document.getElementById('profile-image-preview').src = e.target.result;
      document.getElementById('image-path').textContent = input.value;
    }

    reader.readAsDataURL(input.files[0]);
  }
}
//XỬ LÍ LOAD ẢNH
//kiếm element có id là product-image-link, khi có thay đổi thì kích hoạt
// $(document).ready(function() {
//   $('#profile-image-input').change(function() {
//     var input = this;
//     var file = input.files[0];
//     var url = $(this).val();
//     var ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();

//     if (file && (ext === "gif" || ext === "png" || ext === "jpeg" || ext === "jpg")) {
//       var reader = new FileReader();
//       reader.onload = function(e) {
//         $('#profile-image-preview').attr('src', e.target.result);
//       };
//       reader.readAsDataURL(file);
//     } else {
//       $('#profile-image-preview').attr('src', '/admin/img/hinh1.jpg');
//     }
//   });

//   $('#myForm').submit(function(e) {
//     e.preventDefault(); // Chặn hành vi mặc định của biểu mẫu

//     var form = $(this);
//     var url = form.attr('action');
//     var formData = new FormData(form[0]); // Tạo đối tượng FormData từ biểu mẫu

//     $.ajax({
//       type: 'POST',
//       url: url,
//       data: formData,
//       contentType: false, // Không thiết lập header '

//       processData: false, // Không xử lý dữ liệu FormData
//       success: function(response) {
//         // Xử lý phản hồi thành công ở đây
//         // Ví dụ: Cập nhật bảng dữ liệu trong trang với phản hồi từ máy chủ
//         $('#product-table-body').empty().html($(response).find('#product-table-body').html());
//         $('#myForm').replaceWith($(response).find('#myForm')); // Thay thế nội dung của biểu mẫu
//       },
//       error: function(xhr, status, error) {
//         // Xử lý lỗi ở đây 
//       }
//     });
//   });
// });

$('#save-account-btn').click(function (e) {
  e.preventDefault();
  var productId = $('#id').val();
  var categoryId = $("#username").val();
  var productName = $("#fullname").val();
  var productPrice = $("#password").val();
  var productQuantity = $("#address").val();
  var productDescription = $("#email").val();
  var productImageName = $('#phone').val();

  if (productId.length < 1) {
    alert('Vui lòng nhập Mã Sản Phẩm');
  }

  // if (categoryId == 'Xin vui lòng chọn Mã Loại') {
  //   alert('Vui lòng chọn Mã Loại');

  // }

  // if (productName.length < 1) {
  //   alert('Vui lòng nhập Tên Sản Phẩm');

  // }

  // if (productPrice.length < 1) {
  //   alert('Vui lòng nhập Giá Sản Phẩm');

  // }

  // if (productQuantity.length < 1) {
  //   alert('Vui lòng nhập Số Lượng (Trong Kho)');
  // }

  // $('#profile-image-input').on('error', function() {
  //   // Lỗi xảy ra khi hình ảnh không được load thành công
  //   // Thực hiện xử lý lỗi tại đây, ví dụ như đặt hình ảnh mặc định
  //   productImageName = "";
  // });

  $.ajax({
    url: '/client/profile',
    type: 'POST',
    data: {
      id: productId,
      categoryId: categoryId,
      productName: productName,
      productPrice: productPrice,
      productQuantity: productQuantity,
      productDescription: productDescription,
      productImageName: productImageName
    },
    success: function (result) {

    },
    error: function (result) {

    }
  });

});

$(document).ready(function () {
  $("a[href*=lang]").on("click", function () {
    var param = $(this).attr("href");
    $.ajax({
      url: "/client/index" + param,
      success: function () {
        location.reload();
      }
    });
    return false;
  });
});
