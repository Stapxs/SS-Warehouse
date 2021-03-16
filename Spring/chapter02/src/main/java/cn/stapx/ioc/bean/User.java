package cn.stapx.ioc.bean;

/**
 * @Version: 1.0
 * @Date: 2021/3/11 下午 01:45
 * @ClassName: User
 * @Author: Stapxs
 * @Description TO DO
 **/
public class User {
    private String name;
    private int age;

    public User(){
        System.out.println("构造方法创建user对象");
        this.name = "无名氏";
        this.age = 0;
    }

    public User(String name) {
        this.name = name;
    }

    public User(int age) {
        this.age = age;
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}