package com.edatasite.workforce.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MethodExecutionTimeAspect {

    private final MethodExecutionStatisticService methodExecutionStatisticService;

    public MethodExecutionTimeAspect(MethodExecutionStatisticService methodExecutionStatisticService) {
        this.methodExecutionStatisticService = methodExecutionStatisticService;
    }

    @Around("execution(* com.edatasite.workforce.rest..*(..)) " +
            "|| execution(* com.edatasite.workforce.gwt..*(..)) ")/**/
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return process(joinPoint);
    }

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTimeByAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        return process(joinPoint);
    }

    private Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            final long start = System.currentTimeMillis();
            final Object proceed = joinPoint.proceed();
            String methodWithPackageKey = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            methodExecutionStatisticService.accept(methodWithPackageKey, System.currentTimeMillis() - start);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }
}

