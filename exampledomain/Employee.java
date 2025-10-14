package exampledomain;
import java.text.NumberFormat;
 public  abstract class Employee {
  public static int nextId=101;
  private int empId;
  private String name="John";
  private String ssn="A123456789";
  protected Branch branch;
  protected double salary=26400;
  protected NumberFormat formatter=NumberFormat.getInstance();

  public Employee(String name,String ssn,double salary,Branch branch){
    this.empId=nextId++;
    if (name!=null && name.length()!=0)
      this.name=name;
    if (ssn!=null && name.length()!=0)
      this.ssn=ssn;
    if (salary>26400)
      this.salary=salary;
    this.branch=branch;
  }
  public abstract double getPay();
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + empId;
    result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Employee other = (Employee) obj;
    if (empId != other.empId)
      return false;
    if (ssn == null) {
      if (other.ssn != null)
        return false;
    } else if (!ssn.equals(other.ssn))
      return false;
    return true;
  }

  public int getEmpId(){
    return empId;
  }
  public String getName(){
    return name;
  }
  public String getssn(){
    return ssn;
  }
  public double getsalary(){
    return salary;
  }
  public Branch getBranch() {
    return this.branch;
}
  public void setName(String name){
    if (name!=null && name.length()!=0)
      this.name=name;
    else
      System.out.println("參數為空值改名失敗");
  }
  public void raisesalary(double incease){
    if (incease>0)
      this.salary+=incease;
    else
      System.out.println("參數錯誤加薪失敗");
  }
 
  
  @Override
  public String toString() {
    return "====員工資訊===="+"\n"+
           "編號: "+this.empId+"\n"+
           "姓名: "+this.name+"\n"+
           "ssn: "+this.ssn+"\n"+
           "薪水: "+this.branch.getCurrency()+formatter.format(this.salary)+"元";
  }

  
}
