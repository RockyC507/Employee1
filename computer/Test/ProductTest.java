package computer.Test;
import computer.product.Desktop;
import computer.product.Laptop;
import computer.product.computer;
public class ProductTest {
  public static void main(String[] args) {
    computer c1=new computer("微軟", "8G", 50000);
    Desktop D1=new Desktop("輝達", "16G", 60000,"GTX-1080");
    Laptop L1=new Laptop("緯創", "32G", 35500,"100%");
    c1.display();
    D1.display();
    System.out.println("顯示卡型號: "+D1.getGPUModel());
    L1.display();
    System.out.println("電池容量: "+L1.getBatteryCapacity());
  }
}
