LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := usingCLog

LOCAL_SRC_FILES := com_jikexueyuan_usingcprintlog_UsingCPrintLog.cpp

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)

