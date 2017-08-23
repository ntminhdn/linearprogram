package com.example.minhnt.linearprogram;

/**
 * Created by minh.nt on 8/22/2017.
 */

public class ItemObject {
    public static final int INPUT = 0;
    public static final int PLUS = 1;
    public static final int RELATIONSHIP = 2;
    public static final int CONSTANT = 3;
    public int type;
    public Object object;
    public int position;

    // Constructor: RELATIONSHIP,CONSTANT
    public ItemObject(int type, Object object) {
        this.type = type;
        this.object = object;
        position = 0;
    }

    // Constructor: INPUT
    public ItemObject(int type, Object object, int position) {
        this.type = type;
        this.object = object;
        this.position = position;
    }

    // Constructor: PLUS
    public ItemObject(int type) {
        this.type = type;
    }
}
