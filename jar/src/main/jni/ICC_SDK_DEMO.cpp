// 引用类库
#include "ICC_SDK_DEMO.h"

/**
 * 测试Java调用C++版SDK接口
 */
void Java_com_iccgame_sdk_JniHelper_tester(JNIEnv *jniEnv, jobject jniObject, jint method,
                                           jstring paramJSON) {
    if ((int) method == ICC_SDK::METHOD_REGISTER) { // 注册、转正
        // --------------- Begin ---------------
        RegisterCallback *callback = new RegisterCallback();
        ICC_SDK::getInstance()->regist(callback);
        // ---------------  End  ---------------
    }
    else if ((int) method == ICC_SDK::METHOD_LOGIN) { // 登录
        // --------------- Begin ---------------
        LoginCallback *callback = new LoginCallback();
        ICC_SDK::getInstance()->login(callback);
        // ---------------  End  ---------------
    }
    else if ((int) method == ICC_SDK::METHOD_PAY) { // 支付
        const char *tradeInfo = jniEnv->GetStringUTFChars(paramJSON, NULL);
        // --------------- Begin ---------------
        PayCallback *callback = new PayCallback();
        ICC_SDK::getInstance()->pay(tradeInfo, callback);
        // ---------------  End  ---------------
        jniEnv->ReleaseStringUTFChars(paramJSON, tradeInfo);
    }
    else if ((int) method == ICC_SDK::METHOD_CENTER) { // 账号中心
        // --------------- Begin ---------------
        CenterCallback *callback = new CenterCallback();
        ICC_SDK::getInstance()->center(callback);
        // ---------------  End  ---------------
    }
    else if ((int) method == ICC_SDK::METHOD_LOGOUT) { // 注销
        // --------------- Begin ---------------
        LogoutCallback *callback = new LogoutCallback();
        ICC_SDK::getInstance()->logout(callback);
        // ---------------  End  ---------------
    }
}

/**
 * 注册、转正完成处理
 */
void  RegisterCallback::result(const char *resultJSON) {
    LOG_DEBIG("PROXY RegisterCallback->result(%s)", resultJSON);
    delete this;
};

/**
 * 登录完成处理
 */
void  LoginCallback::result(const char *resultJSON) {
    LOG_DEBIG("PROXY LoginCallback->result(%s)", resultJSON);
    delete this;
};

/**
 * 登录完成处理
 */
void  PayCallback::result(const char *resultJSON) {
    LOG_DEBIG("PROXY PayCallback->result(%s)", resultJSON);
    delete this;
};

/**
 * 登录完成处理
 */
void  CenterCallback::result(const char *resultJSON) {
    LOG_DEBIG("PROXY CenterCallback->result(%s)", resultJSON);
    delete this;
};

/**
 * 登录完成处理
 */
void  LogoutCallback::result(const char *resultJSON) {
    LOG_DEBIG("PROXY LogoutCallback->result(%s)", resultJSON);
    delete this;
};