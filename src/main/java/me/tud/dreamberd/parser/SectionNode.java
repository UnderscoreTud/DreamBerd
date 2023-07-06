package me.tud.dreamberd.parser;

import me.tud.dreamberd.lexer.Token;
import org.jetbrains.annotations.Nullable;

public abstract class SectionNode extends Node {

    private @Nullable Node first, last;

    protected SectionNode(Parser parser, int line, Token[] tokens) {
        super(parser, line, tokens);
    }

    public Node getFirst() {
        return first;
    }

    public void setFirst(Node first) {
        this.first = first;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
    }

}
