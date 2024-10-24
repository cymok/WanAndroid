# WanAndroid, View System + Compose

## 效果图

略

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

- [x] Splash 页面适配, 仿 bilibili, 无缝衔接

- [x] 首页, 调整为一个列表, Banner + Other接口 + Paging 的列表聚合

- [x] 可拖拽悬浮按钮 可自动贴边

- [x] APP 跳转, 包含微信协议跳转, 以及自定义 Scheme, 应用商店 Scheme, 邮件 Scheme, 指定应用跳转, 并且过滤处理好相应的应用列表

- [ ] TODO: 自定义 http https 的 Scheme 处理列表 (当用户在系统设置了默认浏览器应用, 之后只能调起这一个 APP 跳转; 研究到微信的方案似乎是用穷举法列举出来的, 因为他的列表包含系统能识别 Scheme 之外的应用)

- [x] 自定义管理空间页面, 参考 Chrome, 具体路径: 应用详情 -> 清除数据 -> 管理空间 -> 自定义页面

- [x] 集成 Compose, 将个人项目 WanCompose 整合到 WanAndroid, 目前已整合依赖和基础页面, 可正常进行 Compose 页面开发

- [x] 查看/删除 本地浏览历史, Compose UI, 基于 DataStore 本地数据存取

- [ ] TODO: 页面过度 空白页 WebView错误页 等

- 其它 工具类 便捷库

  - 工具类, [AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

  - Kotlin Android 扩展, [Splitties](https://github.com/LouisCAD/Splitties)

  - 弹窗, [XPopup](https://github.com/li-xiaojun/XPopup)

  - 布局, [RWidgetHelper](https://github.com/RuffianZhong/RWidgetHelper)

  - ViewBinding 扩展, [ViewBindingPropertyDelegate](https://github.com/androidbroadcast/ViewBindingPropertyDelegate)

  - RecyclerView ItemDecoration [SpacingItemDecoration](https://github.com/grzegorzojdana/SpacingItemDecoration)

