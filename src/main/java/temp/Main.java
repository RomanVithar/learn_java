package temp;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static List<Group> groups = new ArrayList<>();

    public static void main(String[] args) {
        List<Student> temp = new ArrayList<>();
        temp.add(new Student("Vasa",43, Gender.man));
        temp.add(new Student("P",3, Gender.woman));
        temp.add(new Student("Q",13, Gender.man));
        temp.add(new Student("EW",53, Gender.man));

        groups.add(new Group(temp,"fsd","gfdgs"));

        for(Group group:groups) {
            for(int i=0;i<group.getStudents().size();i++) {
                if(group.getStudents().get(i).getGender() == Gender.man
                &&group.getStudents().get(i).getAge()<17){
                    System.out.println(group.getStudents().get(i));
                }
            }
        }
    }
}