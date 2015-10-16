module.exports = function(grunt) {

    'use strict';

    require('bsp-grunt')(grunt, {
        bsp: {
            brightspotBase: {
                enable: true
            },

            styles: {
                dir: 'assets/styles',
                less: [
                    ' *.less',
                    "cms/jsg-cms.less"
                ],
                autoprefixer: true
            },

            scripts: {
                dir: 'assets/scripts'
            },

            bower: {

                'bootstrap': [
                    {
                        cwd: 'less/',
                        src: '**/*',
                        dest: 'bower/bootstrap',
                        expand: true,
                        flatten: false
                    },
                    {
                        cwd: 'js/',
                        src: '**/*',
                        dest: 'bower/bootstrap',
                        expand: true,
                        flatten: false
                    }
                ],

                'bsp-carousel': [
                    {
                        cwd: 'dist/bsp-carousel/',
                        src: 'bsp-carousel.css',
                        dest: '../styles/bower/bsp-carousel',
                        expand: true
                    },
                    {
                        cwd: 'dist/bsp-carousel/',
                        src: '*.js',
                        dest: '', //root of scripts
                        expand: true
                    },
                    {
                        cwd: 'dist/bsp-carousel-thumbnav/',
                        src: '*.js',
                        dest: '', //root of scripts
                        expand: true
                    },
                    {
                        cwd: 'src/less/bsp-carousel-gallery/',
                        src: '*.less',
                        dest: '../styles/bower/bsp-carousel-gallery',
                        expand: true
                    }
                ],

                'bsp-tabber': [
                    {
                        src: 'src/css/bsp-tabber.css',
                        dest: '../styles/bower/bsp-tabber.css'
                    },
                    {
                        cwd: 'src/js/',
                        src: '*.js',
                        dest: '',
                        expand: true
                    }
                ],

                'datetimepicker': [
                    {
                        src: 'jquery.datetimepicker.js',
                        dest: 'jquery.datetimepicker.js'
                    },
                    {
                        src: 'jquery.datetimepicker.css',
                        dest: '../styles/bower/jquery.datetimepicker.css'
                    }
                ],

                'es6-collections': [
                    {
                        src: 'es6-collections.js',
                        dest: 'es6-collections.js'
                    }
                ],

                'systemjs': [
                    {
                        src: 'dist/system.js',
                        dest: 'system.js'
                    },
                    {
                        src: 'dist/system-polyfills.js',
                        dest: 'system-polyfills.js'
                    }
                ],

                'fontawesome': [
                    {
                        src: 'less/*',
                        dest: '../styles/bower/fontawesome',
                        expand: true,
                        flatten: true
                    },
                    {
                        src: 'fonts/*',
                        dest: '../fonts',
                        expand: true,
                        flatten: true
                    }
                ],
            }
        },

        'systemjs': {
            'cms': {
                options: {
                  configFile: '<%= bsp.systemjs.configFile %>'
                },
                files: [
                  { '<%= bsp.scripts.minDir %>/main-cms.min.js': '<%= bsp.scripts.devDir %>/main-cms.js' }
                ]
            }
        }
    });

};
