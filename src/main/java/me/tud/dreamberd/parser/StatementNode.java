package me.tud.dreamberd.parser;

import me.tud.dreamberd.exceptions.ParseException;
import me.tud.dreamberd.lang.Identifier;
import me.tud.dreamberd.lang.Statement;
import me.tud.dreamberd.lexer.Token;

import java.util.Set;

public class StatementNode extends Node {

    private Statement statement;

    protected StatementNode(Parser parser, int line, Statement statement, Token[] tokens) {
        super(parser, line, tokens);
        this.statement = statement;
    }

    @Override
    public void init() throws ParseException {
        statement.init(getContext());
    }

    @Override
    public void execute() {
        statement.execute(getContext());
    }

    public void reparse(Set<Identifier> variables, boolean initiate) {
        statement = new Parser(getContext(), tokens, variables).statement();
        if (initiate)
            statement.init(getContext());
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatementNode{");
        sb.append("statement=").append(statement);
        sb.append('}');
        return sb.toString();
    }

}
