package custom.bean;

import java.util.List;

/**
 * 地区数据
 */
public class AreaData<T> {

    private String name;

    private String id;

    private T value;

    private List<AreaData<T>> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public List<AreaData<T>> getItems() {
        return items;
    }

    public void setItems(List<AreaData<T>> items) {
        this.items = items;
    }
}
