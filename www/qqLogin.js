var exec = require('cordova/exec');

exports = module.exports = {
    ssoLogin: function(onSuccess,onFail) {
        exec(function(res){
        	onSuccess(res);
        },onFail, "QQLogin", "ssoLogin", []);
    },
    ssoLogout: function(onSuccess,onFail) {
        exec(function(res){
        	onSuccess(res);
        },onFail, "QQLogin", "ssoLogout", []);
    }
};
