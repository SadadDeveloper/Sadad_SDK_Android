package com.sadadsdk.model;

import java.io.Serializable;

public class BaseModel implements Serializable {

    int position = 0;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
