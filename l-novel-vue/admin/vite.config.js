import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
// https://www.cnblogs.com/web-learn/p/15670257.html
export default defineConfig({
    plugins: [vue(), ElementPlusResolver()], // 配置需要使用的插件列表
    resolve: {
        alias: {}//配置别名
    },
    css: {
        // 指定传递给 css 预处理器的选项
        preprocessorOptions: {
            less: {
                javascriptEnabled: true
            }
        }
    },
    //本地运行配置，以及反向代理配置
    server: {
        host: "0.0.0.0",
        port: 9999,
        https: false,// 是否启用 http 2
        cors: true,// 为开发服务器配置 CORS , 默认启用并允许任何源
        open: true,// 服务启动时自动在浏览器中打开应用
        strictPort: false, // 设为true时端口被占用则直接退出，不会尝试下一个可用端口
        force: true,// 是否强制依赖预构建
        hmr: false,// 禁用或配置 HMR 连接
        // 传递给 chockidar 的文件系统监视器选项
        watch: {
            ignored: ["!**/node_modules/your-package-name/**"]
        },
        // 反向代理配置
        proxy: {
            '/api': {
                target: "http://127.0.0.1:8080/lnovel",
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, '')
            }
        }
    },
})
