DevEire Demo
===============

This is a Demo Site using Brightspot CMS 3.1

What's included
---------------
*	Reusable [LESS CSS](http://lesscss.org/) (based on [Twitter Bootstrap](http://getbootstrap.com/)) and Javascript components with accompanying [Handlebars templates](http://handlebarsjs.com)
*	Preconfigured Grunt build which compiles LESS and transpiles Javascript [ECMAScript 6 modules](http://www.2ality.com/2014/09/es6-modules-final.html) using [SystemJS](https://github.com/systemjs/systemjs) and [Babel](https://babeljs.io/)
*	Preconfigured [Karma](http://karma-runner.github.io/)/[Jasmine](http://jasmine.github.io/) test runner
*	[Preconfigured component style guide](styleguide/) with a local [Express](http://expressjs.com) server which allows development of front end components without a running Brightspot instance

How to use it
-------------
*   Import project into preferred IDE
*   Build Project (Dont run as of yet)
*   Open console/terminal
*   Navigate to the root of this project. E.g cd /Users/{username}/Documents/github/DevEireDemo
*   Run this from the command line - ./node/node styleguide/server.js (Note - by default, the project will occupy port 3000, this can be changed by entering ./node/node styleguide/server.js --port=3001 at runtime. You can have multiple projects running at any one time)
*   The styleguide will then be accessible at http://localhost:3000.

System requirements
-------------------
*	[Java](https://java.com) and [Maven](https://maven.apache.org/). See [Brightspot documentation](http://www.brightspot.com/docs/3.0/overview/installation) for more specific information about which versions to install.
*	[NodeJS](https://nodejs.org)
*	Karma test runner (run `npm install -g karma` after NodeJS is installed)

Useful Info
---------------
*   Handlebars Syntax example - http://handlebarsjs.com/expressions.html
*   ES6 Example - https://github.com/JustinDrake/node-es6-examples

PSD Projects using handlebars
----------------
*   Jordan Speith - https://github.com/perfectsense/jordanspieth
*   BSP Base - https://github.com/perfectsense/brightspot-base
