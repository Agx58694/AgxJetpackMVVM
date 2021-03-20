package com.agx.agxjetpackmvvmtest.ui.fragment.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.model.repository.LoginRepository
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.ext.util.isPhone

class LoginViewModel(application: Application, private val loginRepository: LoginRepository) :
    BaseViewModel(application) {
    //登录枚举
    private var loginEnum =
        LoginEnum.PHONE_LOGIN

    //登陆结果
    val loginResult = MutableLiveData<Result<String>>()

    //用户名
    var userPhone = MutableLiveData<String>()

    //密码
    var password = MutableLiveData<String>()

    //确定按钮文字
    var buttonLoginText = MutableLiveData<String>()

    //登录模式文字
    var textLoginModeText = MutableLiveData<String>()

    //是否显示明文密码
    var isShowTextPwd = MutableLiveData<Boolean>()

    //是否显示image_clean_password
    var isShowImgCleanPwd = MutableLiveData<Boolean>()

    //是否显示imageIdCardBack
    var isShowPassword = MutableLiveData<Boolean>()

    //是否显示editPassword
    var isShowEditPassword = MutableLiveData<Boolean>()

    //是否显示viewPassword
    var isShowViewPassword = MutableLiveData<Boolean>()

    //是否显示textForgetPassword
    var isShowTextForgetPassword = MutableLiveData<Boolean>()

    //是否显示checkPassword
    var isShowCheckPassword = MutableLiveData<Boolean>()

    //登录按钮背景
    var buttonLoginBackground = MutableLiveData(R.drawable.btn_login_rectangle_lightgray)

    //是否显示或隐藏image_clean_password图标
    fun isShowPasswordImg() {
        checkInputData()
        if (loginEnum == LoginEnum.PHONE_LOGIN) {
            isShowImgCleanPwd.value = false
            return
        }
        isShowImgCleanPwd.value = password.value?.isNotEmpty()
    }

    //checkPassword点击事件
    fun clickCheckPassword() {
        isShowTextPwd.value = !(isShowTextPwd.value?: true)
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
        if (userPhone.value?.isPhone() == true && (loginEnum == LoginEnum.PHONE_LOGIN || password.value?.length?: 0 >= 8)) {
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
        //切换登陆模式后检查下数据
        checkInputData()
    }

    fun login() = needLoadingLaunch(
        block = {
            loginRepository.getCode(
                success = { loginResult.value = Result.success(it) }
            )
        }
    )

    enum class LoginEnum {
        PHONE_LOGIN,
        PASSWORD_LOGIN
    }
}