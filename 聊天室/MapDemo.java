package com.yanqun.nio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
/*
List:可以重复 a a ，有序（输入 和输出顺序一直）
Set:不能重复a，无序

 */
public class MapDemo {

    public static void main(String[] args) {
        //ArrayList<String> list = new ArrayList<>();

        HashMap<String,Person> map = new HashMap<>();
        Person zs = new Person();
        zs.setAge(23);
        zs.setName("zs");
        zs.setHeight(170);


        Person ls = new Person();
        ls.setAge(24);
        ls.setName("ls");
        ls.setHeight(160);


        Person ww = new Person();
        ww.setAge(25);
        ww.setName("ww");
        ww.setHeight(180);

        Person zl = new Person();
        zl.setAge(26);
        zl.setName("zl");
        zl.setHeight(177);

        map.put("s01",zl) ;//key-value 键值对
        map.put("s02",ls) ;//key-value 键值对
        map.put("s03",ww) ;//key-value 键值对
        map.put("s04",zl) ;//key-value 键值对

        Person per = map.get("s03");
//        System.out.println(per.getAge()+","+per.getName()+","+per.getHeight());
        //默认情况 ，sout(对象的toString()方法)
        System.out.println(per);

        //遍历map:获取所有的entry,再 获取每个entry中的key-value
        Set<Entry<String,Person> > entries = map.entrySet();

        Iterator<Entry<String,Person>> iter = entries.iterator();

        while (iter.hasNext()) {
          Entry<String,Person> entry =   iter.next() ;
            System.out.print( entry.getKey() +"--");//获取key
            Person person = entry.getValue();//获取value （person）
            System.out.print(person.getName()+","+person.getAge()+","
            +person.getHeight()) ;

            System.out.println();
        }
    }
}
