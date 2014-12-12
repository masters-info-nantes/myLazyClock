/* global google */

angular.module('locompleter', [])
  .factory('googleMaps', ['$window',
    function ($window) {
      return {
        init: function (callback) {
          // Don't init twice
          if (!$window.google) {
            $window.onInit = callback; // Google maps calls `onInit` when loaded

            var script = document.createElement('script');

            script.type = 'text/javascript';
            script.src = 'https://maps.googleapis.com/maps/api/js?libraries=places&sensor=false&callback=onInit';

            document.body.appendChild(script);
          } else {
            callback();
          }
        }
      };
    }
  ])
  .directive('autocompleteLocation', function () {
    return {
      restrict: 'A',
      controller: ['$element', '$scope', 'googleMaps',
        function ($element, $scope, googleMaps) {
          googleMaps.init(function onReady() {
            var autocomplete = new google.maps.places.Autocomplete($element[0], {});

            autocomplete.addListener('place_changed', function () {
              var placeData = autocomplete.getPlace();

              var city;
              var country;

              placeData.address_components.forEach(function (component) {
                if (component.types[0] === 'locality') {
                  city = component.long_name;
                } else if (component.types[0] === 'country') {
                  country = component.long_name;
                }
              });

              $scope.$emit('locationAutocompleted', {
                latitude: placeData.geometry.location.lat(),
                longitude: placeData.geometry.location.lng(),
                city: city,
                country: country
              });

              // Force view model to update after autocompletion
              $element.triggerHandler('input');
            });
          });
        }
      ]
    };
  });