$(document).ready(function() {
    var geocoder;
    var map;

    $('#creaMappa').click(function () {
        var address=$('#address').val();
        console.log("initial address");
        console.log(address);
        if (address == null || address.trim() == "")
            address = "37.9759453,23.77804489999994";

        document.getElementById('address').value = '';

        geocoder = new google.maps.Geocoder();
        geocoder.geocode( { 'address': address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {

                console.log('geocoder results:');
                console.dir(results);

                var mapOptions = {
                    zoom: 15,
                    mapTypeControl: true,
                    mapTypeControlOptions: {
                        style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
                    },
                    zoomControl: true,
                    zoomControlOptions: {
                        style: google.maps.ZoomControlStyle.SMALL
                    },
                    //streetViewControl: false,
                    center: results[0].geometry.location
                };

                map = new google.maps.Map(document.getElementById('map1'), mapOptions);

                document.getElementById('lat').value = results[0].geometry.location.lat();
                document.getElementById('lng').value = results[0].geometry.location.lng();

                //map.setCenter(results[0].geometry.location);
                var marker = new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location,
                    draggable: true,
                    animation: google.maps.Animation.DROP
                });

                google.maps.event.addListener(marker, 'dragend', function() {

                    marker.setAnimation(google.maps.Animation.DROP);

                    var marker_pos = marker.getPosition();

                    console.log('Marker getPosition():');

                    document.getElementById('lat').value = marker_pos.lat();
                    document.getElementById('lng').value = marker_pos.lng();

                    console.log(document.getElementById('lat').value);
                    console.log(document.getElementById('lng').value);

                });

            } else {
                alert('Ύπηρξε πρόβλημα με το χάρτη (' + status + ')');
            }
        });

    })
        .trigger('click');

    // TODO remove this

    $('#reverseGeocoding').click(function () {

        var lat = parseFloat($('#lat').text());
        var lng = parseFloat($('#lng').text());
        var latlng = new google.maps.LatLng(lat, lng);

        geocoder.geocode({'latLng': latlng}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {

                console.log('Reverse Geocoding:');
                console.dir(results);

                $('#reverseGeocodingResult_formatted_address').text(results[0].formatted_address);

                // address components: recupero di tutti gli elementi
                console.log('Address components:');
                console.dir(results[0].address_components);

                var temp=[];
                for(var i=0; i<results[0].address_components.length; i++) {

                    console.log(results[0].address_components[i]);

                    if($.inArray(results[0].address_components[i].long_name, temp) === -1) { // evita potenziali duplicati
                        temp.push(results[0].address_components[i].long_name);
                    }
                    if($.inArray(results[0].address_components[i].short_name, temp) === -1) { // evita potenziali duplicati
                        temp.push(results[0].address_components[i].short_name);
                    }
                }

                console.log('Address components (elaborati):');
                console.dir(temp);
                $('#reverseGeocodingResult_address_components').html(temp.join('<br>'));

            } else {
                alert("Geocoder failed due to: " + status);
            }
        });
    });

});


(function(){
    var $content = $('.modal_info').detach();

    $('.open_button').on('click', function(e){
        modal.open({
            content: $content,
            width: 540,
            height: 270,
        });
        $content.addClass('modal_content');
        $('.modal, .modal_overlay').addClass('display');
        $('.open_button').addClass('load');
    });
}());

var modal = (function(){

    var $close = $('<button role="button" class="modal_close" title="Close"><span></span></button>');
    var $content = $('<div class="modal_content"/>');
    var $modal = $('<div class="modal"/>');
    var $window = $(window);

    $modal.append($content, $close);

    $close.on('click', function(e){
        $('.modal, .modal_overlay').addClass('conceal');
        $('.modal, .modal_overlay').removeClass('display');
        $('.open_button').removeClass('load');
        e.preventDefault();
        modal.close();
    });

    return {
        center: function(){
            var top = Math.max($window.height() - $modal.outerHeight(), 0) / 2;
            var left = Math.max($window.width() - $modal.outerWidth(), 0) / 2;
            $modal.css({
                top: top + $window.scrollTop(),
                left: left + $window.scrollLeft(),
            });
        },
        open: function(settings){
            $content.empty().append(settings.content);

            $modal.css({
                width: settings.width || 'auto',
                height: settings.height || 'auto'
            }).appendTo('body');

            modal.center();
            $(window).on('resize', modal.center);
        },
        close: function(){
            $content.empty();
            $modal.detach();
            $(window).off('resize', modal.center);
        }
    };
}());


$(".modal-trigger").click(function(e){
    e.preventDefault();
    dataModal = $(this).attr("data-modal");
    $("#" + dataModal).css({"display":"block"});
});

$(".close-modal, .modal-sandbox").click(function(){
    $(".modal").css({"display":"none"});
});