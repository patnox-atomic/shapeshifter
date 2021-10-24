package com.patnox.shapeshifter;

import java.util.ArrayList;

/**
 * This is the circle object which is a type of shape
 * @author patnox
 */
public class Circle extends Shape
{
    private int radius = 2;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Circle(String label) {
        setLabel(label);
    }

    public Circle(String label, ArrayList<Shape> children) {
        setLabel(label);
        setChildren(children);
    }

    public Circle() {
    }
}
