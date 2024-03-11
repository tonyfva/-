package cn.heyige.excelpdf.aop;

import cn.heyige.excelpdf.anno.MethodCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect // 标注当前类是一个增强类
@Component
@RequiredArgsConstructor
public class CalcProcessTimeAspect {

    final RedisTemplate<String, Object> redisTemplate;


    // 5种增强类型[前置 后置 环绕 异常 最终]

    // ??? 目标方法是谁？ 增强要切入到什么地方
    // 定义一个切入点 [可以使用注解或者切入点表达式]
    // @Pointcut("execution(* cn.heyige.excelpdf.service.*..*(..))")

    // 想切入到某一个注解标注的方法上
    // 定义切入点有两种方式：1、使用execution切入点表达式 2、使用@annotation注解
    @Pointcut("@annotation(cn.heyige.excelpdf.anno.TimeLog)")
    public void serviceMethod() {
    }


    @Pointcut("@annotation(cn.heyige.excelpdf.anno.MethodCache)")
    public void methodCache() {
    }

    /**
     * 环绕增强的方法
     *
     * @return {@link Object}
     */
    @Around("serviceMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 目标方法执行之前做什么
        // 使用定时器计算时间
        StopWatch watch = new StopWatch();
        // 开始计时
        watch.start();
        // 让目标方法继续执行
        Object proceed = joinPoint.proceed();
        // 目标方法执行后做什么
        // 结束计时
        watch.stop();
        // 获取计时器的运行时间
        long millis = watch.getTotalTimeMillis();
        log.info("方法的执行时间是:{}", millis);
        return proceed;
    }


    /**
     * 自定义缓存注解的
     * 环绕增强的方法
     *
     * @return {@link Object}
     */
    @Around("methodCache()")
    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上标注的注解
        Method method = methodSignature.getMethod();
        MethodCache methodCache = method.getAnnotation(MethodCache.class);
        String key = methodCache.value();

        if (!StringUtils.hasText(key)) {
            // 如果用户没有指定key 那么使用方法名作为key
            key = method.getName();
        }
        Object result = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(result)) {
            log.info("缓存中没有数据，从数据库取数据");
            // 缓存种没有数据
            Object proceed = joinPoint.proceed();
            // 把目标方法的返回值放入redis中
            String ttl = methodCache.ttl();
            redisTemplate.opsForValue().set(key, proceed, Long.parseLong(ttl), TimeUnit.SECONDS);
        }
        return result;
    }

}
