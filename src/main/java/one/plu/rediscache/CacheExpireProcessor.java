package one.plu.rediscache;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class CacheExpireProcessor implements CommandLineRunner {
    Map<String, Long> expireMap = new HashMap<>();

    @Autowired
    RedisCacheManager redisCacheManager;
    @Override
    public void run(String... args) throws Exception {
        Reflections reflections = new Reflections("one.plu.rediscache.controller",new MethodAnnotationsScanner());
        Set<Method> typesAnnotatedWith = reflections.getMethodsAnnotatedWith(CacheExpire.class);
        for (Method method : typesAnnotatedWith) {
            Annotation[] annotations = method.getAnnotations();
            String[] cacheNames = {};
            long expireAfter = 0;
            for (Annotation annotation : annotations) {
                if (annotation instanceof Cacheable) {
                    cacheNames = ((Cacheable) annotation).cacheNames();
                }
                if (annotation instanceof CacheExpire) {
                    expireAfter = ((CacheExpire) annotation).expireAfter();
                }
            }
            if (cacheNames.length > 0 && expireAfter > 0) {
                log.info("cacheNames:{}", cacheNames);
                log.info("expireAfter:{}", expireAfter);
                for (String cacheName : cacheNames) {
                    expireMap.put(cacheName, expireAfter);
                }
            }
        }

        if (!expireMap.isEmpty()) {
            redisCacheManager.setExpires(expireMap);
            log.info("redis expire map has been set!");
        }
    }
}
