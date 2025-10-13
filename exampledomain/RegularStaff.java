package exampledomain;
import java.util.Random;
public interface RegularStaff {
public static final String[] gifts = {"汽車", "機票", "電腦","住宿券","手機","悠遊卡","銘謝惠顧"};

public static String getLuckDraw(){
    int idx=new Random().nextInt(gifts.length);
    return gifts[idx];
}
public default double calcPerMultiplier() {
   return (int)(Math.random()*5+1)*0.5;
}


public double getBonus();
}
