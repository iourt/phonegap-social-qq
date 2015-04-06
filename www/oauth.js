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

exports.sharedToQzone = function(options, success, error) {
	exec(success, error, "oauth", "shared-qzone", [options]);
};

exports.sharedToFriend = function(options, success, error) {
	exec(success, error, "oauth", "shared-friend", [options]);
};
