<template>
    <h1>Home</h1>
    <p>
        <img src="../assets/logo.png" alt="logo"/>
    </p>
    <button @click="state.count++">count is: {{ state.count }}</button>
    <el-button>I am ElButton</el-button>
</template>

<script>
    import {reactive} from 'vue'
    import { ElButton } from 'element-plus'
    export default {
        setup() {
            const state = reactive({count: 0})
            return {state}
        },
        components: { ElButton }
    }
</script>

<style scoped>
    h1,
    a {
        color: green;
    }
</style>
