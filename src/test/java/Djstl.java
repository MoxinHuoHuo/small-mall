class PointAndNumber{
    int number;
    int k;

    public PointAndNumber(int number, int k) {
        this.number = number;
        this.k = k;
    }
}

public class Djstl {

    private static int N = 1000;

    //矩阵的信息
    private static int[][] Graph = {
            { 0 , 4,  N,  2,  N},//A 0
            { 4,  0,  4,  1,  N},//B 1
            { N,  4,  0,  1,  3},//C 2
            { 2,  1,  1,  0,  7},//D 3
            { N,  N,  3,  7,  0},//E 4
            //{ 16,  7,  6,  N,  2,  0,  9},//F 5
            //{ 14,  N,  N,  N,  8,  9,  0} //G 6
    };

    //将点加入完成的节后，对应的Sn置为1
    private static int [] s=new int[Graph.length];


    public static void main(String[] args) {
        //每次选完点后进行相加的数值
        int number=0;

        //设定开始的起点
        int startPoint=0;

        for (int i = 0; i <s.length ; i++) {
            s[i]=0;
        }

        //存放A点到各个点的最短路径
        int [] array1=new int[Graph.length];
        //存储点的路径信息
        int [] array2=new int[Graph.length];

        for (int i = 0; i <array1.length ; i++) {
            array1[i]=N;
        }

        for (int i = 0; i < Graph.length; i++) {
            JiSuanZuiDuanLuJin(array1,array2,number,startPoint);
            PointAndNumber point=netPointJoinS(array1);
            number=point.number;
            startPoint=point.k;
        }

        System.out.println("haha!");
    }

    //判断接下来要选择哪个点加入集合中
    public static PointAndNumber netPointJoinS(int [] array1) {
        int i , min=0, k;

        for (int j = 0; j < array1.length; j++) {
            if (array1[j] != 0)
                min = array1[j];
        }

        k = 0;

        for (i = 0; i < array1.length; i++)
        {
            if (array1[i] != 0 && array1[i] < min && s[i] == 0) {   // 判断最小值
                min = array1[i];
                k = i;
            }
        }

        return new PointAndNumber(min, k);
    }

    //将选中的点的路径信息存储在数组中
    public static void selectPoint(int [] array,int selectPoint) {
        s[selectPoint]=1;
        for (int i = 0; i <Graph.length ; i++) {
            if (s[i]==0 || selectPoint==i)
                array[i]=Graph[selectPoint][i];
            else
                array[i]=-1;
        }
    }

    //根据选中的点，更新储存最短路径的数组的数据
    public static void JiSuanZuiDuanLuJin(int [] array1,int [] array2,int number,int selectPoint){

        selectPoint(array2,selectPoint);

        for (int i = 0; i <Graph.length ; i++) {
            if (array2[i]<N) {
                if (array2[i] != -1 && array2[i] + number < array1[i]) {
                    array1[i] = array2[i] + number;
                }
            }else
                continue;
        }

    }
}
