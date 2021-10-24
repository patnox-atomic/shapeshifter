package com.patnox.shapeshifter;

import java.util.ArrayList;

/**
 * This is the implementation of the shapeshandler
 * It is used to parse the string and mark the shapes
 * @author patnox
 */
public class ShapesHandlerImpl extends ShapesHandler<Circle, Square>
{
    protected Shape value;
    protected ArrayList<Shape> parent = new ArrayList<>();

    @Override
    public Circle startCircle() {
        return new Circle();
    }

    @Override
    public Square startSquare()
    {
        return new Square();
    }

    @Override
    public void endCircle(Circle circle)
    {
        value = circle;
    }

    @Override
    public void endCircleLabel(Circle circle, String name)
    {
        circle.setLabel(name);
    }

    @Override
    public void endSquare(Square square)
    {
        value = square;
    }

    @Override
    public void endSquareLabel(Square square, String name)
    {
        square.setLabel(name);
    }

    @Override
    public void endCircleChild(Circle circle, String name)
    {
        circle.addChild(value);
    }

    @Override
    public void endSquareChild(Square square, String name)
    {
        square.addChild(value);
    }

    public Shape getValue() {
        return value;
    }

    public ArrayList<Shape> getParent() {
        return parent;
    }

    @Override
    public void fold() {
        //insert into parent
        parent.add(value);
    }

    public void setValue(Shape value) {
        this.value = value;
    }
}
