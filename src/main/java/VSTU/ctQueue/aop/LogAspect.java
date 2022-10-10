package VSTU.ctQueue.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

    private static Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* VSTU.ctQueue..*(..))")
    public void log() {
    }

    @Around(value = "execution(public * VSTU.ctQueue.service.EntrantService.register(..))")
    public Object registerTimeLogger(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = jp.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("Method name: {}, executionTime: {} ms", jp.getSignature(), executionTime);
        return proceed;
    }

    @Before("log()")
    public void beforeAdvice(JoinPoint jp) {
        StringBuilder args = new StringBuilder();
        for (Object obj : jp.getArgs()) {
            if (null != obj)
                args.append(obj.getClass() + "  ");
        }
        log.debug("beforeAdvice-Method name: {}, input ARGS: {}", jp.getSignature().getName(), args);
    }

    @AfterReturning(value = "log()", returning = "string")
    public void afterAdvice(JoinPoint jp, String string) {
        log.debug("afterAdvice-Method name: {}, , method return: {}", jp.getSignature().getName(), string);
    }

    @AfterThrowing(value = "log()", throwing = "e")
    public void throwingAdvice(JoinPoint jp, Throwable e) {
        log.error("throwingAdvice-Method name: {}, method return: {}", jp.getSignature().getName(), e.getMessage());
    }
}
