module.exports = {
  isSetAsLauncher: function (callback) {
    if(/ios|iphone|ipod|ipad/i.test(navigator.userAgent)) {
      callback(false); // ios not supported - cannot be in kiosk
      return;
    }
    cordova.exec(function (out) {
      callback(out == "true");
    }, function (error) {
      alert("Launcher.isSetAsLauncher failed: " + error);
    }, "Launcher", "isSetAsLauncher", []);
  },
  exitLauncher: function () {
	cordova.exec(function () {}, function (error) {
		alert("Launcher.exitLauncher failed: " + error);
	}, "Launcher", "exitLauncher", []);
  },
  selectLauncher: function () {
	cordova.exec(function () {}, function (error) {
		alert("Launcher.selectLauncher failed: " + error);
	}, "Launcher", "selectLauncher", []);
  },
  removeLauncher: function () {
	cordova.exec(function () {}, function (error) {
		alert("Launcher.removeLauncher failed: " + error);
	}, "Launcher", "removeLauncher", []);
  }
};
