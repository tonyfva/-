package cn.heyige.excelpdf.anno;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodCache {

    /**
     * key的名称
     * 可以指定也可以自动给获取
     *
     * @return {@link String}
     */
    String value() default "";

    /**
     * ttl
     * key的过期时间 默认值10秒
     *
     * @return {@link String}
     */
    String ttl() default "10";

}
