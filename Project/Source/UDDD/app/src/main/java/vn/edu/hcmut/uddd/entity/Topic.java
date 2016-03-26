package vn.edu.hcmut.uddd.entity;

/**
 * Created by TRAN VAN HEN on 3/5/2016.
 */
public class Topic extends Data{
    private long id;
    private String name;
    private String mean;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
