import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @auther lyd
 * @createDate 2018/11/14 19:57
 */
public class Emian {

    //定义两个图
    static AdjacencyMatrix  A = new AdjacencyMatrix();
    static AdjacencyMatrix  B = new AdjacencyMatrix();


    public static void main(String[] args){
        boolean judgementCondition = true;
        //输入两个图的顶点数量
        try {
            getNumOfPointOfTwoPic();
        } catch (IOException e) {
            System.out.println("读取控制台字符出错！");
        }

        //判断顶点的个数是否相同
        judgeWhetherTheNumOfVerticesIsTheSame();
        //if(!judgementCondition) return ;

        //输入两个图的矩阵
        inputTheMatrixOfTwoGraphs();

        //如果输入的边的数量不相同，不同构
        judgementCondition = judgeWheterTheNumOfEdgesOfTwoGraphs();
        if(!judgementCondition) return ;

        //边的度数不同，不同构
        judgementCondition = getDegrees();
        if(!judgementCondition) return ;

        //对两个图进行列交换
        judgementCondition = adjustingAmatrixtoBMatrix();
        if(judgementCondition) System.out.println("经过检查，两个图同构");
    }


    //获取两个图的顶点的个数
    public static void getNumOfPointOfTwoPic() throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        System.out.println("请输入第一个图的顶点个数");
        A.points = Integer.parseInt(br.readLine());
        System.out.println("请输入第二个图的顶点个数");
        B.points = Integer.parseInt(br.readLine());
        System.out.println("两个图的顶点个数分别是 :" + A.points + "  " + B.points);

    }

    //判断顶点的个数是否相同
    public static void judgeWhetherTheNumOfVerticesIsTheSame(){
        if(!(A.points.equals(B.points))){
            throw new RuntimeException("两个图的顶点个数不相同，不同构！");
        }
    }

    //输入两个图的矩阵
    public static void inputTheMatrixOfTwoGraphs(){
        System.out.println(">>>>>>>>>>>>>>请输入第一个矩阵图");
        A.Matrix = new Integer[A.points][A.points];
        intialVluesForTwoDimensionalArrays(A.Matrix);
        changeArrayByInputValue(A.Matrix);
        System.out.println(">>>>>>>>>>>>>>请输入第二个矩阵图");
        B.Matrix = new Integer[B.points][B.points];
        intialVluesForTwoDimensionalArrays(B.Matrix);
        changeArrayByInputValue(B.Matrix);

    }

    //打印二维数组
    public static void showArray(Integer[][] array){
        for(int i = 0;i < array.length;i++){
            for(int j = 0;j < array[i].length;j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    //通过输入的值改变数组
    public static void changeArrayByInputValue(Integer[][] array){
        Scanner scanner = new Scanner(System.in);
        String read = "";
        //一维 二维 坐标以及值
        Integer x = 0,y = 0,value = 1;
        //计数器
        Integer counter = 0;
        Integer temp = 0;
        while(!((read = scanner.next()).equals("#"))){
            counter++;
            temp = Integer.parseInt(read);
            switch(counter){
                case 1:x = temp;
                    break;
                case 2:y = temp;
                    array[x][y] = value;
                    array[y][x] = value;
                    showArray(array);
                    counter = 0;
                    break;
            }


        }
        showArray(array);


    }

    //判断两个图的边的数量
    public static boolean judgeWheterTheNumOfEdgesOfTwoGraphs(){
        Integer NumOfEdgesOfFirstGraphs = CountTheNumOfGraphs(A);
        Integer NumOfEdgesOfSecondGraphs = CountTheNumOfGraphs(B);
        System.out.println("第一个图的边的数量为 ：" + NumOfEdgesOfFirstGraphs);
        System.out.println("第二个图的边的数量为 ：" + NumOfEdgesOfSecondGraphs);
        if(!(NumOfEdgesOfFirstGraphs.equals(NumOfEdgesOfSecondGraphs))){
            System.out.println("边的数量不同，两个图不同构");
            return false;
        }else{
            return true;
        }

    }

    //获取图的边的数量
    public static Integer CountTheNumOfGraphs(AdjacencyMatrix adjacencyMatrix){
        Integer edges = 0;
        Integer[][] array = adjacencyMatrix.Matrix;
        for(Integer i = 0; i < array.length; i++){
            for(Integer j = 0; j < i  ; j++){
                if(array[i][j].equals(1)) edges++ ;

            }
        }
        adjacencyMatrix.edges = edges;
        return  adjacencyMatrix.edges;
    }

    //给二维数组赋初值
    public static void intialVluesForTwoDimensionalArrays(Integer[][] array){
        for(Integer i = 0; i < array.length ; i++){
            for(Integer j = 0; j <  array.length ; j ++){
                array[i][j] = 0;
                array[j][i] = 0;
            }
        }
    }

    //获取该矩阵的度数，并赋值给该矩阵对象
    public static boolean getDegrees(){
        Integer [] Aweight = new Integer[A.Matrix.length];
        Integer [] Bweight = new Integer[B.Matrix.length];
        //之前的weight在生成的时候没有分配空间，这里将直接引用新new出来的空间
        A.weight = new Integer[A.Matrix.length];
        B.weight = new Integer[B.Matrix.length];
        //给A的weight赋值度数
        int x = 0;
        for(int k = 0; k < A.points ; k++){
            int count = 0;
            for(int y = 0; y < A.points ; y++){
                if(A.Matrix[k][y].equals(1)){
                    count++;
                }
            }
            Aweight[x] = count;
            A.weight[x++] = count;
        }


        //给B的weight赋值度数
        x = 0;
        for(int k = 0; k < B.points ; k++){
            int count = 0;
            for(int y = 0; y < B.points ; y++){
                if(B.Matrix[k][y].equals(1)){
                    count++;
                }
            }
            Bweight[x] = count;
            B.weight[x++] = count;
        }



        //分别进行排序
        Arrays.sort(Aweight);
        Arrays.sort(Bweight);


        //比较度数
        for(int k=0;k<A.points;k++){
            if(Aweight[k]!=Bweight[k]){
                System.out.println("边的度数不同！不同构！");
                return false;
            }
        }
        System.out.println("边的度数相同！继续！");
        return true;
    }

    //行位置交换函数，返回true为正常交换
    public static boolean swapRows(int i,int j){
        int k;
        //进行行交换
        for(k=0;k<A.points;k++){
            int temp;
            temp = A.Matrix[i][k];
            A.Matrix[i][k]= A.Matrix[j][k];
            A.Matrix[j][k]= temp;
        }
        int temp;
        //度交换
        temp =A.weight[i];
        A.weight[i]= A.weight[j];
        A.weight[j]= temp;
        return true;
    }

    public static boolean swapColumns(int currentLayer,int i,int j){
        int k;
        //判断是否能交换
        for(k=0;k<currentLayer;k++){
            if(A.Matrix[k][i]!=A.Matrix[k][j]){
                //无法交换，因为交换后会影响先前调整的结果，故而不同构
                return false;
            }
        }
        //进行列交换
        for(k=0;k<A.points;k++){
            int temp;
            temp =A.Matrix[k][i];
            A.Matrix[k][i]= A.Matrix[k][j];
            A.Matrix[k][j]= temp;
        }
        return true;
    }



    //尝试将A矩阵调整成B矩阵，如果成功，同构，反之，不同构
    public static boolean adjustingAmatrixtoBMatrix(){
        //调整A矩阵成B
        System.out.println("开始调整AB");
        for(int i=0;i<B.points;i++){
            System.out.println(">>>>>>执行完第"+ i +"次");
            for (int j=i; j<A.points; j++) {
                //找到度相同
                if (B.weight[i] == A.weight[j]) {
                    //进行行交换
                    if (i != j) {
                        swapRows(i,j);
                    }

                    //进行列交换
                    if (i != j) {

                        if (swapColumns(i,i,j) == false) {
                            System.out.println("无法调整成相同的邻接矩阵！不同构！");
                        }

                        Integer [] list = new Integer[A.points];
                        int x=0;
                        //判断非零顶点所处列的位置是否相同
                        for(int k=0;k<A.points;k++){//找出位置不同的点放入list
                            if(A.Matrix[i][k]!=B.Matrix[i][k]){
                                list[x++]=k;
                            }
                        }

                        for(int k=0;k<x;k=k+2){
                            if(swapColumns(i,list[k],list[k+1])==false){
                                System.out.println("无法调整成相同的邻接矩阵！不同构！");
                                return false;
                            }
                            swapRows(list[k],list[k+1]);
                        }
                    }
                    break;
                    //  return true;
                }
            }



        }
        return true;
    }

}
