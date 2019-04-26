/** 定义控制器层 */
app.controller('indexController', function($scope, baseService){

    $scope.uname = "";
    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
                $scope.showHeadAndNickname();
                $scope.getUserInfo();
                $scope.uname = $scope.loginName;
            });
    };

    $scope.showHeadAndNickname = function () {
        baseService.sendGet("/user/info/getHead?username="+$scope.loginName)
            .then(function (resp) {
                if (resp.data) {
                    $scope.headPic = resp.data.headPic;
                    $scope.nickName = resp.data.nickName;
                }
            });
    };

});