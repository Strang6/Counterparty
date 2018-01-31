#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_strang6_counterparty_ApiServices_DaDataService_getDadataKey(JNIEnv *env, jobject instance) {
    return (*env) ->  NewStringUTF(env, "ODRkZTFkZmJjMmNkNWYxMDllOWU0ZjNmOGEyODNjMWY2NGRmZGEzMw==");
}

JNIEXPORT jstring JNICALL
Java_com_strang6_counterparty_ApiServices_GeocodingService_getGeocodingKey(JNIEnv *env, jobject instance) {
    return (*env) -> NewStringUTF(env, "QUl6YVN5QjZCb0JaWjJtSXRKN3h0RjhHdVZSaWV3bzAtWlJ3cDlR");
}