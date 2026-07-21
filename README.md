# uiautomation4j

**跨平台桌面 UI 自动化框架（Java）**

uiautomation4j 是一个纯 Java 实现的跨平台桌面 UI 自动化库，提供统一的 API 接口，支持 **Windows**、**macOS** 和 **Linux** 三大操作系统的 GUI 自动化操作。

---

## 目录

- [项目简介](#项目简介)
- [架构设计](#架构设计)
- [模块结构](#模块结构)
- [技术栈](#技术栈)
- [快速开始](#快速开始)
- [核心概念](#核心概念)
  - [控件搜索](#控件搜索)
  - [控件操作](#控件操作)
  - [Pattern 模式](#pattern-模式)
- [跨平台说明](#跨平台说明)
- [ControlType 枚举](#controltype-枚举)
- [平台约束与限制](#平台约束与限制)
- [构建与依赖](#构建与依赖)
- [项目结构](#项目结构)
- [许可证](#许可证)

---

## 项目简介

uiautomation4j 的设计灵感来源于 Python 的 `uiautomation` 库，旨在为 Java 生态提供一套**简洁、统一、跨平台**的桌面 UI 自动化解决方案。

### 核心特性

- **统一 API**：同一套代码在 Windows / macOS / Linux 上运行，无需修改业务逻辑
- **Builder 模式**：链式调用构建搜索条件，代码可读性强
- **SPI 可扩展**：基于 Java SPI 机制，平台后端完全解耦，可独立扩展
- **丰富的控件类型**：支持 30+ 种控件类型的识别和操作
- **7 种 Pattern 支持**：Value、Invoke、Window、Selection、Scroll、ExpandCollapse、Transform
- **智能等待**：内置控件就绪等待和超时重试机制
- **截图能力**：支持控件级截图和全屏截图

---

## 架构设计

```
┌─────────────────────────────────────────────────────────┐
│                    用户代码层                             │
│         (自动化测试脚本 / RPA 流程 / 辅助工具)             │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│              uiautomation4j-core                        │
│                                                         │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────┐  │
│  │   Control   │  │SearchCondition│  │   Pattern     │  │
│  │  (控件体系)  │  │  (搜索条件)   │  │  (交互模式)   │  │
│  └──────┬──────┘  └──────────────┘  └───────────────┘  │
│         │                                               │
│  ┌──────▼──────────────────────────────────────────┐    │
│  │          ControlBackend (SPI 接口)               │    │
│  │    findControl / click / sendKeys / capture...   │    │
│  └──────────────────────┬──────────────────────────┘    │
└─────────────────────────┼───────────────────────────────┘
                          │ SPI 实现
           ┌──────────────┼──────────────┐
           ▼              ▼              ▼
┌──────────────┐ ┌──────────────┐ ┌───────────────────┐
│  win 模块    │ │  mac 模块    │ │   linux 模块      │
│              │ │              │ │                   │
│ JNA + COM    │ │ JNA +        │ │ AT-SPI2 + D-Bus   │
│ UIAutomation │ │ Accessibility│ │ + XTest           │
│ API          │ │ API          │ │                   │
│ (Windows)    │ │ (macOS)      │ │ (Linux)           │
└──────────────┘ └──────────────┘ └───────────────────┘
```

### 分层说明

| 层级 | 职责 | 关键类 |
|------|------|--------|
| **用户代码层** | 编写自动化脚本，调用统一 API | 用户自行编写 |
| **core 层** | 定义控件体系、搜索条件、Pattern 接口、SPI 接口 | `Control`, `SearchCondition`, `Pattern`, `ControlBackend` |
| **平台实现层** | 实现 `ControlBackend` SPI，对接各平台原生 API | `WinControlBackend`, `MacControlBackend`, `LinuxControlBackend` |

---

## 模块结构

| 模块 | 说明 | 平台要求 |
|------|------|----------|
| `uiautomation4j-core` | 核心抽象层：控件、搜索条件、Pattern 接口、SPI 定义 | 无平台要求 |
| `uiautomation4j-win` | Windows 实现：JNA + UIAutomation COM 接口 | Windows |
| `uiautomation4j-mac` | macOS 实现：JNA + Accessibility API (AXUIElement) | macOS |
| `uiautomation4j-linux` | Linux 实现：AT-SPI2 + D-Bus + XTest | Linux (X11/Wayland) |
| `uiautomation4j-example` | 使用示例和测试用例 | 跨平台 |

---

## 技术栈

| 组件 | 技术 | 版本 | 用途 |
|------|------|------|------|
| **语言** | Java | 11+ | 基础运行环境 |
| **构建** | Maven | 3.x | 多模块构建管理 |
| **JNA** | net.java.dev.jna | 5.13.0 | 调用原生系统 API |
| **D-Bus** | com.github.hypfvieh:dbus-java | 4.3.1 | Linux AT-SPI2 通信 |
| **XTest** | libXtst (通过 JNA) | 系统库 | Linux 键盘/鼠标模拟 |

### 各平台底层技术

| 平台 | 控件访问 | 键盘/鼠标模拟 | 截图 |
|------|----------|---------------|------|
| **Windows** | UIAutomation COM (IUIAutomation) | JNA + SendInput | AWT Robot |
| **macOS** | Accessibility API (AXUIElement) | CGEvent (CoreGraphics) | AWT Robot |
| **Linux** | AT-SPI2 (D-Bus) | XTest (libXtst) | AWT Robot |

---

## 快速开始

### 环境要求

- JDK 11+
- Maven 3.x
- 对应平台的运行环境

### 1. 添加依赖

```xml
<!-- 核心模块（必须） -->
<dependency>
    <groupId>io.getbit</groupId>
    <artifactId>uiautomation4j-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- 选择对应平台的模块（三选一） -->
<!-- Windows -->
<dependency>
    <groupId>io.getbit</groupId>
    <artifactId>uiautomation4j-win</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!-- macOS -->
<dependency>
    <groupId>io.getbit</groupId>
    <artifactId>uiautomation4j-mac</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!-- Linux -->
<dependency>
    <groupId>io.getbit</groupId>
    <artifactId>uiautomation4j-linux</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. 初始化平台后端

```java
// Windows
WinAutomation.init();

// macOS
MacAutomation.init();

// Linux
LinuxAutomation.init();
```

### 3. 查找并操作控件

```java
// Builder 模式查找窗口
WindowControl window = Control.window()
        .name("无标题 - 记事本")
        .findWindow();

// 查找编辑框并输入
EditControl edit = Control.edit()
        .searchDepth(5)
        .findEdit();
edit.click();
edit.sendKeys("Hello, uiautomation4j!");

// 查找按钮并点击
Control button = Control.button()
        .name("确定")
        .findButton();
button.click();

// 释放资源
WinAutomation.shutdown();
```

### 4. 使用 SearchCondition 精确搜索

```java
SearchCondition condition = SearchCondition.builder()
        .name("确定")                    // 精确名称匹配
        .controlType(ControlType.Button) // 控件类型过滤
        .searchDepth(5)                  // 搜索深度限制
        .build();

Control button = Control.getBackend().findControl(condition);
button.click();
```

---

## 核心概念

### 控件搜索

#### 搜索条件 (SearchCondition)

通过 Builder 模式构建，支持以下条件：

| 条件 | 方法 | 说明 | 匹配层级 |
|------|------|------|----------|
| 精确名称 | `name(String)` | 完全匹配控件名称 | 平台层 |
| 模糊名称 | `subName(String)` | 子串匹配 | Java 层 |
| 正则名称 | `regexName(String)` | 正则表达式匹配 | Java 层 |
| 类名 | `className(String)` | 控件类名过滤 | 平台层 |
| AutomationId | `automationId(String)` | 控件唯一标识 | 平台层 |
| 控件类型 | `controlType(ControlType)` | 按类型过滤 | 平台层 |
| 搜索深度 | `searchDepth(int)` | 限制最大搜索深度 | 平台层 |
| 元素深度 | `depth(int)` | 指定 UI 树层级 | 平台层 |
| 匹配索引 | `foundIndex(int)` | 多个匹配时取第几个 | 平台层 |
| 搜索起点 | `searchFrom(SearchCondition)` | 从指定父控件开始搜索 | 平台层 |
| 自定义比较器 | `compare(BiPredicate)` | 自定义匹配逻辑 | Java 层 |

#### Builder 快捷入口

```java
Control.window().name("...").findWindow();       // 查找窗口
Control.button().name("...").findButton();       // 查找按钮
Control.edit().searchDepth(5).findEdit();        // 查找编辑框
Control.text().subName("...").findText();        // 查找文本
```

#### 链式子控件搜索

```java
WindowControl window = Control.window().name("主窗口").findWindow();
Control child = window.child("子按钮");                          // 按名称
Control typed = window.child("保存", ControlType.Button);        // 按名称+类型
Control deep = window.child(SearchCondition.builder()...build()); // 按条件
```

### 控件操作

| 方法 | 说明 |
|------|------|
| `click()` | 单击控件（自动等待就绪） |
| `doubleClick()` | 双击控件 |
| `rightClick()` | 右键点击控件 |
| `sendKeys(String)` | 发送按键输入（支持特殊键语法） |
| `captureToImage(String)` | 截图保存到文件 |
| `exists()` / `exists(int)` | 检查控件是否存在 |
| `waitReady()` / `waitReady(int)` | 等待控件就绪 |
| `refind()` | 重新查找控件 |
| `isEnabled()` | 检查是否启用 |
| `isVisible()` | 检查是否可见 |

#### 特殊键语法

```java
edit.sendKeys("{Ctrl}a");          // Ctrl+A 全选
edit.sendKeys("{Enter}");          // 回车
edit.sendKeys("{Tab}");            // Tab 键
edit.sendKeys("{Alt}{F4}");        // Alt+F4 关闭窗口
```

### Pattern 模式

Pattern 代表控件支持的交互能力，不同控件支持不同的 Pattern 组合。

| Pattern | 说明 | 典型控件 |
|---------|------|----------|
| **ValuePattern** | 读写控件值 | Edit, Text, ProgressBar |
| **InvokePattern** | 触发操作 | Button, MenuItem |
| **WindowPattern** | 窗口操作 | Window |
| **SelectionPattern** | 获取选中项 | List, ComboBox, Tab |
| **ScrollPattern** | 滚动操作 | ScrollBar, List, DataGrid |
| **ExpandCollapsePattern** | 展开/折叠 | TreeItem, ComboBox |
| **TransformPattern** | 移动/调整大小/旋转 | Window |

#### Pattern 使用示例

```java
// ValuePattern - 读写值
ValuePattern value = edit.getValuePattern();
String text = value.getValue();
value.setValue("新内容");
boolean readOnly = value.isReadOnly();

// WindowPattern - 窗口操作
WindowPattern wp = window.getWindowPattern();
wp.maximize();
wp.minimize();
wp.restore();
wp.setTopmost(true);
int state = wp.getVisualState(); // 0=正常, 1=最大化, 2=最小化

// SelectionPattern - 选择项
SelectionPattern sp = list.getSelectionPattern();
List<Object> selected = sp.getSelection();
boolean multi = sp.isMultiSelect();

// ScrollPattern - 滚动
ScrollPattern scp = list.getScrollPattern();
scp.scroll(0, ScrollPattern.SCROLL_DIRECTION_DOWN, 0, 1.0);
scp.setScrollPercent(0.0, 50.0);

// ExpandCollapsePattern - 展开折叠
ExpandCollapsePattern ecp = treeItem.getExpandCollapsePattern();
ecp.expand();
ecp.collapse();
int state = ecp.getExpandCollapseState();

// TransformPattern - 变换
TransformPattern tp = window.getTransformPattern();
tp.move(100, 100);
tp.resize(800, 600);
boolean canMove = tp.canMove();
```

---

## 跨平台说明

### 统一 API 原则

用户代码只依赖 `uiautomation4j-core`，通过 SPI 机制在运行时绑定平台实现。**同一份业务代码无需任何修改即可在不同平台运行**。

### 平台初始化对照表

| 平台 | 初始化方法 | 释放方法 | 权限要求 |
|------|-----------|----------|----------|
| Windows | `WinAutomation.init()` | `WinAutomation.shutdown()` | 无 |
| macOS | `MacAutomation.init()` | `MacAutomation.shutdown()` | 辅助功能权限 |
| Linux | `LinuxAutomation.init()` | `LinuxAutomation.shutdown()` | at-spi2-core |

### macOS 辅助功能权限

macOS 需要授予"辅助功能"权限：

```
系统设置 → 隐私与安全 → 辅助功能 → 添加并授权此应用
```

可通过 `MacAutomation.isTrusted()` 检查权限状态。

### Linux 前置条件

- 安装 at-spi2-core：`sudo apt install at-spi2-core`（Ubuntu/Debian）
- 运行图形桌面环境（GNOME、KDE、XFCE 等）
- 某些 GTK3 应用需要：`export GTK_MODULES=gail:atk-bridge`

---

## ControlType 枚举

### ID 范围约定

| 范围 | 平台 | 说明 |
|------|------|------|
| 50000 - 50040 | 跨平台 / Windows | 基准类型，各平台通用 |
| 60000 - 60099 | macOS | AXRole 映射 |
| 70000 - 70099 | Linux | AT-SPI Role 映射 |

### 跨平台通用类型

| 类型 | ID | 说明 |
|------|-----|------|
| Button | 50000 | 按钮 |
| CheckBox | 50002 | 复选框 |
| ComboBox | 50003 | 组合框/下拉列表 |
| Edit | 50004 | 编辑框 |
| Hyperlink | 50005 | 超链接 |
| Image | 50006 | 图片 |
| List | 50008 | 列表 |
| ListItem | 50007 | 列表项 |
| Menu | 50009 | 菜单 |
| MenuBar | 50010 | 菜单栏 |
| MenuItem | 50011 | 菜单项 |
| RadioButton | 50013 | 单选按钮 |
| ScrollBar | 50014 | 滚动条 |
| Slider | 50015 | 滑块 |
| Tab | 50018 | Tab 页签 |
| TabItem | 50019 | Tab 项 |
| Text | 50024 | 文本（只读） |
| Tree | 50023 | 树形控件 |
| TreeItem | 50028 | 树形项 |
| Window | 50032 | 窗口 |
| Pane | 50033 | 面板/容器 |
| Document | 50030 | 文档 |
| Group | 50020 | 分组 |
| ProgressBar | 50012 | 进度条 |
| ToolBar | 50021 | 工具栏 |

### 平台专用类型

**Windows 专用：**
AppBar, Calendar, DataGrid, DataItem, Header, HeaderItem, Spinner, SplitButton, Thumb, TitleBar, ToolTip

**macOS 专用：**
ColorWell, Drawer, Sheet, Handle, Incrementor, Browser, Column, Ruler, SplitGroup, TextArea, TextField, PopUpButton, Application

**Linux 专用：**
Alert, Dialog, Filler, Icon, PageTab, PageTabList, PopupMenu, Table, TableCell, TableColumnHeader, TableRowHeader

### Role 映射方法

```java
// macOS AXRole → ControlType
ControlType type = ControlType.fromMacRole("AXButton");    // → Button
ControlType type = ControlType.fromMacRole("AXWindow");    // → Window

// Linux AT-SPI Role → ControlType
ControlType type = ControlType.fromAtspiRole(74);  // ROLE_PUSH_BUTTON → Button
ControlType type = ControlType.fromAtspiRole(27);  // ROLE_FRAME → Window
```

---

## 平台约束与限制

### 通用约束

1. **控件查找依赖 UI 树**：如果应用未暴露无障碍信息，可能无法查找到控件
2. **操作需要焦点**：`click()`、`sendKeys()` 等操作会自动将焦点移至目标控件
3. **线程安全**：`Control` 对象非线程安全，多线程环境需自行同步
4. **超时机制**：`findControl()` 在找不到控件时抛出 `ControlNotFoundException`

### Windows 约束

- 需要 Windows 7 及以上版本
- UWP 应用可能需要额外配置
- 某些自绘控件可能不暴露标准 UIAutomation 属性

### macOS 约束

- 需要 macOS 10.10+
- **必须授予辅助功能权限**，否则无法访问 UI 元素
- Electron 应用需要启用 `--enable-features=Accessibility`

### Linux 约束

- 需要 at-spi2-core 服务运行
- **TransformPattern 不支持**：AT-SPI2 不提供窗口移动/调整大小的标准接口
- **AutomationId 匹配不支持**：AT-SPI2 没有直接等价概念，搜索时自动跳过
- 需要 X11 或 Wayland 图形环境
- 某些 Qt 应用需要安装 `qt5-at-spi` 或 `qt6-at-spi` 插件

---

## 构建与依赖

### 构建命令

```bash
# 编译全部模块
mvn compile

# 打包
mvn package

# 仅编译特定模块
mvn compile -pl uiautomation4j-core
mvn compile -pl uiautomation4j-linux -am   # -am 自动编译依赖模块
```

### 依赖版本

| 依赖 | 版本 | 说明 |
|------|------|------|
| JDK | 11+ | D-Bus 库需要 Java 11 |
| JNA | 5.13.0 | 原生 API 调用 |
| dbus-java | 4.3.1 | Linux D-Bus 通信 |

---

## 项目结构

```
uiautomation4j/
├── pom.xml                          # 父 POM（模块管理 + 依赖版本）
│
├── uiautomation4j-core/             # 核心抽象层
│   └── src/main/java/.../
│       ├── condition/
│       │   └── SearchCondition.java      # 搜索条件（Builder 模式）
│       ├── control/
│       │   ├── Control.java              # 控件基类 + Builder 入口
│       │   ├── WindowControl.java        # 窗口控件
│       │   ├── ButtonControl.java        # 按钮控件
│       │   ├── EditControl.java          # 编辑框控件
│       │   ├── TextControl.java          # 文本控件
│       │   ├── ListControl.java          # 列表控件
│       │   ├── ListItemControl.java      # 列表项控件
│       │   ├── TreeControl.java          # 树形控件
│       │   ├── TreeItemControl.java      # 树形项控件
│       │   ├── MenuBarControl.java       # 菜单栏控件
│       │   ├── MenuItemControl.java      # 菜单项控件
│       │   ├── CheckBoxControl.java      # 复选框控件
│       │   ├── PaneControl.java          # 面板控件
│       │   ├── ScrollBarControl.java     # 滚动条控件
│       │   ├── TabControl.java           # Tab 控件
│       │   └── TitleBarControl.java      # 标题栏控件
│       ├── enums/
│       │   └── ControlType.java          # 控件类型枚举（跨平台）
│       ├── exception/
│       │   ├── AutomationException.java  # 自动化基础异常
│       │   └── ControlNotFoundException.java # 控件未找到异常
│       ├── pattern/
│       │   ├── Pattern.java              # Pattern 基础接口
│       │   ├── ValuePattern.java         # 值读写
│       │   ├── InvokePattern.java        # 触发操作
│       │   ├── WindowPattern.java        # 窗口操作
│       │   ├── SelectionPattern.java     # 选择项
│       │   ├── ScrollPattern.java        # 滚动
│       │   ├── ExpandCollapsePattern.java # 展开/折叠
│       │   └── TransformPattern.java     # 变换（移动/缩放/旋转）
│       └── spi/
│           └── ControlBackend.java       # SPI 接口定义
│
├── uiautomation4j-win/              # Windows 实现
│   └── src/main/java/.../win/
│       ├── WinAutomation.java            # 初始化入口
│       ├── WinControlBackend.java        # ControlBackend SPI 实现
│       ├── WinControlFactory.java        # 控件工厂
│       ├── com/                          # JNA COM 绑定
│       ├── pattern/                      # Pattern 实现
│       ├── keyboard/                     # 键盘模拟
│       ├── mouse/                        # 鼠标模拟
│       └── screenshot/                   # 截图
│
├── uiautomation4j-mac/              # macOS 实现
│   └── src/main/java/.../mac/
│       ├── MacAutomation.java            # 初始化入口
│       ├── MacControlBackend.java        # ControlBackend SPI 实现
│       ├── MacControlFactory.java        # 控件工厂
│       ├── ax/                           # AXUIElement 封装
│       ├── pattern/                      # Pattern 实现
│       ├── keyboard/                     # 键盘模拟 (CGEvent)
│       ├── mouse/                        # 鼠标模拟 (CGEvent)
│       └── screenshot/                   # 截图
│
├── uiautomation4j-linux/            # Linux 实现
│   └── src/main/java/.../linux/
│       ├── LinuxAutomation.java          # 初始化入口
│       ├── LinuxControlBackend.java      # ControlBackend SPI 实现
│       ├── LinuxControlFactory.java      # 控件工厂
│       ├── ax/                           # AT-SPI2 D-Bus 封装
│       │   ├── AtspiConnection.java      # D-Bus 连接管理
│       │   ├── AtspiElement.java         # 元素封装
│       │   ├── AtspiConstants.java       # 常量定义
│       │   └── (D-Bus 接口定义 x5)       # Accessible/Action/Component/Text/Value
│       ├── pattern/                      # Pattern 实现
│       ├── keyboard/                     # 键盘模拟 (XTest)
│       ├── mouse/                        # 鼠标模拟 (XTest)
│       └── screenshot/                   # 截图 (AWT Robot)
│
└── uiautomation4j-example/          # 使用示例
    └── src/main/java/.../example/
        ├── AllExamplesRunner.java        # 统一入口
        ├── BasicWindowExample.java       # 窗口基本操作
        ├── ControlSearchExample.java     # 控件搜索条件
        ├── PatternExample.java           # Pattern 使用
        ├── CrossPlatformExample.java     # 跨平台使用
        └── LinuxSpecificExample.java     # Linux 专属功能
```

---

## 许可证

本项目遵循 Apache License 2.0 许可条款。

---

## 致谢
- [JNA](https://github.com/java-native-access/jna) — Java 原生访问
- [dbus-java](https://github.com/hypfvieh/dbus-java) — Java D-Bus 绑定
- [AT-SPI2](https://gitlab.gnome.org/GNOME/at-spi2-core) — Linux 无障碍框架
