package temp;

import java.util.List;

public class Group {
    private List<Student> students;
    private String name;
    private String spec;

    public Group(List<Student> students, String name, String spec) {
        this.students = students;
        this.name = name;
        this.spec = spec;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
