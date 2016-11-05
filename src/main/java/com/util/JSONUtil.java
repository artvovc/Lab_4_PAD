package com.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Artemie on 04.10.2016.
 */
public class JSONUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getJSONStringfromJAVAObject(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static Object getJAVAObjectfromJSONString(String json, Class klass) throws IOException {
        return objectMapper.readValue(json, klass);
    }

    public static Object getJAVAObjectfromJSONStringList(String json, TypeReference<?> typeReference) throws IOException {
        return objectMapper.readValue(json, typeReference);
    }

//    public static Object getJAVA_fromJSONStringList(String )
}