package com.patnox.shapeshifter;

import javax.swing.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * This is the main parser. It will parse the text and display all shapes in a TREE Graph
 * @author patnox
 */
public class Parser
{
    private static final int MAX_NESTING_LEVEL = 1000;
    private static final int MIN_BUFFER_SIZE = 10;
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final ShapesHandler<Object, Object> handler;
    private Reader reader;
    private char[] buffer;
    private int bufferOffset;
    private int index;
    private int fill;
    private int line;
    private int lineOffset;
    private int current;
    private StringBuilder captureBuffer;
    private int captureStart;
    private int nestingLevel;
//    ShapesContainer holder = new ShapesContainer();

    /*
     * |                      bufferOffset
     *                        v
     * [a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t]        < input
     *                       [l|m|n|o|p|q|r|s|t|?|?]    < buffer
     *                          ^               ^
     *                       |  index           fill
     */

    /**
     * Creates a new Shapes Parser with the given handler. The parser will report all parser events to
     * this handler.
     *
     * @param handler
     *          the handler to process parser events
     */
    @SuppressWarnings("unchecked")
    public Parser(ShapesHandler<?, ?> handler)
    {
        if (handler == null) {
            throw new NullPointerException("handler is null");
        }
        this.handler = (ShapesHandler<Object, Object>)handler;
        handler.parser = this;
    }

    /**
     * Parses the given input string. The input must contain a valid TEXT value, optionally padded
     * with whitespace.
     *
     * @param string
     *          the input string, must be valid TEXT
     * @throws ParseException
     *           if the input is not valid TEXT
     */
    public void parse(String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        string = validateInput(string);
        int bufferSize = Math.max(MIN_BUFFER_SIZE, Math.min(DEFAULT_BUFFER_SIZE, string.length()));
        try
        {
            parse(new StringReader(string), bufferSize);
        }
        catch (IOException exception)
        {
            // StringReader does not throw IOException
            throw new RuntimeException(exception);
        }
    }

    /**
     * Reads the entire input from the given reader and parses it as TEXT. The input must contain a
     * valid TEXT value, optionally padded with whitespace.
     * <p>
     * Characters are read in chunks into a default-sized input buffer. Hence, wrapping a reader in an
     * additional <code>BufferedReader</code> likely won't improve reading performance.
     * </p>
     *
     * @param reader
     *          the reader to read the input from
     * @throws IOException
     *           if an I/O error occurs in the reader
     * @throws ParseException
     *           if the input is not valid TEXT
     */
    public void parse(Reader reader) throws IOException {
        parse(reader, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Reads the entire input from the given reader and parses it as TEXT. The input must contain a
     * valid TEXT value, optionally padded with whitespace.
     * <p>
     * Characters are read in chunks into an input buffer of the given size. Hence, wrapping a reader
     * in an additional <code>BufferedReader</code> likely won't improve reading performance.
     * </p>
     *
     * @param reader
     *          the reader to read the input from
     * @param buffersize
     *          the size of the input buffer in chars
     * @throws IOException
     *           if an I/O error occurs in the reader
     * @throws ParseException
     *           if the input is not valid TEXT
     */
    public void parse(Reader reader, int buffersize) throws IOException
    {
        if (reader == null)
        {
            throw new NullPointerException("reader is null");
        }
        if (buffersize <= 0)
        {
            throw new IllegalArgumentException("buffersize is zero or negative");
        }
        this.reader = reader;
        buffer = new char[buffersize];
        bufferOffset = 0;
        index = 0;
        fill = 0;
        line = 1;
        lineOffset = 0;
        current = 0;
        captureStart = -1;
        read();
        skipWhiteSpace();
        while(!isEndOfText())
        {
            readValue();
            handler.fold();
        }
        skipWhiteSpace();
        if (!isEndOfText())
        {
            throw error("Unexpected character");
        }
    }

    private String validateInput(String process)
    {
        process = process.trim();
        if(process.length() <= 0)
        {
            System.err.println("Error: Invalid Syntax: No shapes data");
            throw new ParseException("Error: Invalid Syntax: No shapes data", getLocation());
        }
        if(!isValidChars(process))
        {
            System.err.println("Error: Invalid Syntax: Invalid Characters Found");
            throw new ParseException("Error: Invalid Syntax: Invalid Characters Found", getLocation());
        }
        long countCircleChars = process.chars().filter(ch -> ch == '(').count() + process.chars().filter(ch -> ch == ')').count();
        long countSquareChars = process.chars().filter(ch -> ch == '[').count() + process.chars().filter(ch -> ch == ']').count();
        if(countCircleChars != 0 && (countCircleChars % 2 != 0))
        {
            System.err.println("Error: Invalid Syntax: All Circles Should be closed. Mismatching ( and )");
            throw new ParseException("Error: Invalid Syntax: All Circles Should be closed. Mismatching ( and )", getLocation());
        }
        if(countSquareChars != 0 && (countSquareChars % 2 != 0))
        {
            System.err.println("Error: Invalid Syntax: All Squares Should be closed. Mismatching [ and ]");
            throw new ParseException("Error: Invalid Syntax: All Squares Should be closed. Mismatching [ and ]", getLocation());
        }
        if(countCircleChars == 0 && countSquareChars == 0)
        {
            System.err.println("Error: Invalid Syntax: No Shapes Found");
            throw new ParseException("Error: Invalid Syntax: No Shapes Found", getLocation());
        }
        return(process);
    }

    private void readValue() throws IOException {
        switch (current)
        {
            case '[':
                readSquare();
                break;
            case '(':
                readCircle();
                break;
            default:
                throw expected("value");
        }
    }

    private void readSquare() throws IOException
    {
        Object object = handler.startSquare();
        read();
        if (++nestingLevel > MAX_NESTING_LEVEL) {
            throw error("Nesting too deep");
        }
        skipWhiteSpace();
        handler.startSquareLabel(object);
        String label = readLabel();
        if(!isValidSquareLabel(label))
        {
            System.err.println("Error: Invalid Syntax: Invalid Square Label Found");
            throw new ParseException("Error: Invalid Syntax: Invalid Square Label Found", getLocation());
        }
        handler.endSquareLabel(object, label);
        skipWhiteSpace();

        while (!readChar(']'))
        {
            handler.startSquareChild(object, label);
            readValue();
            handler.endSquareChild(object, label);
            skipWhiteSpace();
        }
        nestingLevel--;
        handler.endSquare(object);
        System.out.println("Square Found");
    }

    private void readCircle() throws IOException
    {
        Object object = handler.startCircle();
        read();
        if (++nestingLevel > MAX_NESTING_LEVEL) {
            throw error("Nesting too deep");
        }
        skipWhiteSpace();
        handler.startCircleLabel(object);
        String label = readLabel();
        if(!isValidCircleLabel(label))
        {
            System.err.println("Error: Invalid Syntax: Invalid Circle Label Found");
            throw new ParseException("Error: Invalid Syntax: Invalid Circle Label Found", getLocation());
        }
        handler.endCircleLabel(object, label);
        skipWhiteSpace();
        while (!readChar(')'))
        {
            handler.startCircleChild(object, label);
            readValue();
            handler.endCircleChild(object, label);
            skipWhiteSpace();
        }
        nestingLevel--;
        handler.endCircle(object);
        System.out.println("Circle Found");
    }

    private String readLabel() throws IOException
    {
        return readStringInternal();
    }

    private String readStringInternal() throws IOException
    {
        startCapture();
        read();
        while(((current >= 'a') && (current <= 'z')) || ((current >= 'A') && (current <= 'Z')) || ((current >= '0') && (current <= '9')))
        {
            read();
        }
        String string = endCapture();
        return string;
    }

    private String endCapture()
    {
        int start = captureStart;
        int end = index - 1;
        captureStart = -1;
        if (captureBuffer.length() > 0)
        {
            captureBuffer.append(buffer, start, end - start);
            String captured = captureBuffer.toString();
            captureBuffer.setLength(0);
            return captured;
        }
        return new String(buffer, start, end - start);
    }

    private void startCapture()
    {
        if (captureBuffer == null)
        {
            captureBuffer = new StringBuilder();
        }
        captureStart = index - 1;
    }

    private void pauseCapture()
    {
        int end = current == -1 ? index : index - 1;
        captureBuffer.append(buffer, captureStart, end - captureStart);
        captureStart = -1;
    }

    private boolean readChar(char ch) throws IOException
    {
        if (current != ch)
        {
            return false;
        }
        read();
        return true;
    }

    private void skipWhiteSpace() throws IOException
    {
        while (isWhiteSpace())
        {
            read();
        }
    }

    private boolean isWhiteSpace()
    {
        return current == ' ' || current == '\t' || current == '\n' || current == '\r';
    }

    private void read() throws IOException
    {
        if (index == fill)
        {
            if (captureStart != -1)
            {
                captureBuffer.append(buffer, captureStart, fill - captureStart);
                captureStart = 0;
            }
            bufferOffset += fill;
            fill = reader.read(buffer, 0, buffer.length);
            index = 0;
            if (fill == -1)
            {
                current = -1;
                index++;
                return;
            }
        }
        if (current == '\n')
        {
            line++;
            lineOffset = bufferOffset + index;
        }
        current = buffer[index++];
    }

    Location getLocation()
    {
        int offset = bufferOffset + index - 1;
        int column = offset - lineOffset + 1;
        return new Location(offset, line, column);
    }

    private ParseException expected(String expected)
    {
        if (isEndOfText())
        {
            return error("Unexpected end of input");
        }
        return error("Expected " + expected);
    }

    private ParseException error(String message)
    {
        return new ParseException(message, getLocation());
    }

    private boolean isEndOfText()
    {
        return current == -1;
    }

    private static boolean isValidChars(String s)
    {
        boolean valid = false;
        char[] a = s.toCharArray();
        for (char c: a)
        {
            valid = ((c >= 'a') && (c <= 'z')) ||
                    ((c >= 'A') && (c <= 'Z')) ||
                    ((c >= '0') && (c <= '9')) ||
                    ((c == '(')) || ((c == ')')) ||
                    ((c == '[')) || ((c == ']'));
            if (!valid)
            {
                break;
            }
        }
        return valid;
    }

    private static boolean isValidCircleLabel(String s)
    {
        boolean valid = false;
        char[] a = s.toCharArray();
        for (char c: a)
        {
            valid = ((c >= 'A') && (c <= 'Z'));
            if (!valid)
            {
                break;
            }
        }
        return valid;
    }

    private static boolean isValidSquareLabel(String s)
    {
        boolean valid = false;
        char[] a = s.toCharArray();
        for (char c: a)
        {
            valid = ((c >= '0') && (c <= '9'));
            if (!valid)
            {
                break;
            }
        }
        return valid;
    }

    public static void main(String[] args )
    {
        System.out.println( "Starting ShapeShift Parser" );
        String process = "[12](BALL(INK[1[35]](CHARLIE)))";
        //String process = "(38)";
        //String process = "[44]";
        //String process = "[DOG]";
        //String process = "(DOG)";
        //String process = "(DOG[44RE])";
        //String process = "$@#";
        //String process = "[13)";
        //String process = "[72(HELLO)]";
        //String process = "[allow]";
        //String process = "[(87)]";
        //String process = "[]";
        //String process = "()";
        //String process = "([])";
        //String process = "";
        if(args.length > 0)
        {
            String input = args[0];
            if (input.trim().length() > 0)
            {
                process = input;
            }
        }

        ShapesHandlerImpl handler = new ShapesHandlerImpl();
        new Parser(handler).parse(process);
        ShapesContainer holder = new ShapesContainer(handler.getParent());
        holder.setLabel("Container");

        TreeView frame = new TreeView(holder);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }

}
