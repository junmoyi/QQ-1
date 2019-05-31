package com.yanqun.nio;

public interface MyMath {//苹果公司
    //interface接口中 只能有抽象方法，抽象方法没有方法体
    public abstract int add(int num1,int num2);

    //普通方法
//    public void add(int num1,int num2){
//        return num1+num2 ;
//    }
}


//做手机： 苹果公司 只定义规则，不实际去做手机。
//      富士康：  根据规定，做手机
/*
class MyMathImpl implements  MyMath{
    public int add(int num1,int num2){
        return num1+num2;
    }
}

class TestMyMath{
    public static void main(String[] args) {
//       MyMath math = new MyMath();//接口不能new
        MyMathImpl math = new MyMathImpl();
        int result =  math.add(1,2) ;
        System.out.println(result);

    }

}
*/
class TestMyMath{
        public static void test1(MyMath math,int a,int b){

            System.out.println( math.add(a,b)  );;
        }

       public static void main(String[] args) {
       /*
        MyMath math = new MyMath(){
            @Override
            public int add(int num1, int num2) {
                return num1+num2;
            }
        };
//        int result =  math.add(1,2) ;
//        System.out.println(result);

           test1(math,2,3);
        */

            //方法二： lambda表达式  。基础要求很高
           //lambda语法要求：如果一个参数是接口类型 （且该接口中 有且仅有一个抽象方法），则可以考虑 使用lambda表达式
           test1(
                ( num1,num2)->num1+num2

           ,2,3);
    }

}
