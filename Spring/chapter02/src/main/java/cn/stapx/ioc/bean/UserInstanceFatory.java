package cn.stapx.ioc.bean;

/**
 * @Version: 1.0
 * @Date: 2021/3/11 下午 01:59
 * @ClassName: UserInstanceFatory
 * @Author: Stapxs
 * @Description TO DO
 **/
public class UserInstanceFatory {
    public static User creatInstane() {
        System.out.println("工厂方法实例化");
        return  new User();
    }
}
