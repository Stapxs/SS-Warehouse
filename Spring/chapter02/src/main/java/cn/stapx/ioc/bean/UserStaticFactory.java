package cn.stapx.ioc.bean;

/**
 * @Version: 1.0
 * @Date: 2021/3/11 下午 01:56
 * @ClassName: UserStaticFacory
 * @Author: Stapxs
 * @Description TO DO
 **/
public class UserStaticFactory {
    private static User instance = new User();
    public static User createInstance() {
        System.out.println("静态工厂实例化");
        return instance;
    }
}
