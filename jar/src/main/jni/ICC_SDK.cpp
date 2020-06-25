// 引用类库
#include "ICC_SDK.h"

// 初始实例
static JavaVM *gJVM = NULL;
ICC_SDK *ICC_SDK::_instance = NULL;

/**
 * 回调通知统一处理
 */
void Java_com_iccgame_sdk_JniHelper_result(JNIEnv *jniEnv, jobject jniObject, jint callback,
                                           jstring paramJSON) {
    // 转换格式
    const char *resultJSON = jniEnv->GetStringUTFChars(paramJSON, NULL);
    // 显示日志
    LOG_DEBIG("PROXY ICC_Callback->result(%s)", resultJSON);
    // 回调结果
    ICC_SDK::getInstance()->executeCallback(callback, resultJSON);
    // 释放资源
    jniEnv->ReleaseStringUTFChars(paramJSON, resultJSON);
}

/**
 * 引用实例
 */
ICC_SDK *ICC_SDK::getInstance() {
    if (_instance == NULL) {
        _instance = new ICC_SDK();
    }
    return _instance;
}

/**
 * 构造函数
 */
ICC_SDK::ICC_SDK() {
    // 初始化互斥管理器
    pthread_mutex_init(&this->_mutex, NULL);
    // 计算最大回调队列
    this->_maxCallbackQueue = sizeof(this->_callbacks) / sizeof(ICC_Callback);
    LOG_DEBIG("PROXY Callback Queue(%d) Initialization", this->_maxCallbackQueue);
    // 开始序列化调用
    pthread_mutex_lock(&this->_mutex);
    for (int i = 0; i < this->_maxCallbackQueue; i++) {
        this->_callbacks[i] = NULL;
    }
    // 结束序列化调用
    pthread_mutex_unlock(&this->_mutex);
}

/**
 * 析构实例
 */
ICC_SDK::~ICC_SDK() {
    // 销毁互斥管理器
    pthread_mutex_destroy(&this->_mutex);
    // 释放资源
    if (_instance != NULL) {
        delete _instance;
    }
}

/**
 * 调用 SDK 方法
 * C++ 调用 JAVA
 */
int ICC_SDK::callSDK(int method, ICC_Callback *callback, const char *params) {
    static JNIEnv *jniEnv = NULL;
    if (jniEnv == NULL) {
        gJVM->AttachCurrentThread(&jniEnv, NULL);
    }
    if (jniEnv == NULL) {
        LOG_ERROR("PROXY JNIEnv is NULL.");
        return -1;
    }
    // 判断对象
    jclass jniClass = 0;
    jniClass = jniEnv->FindClass("com/iccgame/sdk/JniHelper");
    //jniClass = jniEnv->FindClass("com/iccgame/sdk/ICC_Cocos2dxActivity");
    if (!jniClass) {
        LOG_ERROR("PROXY class com.iccgame.sdk.JniHelper not find.");
        return -2;
    }
    // 判断方法
    jmethodID jniMethod = 0;
    jniMethod = jniEnv->GetStaticMethodID(jniClass, "callSdkMethod",
                                          "(IILjava/lang/String;)V");
    if (!jniMethod) {
        LOG_ERROR("PROXY method ICC_SDKJni.callSdkMethod(int, int, String) not find.");
        return -3;
    }
    // 调用 SDK 实体
    int handle = this->registerCallback(callback);
    if (handle < 0) {
        LOG_ERROR("PROXY callback queue is full.");
        return -4;
    }
    // 调用实体
    jniEnv->CallStaticVoidMethod(jniClass, jniMethod, method, handle,
                                 jniEnv->NewStringUTF(params));
    return 0;
}

/**
 * 绑定一个回调函数
 */
int ICC_SDK::registerCallback(ICC_Callback *callback) {
    int handle = -1;
    for (int i = 0; i < this->_maxCallbackQueue; i++) {
        if (this->_callbacks[i] != NULL) {
            continue;
        }
        this->_callbacks[i] = callback;
        handle = i;
        break;
    }
    pthread_mutex_unlock(&this->_mutex);
    if (handle > -1) {
        LOG_DEBIG("PROXY register callback(%d)", handle);
    }
    return handle;
}

/**
 * 执行一个回调函数
 */
int ICC_SDK::executeCallback(int handle, const char *resultJSON) {
    if (handle < 0 || handle >= this->_maxCallbackQueue) {
        LOG_ERROR("PROXY ICC_Callback[%d] not find.", handle);
        return -1;
    }
    pthread_mutex_lock(&this->_mutex);
    if (this->_callbacks[handle] == NULL) {
        LOG_ERROR("PROXY ICC_Callback[%d] not find.", handle);
        return -2;
    }
    ICC_Callback *callback = this->_callbacks[handle];
    this->_callbacks[handle] = NULL;
    pthread_mutex_unlock(&this->_mutex);
    LOG_DEBIG("PROXY execute callback(%d)", handle);
    callback->result(resultJSON);
    LOG_DEBIG("PROXY callback(%d) removed", handle);
    return 0;
}

/**
 * 游戏注册/转正
 */
void ICC_SDK::regist(ICC_Callback *callback) {
    LOG_DEBIG("PROXY ICC_SDK->regist(ICC_Callback*)");
    // 默认参数
    char paramJSON[] = {0};
    // 调用方法
    this->callSDK(ICC_SDK::METHOD_REGISTER, callback, paramJSON);
}

/**
 * 游戏登录
 */
void ICC_SDK::login(ICC_Callback *callback) {
    LOG_DEBIG("PROXY ICC_SDK->login(ICC_Callback*)");
    // 默认参数
    char paramJSON[] = {0};
    // 调用方法
    this->callSDK(ICC_SDK::METHOD_LOGIN, callback, paramJSON);
}

/**
 * 游戏支付
 */
void ICC_SDK::pay(const char *tradeInfo, ICC_Callback *callback) {
    LOG_DEBIG("PROXY ICC_SDK->pay(char*, ICC_Callback*)");
    // 调用方法
    this->callSDK(ICC_SDK::METHOD_PAY, callback, tradeInfo);
}

/**
 * 游戏账号中心
 */
void ICC_SDK::center(ICC_Callback *callback) {
    LOG_DEBIG("PROXY ICC_SDK->center(ICC_Callback*)");
    // 默认参数
    char paramJSON[] = {0};
    // 调用方法
    this->callSDK(ICC_SDK::METHOD_CENTER, callback, paramJSON);
}

/**
 * 游戏注销
 */
void ICC_SDK::logout(ICC_Callback *callback) {
    LOG_DEBIG("PROXY ICC_SDK->logout(ICC_Callback*)");
    // 默认参数
    char paramJSON[] = {0};
    // 调用方法
    this->callSDK(ICC_SDK::METHOD_LOGOUT, callback, paramJSON);
}

/*
* This is called by the VM when the shared library is first loaded.
*/
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOG_DEBIG("PROXY Initialization");
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    gJVM = vm;
    return JNI_VERSION_1_4;
};