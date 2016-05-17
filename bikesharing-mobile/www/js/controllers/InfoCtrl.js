angular.module('viaggia.controllers.info', [])

.controller('InfoCtrl', function ($scope) {})

.controller('BikeSharingCtrl', function ($scope, $rootScope, $state, $stateParams, $timeout, $filter, $ionicModal, $ionicPopup, $location, ionicMaterialMotion, ionicMaterialInk, leafletData, mapService, bikeSharingService, Config, bookmarkService) {
    $scope.parkings = null;

    $scope.markers = [];

    $scope.title = $filter('translate')('menu_real_time_bike');
    $scope.bookmarkStyle = bookmarkService.getBookmarkStyle($scope.selected);

    $scope.$on('ngLastRepeat.parkings', function (e) {
        $timeout(function () {
            ionicMaterialMotion.ripple();
            ionicMaterialInk.displayEffect()
        }); // No timeout delay necessary.
    });



    $scope.load = function (selectedId) {
        bikeSharingService.getStations($scope.agencyId).then(function (data) {
            $scope.parkings = data;
            $scope.parkings.forEach(function (e) {
                if (decodeURI(selectedId) == e.id) {
                    $scope.select(e);
                }

            });
            $scope.loading = false;
            Config.loaded();
            $scope.$broadcast('scroll.refreshComplete');
            //            if ($scope.selected) {
            //                $scope.showMap(true);
            //            }
        }, function (err) {
            $scope.parkings = null;
            $scope.$broadcast('scroll.refreshComplete');
            $scope.showNoConnection();
            $scope.loading = true;
            Config.loaded();
        });
    }

    $scope.init = function () {
      Config.init().then(function () {
        $scope.agencyId = $stateParams.agencyId;
        if (!$scope.agencyId) {
          $scope.agencyId = Config.getAgencyId();
        }
        angular.extend($scope, {
            center: {
                lat: Config.getMapPosition().lat,
                lng: Config.getMapPosition().long,
                zoom: Config.getMapPosition().zoom
            },
            markers: [],
            events: {}
        });

        $ionicModal.fromTemplateUrl('templates/mapModalBike.html', {
            id: '1',
            scope: $scope,
            backdropClickToClose: false,
            animation: 'slide-in-up'
        }).then(function (modal) {
            $scope.modalMap = modal;
        });


        $scope.loading = true;
        Config.loading();
        $scope.load($stateParams.id);
      });
    };

    $scope.selected = null;
    $scope.select = function (p, path) {
        //        if ($scope.selected == p) {
        //          $scope.selected = null;
        //        } else {
        $scope.selected = p;
        var path = $location.path();
        if ($state.current.name == 'app.bikestation') {
            path = path.substr(0, path.lastIndexOf('/'));
        }
        path += '/' + p.id;
        $scope.bookmarkStyle = bookmarkService.getBookmarkStyle(path);
        //        }
        $scope.showMap(true);
    };

    $rootScope.showMap = function (withPopup) {
        $scope.modalMap.show().then(function () {
            var markers = [];

            var list = ($scope.selected != null && withPopup) ? [$scope.selected] : $scope.parkings;
            if (list == null) list = [];
            var boundsArray = [];
            for (var i = 0; i < list.length; i++) {
                markers.push({
                    parking: list[i],
                    lat: parseFloat(list[i].position[0]),
                    lng: parseFloat(list[i].position[1]),
                    icon: {
                        iconUrl: 'img/ic_bike.png',
                        iconSize: [36, 50],
                        iconAnchor: [18, 50],
                        popupAnchor: [-0, -50]
                    },
                    //                        focus: true
                });
                boundsArray.push(list[i].position);
            }
            if (boundsArray.length > 0) {
                var bounds = L.latLngBounds(boundsArray);
                mapService.getMap('modalMapBike').then(function (map) {
                    map.fitBounds(bounds);
                });
            }
            $scope.markers = markers;
            if (withPopup) {
                showPopup(list[0]);
            }
        });
    };

    $scope.closeMap = function () {
        $scope.modalMap.hide();
    };
    $scope.initMap = function () {
        mapService.initMap('modalMapBike');
    };

    var showPopup = function (p) {
        $scope.popupParking = p;
        $scope.selected = p;

        $ionicPopup.show({
            templateUrl: 'templates/bikesharingPopup.html',
            title: $filter('translate')('lbl_bike_station'),
            cssClass: 'parking-popup',
            scope: $scope,
            buttons: [
                {
                    text: $filter('translate')('btn_close'),
                    type: 'button-close'
                }
            ]
        });

    }

    $scope.$on('leafletDirectiveMarker.modalMapBike.click', function (e, args) {
        var p = $scope.markers[args.modelName].parking;
        showPopup(p);
    });

    $scope.$on('$ionicView.beforeEnter', function () {
        mapService.refresh('modalMapBike');
    });

    $scope.navigate = function () {
        planService.setPlanConfigure({
            to: {
                name: $scope.selected.address,
                lat: $scope.selected.position[0],
                long: $scope.selected.position[1]
            },
        });
        planService.setName('to', $scope.selected.address);
        $scope.closeMap();
        $state.go('app.plan');
    };

    $scope.bookmark = function () {
        var path = $stateParams.id ? $location.path() : ($location.path() + '/' + $scope.selected.id);
        bookmarkService.toggleBookmark(path, $scope.selected.name, 'BIKESHARING', {
            agencyId: $scope.agencyId,
            parkingId: $scope.selected.id
        }).then(function (style) {
            $scope.bookmarkStyle = style;
        });
    };
})
