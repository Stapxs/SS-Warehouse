package cn.stapx.ioc.bean;

import org.springframework.context.annotation.Bean;

/**
 * @Version: 1.0
 * @Date: 2021/3/11 下午 01:44
 * @ClassName: UserConfig
 * @Author: Stapxs
 * @Description TO DO
 **/
public class UserConfig {
    @Bean(value="user1")
    public User getUser1() {
        return new User("李四", 40);
    }
    @Bean(value="user2")
    public User getUser2() {
        return new User("张三",  30);
    }

    @Bean(value="userStaticFactory")
    public User getUserStaticFactory() {
        return  UserStaticFactory.createInstance();
    }
    @Bean(value="userInstanceFactory")
    public User getUserInstanceFactory() {
        return  UserInstanceFatory.creatInstane();
    }
}
