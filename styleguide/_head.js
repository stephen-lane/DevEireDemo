/* jshint ignore:start */

// These will be done by PageStage in the real site
document.write('<meta http-equiv="X-UA-Compatible" content="IE=edge">');
document.write('<meta name="viewport" content="width=device-width, initial-scale=1">');

// <link href='https://fonts.googleapis.com/css?family=Roboto:400,300,100' rel='stylesheet' type='text/css'>

document.write('<link href="http://fonts.googleapis.com/css?family=Roboto:300|Monda:400" rel="stylesheet" type="text/css">');

// we use compiled CSS by default. If you'd like to switch to using browser compiled .less
// run localStorage.setItem('useLESS',true); in your console
if(localStorage.getItem('useLESS')) {

    // our non compiled main LESS
    document.write('<link rel="stylesheet/less" type="text/css" href="/assets/styles/main.less" />');
    document.write('<link rel="stylesheet/less" type="text/css" href="/assets/styles/styleguide-helpers.less" />');
    document.write('<script src="/assets/scripts/less.js"></script>');

    // styleguide CSS overrides
    document.write('<link rel="stylesheet" type="text/css" href="/styleguide.css" />');

} else {
    // our compiled CSS copied over from target via Grunt task
    document.write('<link rel="stylesheet" type="text/css" href="/assets/styles/main.min.css" />');
    document.write('<link rel="stylesheet" type="text/css" href="/assets/styles/styleguide-helpers.min.css" />');

    // styleguide CSS overrides
    document.write('<link rel="stylesheet" type="text/css" href="/styleguide.css" />');
}

// we use compiled JS by default. If you'd like to switch to using browser compiled .js using systemJS
// run localStorage.setItem('useSystemJS',true); in your console
if(localStorage.getItem('useSystemJS')) {

    // for IE9 and IE10, could call conditionally in main template
    document.write('<script src="/assets/scripts/es6-collections.js"></script>');
    document.write('<script src="/assets/scripts/system-polyfills.js"></script>');
    
    document.write('<script src="/assets/scripts/system.js"></script>');
    document.write('<script src="/assets/scripts/config.js"></script>');

    document.addEventListener("DOMContentLoaded", function(event) {
        System.config({ baseURL: '/assets/scripts' });
        System.import('main');
        System.import('bsp-template-plugin');
    });

} else {

    // for IE9 and IE10, could call conditionally in main template
    document.write('<script src="/assets/scripts/es6-collections.js"></script>');
    document.write('<script src="/assets/scripts/system-polyfills.js"></script>');
    
    document.write('<script src="/assets/scripts/main.min.js"></script>');
}
/* jshint ignore:end */





/* jshint ignore:start */
// document.write('<link rel="stylesheet/less" type="text/css" href="/assets/styles/main.less" />');
// document.write('<link href="http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300|Open+Sans:400,300,700|Yanone+Kaffeesatz:400,200,300,700" rel="stylesheet" type="text/css">');
// document.write('<script src="/assets/scripts/less.js"></script>');
// document.write('<script src="/assets/scripts/system.js"></script>');
// document.write('<script src="/assets/scripts/config.js"></script>');

// document.write('<meta http-equiv="X-UA-Compatible" content="IE=edge">');
// document.write('<meta name="viewport" content="width=device-width, initial-scale=1">');

/* jshint ignore:end */

// document.addEventListener("DOMContentLoaded", function(event) {
// 	System.config({ baseURL: '/assets/scripts' });
// 	System.import('main');
// 	System.import('bsp-template-plugin');
// });
