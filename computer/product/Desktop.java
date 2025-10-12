package computer.product;
public class Desktop extends computer{
  private String GPUModel;
  public Desktop(String brand,String Memory,int price,String GPUModel){
    super(brand, Memory, price);
    this.GPUModel=GPUModel;
  }
  public String getGPUModel(){
    return GPUModel;
  }
}
