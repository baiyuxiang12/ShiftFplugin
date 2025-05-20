# ShiftFPlugin 使用说明

## 插件介绍
ShiftFPlugin 是一个简单实用的 Minecraft Paper 服务器插件，允许玩家通过按下 Shift+F 组合键（实际实现为 Shift+右键点击）来执行配置文件中指定的命令。

## 安装方法
1. 确保您的服务器运行的是 Minecraft 1.21.1 Paper 或兼容版本
2. 将 ShiftFPlugin.jar 文件放入服务器的 plugins 文件夹中
3. 重启服务器或使用插件管理器加载插件
4. 插件将自动生成默认配置文件

## 配置文件
配置文件位于 `plugins/ShiftFPlugin/config.yml`，包含以下选项：

```yaml
# 当玩家按下Shift+F时将执行的命令（不需要包含斜杠）
command: "say 你按下了Shift+F！"

# 是否在执行命令后向玩家显示反馈信息
show-feedback: true

# 调试模式（设置为true时会在控制台输出更多信息）
debug: false
```

您可以修改 `command` 选项来更改按下 Shift+F 时执行的命令。

## 使用方法
1. 玩家需要同时按住 Shift 键并右键点击（模拟 F 键）
2. 插件将以玩家身份执行配置文件中设置的命令
3. 如果 `show-feedback` 设置为 true，玩家将收到命令执行的反馈信息

## 权限
- `shiftfplugin.use` - 允许玩家使用 Shift+F 功能（默认所有玩家都有）
- `shiftfplugin.admin` - 允许使用管理命令（默认仅 OP 有）

## 命令
- `/shiftf reload` - 重新加载配置文件（需要 `shiftfplugin.admin` 权限）

## 技术说明
由于 Minecraft 服务端无法直接监听客户端的按键事件，本插件通过以下方式模拟 Shift+F 组合键：
- 监听玩家的潜行状态（Shift 键）
- 监听玩家的右键交互事件（模拟 F 键）
- 当两个条件同时满足时，执行配置的命令

## 故障排除
如果插件不能正常工作：
1. 确保您使用的是兼容的 Paper 服务器版本
2. 检查控制台是否有错误信息
3. 确认玩家有 `shiftfplugin.use` 权限
4. 尝试使用 `/shiftf reload` 重新加载配置

## 源码说明
插件源码包含以下主要文件：
- `ShiftFPlugin.java` - 插件主类，包含所有功能实现
- `plugin.yml` - 插件描述文件
- `config.yml` - 默认配置文件
