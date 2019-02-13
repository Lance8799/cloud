package org.lance.cloud.fixture;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.lance.cloud.domain.transmit.DataTransmit;
import org.lance.cloud.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局多功能的AOP拦截
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalAspect {

    private static Logger logger = LoggerFactory.getLogger(GlobalAspect.class);

//    private static ThreadLocal<Long> elapse = new ThreadLocal<>();
    private static Map<String, String> publicKeys = new HashMap<>();

    static {
        publicKeys.put("1000", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDovNC1bzzZ3lu//xLUIud8a8Fu\n" +
                "bvgXVxFu7Z3jbBjc3thot8gIrpZt951MkdfcUK091kHiDmwiAnMkDJvZI1Y9cWcF\n" +
                "gyKjczR1iDusUTPMGwHkligBx4ocVyoREz8mC0JliSnn8OKhutvnegyFsDI5lVuV\n" +
                "ZyFQPGbzvXtYIJ+cBwIDAQAB");
    }


    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void request() {
    }

    /**
     * 调用参数日志
     *
     * @param joinPoint
     */
//    @Before("request()")
//    public void paramLog(JoinPoint joinPoint) {
//        elapse.set(System.currentTimeMillis());
//        logger.info("方法：" + joinPoint.getSignature().getName() + " | 参数：" + joinPoint.getArgs());
//    }

    /**
     * 调用用时日志
     *
     * @param joinPoint
     */
//    @AfterReturning(value = "request()", returning = "ret")
//    public void countdownLog(JoinPoint joinPoint, Object ret) {
//        Long elapseTime = System.currentTimeMillis() - elapse.get();
//        logger.info("方法：" + joinPoint.getSignature().getName() + " | 用时：" + elapseTime + "毫秒 | 返回值：" + ret);
//        elapse.remove();
//    }


    /**
     * 调用方法用时日志
     * @param point
     * @return
     * @throws Throwable
     */
    @Around(value = "request()")
    public Object requestLog(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = point.proceed();
        long elapseTime = System.currentTimeMillis() - start;
        logger.info("调用方法名：{} | 用时：{}毫秒 | 返回值：[{}]", point.getSignature().getName(), elapseTime, proceed);
        return proceed;
    }

    /**
     * 调用方法异常处理
     * @param joinPoint
     */
    @AfterThrowing(value = "request()")
    public void afterThrowing(JoinPoint joinPoint) {
        System.err.println("[Aspect1] afterThrowing advise");
    }


    /**
     * 签名校验
     *
     * @param auth
     * @param sign
     * @param transfer
     */
    @Before("@annotation(org.lance.cloud.annotation.SignVerify) && args(auth, sign, transfer)")
    public void signVerify(String auth, String sign, DataTransmit transfer) {
        boolean isSign = RsaUtils.verify(transfer.json(), sign, publicKeys.get(auth));
        Assert.isTrue(isSign, "验签失败");
    }

}
