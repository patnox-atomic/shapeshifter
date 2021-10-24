package com.patnox.shapeshifter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = ParseException.class)
    public void parseTestIllegalChars()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("*&£$£%");
    }

    @Test(expected = ParseException.class)
    public void parseTestNoLabel()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("([])");
    }

    @Test(expected = ParseException.class)
    public void parseTestDelimiterMismatch()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("(TEST");
    }

    @Test(expected = ParseException.class)
    public void parseTestNoShapes()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("TEST");
    }

    @Test(expected = ParseException.class)
    public void parseTestEmptyString()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("");
    }

    @Test(expected = ParseException.class)
    public void parseTestSquaresCanOnlyContainSquares()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("[TEST(TEST)]");
    }

    @Test(expected = ParseException.class)
    public void parseTestCircleLabel()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("(test)");
    }

    @Test(expected = ParseException.class)
    public void parseTestSquareLabel()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("[test]");
    }

    @Test
    public void parseTestGoodString()
    {
        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse("[12](BALL(INK[1[35]](CHARLIE)))");
    }

    @Test(expected = NullPointerException.class)
    public void testThrow()
    {
        String test = null;
        test.length();
    }
}