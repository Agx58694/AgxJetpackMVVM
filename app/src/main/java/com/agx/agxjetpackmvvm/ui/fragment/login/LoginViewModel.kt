package com.agx.agxjetpackmvvm.ui.fragment.login

import android.app.Application
import android.widget.Toast
import com.agx.agxjetpackmvvm.R
import com.agx.agxjetpackmvvm.model.repository.LoginRepository
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.BooleanLiveData
import me.hgj.jetpackmvvm.callback.livedata.IntLiveData
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData
import me.hgj.jetpackmvvm.ext.util.isPhone

class LoginViewModel(application: Application, private val loginRepository: LoginRepository) :
    BaseViewModel(application) {
    //登录枚举
    private var loginEnum =
        LoginEnum.PHONE_LOGIN

    //用户名
    var userPhone = StringLiveData()

    //密码
    var password = StringLiveData()

    //确定按钮文字
    var buttonLoginText = StringLiveData()

    //登录模式文字
    var textLoginModeText = StringLiveData()

    //是否显示明文密码
    var isShowTextPwd = BooleanLiveData()

    //是否显示image_clean_password
    var isShowImgCleanPwd = BooleanLiveData()

    //是否显示imageIdCardBack
    var isShowPassword = BooleanLiveData()

    //是否显示editPassword
    var isShowEditPassword = BooleanLiveData()

    //是否显示viewPassword
    var isShowViewPassword = BooleanLiveData()

    //是否显示textForgetPassword
    var isShowTextForgetPassword = BooleanLiveData()

    //是否显示checkPassword
    var isShowCheckPassword = BooleanLiveData()

    //登录按钮背景
    var buttonLoginBackground = IntLiveData(R.drawable.btn_login_rectangle_lightgray)

    //是否显示或隐藏image_clean_password图标
    fun isShowPasswordImg() {
        checkInputData()
        if (loginEnum == LoginEnum.PHONE_LOGIN) {
            isShowImgCleanPwd.value = false
            return
        }
        isShowImgCleanPwd.value = password.value.isNotEmpty()
    }

    //checkPassword点击事件
    fun clickCheckPassword() {
        isShowTextPwd.value = !isShowTextPwd.value
    }

    //textLoginMode点击事件,切换登录模式
    fun checkTextLoginMode() {
        loginEnum = when (loginEnum) {
            LoginEnum.PHONE_LOGIN -> LoginEnum.PASSWORD_LOGIN
            LoginEnum.PASSWORD_LOGIN -> LoginEnum.PHONE_LOGIN
        }
        switchLoginMode()
    }

    //image_clean_password点击事件，清除密码框文本
    fun checkImageCleanPassword() {
        password.value = ""
    }

    fun checkInputData() {
        if (userPhone.value.isPhone() && password.value.length >= 8) {
            buttonLoginBackground.value = R.drawable.btn_login_rectangle_lightskyblue
            return
        }
        buttonLoginBackground.value = R.drawable.btn_login_rectangle_lightgray
    }

    //切换登录模式
    fun switchLoginMode() {
        val isShow: Boolean
        when (loginEnum) {
            LoginEnum.PHONE_LOGIN -> {
                isShow = false
                buttonLoginText.value = "获取验证码"
                textLoginModeText.value = "密码登录"
            }
            LoginEnum.PASSWORD_LOGIN -> {
                isShow = true
                buttonLoginText.value = "登录"
                textLoginModeText.value = "验证码登录"
            }
        }
        isShowPassword.value = isShow
        isShowCheckPassword.value = isShow
        isShowViewPassword.value = isShow
        isShowTextForgetPassword.value = isShow
        isShowEditPassword.value = isShow
    }

    fun login() = needLoadingLaunch(
        block = {
            loginRepository.getCode(
                success = { Toast.makeText(getApplication(), it, Toast.LENGTH_SHORT).show() }
            )
        }
    )

    enum class LoginEnum {
        PHONE_LOGIN,
        PASSWORD_LOGIN
    }
}