//
// Created by Administrator on 2015/11/17.
//
#ifndef __ICC_SDK_DEMO__
#define __ICC_SDK_DEMO__

// 引用类库
#include "ICC_SDK.h"

// 标记C++代码
#ifdef __cplusplus
extern "C" {
#endif
/**
 * 测试调用
 */
void Java_com_iccgame_sdk_JniHelper_tester(JNIEnv *jniEnv, jobject jniObject, jint method,
                                           jstring paramJSON);
#ifdef __cplusplus
}
#endif

/**
 * 注册回调演示
 */
class RegisterCallback : public ICC_Callback {
    void result(const char *resultJSON);
};

/**
 * 登录回调演示
 */
class LoginCallback : public ICC_Callback {
    void result(const char *resultJSON);
};

/**
 * 支付回调演示
 */
class PayCallback : public ICC_Callback {
    void result(const char *resultJSON);
};

/**
 * 账号中心回调演示
 */
class CenterCallback : public ICC_Callback {
    void result(const char *resultJSON);
};

/**
 * 注销回调演示
 */
class LogoutCallback : public ICC_Callback {
    void result(const char *resultJSON);
};

#endif //__ICC_SDK_DEMO__
