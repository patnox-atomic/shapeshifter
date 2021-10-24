package com.patnox.shapeshifter;

import java.util.*;

/**
 * This is the square object which is a type of shape
 * @author patnox
 */
public class Square extends Shape
{

    public Square() {
    }

    @Override
    public void addChild(Shape newShape)
    {
        if(newShape instanceof Square)
        {
            super.addChild(newShape);
        }
        else
        {
            System.err.println("Error: Invalid Syntax: Squares Should only contain other squares");
            throw new ParseException("Error: Invalid Syntax: Squares Should only contain other squares", new Location(0,0,0));
        }
    }

    public Square(int label) {
        setLabel(String.valueOf(label));
    }

    public Square(int label, ArrayList<Shape> children) {
        setLabel(String.valueOf(label));
        setChildren(children);
    }
}
