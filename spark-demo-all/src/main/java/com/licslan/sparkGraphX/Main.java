package com.licslan.sparkGraphX;

import java.util.Scanner;
class GraphMatrix
{
    static final int MaxNum = 20;
    static final int MaxValue = 65535;
    char[] Vertex = new char[MaxNum];//保存顶点信息,序号或者字母
    int GType;//图的类型（0: 无向图, 1: 有向图）
    int VertexNum;//顶点的数量
    int EdgeNum; //边的数量
    int[][] EdgeWeight = new int[MaxNum][MaxNum];//保存边的权
    int[] isTrav = new int[MaxNum];

}

public class Main {
    static Scanner input = new Scanner(System.in);

    //创建图
    private static void CreateGraph(GraphMatrix GM) {
        int weight;
        char EstartV, EndV;//边的起始顶点
        System.out.println("输入图中各顶点的信息:");
        //输入顶点
        for ( int i = 0; i < GM.VertexNum; i++) {
            System.out.println("第" + (i+1) + "个顶点");
            GM.Vertex[i] = (input.next().toCharArray())[0];
        }
        System.out.println("输入构成各边的顶点及权值");
        for ( int j = 0; j < GM.EdgeNum; j++ ) {
            System.out.println("第" + (j+1) + "边");
            EstartV = input.next().charAt(0);
            EndV = input.next().charAt(0);
            weight = input.nextInt();
            int o, p;
            for (o = 0; EstartV != GM.Vertex[o]; o++);//在已有的顶点中查找开始点
            for (p = 0; EndV != GM.Vertex[p]; p++);//在已有的顶点中查找结束点
            GM.EdgeWeight[o][p] = weight;
            if (GM.GType == 0)
            {
                GM.EdgeWeight[p][o] =  weight;
            }
        }
    }
    //清空图
    private static void ClearGraph(GraphMatrix GM) {
        for (int i = 0 ; i < GM.VertexNum; i++) {
            for (int j = 0; j < GM.VertexNum; j++) {
                GM.EdgeWeight[i][j] = GraphMatrix.MaxValue;
            }
        }
    }
    //遍历输出图
    private static void OutGraph(GraphMatrix GM) {
        for (int i = 0; i < GM.VertexNum; i++) {
            System.out.printf("\t%c", GM.Vertex[i]);//第一行输出顶点的信息
        }
        System.out.println();
        for (int i = 0; i < GM.VertexNum; i++) {
            System.out.printf("%c", GM.Vertex[i]);
            for (int j = 0; j < GM.VertexNum; j++) {
                if (GM.EdgeWeight[i][j] == GraphMatrix.MaxValue) {
                    System.out.printf("\tZ");
                }
                else {
                    System.out.printf("\t%d", GM.EdgeWeight[i][j]);
                }
            }
            System.out.println();
        }
    }
    //深度优先搜索遍历
    private static void DeepTraOne(GraphMatrix GM, int n) {//从第n个结点开始, 深度遍历图
        GM.isTrav[n] = 1;
        System.out.print("->" + GM.Vertex[n]);//输出结点数据
        //添加结点的操作
        for (int i = 0; i < GM.VertexNum; i++) {
            if (GM.EdgeWeight[n][i] != GraphMatrix.MaxValue && GM.isTrav[n] == 0) {
                DeepTraOne(GM, i);
            }
        }
    }
    private static void DeepTraGraph(GraphMatrix GM) {
        for (int i = 0; i < GM.VertexNum; i++) {
            GM.isTrav[i] = 0;
        }
        System.out.println("深度优先遍历结点:");
        for (int i = 0; i < GM.VertexNum; i++) {
            if (GM.isTrav[i] == 0) {//若该点没有遍历
                DeepTraOne(GM, i);
            }
        }
    }
    public static void main(String[] args) {
        GraphMatrix GM = new GraphMatrix();//定义保存邻接表结构的图
        System.out.println("输入生成图的类型:");
        GM.GType = input.nextInt();//图的种类。 无向图和有向图
        System.out.println("输入图的顶点数量:");
        GM.VertexNum = input.nextInt();//输入图的顶点数
        System.out.println("输入图的边数量:");
        GM.EdgeNum = input.nextInt();//输入图的边数
        ClearGraph(GM);//清空图
        CreateGraph(GM);//生成邻接表结构的图
        System.out.println("该图的邻接矩阵数据如下: ");
        OutGraph(GM);//输出邻接矩阵
        DeepTraGraph(GM);//深度优先搜索遍历
    }
}

