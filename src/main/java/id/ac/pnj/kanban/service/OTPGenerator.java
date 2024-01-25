package id.ac.pnj.kanban.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPGenerator {
    private Cache<String, Integer> cache = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

    public Integer generateOtp(String key) {
        Random random = new Random();
        int OTP = 100000 + random.nextInt(900000);
        cache.put(key, OTP);
        return OTP;
    }

    public Integer getOTPByKey(String key)
    {
        return cache.getIfPresent(key);
    }

    public void clearOtp(String key) {
        cache.invalidate(key);
    }



}
