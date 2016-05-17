angular.module('viaggia.services.bookmarks', [])

.factory('bookmarkService', function ($q, $rootScope, Config, bikeSharingService) {
    var repo = '';
    Config.init().then(function () {
        repo = Config.getAppId() + '_bookmarks';
    });

    var getStoredBookmarks = function () {
        var value = localStorage.getItem(repo);
        if (!value) {
            value = [];
        } else {
            value = JSON.parse(value);
        }
        return value;
    };
    var getDefaultBookmarks = function () {
        return [];
    };
    var getBookmarks = function () {
        if (!localStorage.getItem(repo)) {
            var defList = getDefaultBookmarks();
            defList.forEach(function (e) {
                e.home = true;
            });
            localStorage.setItem(repo, JSON.stringify(defList));
        }
        return getStoredBookmarks();
    };

    /**
     * Custom template for specific bookmark type
     */
    $rootScope.getBookmarkItemTemplate = function (type) {
            return 'templates/bm/bikesharing.html';
    }


    var updateRT = function (b) {
      bikeSharingService.getStation(b.data.agencyId, b.data.parkingId).then(function (p) {
          b.parking = p;
      });
    }

    return {
        /**
         * add bookmark to the list. Return promise of the update bookmark list
         */
        addBookmark: function (bm) {
            var deferred = $q.defer();

            var list = getBookmarks();
            bm.home = true;
            bm.removable = true;
            list.splice(0, 0, bm);
            localStorage.setItem(repo, JSON.stringify(list));
            deferred.resolve(list);

            Config.log({action:'personalize',subaction: 'add'});

            return deferred.promise;
        },
        /**
         * Return promise of current list of bookmarks with real time data.
         */
        getBookmarksRT: function () {
            var deferred = $q.defer();
            var list = getBookmarks();
            var filtered = [];
            list.forEach(function (b) {
                updateRT(b);
            });
            deferred.resolve(list);
            return deferred.promise;
        },
        /**
         * Return promise of current list of bookmarks. Initially, set to the list of predefined bookmarks from the configuration (cannot be removed permanently).
         */
        getBookmarks: function () {
            var deferred = $q.defer();
            deferred.resolve(getBookmarks());
            return deferred.promise;
        },
        /**
         * Return position of the bookmark with the specified path in the list.
         */
        indexOfBookmark: function (bm) {
            var list = getBookmarks();
            for (var i = 0; i < list.length; i++) {
                if (bm == list[i].state) return i;
            }
            return -1;
        },
        /**
         * Remove the bookmark at the specified index from the list (if possible). Return promise of the update bookmark list
         */
        removeBookmark: function (idx) {
            var deferred = $q.defer();
            var list = getBookmarks();
            if (list.length > idx && list[idx].removable) {
                list.splice(idx, 1);
                localStorage.setItem(repo, JSON.stringify(list));
            }
            deferred.resolve(list);

            Config.log({action:'personalize',subaction: 'remove'});
            return deferred.promise;
        },
        /**
         * Change order of two bookmarks. Return promise of the update bookmark list
         */
        reorderBookmark: function (idxFrom, idxTo) {
            var deferred = $q.defer();

            var list = getBookmarks();
            var from = list[idxFrom];
            if (idxTo + 1 == list.length) {
                list.push(from); //add to from
            } else {
                list.splice(idxTo, 0, from); //add to from
            }
            if (idxFrom > idxTo) { //remove from
                list.splice(idxFrom + 1, 1);
            } else {
                list.splice(idxFrom, 1);
            }
            localStorage.setItem(repo, JSON.stringify(list));
            deferred.resolve(list);

            return deferred.promise;
        },
        /**
         * Return the style of the bookmark button for the element
         */
        getBookmarkStyle: function (bm) {
            return this.indexOfBookmark(bm) >= 0 ? 'ion-ios-star' : 'ion-ios-star-outline';
        },
        /**
         * Add/remove a bookmark for the element of the specified type, path, and title. Returns promise for the update style.
         */
        toggleBookmark: function (path, title, type, data) {
            var deferred = $q.defer();
            var pos = this.indexOfBookmark(path);
            if (pos >= 0) {
                this.removeBookmark(pos).then(function () {
                    deferred.resolve('ion-ios-star-outline');
                });
            } else {
                var color = null,
                    icon = null;

                var ct = Config.getColorsTypes()['BIKESHARING'];
                color = ct.color;
                icon = 'ic_m_bike';

                this.addBookmark({
                    "state": path,
                    "label": title,
                    "icon": icon,
                    "color": color,
                    type: type,
                    data: data
                }).then(function () {
                    deferred.resolve('ion-ios-star');
                });
            }
            return deferred.promise;
        }
    };
})
