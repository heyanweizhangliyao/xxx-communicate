package com.study.demo.pieces;

import javassist.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by 80263843 on 2019/7/23.
 */
public class CaseDemo {

    /**
     * System.err与System.out共用时输出乱序
     */
    public static void testPrintWithoutOrder(){


        System.out.println("start...");
        System.err.println("middle...");
        System.out.println("end...");

    }


    /**
     * 动态生成java类
     * @throws Exception
     */
    public static void createUser() throws Exception{
        ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        //定义类
        CtClass ctClass = pool.makeClass("com.oppo.example.casetest.User");
        //添加属性
        CtField age = new CtField(CtClass.intType,"age",ctClass);
        ctClass.addField(age);

        ctClass.addField(new CtField(pool.get("java.lang.String"),"name",ctClass));


        //创建方法
        ctClass.addMethod(CtNewMethod.make("public void setName(String name){this.name=name;}",ctClass));
        ctClass.addMethod(CtNewMethod.make("public String getName(){return this.name;}",ctClass));
        ctClass.addMethod(CtNewMethod.make("public void setAge(int age){this.age=age;}",ctClass));
        ctClass.addMethod(CtNewMethod.make("public int getAge(){return this.age;}",ctClass));

        Class<?> clazz = ctClass.toClass();

        System.out.println("class:" + clazz.getName());
        System.out.println("属性列表----------");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getType() + ":" + field.getName());
        }

        System.out.println("方法列表------------------------");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getReturnType() + "-" + method.getName() + "-"
                    + Arrays.toString(method.getParameterTypes()));
        }

    }

    public static void updateMethod() throws Exception{
        ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        CtClass caseDemoClass = pool.get("com.study.demo.pieces.JavassistTest");

        CtMethod sayHelloMethod = caseDemoClass.getDeclaredMethod("sayHello");
        sayHelloMethod.setName(sayHelloMethod.getName()+"$copy");
        //复制原来的方法
        CtMethod copyMethod = CtNewMethod.copy(sayHelloMethod, "sayHello", caseDemoClass, null);

        //需要增强的代码
        // 注入的代码
        StringBuilder body = new StringBuilder();

        body.append("{\nlong start = System.currentTimeMillis();\n");
        // 调用原有代码，类似于method();  ($$)表示所有的参数
        body.append("sayHello$copy($$);\n");
        body.append("System.out.println(\" take \" +\n (System.currentTimeMillis()-start) + " + "\" ms.\");\n");

        body.append("}");

        copyMethod.setBody(body.toString());
        caseDemoClass.addMethod(copyMethod);

        JavassistTest instance = (JavassistTest) caseDemoClass.toClass().newInstance();
        instance.sayHello("wangwu");

    }




    public static void main(String[] args) {
        try {
            updateMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
