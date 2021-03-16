package cn.stapx.ioc.service;

import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Version: 1.0
 * @Date: 2021/3/11 下午 02:06
 * @ClassName: TestService
 * @Author: Stapxs
 * @Description TO DO
 **/
@Service
public class TestService {
    @PostConstruct
    public void initService(){
        System.out.println("initMethod. 传递一些参数，用来进行接受相关初始化数据");
    }
    public TestService(){
        System.out.println("构造方法");
    }
    @PreDestroy
    public void destroyService(){
        System.out.println("destoryMethod,对象被销毁时做的工作");
    }
}
