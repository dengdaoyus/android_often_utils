// IMyAidlInterface2.aidl
package aidl.sketchemo.sketchemo;

// Declare any non-default types here with import statements
import aidl.sketchemo.sketchemo.ITaskCallBack;
interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);


        void registerCallback(ITaskCallback cb);
        void unregisterCallback(ITaskCallback cb);
        void func1(int commandType,ITaskCallback callback);

}
