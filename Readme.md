# 必看！梳理本项目架构

## 基于 MVVM

代码实现

- V - 使用 `ViewBinding`, 基类带 `V` 的, [VBaseActivity] [VVMBaseActivity] [VBaseFragment] [VVMBaseFragment], 只需继承实现 `binding`
- VM - 使用 `ViewModel`, 基类带 `VM` 的, [VMBaseActivity] [VVMBaseActivity] [VMBaseFragment] [VVMBaseFragment], 只需继承实现 `viewModel`
- VVM - 综合以上两个, 基类带 `VVM` 的, [VVMBaseActivity] [VVMBaseFragment]

根据实际情况使用, 不需要 `ViewModel` 的页面就不用继承带 `VM` 的类, 不需要 `ViewBinding` 的页面就不用继承带 `V` 的类

