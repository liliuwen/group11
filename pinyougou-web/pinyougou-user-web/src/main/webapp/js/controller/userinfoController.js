app.controller('userinfoController', function($scope,$controller,baseService,$filter){

    $controller('indexController',{$scope:$scope});

    //初始化值
    $scope.userInfo = {};

    //选择职业
    $scope.profession = {
        id:null,
        opt:[{id:1,name:'程序员'},{id:2,name:'产品经理'},{id:3,name:'UI设计师'}]
    };

    //选择所在地
    $scope.Local1 = {id:null,opt:[]};
    $scope.Local2 = {d:null,opt:[]};
    $scope.Local3 = {id:null,opt:[]};

    //回显所在地的ID
    $scope.LocalJson = {
        province :null,
        city:null,
        area:null
    };

    //获取同户信息
    $scope.getUserInfo = function () {
        baseService.sendGet("http://user.pinyougou.com/user/info/get?username="+$scope.loginName)
            .then(function (resp) {
                if (resp.data){
                    //alert(JSON.stringify(resp.data));
                    $scope.userInfo = resp.data;

                    if (resp.data.address){
                        $scope.userInfo.address = resp.data.address;
                    }else{
                        $scope.userInfo.address = $scope.LocalJson;
                    }
                    //$scope.userInfo.address = JSON.parse(resp.data.address);
                    // 格式化日期
                    $scope.userInfo.birthday =  $filter('date')($scope.userInfo.birthday, "yyyy-MM-dd");
                    return $scope.userInfo.headPic;
                }
            });

    };

    //更新资料
    $scope.sendApply = function () {
        $scope.userInfo = JSON.stringify($scope.userInfo);
       baseService.sendPost("http://user.pinyougou.com/user/info/sendApply",$scope.userInfo)
           .then(function (resp) {
               if (resp.data) {
                   alert("修改成功");
                   location.reload(true);
               }else{
                   alert("修改失败");
               }
           }) ;
    };

    $scope.$watch('userInfo',function (newVal, oldVal) {
        if (newVal) {
            //回显职业
            $scope.userInfo.profession = JSON.parse(newVal.profession);
            //回显所在地
            $scope.LocalJson = JSON.parse(newVal.address);
            $scope.Local1.id = $scope.LocalJson.province;
            $scope.Local2.id = $scope.LocalJson.city;
            $scope.Local3.id = $scope.LocalJson.area;
        }
    });

    $scope.$watch('LocalJson',function (newVal, oldVal) {
        if (newVal) {
            $scope.userInfo.address = newVal;
        }
    });

    //加载省份选项
    $scope.getProvince = function () {
       baseService.sendGet("http://user.pinyougou.com/user/info/getProvince")
           .then(function (resp) {
               if (resp.data) {
                   $scope.Local1.opt = resp.data;
               }
           }) ;
    };

    $scope.$watch('Local1.id',function (newVal, oldVal) {
        if (newVal) {
            $scope.LocalJson.province = newVal;
            baseService.sendGet("http://user.pinyougou.com/user/info/getCitys?provinceId=" + newVal)
                .then(function (resp) {
                    if (resp.data) {
                        $scope.Local2.opt = resp.data;
                    }
                });
        }
    });

    $scope.$watch('Local2.id',function (newVal, oldVal) {
        if (newVal) {
            $scope.LocalJson.city = newVal;
            baseService.sendGet("http://user.pinyougou.com/user/info/getAreas?cityId=" + newVal)
                .then(function (resp) {
                    if (resp.data) {
                        $scope.Local3.opt = resp.data;
                    }
                });
        }
    });

    $scope.$watch('Local3.id',function (newVal, oldVal) {
        $scope.LocalJson.area = newVal;
    });

    $scope.uploadPic = {};

    /**上传图片 */
    $scope.uploadFile = function(){
        baseService.uploadFile().then(function(response) {
            /** 如果上传成功，取出url */
            if(response.data.status == 200){
                /** 设置图片访问地址 */
               $scope.uploadPic.headPic = response.data.url;
               $scope.uploadPic.id = $scope.userInfo.id;
               baseService.sendPost("http://user.pinyougou.com/user/info/sendApply",$scope.uploadPic)
                   .then(function (resp) {
                       alert("上传成功！");
                       $scope.userInfo.headPic = $scope.uploadPic.headPic;
                   });
            }else{
                alert("上传失败！");
            }
        });
    };

});