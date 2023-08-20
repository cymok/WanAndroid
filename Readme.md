# WanAndroid 传统 Kotlin 版

## 项目架构

MVVM 架构, 基于 Kotlin + JetPack 组件: Lifecycle + LiveData + ViewModel + Paging

## 功能及技术点

- [x] 网络, [OkHttp3](https://github.com/square/okhttp) + [Retrofit2](https://github.com/square/retrofit) + Coroutine, [PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar)

- [x] 图片, [Glide](https://github.com/bumptech/glide)

- [x] 消息, [LiveEventBus](https://github.com/JeremyLiao/LiveEventBus)

- [x] 权限, [PermissionX](https://github.com/guolindev/PermissionX)

- [x] 首页导航, 使用 TabLayout + ViewPager2 + Fragment, 自定义 TabItem, 再次点击 Tab 刷新页面

- [x] DrawerLayout 侧滑导航

- [x] 状态栏沉浸, [ImmersionBar](https://github.com/gyf-dev/ImmersionBar)

- [x] Banner, [banner](https://github.com/youth5201314/banner)

- [x] 列表加载, Paging 组件

- [x] 列表刷新, ~~[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)~~ 原生

- [x] 深色模式, 跟随系统 + 手动模式

- [x] 首页, 调整为一个列表, Banner + Other接口 + Paging 的列表聚合

- [x] 可拖拽悬浮按钮 可自动贴边

- [] TODO 本地缓存, [LitePal](https://github.com/guolindev/LitePal) Room Sqlite

- 其它 工具类 便捷库

  - 工具类, [AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

  - Kotlin Android 扩展, [Splitties](https://github.com/LouisCAD/Splitties)

  - 弹窗, [XPopup](https://github.com/li-xiaojun/XPopup)

  - 布局, [RWidgetHelper](https://github.com/RuffianZhong/RWidgetHelper)

  - ViewBinding 扩展, [ViewBindingPropertyDelegate](https://github.com/androidbroadcast/ViewBindingPropertyDelegate)

  - RecyclerView ItemDecoration [SpacingItemDecoration](https://github.com/grzegorzojdana/SpacingItemDecoration)
