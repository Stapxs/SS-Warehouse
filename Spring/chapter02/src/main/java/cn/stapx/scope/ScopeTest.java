package cn.stapx.scope;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ScopeTest {
    public static void main(String[] args) {
//        加载配置文件
        ApplicationContext ac = new ClassPathXmlApplicationContext("scopeBeans.xml");
        Scope scope1 = ac.getBean("scope",Scope.class);
        Scope scope2 = ac.getBean("scope",Scope.class);
        System.out.println(scope1);
        System.out.println(scope2);
    }
}