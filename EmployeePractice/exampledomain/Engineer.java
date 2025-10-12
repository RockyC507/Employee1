package EmployeePractice.exampledomain;

public class Engineer extends Employee{
  private String[] skills;
  private int skillCount;
  public Engineer(String name,String ssn,double salary){
    super(name, ssn, salary);
    skills=new String[5];
    skillCount=0;
  }
  public void addSkill(String skill){
      if(skillCount<5)
      skills[skillCount++]=skill;
      else
          System.out.println("最多登記註冊五種技能");
  }
  
  @Override
  public String toString() {
    StringBuilder sb=new StringBuilder(super.toString());
    if (skillCount>0){
      sb.append("\n技能:");
        for (int i=0;i<skillCount;i++)
            sb.append(" "+skills[i]);
    }
    return sb.toString();
  }
  
}
