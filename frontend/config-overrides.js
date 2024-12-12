const path = require('path');

module.exports = function override(config, env) {
    if (env === 'development') {
      config.devtool = 'cheap-module-source-map';
    }
  
    config.resolve = {
        ...config.resolve, 
        alias: {
        ...config.resolve.alias,
        '@components': path.resolve(__dirname, './src/components/'),
        '@shared': path.resolve(__dirname, './src/shared/'),
        '@assets': path.resolve(__dirname, './src/assets/'),
        },
    };

    config.module.rules.push({
      test: /\.md$/,
      use: 'raw-loader',
    });
  
    return config;
  };
  