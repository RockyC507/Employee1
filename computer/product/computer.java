package computer.product;

import java.text.NumberFormat;

public class computer {
  private String brand;
  private String Memory;
  private int price;
  public computer(String brand,String Memory,int price){
    this.brand=brand;
    this.Memory=Memory;
    this.price=price;
  }
  public String getbrand(){
    return brand;
  }
  public String getMemory(){
    return Memory;
  }
  public int getprice(){
    return price;
  }
  public void setname(String brand){
    this.brand=brand;
  }
  public void display(){
    NumberFormat formatter=NumberFormat.getCurrencyInstance();
    System.out.println("品牌: "+this.brand);
    System.out.println("名字: "+this.Memory);
    System.out.println("價格: "+formatter.format(this.price)+"元");
  }
}
