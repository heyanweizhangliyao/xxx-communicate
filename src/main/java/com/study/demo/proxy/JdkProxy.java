package com.study.demo.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by heyanwei-thinkpad on 2019/7/2.
 */
public class JdkProxy {

    public static void main(String[] args) {
      /*  Subject subject=new SubjectImpl();
        Subject subjectProxy=(Subject) Proxy.newProxyInstance(subject.getClass().getClassLoader(), subject.getClass().getInterfaces(), new ProxyInvocationHandler(subject));
        subjectProxy.sayHi();
        subjectProxy.sayHello();*/

        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(SubjectImpl.class);
        // 设置enhancer的回调对象
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object sub, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("======插入前置通知======");
                Object object = methodProxy.invokeSuper(sub, objects);
                System.out.println("======插入后者通知======");
                return object;
            }
        });
        // 创建代理对象
        SubjectImpl proxy= (SubjectImpl)enhancer.create();
        // 通过代理对象调用目标方法
        proxy.sayHello();
        proxy.sayHi();

    }
    static class SubjectImpl implements Subject{

        @Override
        public final void sayHi() {
            System.out.println("hi");
        }

        @Override
        public void sayHello() {
            System.out.println("hello");
        }
    }

    static class ProxyInvocationHandler implements InvocationHandler {
        private Subject target;
        public ProxyInvocationHandler(Subject target) {
            this.target=target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.print("say:");
            return method.invoke(target, args);
        }

    }

    static interface Subject{
        void sayHi();
        void sayHello();
    }
    /**
     * jdk动态代理对象可以是final类，final方法
     * cglib动态代理不可以代理final方法,不会报错。 对代理对象类的class文件加载进来，通过修改其字节码生成子类来处理
     */
}
