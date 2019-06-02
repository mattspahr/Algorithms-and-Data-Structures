package csi403;

public class Job implements Comparable<Job>{
    private String name;
    private int pri;
    
    public Job(String name, int pri) {
        this.name = name;
        this.pri = pri;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getPri() {
        return pri;
    }
    
    public void setPri(int pri) {
        this.pri = pri;
    }
    
    @Override
    public int compareTo(Job job) {
        if (this.getPri() > job.getPri()) {
            return 1;
        } else if (this.getPri() < job.getPri()) {
            return -1;
        } else {
            return 0;
        }
    }        
}