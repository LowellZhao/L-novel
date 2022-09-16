import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import cookie from 'js-cookie'
import api from './api'
import Message from 'element-plus'
import * as Icons from "@element-plus/icons-vue"; //导入所有element icon图标

const app = createApp(App);

app.use(router);
app.use(store);
app.use(ElementPlus);

// 注册全局图标
Object.keys(Icons).forEach((key) => {
    app.component(key, Icons[key]);
});

app.config.globalProperties.$cookie = cookie
app.config.globalProperties.$api = api
app.config.globalProperties.$message = Message

app.mount('#app');
