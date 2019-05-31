package com.yanqun.nio;
//Object
public class Person  extends  Object{
    private String name ;
    private int age  ;
    private double height ;

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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return name+","+age+","+height;
    }
}
