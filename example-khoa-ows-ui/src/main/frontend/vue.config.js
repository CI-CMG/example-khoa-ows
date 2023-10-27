module.exports = {
  outputDir: 'dist',
  publicPath: process.env.NODE_ENV === 'production' ? '@contextRoot@/' : `${process.env.VUE_APP_BASE_URL}/`,
  configureWebpack: {
    devtool: process.env.NODE_ENV === 'production' ? 'source-map' : 'eval-source-map',
  },
  devServer: {
    hotOnly: true,
    proxy: {
      '^/ows/api': {
        target: process.env.PROXY,
        secure: false,
      },
      '^/ows/docs': {
        target: process.env.PROXY,
        secure: false,
      },
    },
  },
};
