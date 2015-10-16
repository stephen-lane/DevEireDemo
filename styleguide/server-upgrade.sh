BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd)"

# clean up bower/npm
# nuking the whole npm folder is not necessary and would
# slow down the update significantly
rm -rf $BASE_DIR/bower_components $BASE_DIR/node_modules/brightspot-base

# run bower/npm
cd $BASE_DIR && bower install && npm install && grunt