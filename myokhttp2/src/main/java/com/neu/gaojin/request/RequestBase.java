package com.neu.gaojin.request;

import android.os.Bundle;
import android.text.TextUtils;

import com.neu.gaojin.annotation.HttpMethod;
import com.neu.gaojin.annotation.HttpUrlParams;
import com.neu.gaojin.annotation.RequiredParam;
import com.neu.gaojin.entity.RequestEntity;
import com.neu.gaojin.exception.NetWorkException;
import com.neu.gaojin.mConstant;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by gaojin on 2017/4/10.
 */

public abstract class RequestBase<T> implements mConstant {
    //请求实体
    private RequestEntity mRequestEntity;

    public RequestEntity getmRequestEntity() throws NetWorkException {
        if (mRequestEntity != null) {
            return mRequestEntity;
        }
        mRequestEntity = new RequestEntity();
        mRequestEntity.setUrlParams(getURLParams());
        mRequestEntity.setRequiredParam(getRequiredParam());
        mRequestEntity.setRequestMethod(getMethodParam());
        return mRequestEntity;
    }

    protected Bundle getRequiredParam() throws NetWorkException {
        Class<?> c = this.getClass();
        ArrayList<Class<?>> classList = new ArrayList<>();
        while (c != RequestBase.class) {
            classList.add(0, c);
            c = c.getSuperclass();
        }
        Bundle ret = new Bundle();
        for (Class<?> cl : classList) {
            getHttpRequiredParam(cl, ret);
        }
        return ret;
    }

    protected Bundle getURLParams() throws NetWorkException {
        Class<?> c = this.getClass();
        ArrayList<Class<?>> classList = new ArrayList<>();
        while (c != RequestBase.class) {
            classList.add(0, c);
            c = c.getSuperclass();
        }
        Bundle ret = new Bundle();
        for (Class<?> cl : classList) {
            getHttpUrlParams(cl, ret);
        }
        return ret;
    }

    protected int getMethodParam() throws NetWorkException {
        Class<?> c = this.getClass();
        ArrayList<Class<?>> classList = new ArrayList<>();
        int method = -1;
        while (c != RequestBase.class) {
            classList.add(0, c);
            c = c.getSuperclass();
        }
        for (Class<?> cl : classList) {
            method = getHttpMethodParam(cl);
            if (method != -1) {
                break;
            }
        }
        return method;
    }

    private Bundle getHttpUrlParams(Class<?> c, Bundle bundle) throws NetWorkException {
        Field[] fields = c.getDeclaredFields();
        Bundle params = bundle;
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                //判断注解类是不是当前类的注解
                if (field.isAnnotationPresent(HttpUrlParams.class)) {
                    HttpUrlParams httpUrlParams = field.getAnnotation(HttpUrlParams.class);
                    if (httpUrlParams != null) {
                        String name = httpUrlParams.value();
                        Object object = field.get(this);
                        if (object == null) {
                            throw new NetWorkException("Param " + name + " MUST NOT be null");
                        }
                        String value = String.valueOf(object);
                        if (TextUtils.isEmpty(value)) {
                            value = "";
                        }
                        params.putString(name, value);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    private int getHttpMethodParam(Class<?> c) throws NetWorkException {
        int method = -1;
        if (c.isAnnotationPresent(HttpMethod.class)) {
            HttpMethod httpMethod = c.getAnnotation(HttpMethod.class);
            String name = httpMethod.value();
            if (!TextUtils.isEmpty(name)) {
                if ("GET".equals(name)) {
                    method = GET;
                } else if ("POST".equals(name)) {
                    method = POST;
                }
            } else {
                throw new NetWorkException("Param " + name + " MUST NOT be null");
            }
        }
        return method;
    }

    private Bundle getHttpRequiredParam(Class<?> c, Bundle bundle) throws NetWorkException {
        Field[] fields = c.getDeclaredFields();
        Bundle params = bundle;
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                //判断注解类是不是当前类的注解
                if (field.isAnnotationPresent(RequiredParam.class)) {
                    RequiredParam requiredParam = field.getAnnotation(RequiredParam.class);
                    if (requiredParam != null) {
                        String name = requiredParam.value();
                        Object object = field.get(this);
                        if (object == null) {
                            throw new NetWorkException("Param " + name + " MUST NOT be null");
                        }
                        String value = String.valueOf(object);
                        if (TextUtils.isEmpty(value)) {
                            value = "";
                        }
                        params.putString(name, value);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    /**
     * 获取T的类型
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getGenericType() {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return null;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params.length < 1) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[0] instanceof Class)) {
            return null;
        }
        return (Class<T>) params[0];
    }

}
