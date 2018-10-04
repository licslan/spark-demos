package com.licslan.sparkGraphX;

/**
 * Created by Administrator on 2018/10/2.
 */
import java.util.Scanner;//代码中使用了Scanner类

public class GraphLearn {
    //定义一下的属性变量
    private int vertexSize;//顶点数量，虽然是私有的，但是可以用set get方法进行设置
    //问题1.eclipse提醒未使用vertexSize,原因是什么？

    private int [] vertex;//定义顶点数组
    private int [] [] matrix;//定义邻接矩阵，是一个二维数组
    private static final int MAX=50;//设置最大权重50代表无穷大,常量  因为是静态私有的常量，只能在本类中有效

    //图类的构造函数
    public GraphLearn(int vertexSize){
        this.vertexSize = vertexSize;//接收传来的顶点数   在邻接矩阵的定义处可以知道，传的数是5
        matrix=new int [vertexSize] [vertexSize];//邻接绝阵是方阵，行列是定点数
        //创建顶点数组,其实就是用顶点个数定义顶点数组，最终定点数组的内容是0，1,2,3,4
        vertex=new int [vertexSize];
        for(int i=0;i<vertexSize;i++){
            vertex[i]=i;
        }
    }

    //创建vertex的set get方法   快捷键： alt+shift+s
    public int[] getVertex() {
        return vertex;
    }

    public void setVertex(int[] vertex) {
        this.vertex = vertex;//可以调用图的这个方法设置图的顶点数组
    }

    //---主函数部分
    public static void main(String[] args) {
        GraphLearn graph = new GraphLearn(5);//创建一个真正的图，顶点数是5，并输入邻接矩阵具体值
        //这里的5难道不是在使用vertexSize吗？
        int [] a1=new int[]{0,MAX,MAX,MAX,6};
        int [] a2=new int[]{9,0,3,MAX,MAX};
        int [] a3=new int[]{2,MAX,0,5,MAX};
        int [] a4=new int[]{0,MAX,MAX,0,1};
        int [] a5=new int[]{0,MAX,MAX,MAX,0};//创建5个一维数组

        //将每行的数据赋值给图的邻接矩阵,邻接矩阵每行就是一个数组
        graph.matrix[0]=a1;
        graph.matrix[1]=a2;
        graph.matrix[2]=a3;
        graph.matrix[3]=a4;
        graph.matrix[4]=a5;//到此邻接矩阵创建完成

        //---使用输入的方式查询项要看的顶点，应该还加上判断顶点是否超出范围
        System.out.println("输入你想查看的顶点：");
        Scanner sc =new Scanner(System.in );
        int mm=0;
        if(sc.hasNext())
            mm=sc.nextInt();
        int num = graph.getOutDegree(mm);
        System.out.println("顶点"+mm+"的出度是："+num);
        sc.close();
    }

    //测试1：获取某个顶点的出度，即是邻接矩阵中这个顶点对应的行中大于0小于无穷大的数字
    public int getOutDegree(int index){
        int degree=0;//初始化出度为0
        for(int j=0;j<matrix[index].length;j++){
            if(matrix[index][j]>0 & matrix[index][j]<MAX)
                degree++;
        }
        return degree;
    }
}

