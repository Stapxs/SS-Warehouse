package cn.stapx.ioc.config;

import cn.stapx.ioc.service.TestService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Version: 1.0
 * @Date: 2021/3/11 下午 02:12
 * @ClassName: TestServieConfig
 * @Author: Stapxs
 * @Description TO DO
 **/
public class TestServieConfig {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(TestService.class);
        TestService testService = app.getBean(TestService.class);
        System.out.println(testService);
    }
}
