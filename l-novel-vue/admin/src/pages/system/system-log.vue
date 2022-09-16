<template>
  <div>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="id" label="id"/>
      <el-table-column prop="optModule" label="optModule"/>
      <el-table-column prop="optType" label="AddressAddress"/>
      <el-table-column prop="createTime" label="createTime"/>
    </el-table>
    <el-pagination
        background
        layout="prev, pager, next"
        :current-page="current"
        :total="total"
        :page-size="pageSize"/>
  </div>
</template>

<script>
export default {
  name: "system-log",
  data() {
    return {
      tableData: [],
      pageSize: 10,
      total: 0,
      current: 1
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData(query) {
      let params = {
        current: this.current,
        size: this.pageSize
      }
      this.$api.operationLog.pageSearch(params).then(res => {
        if (res.code === 0) {
          this.tableData = res.data.records
        } else {
          this.$message.error(res.userMsg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
