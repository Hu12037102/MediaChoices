package com.xiaobai.media.manger;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/8/19 19:51
 * 更新时间: 2020/8/19 19:51
 * 描述:
 */
public class ParcelableManger {
    private static ParcelableManger mInstance = new ParcelableManger();
    private Object object;

    private ParcelableManger() {

    }


    public static ParcelableManger get() {
        return mInstance;
    }

    public void putData(Object t) {
        this.object = t;
    }

    public Object getData() {
        return object;
    }
}
