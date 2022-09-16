import layout from "../layout/layout.vue";

function load(component) {
    return () => import(`../pages/${component}.vue`)
}

export default [
    {
        path: '/login',
        component: load('login'),
        meta: {requireAuth: true, title: '登录'},
        children: []
    },
    {
        path: '/',
        component: layout,
        meta: {requireAuth: true, title: '首页'},
        children: [
            {
                path: '',
                component: load('index'),
                meta: {requireAuth: true, title: ''},
                children: []
            },
            {
                path: 'index',
                component: load('index'),
                meta: {requireAuth: true, title: 'index'},
                children: []
            }
        ]
    },
    {
        path: '/system',
        component: layout,
        meta: {requireAuth: true, title: 'system'},
        children: [
            {
                path: 'system-user',
                component: load('system/system-user'),
                meta: {requireAuth: true, title: 'system-user'},
                children: []
            },
            {
                path: 'system-log',
                component: load('system/system-log'),
                meta: {requireAuth: true, title: 'system-log'},
                children: []
            }
        ]
    },
    {
        path: '/crawlSource',
        component: layout,
        meta: {requireAuth: true, title: 'crawlSource'},
        children: [
            {
                path: '',
                component: load('crawlSource/index'),
                meta: {requireAuth: true, title: 'index'},
                children: []
            }
        ]
    },
    {
        path: '/book',
        component: layout,
        meta: {requireAuth: true, title: 'book'},
        children: [
            {
                path: '',
                component: load('book/index'),
                meta: {requireAuth: true, title: 'index'},
                children: []
            }
        ]
    },
    {
        path: '/categoryInfo',
        component: layout,
        meta: {requireAuth: true, title: 'categoryInfo'},
        children: [
            {
                path: '',
                component: load('categoryInfo/index'),
                meta: {requireAuth: true, title: 'index'},
                children: []
            }
        ]
    }
]
