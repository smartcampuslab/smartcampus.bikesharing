angular.module('viaggia.controllers.home', [])

.controller('HomeCtrl', function ($scope, $state, $rootScope, $ionicPlatform, $timeout, $filter, $location, $ionicHistory, Config, GeoLocate, mapService, ionicMaterialMotion, ionicMaterialInk, bookmarkService) {

    $scope.tab = 0;

    var titles = ['menu_real_time_bike','menu_bookmarks'];

    Config.init().then(function () {
      $rootScope.title = Config.getAppName();
    });


    $scope.select = function(tab) {
      $scope.tab = tab;
      $rootScope.viewTitle = $filter('translate')(titles[tab]);
    }

    $scope.action = function() {
      if ($scope.tab == 1)  $rootScope.forceTutorial();
      else $scope.showMap();
    }

    $scope.go = function (state) {
        $location.path(state);
    }

})
