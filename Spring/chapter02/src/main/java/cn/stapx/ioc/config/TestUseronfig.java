package cn.stapx.ioc.config;

import cn.stapx.ioc.bean.User;
import cn.stapx.ioc.bean.UserConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Version: 1.0
 * @Date: 2021/3/11 下午 01:51
 * @ClassName: TesttUseronfig
 * @Author: Stapxs
 * @Description TO DO
 **/
public class TestUseronfig {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(UserConfig.class);

        User user1 = (User)app.getBean("user1");
        User user2 = (User)app.getBean("user1");
        System.out.println(user1);
        System.out.println(user2);
        User user3 = (User)app.getBean("userStaticFactory");
        User user4 = (User)app.getBean("userInstanceFactory");
        System.out.println(user3);
        System.out.println(user4);
    }
}
