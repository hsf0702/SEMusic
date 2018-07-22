package com.se.music.retrofit.base

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.internal.`$Gson$Types`
import java.io.Reader
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *Author: gaojin
 *Time: 2018/6/23 下午5:09
 */

class ConverterDataInterceptor : ConvertIntercepter {
    override fun onConvert(type: Type, gson: Gson, reader: Reader): Any? {
        val clz = getConvertDataClass(type) ?: return null
        //! copy from old code.
        //! 我也不知道为什么原来的代码要这么写，元素持有集合,然后由元素生成集合对象
        return try {
            val converter = clz.newInstance() as ConvertData<*>
            val rootElement = JsonParser().parse(reader)
            val convertData = converter.convertData(rootElement)
            convertData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private fun getConvertDataClass(type: Type): Class<*>? {

        val cls = `$Gson$Types`.getRawType(type)
        //要对list等类型进行判断
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            if (types.isNotEmpty()) {
                for (tmpType in types) {
                    val tmpClass = `$Gson$Types`.getRawType(tmpType)
                    if (ConvertData::class.java.isAssignableFrom(tmpClass)) {
                        return tmpClass
                    }
                }
            }
        }
        return if (ConvertData::class.java.isAssignableFrom(cls)) {
            cls
        } else null
    }
}