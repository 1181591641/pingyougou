//控制层 
app.controller('userController', function($scope, userService, $interval) {

	$scope.reg = function() {

		userService.add($scope.entity,$scope.smscode).success(function(response) {
			alert(response.message);
		});
	}
	/**
	 * 发送验证
	 */
	$scope.canClick = false;
	$scope.description = "获取验证码";
	var second = 59;
	var timerHandler;

	$scope.getTestCode = function(phone) {
		if ($scope.canClick == false) {
			timerHandler = $interval(function() {
				if (second <= 0) {
					$interval.cancel(timerHandler);
					second = 59;
					$scope.description = "获取验证码";
					$scope.canClick = false;
				} else {

					$scope.description = second + "s后重发";
					second--;
					$scope.canClick = true;

				}
			}, 1000)
			createSmsCode(phone);
		}
	};
	// 发送验证码
	createSmsCode = function(phone) {
		userService.createSmsCode(phone).success(function() {

		});
	}

});
