const webpack = require('webpack');
const WebpackDevServer = require('webpack-dev-server');
const config = require('./webpack/dev');

new WebpackDevServer(webpack(config), {
  publicPath: config.output.publicPath,
  hot: true,
  inline: false,
  historyApiFallback: true,
  quiet: true,
}).listen(3000, '0.0.0.0', function (error, result) {
  if (error) {
    console.log(error, result);
  }

  console.log('Listening at http://localhost:3000!');
});
