const path = require('path');
const fs = require('fs');

config.resolve = Object.assign({}, config.resolve, {
    fallback: Object.assign({}, config.resolve && config.resolve.fallback, {
        fs: false,
        path: false,
        crypto: false,
    })
});

// sql.js is only installed for the JS target (wasmJs does not use SQLDelight).
// Skip the copy step if the wasm file is not present in this build's node_modules.
try {
    const wasmSrc = path.resolve(__dirname, '../../node_modules/sql.js/dist/sql-wasm.wasm');
    if (fs.existsSync(wasmSrc)) {
        const CopyWebpackPlugin = require('copy-webpack-plugin');
        config.plugins.push(
            new CopyWebpackPlugin({
                patterns: [{ from: wasmSrc, to: '.' }]
            })
        );

        // Suppress "Critical dependency" warnings from sql.js dynamic requires.
        config.module = Object.assign({}, config.module, {
            noParse: [/sql\.js/]
        });
    }
} catch (e) {
    console.log(e);
}