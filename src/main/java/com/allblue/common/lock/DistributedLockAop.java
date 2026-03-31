package com.allblue.common.lock;

import com.allblue.common.error.BusinessException;
import com.allblue.common.error.GlobalErrorCode;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK_DECK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.allblue.common.lock.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX
                + CustomSpringELParser.getDynamicValue(
                        signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());

        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available =
                    rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            if (!available) {
                log.warn("Redisson Lock 획득 실패 [{}]", key);
                throw new BusinessException(GlobalErrorCode.LOCK_ACQUISITION_FAILED);
            }

            log.debug("Redisson Lock 획득 [{}]", key);
            return aopForTransaction.proceed(joinPoint);

        } catch (InterruptedException e) {
            log.error("Redisson Lock 인터럽트 발생 [{}]", key, e);
            Thread.currentThread().interrupt();
            throw new BusinessException(GlobalErrorCode.INTERNAL_ERROR);
        } finally {
            try {
                if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                    log.debug("Redisson Lock 해제 [{}]", key);
                }
            } catch (IllegalMonitorStateException e) {
                log.warn("Redisson Lock 해제 실패 [{}]", key);
            }
        }
    }
}
