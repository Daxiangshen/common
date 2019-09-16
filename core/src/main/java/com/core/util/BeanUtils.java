package com.core.util;

import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeanUtils extends org.springframework.beans.BeanUtils {
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null)
            return null;

        T obj = beanClass.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }

        return obj;
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
    }


    public static List<Object> getResultData(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
        int columnCount = md.getColumnCount();   //获得列数
        List<Object> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<>();
            if (columnCount > 1) {
                for (int i = 1; i <= columnCount; i++) {
                    if (Types.TIMESTAMP == md.getColumnType(i) && rs.getTimestamp(i) != null) {
                        rowData.put(md.getColumnName(i), DateUtil.getTimeStr(rs.getTimestamp(i)));
                    } else {
                        rowData.put(md.getColumnName(i), rs.getObject(i));
                    }
                }
                list.add(rowData);
            } else {
                for (int i = 1; i <= columnCount; i++) {
                    String line = (String) rs.getObject(i);
                    String regex = "template|postgres";
                    Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                    Matcher m = r.matcher(line);
                    if (!m.find()) {
                        list.add(line);
                    }
                }
            }
        }
        return list;
    }

    public static Object convertJdbcType(Object o) {
        if (o instanceof String) {
            String line = (String) o;
            String regex = "^[0-9]*$";
            Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher m = r.matcher(line);
            if (m.find()) {
                return Integer.valueOf(line);
            }

            regex = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
            r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            m = r.matcher(line);
            if (m.find()) {
                return Date.valueOf(line);
            }

            regex = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
            r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            m = r.matcher(line);
            if (m.find()) {
                return Timestamp.valueOf(line);
            }
        }
        return o;
    }

    /**
     * 根据属性名获取属性值
     */
    public static double getFieldValue(String fieldName, Object object) {
        try {

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (double) field.get(object);
        } catch (Exception e) {

            return 0;
        }
    }

    /**
     * 通过json数据格式转换实现复制list
     */
    public static <T> List copyList(List<T> list, Class clazz) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }
        return JSON.parseArray(JSON.toJSONString(list), clazz);
    }

    /**
     * 通过json数据格式转换实现复制Map
     */
    public static Map<String, Object> copyMap(Map map) {
        return JSON.parseObject(JSON.toJSONString(map));
    }
}
