var exec = require('cordova/exec');

exports.sayHello = function(success, error) {
	exec(success, error, "oauth", "say-hello", []);
};

exports.qqLogin = function(success, error) {
	exec(success, error, "oauth", "qq-login", []);
};

exports.qqLogout = function(success, error) {
	exec(success, error, "oauth", "qq-logout", []);
};

exports.getUserInfo = function(options, success, error) {
	exec(success, error, "oauth", "get-userinfo", [options]);
};

exports.qqShared = function(success, error) {
	exec(success, error, "oauth", "qq-shared", []);
};
