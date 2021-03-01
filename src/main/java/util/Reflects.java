package util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.joor.Reflect.*;
import static util.Options.*;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2020-11-16 19:50
 * @description : 反射与字节码增强工具类，提供一些特殊方法增强函数或者在某些特殊场景使用的方法
 * 反射基于joor（jdk原生反射基础上的封装），字节码增强基于ByteBuddy
 */
public abstract class Reflects {
    /**
     * 通过反射获取一个对象所有的字段和值
     *
     * @param object 对象
     * @param <T>    对象类型
     * @return 字段和值的映射
     */
    public static <T> Map<String, Object> fieldsMap(T object) {
        return on(object).fields().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get(),
                        (oldVal, newVal) -> newVal
                ));
    }

    /**
     * 返回一个类的所有字段的集合，包含其从父类继承的字段
     *
     * @param clazz 类
     * @param <T>   类的类型
     * @return 字段集合
     */
    public static <T> Set<Field> fieldsSet(Class<? extends T> clazz) {
        Set<Field> result = setOf();
        Class<?> t = clazz;
        do {
            result.addAll(listOf(t.getDeclaredFields()));
            t = t.getSuperclass();
        }
        while (t != null);
        return result;
    }

    /**
     * 字节码增强转换类，具体增强方法不提供
     *
     * @param clazz    类
     * @param enhancer 增强方式
     * @param <T>      类的类型
     * @return 增强后的类型
     */
    public static <T> Class<? extends T> enhance(Class<? extends T> clazz,
                                                 Function<? super Class<? extends T>, ? extends Class<? extends T>> enhancer
    ) {
        return enhancer.apply(clazz);
    }

    /**
     * 使用反射调用方法
     *
     * @param clazz      类
     * @param returnType 方法的返回类型
     * @param methodName 方法名
     * @param <T>        方法返回类型
     * @param <C>        对象类型
     * @return 方法返回值
     */
    public static <T, C> T call(Class<C> clazz, Class<T> returnType, String methodName) {
        return returnType.cast(onClass(clazz).create().call(methodName).get());
    }

    /**
     * 使用反射调用方法
     *
     * @param clazz      类
     * @param returnType 方法的返回类型
     * @param methodName 方法名
     * @param args       方法参数
     * @param <T>        方法返回类型
     * @param <C>        对象类型
     * @return 方法返回值
     */
    public static <T, C> T call(Class<C> clazz, Class<T> returnType, String methodName, Object... args) {
        return returnType.cast(onClass(clazz).create().call(methodName, args).get());
    }

    /**
     * 可用获取泛型类的类型的包装类，创建一个此类的匿名类即可
     *
     * @param <T> 想要获取到其类型的类型，可以是泛型类型
     */
    public static class GenericsType<T> {
        private final Type type;

        public GenericsType() {
            this.type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

        public Type getType() {
            return type;
        }

    }

}
