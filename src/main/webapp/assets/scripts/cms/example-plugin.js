/**
 * Example plugin
 * 
 * Usage:
 * load the CMS javascript:
 * <script src="/assets/scripts/main-cms.min.js"></script>
 *
 * Call by specifying the data-jsgcms-example attribute:
 * <div data-jsgcms-example></div>
 */

/**
 * Jquery, bsp-utils, and others are dropped into the script root folder
 * by the grunt bower task on build. This saves a lot of work having to
 * resolve paths in the config file.
 */
import bsp_utils from 'bsp-utils';
import $ from 'jquery';

/**
 * Import paths are relative to the script root, not the cms folder.
 * The convention is to point to paths this way, though it is possible
 * to point to files like this too:
 *
 * import Example from './example';
 *
 * You could also just keep everything contained in the plugin
 * file for something small by specifying a class or an object 
 * literal inside the plugin file. We have been following the 
 * pattern of separating utilities and plugins so that most
 * functionality will be reusable.
 */
import Example from 'cms/example';


export default bsp_utils.plugin(false, 'jsgcms', 'example', {
    '_each': function(item) {
    	/**
    	 * This example uses an ES6 class, but you can also use
    	 * an object literal as older plugins did, just note the
    	 * slightly different ES6 syntax for specifying methods on 
    	 * an object literal. ES6 classes are preferred.
    	 * 
		 * Example:
		 * var Plugin = {
		 *   init($el, options) {
		 *     // do stuff
		 *   }
		 * }
		 * Object.create(Plugin).init($el, options);
		 */
		var $el =  $(item);
		var options = this.option(item);
        new Example( $el, options );
    }
});