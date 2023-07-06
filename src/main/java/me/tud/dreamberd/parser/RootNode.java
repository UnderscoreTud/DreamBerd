package me.tud.dreamberd.parser;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.lang.Statement;
import me.tud.dreamberd.lang.statements.AssignStatement;
import me.tud.dreamberd.lang.variable.Lifetime;
import me.tud.dreamberd.lexer.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RootNode extends Node {

    private final Node node;

    public RootNode(Node node, Token[] tokens) {
        super(node.parser, node.line, tokens);
        this.node = node;
    }

    @Override
    public void init() throws ParseException {
        validateAssignments();
        forEach(node -> {
            node.init();
            if (node instanceof StatementNode statementNode && statementNode.getStatement() instanceof AssignStatement statement) {
                Long offset = statement.getLifetime().orElse(new Lifetime(Long.MAX_VALUE)).getLines();
                reparse(node, offset);
            }
        });
    }

    private void validateAssignments() {
        stream()
                .map(node -> (StatementNode) (node instanceof StatementNode ? node : null))
                .filter(Objects::nonNull)
                .forEach(node -> {
                    if (!(node.getStatement() instanceof AssignStatement assign))
                        return;
                    Long lines = assign.getLifetime().map(Lifetime::getLines).orElse(null);
                    if (lines == null)
                        return;
                    if (!parser.getVariables().add(assign.getIdentifier()))
                        return;
                    reparse(node, lines);
                });
    }

    @Override
    public void execute() {
        forEach(Node::execute);
    }

    public void forEach(Consumer<? super Node> consumer) {
        Node current = node;
        do {
            Node next = current.getNext();
            consumer.accept(current);
            current = next;
        } while (current != null);
    }

    public List<Node> asList() {
        List<Node> list = new LinkedList<>();
        forEach(list::add);
        return list;
    }

    public Stream<Node> stream() {
        return asList().stream();
    }

    private static void reparse(Node node, Long offset) {
        if (offset == null)
            offset = Long.MAX_VALUE;
        if (offset == 0)
            return;
        boolean backwards = offset < 0;
        if (backwards)
            offset = Math.abs(offset);
        Node current = node;
        for (int i = 0; i < offset && current != null; i++) {
            if (current instanceof SectionNode sectionNode) {
                current = backwards ? sectionNode.getLast() : sectionNode.getFirst();
            } else {
                current = backwards ? current.getPrevious() : current.getNext();
            }
            if (current instanceof StatementNode statementNode);
//                statementNode.reparse(backwards);
        }
    }

    private static void linkAfter(Node previous, Node node) {
        linkBetween(previous, node, previous.getNext());
    }

    private static void linkBefore(Node node, Node next) {
        linkBetween(next.getPrevious(), node, next);
    }

    private static void linkBetween(Node previous, Node node, Node next) {
        if (previous != null) {
            previous.setNext(node);
            node.setPrevious(previous);
        }
        if (next != null) {
            node.setNext(next);
            next.setPrevious(node);
        }
    }

    private static void remove(Node node) {
        Node previous = node.getPrevious();
        Node next = node.getNext();
        if (previous == null && next == null)
            return;
        if (previous != null) {
            node.setPrevious(null);
            previous.setNext(next);
        }
        if (next != null) {
            node.setNext(null);
            next.setPrevious(previous);
        }
    }

}
