package redis.poc.redisApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfiguration {


    @Autowired
    RedisProperties properties;

    /**
     * Redis configuration
     *
     * @return redisStandaloneConfiguration
     */
//    @Bean
//    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(properties.getHostName(), properties.getPort());
//        redisStandaloneConfiguration.setPassword(properties.getPassword());
//        return redisStandaloneConfiguration;
//    }

    /**
     * Client Options
     * Reject requests when redis is in disconnected state and
     * Redis will retry to connect automatically when redis server is down
     *
     * @return client options
     */
//    @Bean
//    public ClientOptions clientOptions() {
//        return ClientOptions.builder()
//                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
//                .autoReconnect(true)
//                .build();
//    }

    /**
     * Create a LettuceConnection with redis configurations and client options
     *
     * @return RedisConnectionFactory
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        JedisConnectionFactory factory = new JedisConnectionFactory();
//        factory.setPassword("sOmE_sEcUrE_pAsS");

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("redis", 6379);
        return new JedisConnectionFactory(config);
    }


    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public Jackson2JsonRedisSerializer<Student> jacksonJsonRedisJsonSerializer() {
        Jackson2JsonRedisSerializer<Student> jacksonJsonRedisJsonSerializer = new Jackson2JsonRedisSerializer<>(Student.class);
        return jacksonJsonRedisJsonSerializer;
    }

    /**
     * Redis template use redis data access
     *
     * @param redisConnectionFactory redisConnectionFactory
     * @return redisTemplate
     */
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Student> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Student> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer());
        template.setValueSerializer(jacksonJsonRedisJsonSerializer());
        return template;
    }
}