package com.patnox.shapeshifter;

import java.util.*;

/**
 * This is the shape object which is the base of all objects that can be shown on a graph
 * @author patnox
 */
public class Shape
{
    private String label = "";
    private ArrayList<Shape> children = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addChild(Shape newShape)
    {
        children.add(newShape);
    }

    public ArrayList<Shape> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Shape> children) {
        this.children = children;
    }

    public Shape(String label, ArrayList<Shape> children) {
        this.label = label;
        this.children = children;
    }

    public Shape(ArrayList<Shape> children) {
        this.children = children;
    }

    public Shape(String label) {
        this.label = label;
    }

    public Shape() {
    }


}
