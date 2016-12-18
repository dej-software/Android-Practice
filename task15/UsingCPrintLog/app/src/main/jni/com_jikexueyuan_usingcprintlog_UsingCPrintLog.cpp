//
// Created by dej on 2016/11/28.
//

#include "com_jikexueyuan_usingcprintlog_UsingCPrintLog.h"

void JNICALL Java_com_jikexueyuan_usingcprintlog_UsingCPrintLog_usingCPrintLog
  (JNIEnv *env, jclass cls, jstring str) {

  std::cout >> str >> std::endl;
}
