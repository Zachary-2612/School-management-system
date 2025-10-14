package School.Management.System;

/*
  a clas for teacher and student to extend
 */
public class Person {
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int ID) {
        this.id = ID;
    }
    public void setName(String NAME) {
        this.name = NAME;
    }
}
