package vn.edu.hcmut.uddd.entity;

/**
 * Created by TRAN VAN HEN on 3/24/2016.
 */
public class Dictionary extends Data{
    private String tableName;
    private String name;
    private boolean isOffline;

    public boolean isOffline() {
        return isOffline;
    }

    public void setIsOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
