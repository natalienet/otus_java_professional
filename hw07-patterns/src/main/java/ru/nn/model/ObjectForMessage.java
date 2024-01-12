package ru.nn.model;

import java.util.List;

public class ObjectForMessage implements Copyable<ObjectForMessage> {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage copy() {
        ObjectForMessage msg = new ObjectForMessage();
        msg.setData(this.data.stream().toList());
        return msg;
    }
}
