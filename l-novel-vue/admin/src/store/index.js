import { createStore } from 'vuex'
import Cookies from 'js-cookie'

const store = createStore({
    state () {
        return {
            token: '' || Cookies.get('l-token'),
            loading: false
        }
    },
    mutations: {
        setToken (state, token) {
            state.token = token
            Cookies.set('l-token', token)
        },
        removeToken (state) {
            state.token = ''
            Cookies.remove('l-token')
        }
    }
})

export default store
