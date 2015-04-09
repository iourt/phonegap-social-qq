var exec = require('cordova/exec');

exports.sayHello = function(success, error) {
	exec(success, error, "qq", "say-hello", []);
};

exports.qqLogin = function(options, success, error) {
	exec(success, error, "qq", "qq-login", [options]);
};

exports.qqLogout = function(success, error) {
	exec(success, error, "qq", "qq-logout", []);
};

exports.sharedToQzone = function(options, success, error) {
	exec(success, error, "qq", "shared-qzone", [options]);
};

exports.sharedToFriend = function(options, success, error) {
	exec(success, error, "qq", "shared-friend", [options]);
};
