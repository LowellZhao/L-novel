import fetch from './fetch'

export default {
    operationLog: {
        pageSearch(params) {
            return fetch('/api/operationLog/pageSearch', params)
        }
    }
}
