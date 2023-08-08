# 项目架构

## MVVM 架构, 基于 Kotlin, JetPack: Lifecycle + LiveData + ViewModel + Paging, 网络: Retrofit2 + OkHttp3 + Coroutine, 本地缓存: 

### 功能及技术点

[x] 网络, [OkHttp3](https://github.com/square/okhttp) + [Retrofit2](https://github.com/square/retrofit), [PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar)

[x] 图片, [Glide](https://github.com/bumptech/glide)

[x] 消息, [LiveEventBus](https://github.com/JeremyLiao/LiveEventBus)

[x] 权限, [PermissionX](https://github.com/guolindev/PermissionX)

[x] 首页导航, 使用 TabLayout + ViewPager2 + Fragment, 自定义 TabItem, 再次点击 Tab 刷新页面

[x] 侧滑导航, DrawerLayout + 悬浮可拖拽自动贴边 View

[x] 状态栏沉浸, [ImmersionBar](https://github.com/gyf-dev/ImmersionBar), Fragment 使用 paddingTop 解决

[x] Banner, [banner](https://github.com/youth5201314/banner)

[x] 列表加载, Paging 组件

[x] 列表刷新, [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout) 

[] 黑暗模式, 自动适应系统设置, TODO: 手动模式

[] 首页, 调整为一个列表, 用 rv 的 multiType

[] TODO 收藏页面, 二级 Tab

[] TODO 个人中心, 设置, 登入, 登出

[] TODO 本地缓存, [LitePal](https://github.com/guolindev/LitePal) Room Sqlite

- 其它 工具类 便捷库

  - 工具类, [AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

  - Kotlin Android 扩展, [Splitties](https://github.com/LouisCAD/Splitties)

  - 弹窗, [XPopup](https://github.com/li-xiaojun/XPopup)

  - 布局, [RWidgetHelper](https://github.com/RuffianZhong/RWidgetHelper)

  - ViewBinding 扩展, [ViewBindingPropertyDelegate](https://github.com/androidbroadcast/ViewBindingPropertyDelegate)

  - RecyclerView ItemDecoration [SpacingItemDecoration](https://github.com/grzegorzojdana/SpacingItemDecoration)

  - 

---

### 代码相关

- V - 使用 `ViewBinding`, 基类带 `V` 的, [VBaseActivity] [VVMBaseActivity] [VBaseFragment] [VVMBaseFragment], 只需继承实现 `binding`
- VM - 使用 `ViewModel`, 基类带 `VM` 的, [VMBaseActivity] [VVMBaseActivity] [VMBaseFragment] [VVMBaseFragment], 只需继承实现 `viewModel`
- VVM - 综合以上两个, 基类带 `VVM` 的, [VVMBaseActivity] [VVMBaseFragment], 需继承实现 `binding` `viewModel`

根据情况, 需要 `ViewModel` 的页面就继承带 `VM` 的类, 需要 `ViewBinding` 的页面就继承带 `V` 的类

---

