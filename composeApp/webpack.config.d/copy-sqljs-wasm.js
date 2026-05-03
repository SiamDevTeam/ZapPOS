// Merge fallback into existing resolve config instead of replacing it
config.resolve = Object.assign({}, config.resolve, {
    fallback: Object.assign({}, config.resolve && config.resolve.fallback, {
        fs: false,
        path: false,
        crypto: false,
    })
});

const CopyWebpackPlugin = require('copy-webpack-plugin');
config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [{
            from: '../../node_modules/sql.js/dist/sql-wasm.wasm',
            to: '.'
        }]
    })
);