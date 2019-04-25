/** 定义控制器层 */
app.controller('sellerController', function ($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 添加或修改 */
    $scope.saveOrUpdate = function () {

        /** 发送post请求 */
        baseService.sendPost("/seller/save", $scope.seller)
            .then(function (response) {
                if (response.data) {
                    /** 跳转到登录页面 */
                    location.href = "/shoplogin.html";
                } else {
                    alert("操作失败！");
                }
            });


    };


    $scope.update = function () {
        /** 发送post请求 */
        baseService.sendPost("/seller/update", $scope.seller).then(function (response) {
            if (response.data) {
                alert("修改成功！");
                $scope.show();
            } else {
                alert("操作失败！");
            }
        });
    }


    /** 显示修改 */
    $scope.show = function (entity) {
        baseService.sendGet("/seller/findOne").then(function (response) {
            $scope.seller = response.data;
            /** 把json对象转化成一个新的json对象 */
            $scope.seller = JSON.parse(JSON.stringify(seller));
        })
    };


    //查询原密码
    $scope.findOldPassword = function () {
        baseService.sendPost("/seller/findOldPassword?oldPassword=" + $scope.oldPassword ).then(function (response) {
            if(!response.data){
                alert("原密码不正确");
            }
        });
    };

    //保存密码
    $scope.updatePassword = function () {
        //确认密码
        if ($scope.confirmNewPassword && $scope.newPassword == $scope.confirmNewPassword){
            baseService.sendPost("/seller/updatePassword?newPassword=" + $scope.newPassword).then(function (response) {
                if(response.data){
                    location.href = ("/shoplogin.html");
                }else {
                    alert("修改失败");
                }
            });
        }else {
            alert("密码不一致，请重新输入！");
            return;
        }
    };

    //清空
    $scope.clear = function () {
        $scope.oldPassword = "";
        $scope.newPassword = "";
        $scope.confirmNewPassword = "";

    };


    /** 查询条件对象 */
    $scope.searchEntity = {};

    /** 分页查询(查询条件) */
    $scope.search = function (page, rows) {
        baseService.findByPage("/seller/findByPage", page,
            rows, $scope.searchEntity)
            .then(function (response) {
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/seller/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        /** 重新加载数据 */
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        } else {
            alert("请选择要删除的记录！");
        }
    };
});