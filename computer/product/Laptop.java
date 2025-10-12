package computer.product;

public class Laptop extends computer {
    private String BatteryCapacity;
  public Laptop(String brand,String Memory,int price,String BatteryCapacity){
    super(brand,Memory,price);
    this.BatteryCapacity=BatteryCapacity;
  }
  public String getBatteryCapacity(){
    return this.BatteryCapacity;
  }
}
