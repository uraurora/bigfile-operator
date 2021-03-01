package reflect;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-26 16:24
 * @description :类的方法增强工具类，借助ByteBuddy的使用增强类方法
 */

import com.google.common.collect.Sets;
import util.Reflects;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.joor.Reflect.onClass;
import static util.Options.setOf;
import static util.Reflects.enhance;

public class ClassEnhancer<T> {
    /**
     * 增强后的类
     */
    private final Class<? extends T> clazz;
    /**
     * 增强后的类名
     */
    private final String name;
    /**
     * 增强类新增声明的方法
     */
    private final Set<String> extensionMethods = setOf();

    private ClassEnhancer(ClassEnhancerBuilder<T> builder) {
        Function<? super Class<? extends T>, ? extends Class<? extends T>> enhancer = builder.enhancer;
        Class<? extends T> aClass = builder.clazz;
        Set<String> beforeMethods = setOf(aClass.getDeclaredMethods()).stream()
                .map(Method::getName)
                .collect(Collectors.toSet());
        this.clazz = enhance(aClass, enhancer);
        this.name = this.clazz.getName();
        Set<String> afterMethods = setOf(this.clazz.getDeclaredMethods()).stream()
                .map(Method::getName)
                .collect(Collectors.toSet());
        extensionMethods.addAll(Sets.difference(afterMethods, beforeMethods));
    }

    public static <T> ClassEnhancerBuilder<T> builder() {
        return new ClassEnhancerBuilder<>();
    }

    public static class ClassEnhancerBuilder<T> {
        private Class<? extends T> clazz;
        private Function<? super Class<? extends T>, ? extends Class<? extends T>> enhancer;

        public ClassEnhancerBuilder<T> withClazz(Class<? extends T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public ClassEnhancerBuilder<T> withEnhancer(Function<? super Class<? extends T>, ? extends Class<? extends T>> enhancer) {
            this.enhancer = enhancer;
            return this;
        }

        public ClassEnhancer<T> build() {
            return new ClassEnhancer<>(this);
        }
    }

    public boolean extensionMethodCheck(String name) {
        return extensionMethods.contains(name);
    }

    public Class<? extends T> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public Set<String> getExtensionMethods() {
        return extensionMethods;
    }

    /**
     * 指定方法名调用方法，专门针对对现有类动态新增方法的场景使用
     *
     * @param returnType 返回类型
     * @param methodName 方法名
     * @param <P>        方法返回类型
     * @return 方法返回值
     */
    public <P> P call(Class<P> returnType, String methodName) {
        return Reflects.call(clazz, returnType, methodName);
    }

    /**
     * 指定方法名调用方法，专门针对对现有类动态新增方法的场景使用
     *
     * @param returnType 返回类型
     * @param methodName 方法名
     * @param args       方法参数
     * @param <P>        方法返回类型
     * @return 方法返回值
     */
    public <P> P call(Class<P> returnType, String methodName, Object... args) {
        return Reflects.call(clazz, returnType, methodName, args);
    }

    /**
     * 通过反射创建新实例，调用无参构造函数
     *
     * @return 实例
     */
    public T create() {
        return onClass(clazz).create().get();
    }

    /**
     * 通过反射创建新实例，调用有参构造函数
     *
     * @param args 构造函数参数
     * @return 实例
     */
    public T create(Object... args) {
        return onClass(clazz).create(args).get();
    }
}

