package org.firstinspires.ftc.teamcode.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BarCodeObject implements Comparable<BarCodeObject> {

    private boolean isDuck;
    private float left;
    private float right;

    public BarCodeObject(float left, float right, boolean isDuck) {
        this.left = left;
        this.right = right;
        this.isDuck = isDuck;
    }

    public boolean isDuck() {
        return isDuck;
    }

    @Override
    public int compareTo(BarCodeObject other) {
        if((this.left < other.left) && (this.right< other.right)) {
            return -1;
        } else if((this.left > other.left) && (this.right > other.right)) {
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        List<BarCodeObject> barcodeObjects = new ArrayList<>();

        barcodeObjects.add(new BarCodeObject(2.0f, 3.0f, true));
        barcodeObjects.add(new BarCodeObject(1.5f, 2.5f, false));
        barcodeObjects.add(new BarCodeObject(0.6f, 1.6f, false));

        Collections.sort(barcodeObjects);

        int pos = 1;
        for (BarCodeObject obj : barcodeObjects) {
            System.out.println(obj.left + " " + obj.right + " " + obj.isDuck());
            if(obj.isDuck()) {
                break;
            }
            pos++;
        }
        System.out.println(pos);
    }
}
