import axios from 'axios'
import store from '../store'
import router from '../router'

// 超时时间
const AXIOS_DEFAULTS_TIMEOUT = 15000
axios.defaults.baseURL = ''

// type默认为form，如果要在请求体里传json，则传json，如果要上传文件，传file
function fetch(
    url = '',
    params = {},
    method = 'get',
    contentType = 'form',
    headers = {},
    responseType = 'json',
    timeout = AXIOS_DEFAULTS_TIMEOUT,
) {
    contentType === 'form' && (contentType = 'application/x-www-form-urlencoded')
    contentType === 'json' && (contentType = 'application/json')
    contentType === 'file' && (contentType = 'multipart/form-data')
    const query = Object.keys(params).map(k => {
        const val = params[k] === undefined ? '' : params[k]
        return `${k}=${val}`
    })
    let qs = query.join('&')
    if (method.toLowerCase() === 'get' && query.length > 0) {
        url += (url.indexOf('?') < 0 ? '?' : '&') + qs
    }
    if (contentType !== 'application/x-www-form-urlencoded' && method !== 'get') {
        qs = params
    }

    return new Promise((resolve, reject) => {
        axios({
            method: method,
            url: url,
            data: qs,
            headers: {
                Authorization: store.state.token,
                'Content-Type': contentType,
                ...headers
            },
            responseType,
            timeout,
        }).then(function (response) {
            if (response.status >= 200 && response.status < 400) {
                // 未登录
                if (response.data && response.data.code === 1000) {
                    store.commit('removeToken', response.data)
                    router.push('/login').then(r => {})
                    reject(new Error('invalid token'))
                    return
                }
                // 无权限
                if (response.data && response.data.code === 1002) {
                    window.alert('你没有权限进行此操作，如需开权限，请联系管理员')
                    reject(new Error('无权限'))
                    return
                }
                resolve(response.data)
            } else {
                this.$message.error(response.statusText)
            }
        }).catch(function (err) {
            if (err.message.includes('timeout')) {
                this.$message.error('请求超时，请稍后刷新重试。')
            } else {
                this.$message.error(url + '请求失败：' + err.message)
            }
            reject(err)
        })
    })
}

export default fetch
