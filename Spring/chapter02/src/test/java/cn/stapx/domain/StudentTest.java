package cn.stapx.domain;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext("stuBeans.xml");
        Student stu1 = (Student)app.getBean("stu1");
        Student stu2 = app.getBean("stu2", Student.class);
        System.out.println(stu1);
        System.out.println(stu2);
    }
}