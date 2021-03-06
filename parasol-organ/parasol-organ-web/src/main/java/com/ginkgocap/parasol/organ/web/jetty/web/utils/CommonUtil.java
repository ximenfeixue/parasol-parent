package com.ginkgocap.parasol.organ.web.jetty.web.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class CommonUtil {

	public static final String JTMOBILE_SERVER_ROOT = ResourceBundle.getBundle("application").getString("jtmobileserver.root");
	static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	/**
	 * 获取本机地址，格式为：http://www.gingont.com:4445/，或者为:http://192.168.101.41:4445/
	 * @param request
	 * @return
	 */
	public static String getServerDomainPath(HttpServletRequest request) {
		if (StringUtils.isNotBlank(JTMOBILE_SERVER_ROOT) && JTMOBILE_SERVER_ROOT.startsWith("http") && JTMOBILE_SERVER_ROOT.endsWith("/"))
			return JTMOBILE_SERVER_ROOT;
		if (null == request)
			throw new RuntimeException("传入参数不正确");
		StringBuilder link = new StringBuilder();
		link.append(request.getScheme()).append("://").append(request.getServerName());
		if ((request.getScheme().equals("http") && request.getServerPort() != 80)
				|| (request.getScheme().equals("https")
				&& request.getServerPort() != 443)) {
			link.append(':');
			link.append(request.getServerPort());
		}
		link.append(request.getContextPath());
		link.append("/");
		return link.toString();
	}
	
	/**
	 * web端的JavaScript对长整型的数值无法处理，因此需要转换为字符串
	 */
	public static GsonBuilder createGsonBuilder() {
		GsonBuilder gBuilder = new GsonBuilder().disableHtmlEscaping();
		if (CommonUtil.getRequestIsFromWebFlag() == false) {
			return gBuilder;//不是web端过来的请求，则长整型数值json后不加引号
		}
		gBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(Number.class, new TypeAdapter<Number>() {
			@Override
			public Number read(JsonReader in) throws IOException {
				JsonToken jsonToken = in.peek();
				switch (jsonToken) {
					case NULL:
						in.nextNull();
						return null;
					case NUMBER:
						return new LazilyParsedNumber(in.nextString());
					default:
						throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
				}
			}

			@Override
			public void write(JsonWriter out, Number value) throws IOException {
				if (value != null && (value instanceof Long || value instanceof BigInteger))
					out.value(value.toString());
				out.value(value);
			}
		}));
		gBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(BigInteger.class, new TypeAdapter<BigInteger>() {
			@Override
			public BigInteger read(JsonReader in) throws IOException {
				if (in.peek() == JsonToken.NULL) {
					in.nextNull();
					return null;
				}
				try {
					return new BigInteger(in.nextString());
				} catch (NumberFormatException e) {
					throw new JsonSyntaxException(e);
				}
			}

			@Override
			public void write(JsonWriter out, BigInteger value) throws IOException {
				if (null == value)
					out.value(value);
				out.value(value.toString());
			}
		}));
		gBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		return gBuilder;
	}
	
	/**
	 * web端的JavaScript对长整型的数值无法处理，因此需要转换为字符串
	 */
	public static Gson createGson() {
		Gson gson = createGsonBuilder().create();
		return gson;
	}

	public static long getLongFromJSONObject(JSONObject jsonObject, String key) {
//		String value = jsonObject.getString(key);
//		if (StringUtils.isBlank(value) || StringUtils.isNumeric(value) == false)
//			return 0L;
		return optLongFromJSONObject(jsonObject,key);
	}
	
	public static long optLongFromJSONObject(JSONObject jsonObject, String key) {
		String value = jsonObject.optString(key);
		long result = 0L;
		if (StringUtils.isBlank(value))
			return result;
		try {
			return Long.valueOf(value.trim());
		} catch (Exception e) {
			logger.error("convert string [ " + value.trim() + " ] into long error:" + e);
			return result;
		}
	}

	private static final ThreadLocal<Boolean> REQUEST_IS_FROM_WEB = new ThreadLocal<Boolean>(){
		protected Boolean initialValue() {
			return false;
		}
	};
	public static void setRequestIsFromWebFlag(boolean requestIsFromWeb) {
		REQUEST_IS_FROM_WEB.set(requestIsFromWeb);
	}
	public static boolean getRequestIsFromWebFlag() {
		return REQUEST_IS_FROM_WEB.get();
	}

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws java.beans.IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws java.lang.reflect.InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
}
