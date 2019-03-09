/**
 * @auther lyd
 * @createDate 2019/2/28 21:12
 */
public class SwapExecute<T> {

    /*public void swap(T x1, T x2){
        T tmp = x1;
        x1 = x2;
        x2 =tmp;

        System.out.println(x1+"----**---"+x2);
    }*/

    public void swap(T [] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
