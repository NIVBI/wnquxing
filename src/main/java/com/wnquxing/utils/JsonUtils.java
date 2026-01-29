package com.wnquxing.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wnquxing.entity.enums.ResponseCodeEnum;
import com.wnquxing.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JsonUtils {

    public static SerializerFeature[] FEATURES = new SerializerFeature[]{SerializerFeature.WriteMapNullValue};

    public static String convertObj2Json(Object obj) {
        return JSON.toJSONString(obj, FEATURES);
    }

    public static <T> T convertJson2Obj(String json, Class<T> clazz) throws BusinessException {
        try {
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("convertJson2Obj异常, json:{}", json);
            throw new BusinessException(ResponseCodeEnum.CODE_601);
        }
    }

    public static <T> List<T> convertJsonArray2List(String json, Class<T> clazz) throws BusinessException {
        try {
            return JSONArray.parseArray(json, clazz);
        } catch (Exception e) {
            log.error("convertJsonArray2List异常, json:{}", json, e);
            throw new BusinessException(ResponseCodeEnum.CODE_601);
        }
    }
}
