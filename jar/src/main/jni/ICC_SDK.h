#ifndef __ICC_SDK__
#define __ICC_SDK__

// 引用类库
#include <stdio.h>
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <pthread.h>

// 日志函数简化
#define  LOG_ERROR(...) __android_log_print(ANDROID_LOG_ERROR, "ICCGAME SDK", __VA_ARGS__)
#define  LOG_DEBIG(...) __android_log_print(ANDROID_LOG_DEBUG, "ICCGAME SDK", __VA_ARGS__)

// 标记C++代码
#ifdef __cplusplus
extern "C" {
#endif
/**
 * 返回结果
 */
void Java_com_iccgame_sdk_JniHelper_result(JNIEnv *jniEnv, jobject jniObject, jint callback,
                                           jstring paramJSON);
#ifdef __cplusplus
}
#endif

/**
 * 回调接口
 */
class ICC_Callback {
public:
    /**
     * 操作结束后会回调此方法告知结果
     * 结果字符串由 json 组成，考虑后期的扩展
     */
    virtual void result(const char *resultJSON) = 0;
};

/**
 * ICCGAME SDK
 */
class ICC_SDK {
public:

    /**
     * 预定义常量
     */
    enum {
        METHOD_REGISTER = 1,
        METHOD_LOGIN = 2,
        METHOD_PAY = 4,
        METHOD_CENTER = 8,
        METHOD_LOGOUT = 16,
        METHOD_LOGIN_SWITCH = 32
    };

    /**
     * 引用实例
     */
    static ICC_SDK *getInstance();

    /**
    * 唤起注册或转正窗口
    * login 里面包含注册，可以忽略不用。
    */
    void regist(ICC_Callback *callback);

    /**
   * 唤起登录窗口
   */
    void login(ICC_Callback *callback);

    /**
     * 唤起支付窗口
     */
    void pay(const char *orderInfo, ICC_Callback *callback);

    /**
     * 唤起账号中心窗口
     */
    void center(ICC_Callback *callback);

    /**
     * 注销登录
     * 调用后，再次启动游戏时将不会自动登录。
     */
    void logout(ICC_Callback *callback);

    /**
     * 析构函数
     */
    ~ICC_SDK();

    /**
     * 结果回调
     */
    int executeCallback(int handle, const char *resultJSON);

protected:
    /**
     * 引用实例
     */
    static ICC_SDK *_instance;
    /**
     * 互斥锁
     */
    pthread_mutex_t _mutex;

    /**
     * 回调队列
     */
    ICC_Callback *_callbacks[4];

    /**
     * 最大调用队列
     */
    int _maxCallbackQueue;

    /**
     * 获取SDK入口函数信息
     */
    int callSDK(int method, ICC_Callback *callback, const char *params);

    /**
     * 构造函数
     */
    ICC_SDK();

    /**
     * 绑定回调
     */
    int registerCallback(ICC_Callback *callback);


};

#endif // ICC_SDK

