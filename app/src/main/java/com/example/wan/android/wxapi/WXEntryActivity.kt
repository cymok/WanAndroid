package com.example.wan.android.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.wan.android.constant.AppConst
import com.example.wan.android.utils.toast
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

open class WXEntryActivity : Activity(), IWXAPIEventHandler {

    private val wxApi: IWXAPI by lazy { WXAPIFactory.createWXAPI(this, AppConst.WX_APP_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxApi.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        wxApi.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        toast(req.toString())
    }

    override fun onResp(resp: BaseResp) {

        when (resp.type) {
            ConstantsAPI.COMMAND_PAY_BY_WX -> { // 支付
                // 这里处理统一的东西
                when (resp.errCode) {
                    BaseResp.ErrCode.ERR_OK -> {
                        //成功回调
                    }

                    BaseResp.ErrCode.ERR_USER_CANCEL -> {
                        //用户取消
                        toast("取消支付")
                    }

                    BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                        toast("拒绝授权，请更新微信")
                    }

                    BaseResp.ErrCode.ERR_UNSUPPORT -> {
                        toast("不支持的微信版本，请更新微信")
                    }

                    else -> {
                        toast("未正常支付")
                    }
                }

                // 把回调传回页面处理
                // todo bus
//                postEvent(EventBus.WX_PAY, resp.errCode)

            }

            else -> { // 授权 分享 等

            }
        }

        finish()
    }

}
