## Beans

### 实例化

User.java - 用户类

~~~java
public class User {
    private String name;
    private int age;
}
~~~

#### 静态构造器

beans.xml

~~~xml
<!-- 静态 Bean -->
<bean id="bean" class="cn.stapxs.index.Bean"/>
~~~

UserTest.java

~~~java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/beans.xml"})
class UserTest {
    @Autowired
    Bean bean;

    @Test
    public void test() {
        System.out.println(bean);
    }
}
~~~

#### 静态工厂

beans.xml

~~~xml
<!-- 静态工厂 -->
<bean id="bean2" class="cn.stapxs.staticFactory.BeanFactory" factory-method="creater"/>
~~~

BeanFactory.java

~~~java
public class BeanFactory {
    public static Bean creater() { 
        return new Bean();
    }
}
~~~

UserTest.java

~~~java
@Autowired
@Qualifier("bean2")
Bean bean2;
~~~

#### 动态工厂

beans.xml

~~~xml
<!-- 动态工厂 -->
<bean id="factory1" class="cn.stapxs.staticFactory.BeanFactory1"/>
<bean id="bean3" factory-bean="factory1" factory-method="creater"/>
~~~

BeanFactory1.java

~~~java
public class BeanFactory1 {
    public BeanFactory1() {
        System.out.println("动态工厂正在实例化……");
    }
    public Bean creater() {
        return new Bean();
    }
}
~~~

UserTest.java

~~~java
@Autowired
@Qualifier("bean3")
Bean bean3;
~~~

### 作用域

- singleton（单例） -  只会创建一个实例
- protoype （原型）-  每次获取 Bean 都将创建新的实例

#### singleton

创建 Bean 默认为 singleton 作用域

#### protoype

beanScope.xml

~~~xml
<bean id="scope" class="cn.stapxs.Scope.Scope" scope="prototype"/>
~~~

ScopeTest.java

~~~java
public class ScopeTest {
    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext("beansScope.xml");
        System.out.println(app.getBean("scope"));
        System.out.println(app.getBean("scope"));
    }
}
~~~

可以看到结果返回的两个 Bean 是不同的

~~~
cn.stapxs.Scope.Scope@54a7079e
cn.stapxs.Scope.Scope@26e356f0
~~~

#### 注解使用作用域

~~~java
@Configuration
@ComponentScan("cn.stapx.ioc")
public IoConfig {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TestDao test() {
        return new TestDaoImpl();
    }
}
~~~

是