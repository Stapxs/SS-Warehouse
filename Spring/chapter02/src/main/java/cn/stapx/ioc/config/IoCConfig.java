package cn.stapx.ioc.config;


import cn.stapx.domain.User;
import cn.stapx.ioc.bean.MyBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan("com.chuhelan.ioc")
public class IoCConfig {
    @org.springframework.context.annotation.Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    MyBean getBean(){
        return new MyBean();
    }

    @Bean(name="user1")
    User getUser1(){
        return  new User("Tom",20);
    }
}