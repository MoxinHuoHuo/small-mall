import com.mmall.util.BigDecimalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class App {
    public static void main(String[] args) {
        /*Set<Person> set=new HashSet<>();
        set.add(new Person("li",20));
        set.add(new Person("li",20));

        Iterator it=set.iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }*/


        BigDecimal b1=new BigDecimal(2.36);
        BigDecimal b2=new BigDecimal(1.69);
        BigDecimal b3=new BigDecimal("0");

        BigDecimal sum=BigDecimalUtil.sub(b3.doubleValue(),b2.doubleValue());
        System.out.println(sum);
        //System.out.println(bsum);

        Person person=new Person("liming",200);

    }
}
