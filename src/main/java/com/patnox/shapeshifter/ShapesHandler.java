package com.patnox.shapeshifter;

/**
 * A handler for parser events. Instances of this class can be given to a {@link Parser}. The
 * parser will then call the methods of the given handler while reading the input.
 * <p>
 * The default implementations of these methods do nothing. Subclasses may override only those
 * methods they are interested in. They can use <code>getLocation()</code> to access the current
 * character position of the parser at any point. The <code>start*</code> methods will be called
 * while the location points to the first character of the parsed element. The <code>end*</code>
 * methods will be called while the location points to the character position that directly follows
 * the last character of the parsed element. Example:
 * </p>
 *
 * <pre>
 * ["lorem ipsum"]
 *  ^            ^
 *  startString  endString
 * </pre>
 * <p>
 * Subclasses that build an object representation of the parsed SHAPE can return arbitrary handler
 * objects for CIRCLES and SQUARES in {@link #startCircle()} and {@link #startSquare()}.
 * These handler objects will then be provided in all subsequent parser events for this particular
 * array or object. They can be used to keep track the elements of a CIRCLE or SQUARE.
 * </p>
 *
 * @param <A>
 *          The type of handlers used for CIRCLE
 * @param <O>
 *          The type of handlers used for SQUARE
 * @see Parser
 */
public abstract class ShapesHandler<A, O>
{
    Parser parser;

    /**
     * Returns the current parser location.
     *
     * @return the current parser location
     */
    protected Location getLocation()
    {
        return parser.getLocation();
    }

    /**
     * Indicates the beginning of a SQUARE in the TEXT input. This method will be called when reading
     * the opening curly bracket character (<code>'{'</code>).
     * <p>
     * This method may return an object to handle subsequent parser events for this object. This
     * object handler will be provided in all calls to {@link #startSquareLabel(Object)
     * startObjectName()}, {@link #endSquareLabel(Object, String) endObjectName()},
     * {@link #startSquare() startSquare()},
     * {@link #endSquare(Object) endSquare()}, and {@link #endSquare(Object)
     * endObject()} for this object.
     * </p>
     *
     * @return a handler for this object, or <code>null</code> if not needed
     */
    public O startSquare() {
        return null;
    }

    /**
     * Indicates the end of a SQUARE in the TEXT input. This method will be called after reading the
     * closing curly bracket character (<code>'}'</code>).
     *
     * @param square
     *          the object handler returned from {@link #startSquare()}, or null if not provided
     */
    public void endSquare(O square) {
    }

    /**
     * Indicates the beginning of the name of a SQUARE member in the TEXT input. This method will be
     * called when reading the opening quote character ('&quot;') of the member name.
     *
     * @param square
     *          the object handler returned from {@link #startSquare()}, or <code>null</code> if not
     *          provided
     */
    public void startSquareLabel(O square) {
    }

    /**
     * Indicates the end of a SQUARE member name in the TEXT input. This method will be called after
     * reading the closing quote character (<code>'"'</code>) of the member name.
     *
     * @param square
     *          the object handler returned from {@link #startSquare()}, or null if not provided
     * @param name
     *          the parsed member name
     */
    public void endSquareLabel(O square, String name) {
    }

    /**
     * Indicates the beginning of the name of a SQUARE member in the TEXT input. This method will be
     * called when reading the opening quote character ('&quot;') of the member name.
     *
     * @param square
     *          the object handler returned from {@link #startSquare()}, or <code>null</code> if not
     *          provided
     * @param name
     *          the member name
     */
    public void startSquareChild(O square, String name) {
    }

    /**
     * Indicates the end of a SQUARE member value in the TEXT input. This method will be called after
     * reading the last character of the member value, just after the <code>end</code> method for the
     * specific member type
     *
     * @param square
     *          the object handler returned from {@link #startSquare()}, or null if not provided
     * @param name
     *          the parsed member name
     */
    public void endSquareChild(O square, String name) {
    }

    /**
     * Indicates the beginning of a CIRCLE in the TEXT input. This method will be called when reading
     * the opening curly bracket character (<code>'{'</code>).
     * <p>
     * This method may return an object to handle subsequent parser events for this object. This
     * object handler will be provided in all calls to {@link #startCircleLabel(Object)
     * startObjectName()}, {@link #endCircleLabel(Object, String) endObjectName()},
     * {@link #startCircle() startCircle()},
     * {@link #endCircle(Object) endCircle()}, and {@link #endCircle(Object)
     * endObject()} for this object.
     * </p>
     *
     * @return a handler for this object, or <code>null</code> if not needed
     */
    public A startCircle() {
        return null;
    }

    /**
     * Indicates the end of a CIRCLE in the TEXT input. This method will be called after reading the
     * closing curly bracket character (<code>'}'</code>).
     *
     * @param circle
     *          the object handler returned from {@link #startCircle()}, or null if not provided
     */
    public void endCircle(A circle) {
    }

    /**
     * Indicates the beginning of the label of a CIRCLE member in the TEXT input. This method will be
     * called when reading the opening quote character ('&quot;') of the member name.
     *
     * @param circle
     *          the object handler returned from {@link #startCircle()}, or <code>null</code> if not
     *          provided
     */
    public void startCircleLabel(A circle) {
    }

    /**
     * Indicates the end of a CIRCLE member label in the TEXT input. This method will be called after
     * reading the closing quote character (<code>'"'</code>) of the member name.
     *
     * @param circle
     *          the object handler returned from {@link #startCircle()}, or null if not provided
     * @param name
     *          the parsed member name
     */
    public void endCircleLabel(A circle, String name) {
    }

    /**
     * Indicates the beginning of the child of a CIRCLE member in the TEXT input. This method will be
     * called when reading the opening quote character ('&quot;') of the member name.
     *
     * @param circle
     *          the object handler returned from {@link #startCircle()}, or <code>null</code> if not
     *          provided
     * @param name
     *          the member name
     */
    public void startCircleChild(A circle, String name) {
    }

    /**
     * Indicates the end of a CIRCLE member child in the TEXT input. This method will be called after
     * reading the last character of the member value, just after the <code>end</code> method for the
     * specific member type
     *
     * @param circle
     *          the object handler returned from {@link #startCircle()}, or null if not provided
     * @param name
     *          the parsed member name
     */
    public void endCircleChild(A circle, String name) {
    }

    public void fold() {
        //insert into parent
    }

}
