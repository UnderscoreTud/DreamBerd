package me.tud.dreamberd.parser;

import me.tud.dreamberd.exceptions.ParseException;

public class HiddenNode extends Node {

    private final Node node;

    public HiddenNode(Node node) {
        super(node.parser, node.line, node.tokens);
        this.node = node;
    }

    @Override
    public void init() throws ParseException {
        node.init();
    }

    @Override
    public void execute() {
        node.execute();
    }

}
