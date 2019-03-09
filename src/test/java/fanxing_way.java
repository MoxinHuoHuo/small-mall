/**
 * @auther lyd
 * @createDate 2019/2/28 21:08
 */
public class fanxing_way {

    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        SwapExecute<Integer> swapExecute = new SwapExecute<>();
        Integer [] a = new Integer[]{0,1,2,3,4};
        Integer [] b = new Integer[]{5,6,7,8,9};
        swapExecute.swap(a,0,3);
        swap(b,0,3);

        System.out.println(a+"----"+b);
    }
}
