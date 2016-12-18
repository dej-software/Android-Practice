//
// Created by dej on 2016/11/28.
//

#include "com_jikexueyuan_androidclog_UsingCPrintLog.h"

#ifdef __cplusplus
extern "C" {
#endif

void Java_com_jikexueyuan_androidclog_UsingCPrintLog_usingCPrintLog
        (JNIEnv *env, jclass cls, jstring jstr) {

    const char *cstr;
    cstr = env->GetStringUTFChars(jstr, NULL);
    // 打印语句
    __android_log_print(ANDROID_LOG_INFO, "OUTPUT", "%s", cstr);
}

#ifdef __cplusplus
}
#endif
