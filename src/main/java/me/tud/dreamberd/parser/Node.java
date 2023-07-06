package me.tud.dreamberd.parser;

import me.tud.dreamberd.lang.Context;
import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.Script;
import me.tud.dreamberd.lexer.Token;
import org.jetbrains.annotations.Nullable;

public abstract class Node {

    protected final Parser parser;
    protected final int line;
    protected @Nullable Node parent, previous, next;
    protected final Token[] tokens;

    protected Node(Parser parser, int line, Token[] tokens) {
        this.parser = parser;
        this.line = line;
        this.tokens = tokens;
    }

    public Parser getParser() {
        return parser;
    }

    public @Nullable Script getScript() {
        return parser.getScript();
    }

    public Context getContext() {
        return parser.getContext();
    }

    public int getLine() {
        return line;
    }

    public @Nullable Node getParent() {
        return parent;
    }

    public void setParent(@Nullable Node parent) {
        this.parent = parent;
    }

    public @Nullable Node getPrevious() {
        return previous;
    }

    public void setPrevious(@Nullable Node previous) {
        this.previous = previous;
    }

    public @Nullable Node getNext() {
        return next;
    }

    public void setNext(@Nullable Node next) {
        this.next = next;
    }

    public abstract void init() throws ParseException;

    public abstract void execute();

}
