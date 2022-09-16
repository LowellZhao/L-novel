<template>
  <div>
    <!--header-->
    <div class="header">
      <!--头部左边-->
      <div class="header_left">
        <div class="logo">
          <span style="text-align: center">L-Novel后台管理系统</span>
        </div>
        <div @click="handleLeftMenu" class="opt_bar">
          <el-icon>
            <Operation/>
          </el-icon>
        </div>
      </div>
      <!--头部右边-->
      <div class="header_right">

      </div>
    </div>
    <!--菜单和内容-->
    <div class="menu_content">
      <!--菜单按钮导航-->
      <div class="menu" :style="{width: menuWidth + 'px'}">
        <div class="menu_page_bottom is-scroll-left">
          <el-menu
              :default-active="$route.path"
              :collapse="isCollapse"
              :router="true"
              background-color="#ccc"
          >
            <template v-for="item in menus">
              <el-menu-item :index="item.link" v-if="item.children.length === 0">
                <span>{{ item.name }}</span>
              </el-menu-item>

              <el-sub-menu :index="item.link" v-if="item.children.length > 0">
                <template #title>
                  <span>{{ item.name }}</span>
                </template>
                <el-menu-item-group>
                  <el-menu-item :index="c.link" v-for="c in item.children">
                    {{ c.name }}
                  </el-menu-item>
                </el-menu-item-group>
              </el-sub-menu>
            </template>
          </el-menu>
        </div>
      </div>
      <!--内容页-->
      <div class="content el-scrollbar" :style="{left: menuWidth + 'px'}">
        <!--导航面包屑-->
        <div class="breadcrumb">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item
                v-for='(name,index) in matchedArr'
                :key='index'
            >
              {{ name }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <!--内容-->
        <div class="content_content">
          <router-view/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import menu from "./menu";
import {mapState} from "vuex";

export default {
  name: "layout",
  data() {
    return {
      isCollapse: false,
      menuWidth: 256
    }
  },
  methods: {
    handleLeftMenu() {
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.menuWidth = 80
      } else {
        this.menuWidth = 256
      }
    },
    filterMenu() {
      return this.getMenu(menu)
    },
    getMenu(menu) {
      function findMenuTree(target, menus) {
        if (!Array.isArray(target)) {
          return []
        }
        const re = []
        for (const menu of menus) {
          // 子级没有权限时，不添加父级
          if (menu.children?.length) {
            const child = findMenuTree(target, menu.children)
            if (child.length > 0) {
              menu.children = child
              re.push(menu)
            }
          } else {
            re.push(menu)
          }
        }
        return re
      }

      return findMenuTree(menu, JSON.parse(JSON.stringify(menu)))
    },
  },
  computed: {
    ...mapState({
      menus: function (state) {
        return this.filterMenu(state)
      }
    }),
    matchedArr() {
      let temp = [], temps = [];
      this.$route.matched.filter((item) => {
        if (item.meta.title) {
          const name = item.meta.title;
          temp.push(name);
        }
      });
      temp.filter((item) => {
        if (!temps.includes(item)) {
          temps.push(item);
        }
      })
      return temps;
    }
  }
}
</script>

<style scoped lang='less'>
.header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 29;
  transition: width .2s;
  justify-content: space-between;
  height: 60px;
  box-sizing: border-box;
  background: #fff;
  width: 100%;
  display: flex;

  .header_left {
    display: flex;
    align-items: center;

    .logo {
      height: 60px;
      width: 256px;
      text-align: center;
      align-items: center;
      justify-content: space-around;
      display: flex;
    }

    .opt_bar {
      width: 30px;
      text-align: center;
      align-items: center;
      line-height: 60px;
      height: 60px;
      font-size: 20px;
    }
  }

  .header_right {

  }
}

.menu_content {
  position: fixed;
  display: flex;
  flex: auto;
  top: 60px;
  right: 0;
  z-index: 29;
  transition: width .2s;
  justify-content: space-between;
  box-sizing: border-box;
  background: #f0f2f5;
  width: 100%;
  flex-direction: row;

  .menu {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    background-color: #ccc;

    .menu_page_bottom {
      width: 100%;
      overflow-x: hidden;
      overflow-y: scroll;
      flex: 1;
      margin-top: 3px;
      z-index: 10;
      box-shadow: 0 0 10px 0 rgba(230, 224, 224, 0.5)
    }
  }

  .content {
    overflow: auto;
    right: 0;
    top: 0;
    bottom: 0;
    background: #F6F7FC;
    flex: 1;

    .breadcrumb {
      height: 30px;
      line-height: 30px;
      text-align: center;
      align-items: center;
      display: flex;
      margin-left: 20px;
      background-color: #ddd;
    }

    .content_content {
      position: relative;
      margin: 20px;
      width: 100%;
      background: greenyellow;
      overflow-x: hidden;
    }
  }
}


</style>
