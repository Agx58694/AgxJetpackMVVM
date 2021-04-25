# AgxJetpackMvvm

#### 介绍
基于MVVM做的一个快速开发库，按照hegaojian/JetpackMvvm的架构基础进行的改变封装，用了谷歌JetPack官方组件库
kotlin+协程+retrofit2+navigation+koin+room+liveData

#### 软件架构
开发模式：MVVM
网络请求：retrofit2+okhttp+协程
数据库：room+协程
单Activity开发模式，使用navigation导航组件

#### 使用说明
implementation 'com.github.Agx58694:AgxJetpackMVVM:1.0.0'

需继承BaseVmActivity、BaseVmDbActivity、BaseVmVbActivity并重写等待框相关逻辑

处理viewModel原始错误，所以viewModel的原始错误都会到这里，开发者可记录日子或上传服务器记录.
所有ViewModel下使用协程方法产生的错误都会走这里，前提不修改默认错误处理器
setOnAppThrowableListener { error ->
   //错误处理
}
这里处理ViewModel使用协程方法出现的所有错误，开发者可自定义处理数据库异常和网络请求异常，并返回相应的用户可读性的错误
setOnFormatThrowable { throwable, context ->
   "自定义错误格式化"
}

//可设置默认等待框提示语
loadingContent = "加载中"

**具体使用请查看demo示例程序**

*下一版更新为DataStore，加入网络请求示例*