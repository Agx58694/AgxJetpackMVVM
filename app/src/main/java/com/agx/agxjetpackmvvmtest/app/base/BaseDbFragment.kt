package com.agx.agxjetpackmvvmtest.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.agx.jetpackmvvm.base.fragment.BaseVmDbFragment
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.ext.ifTrue
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbFragment<VM, DB>() {
    private var tipDialog: QMUITipDialog? = null
    lateinit var mLoadService: LoadService<*>
    /**
     * 当前Fragment绑定的视图布局
     */
    abstract override fun layoutId(): Int

    abstract override fun initVM(): VM

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法
     */
    abstract override fun lazyLoadData()

    /**
     * 创建LiveData观察者 懒加载之后才会触发
     */
    override fun createObserver() {}

    override fun layoutDataLoading() {}

    override fun layoutDataError(errorMessage: String) {}

    override fun layoutDataTimeout() {}

    open fun onReload(v: View) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mLoadService = LoadSir.getDefault().register(super.onCreateView(inflater, container, savedInstanceState)){
            onReload(it)
        }
        return mLoadService.loadLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mLoadService.showSuccess()
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        tipDialog = QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(message)
            .create()
        tipDialog?.show()
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        tipDialog?.dismiss()
    }

    override fun onDetach() {
        super.onDetach()
        tipDialog?.dismiss()
    }

    /**
     * 弹出错误框
     * 因为协程是绑定当前Fm的生命周期，如delay还未跑完就结束生命周期的话就会产生Dialog无法自动消失。
     * 因此加入block函数进行Dialog消失后执行*/
    fun showErrorMessage(errorMessage: String, block: () -> Unit = {}) {
        QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
            .setTipWord(errorMessage)
            .create().apply {
                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        (event == Lifecycle.Event.ON_DESTROY).ifTrue { dismiss() }
                    }
                })
                lifecycleScope.launch(mViewModel.coroutineExceptionHandler()) {
                    delay(1500)
                    dismiss()
                    block.invoke()
                }
            }.show()
    }

    /**
     * 弹出成功框
     * 因为协程是绑定当前Fm的生命周期，如delay还未跑完就结束生命周期的话就会产生Dialog无法自动消失。
     * 因此加入block函数进行Dialog消失后执行*/
    fun showSuccessMessage(successMessage: String, block: () -> Unit = {}) {
        QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
            .setTipWord(successMessage)
            .create().apply {
                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        (event == Lifecycle.Event.ON_DESTROY).ifTrue { dismiss() }
                    }
                })
                lifecycleScope.launch(mViewModel.coroutineExceptionHandler()) {
                    delay(1500)
                    dismiss()
                    block.invoke()
                }
            }.show()

    }
}