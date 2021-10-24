package com.patnox.shapeshifter;

import java.util.ArrayList;

/**
 * This is the shapes container object to hold all the shapes
 * @author patnox
 */
public class ShapesContainer extends Shape
{
    public ShapesContainer() {
    }

    public ShapesContainer(ArrayList<Shape> children) {
        this.setChildren(children);
    }
}
