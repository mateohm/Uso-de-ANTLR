import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.nio.file.*;
import java.io.*;

public class Calc {
    public static void main(String[] args) throws Exception {
        CharStream input;
        if (args.length > 0) {
            input = CharStreams.fromPath(Paths.get(args[0]));
        } else {
            input = CharStreams.fromStream(System.in);
        }

        LabeledExprLexer lexer = new LabeledExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LabeledExprParser parser = new LabeledExprParser(tokens);
        ParseTree tree = parser.prog();

        EvalVisitor eval = new EvalVisitor();
        eval.visit(tree);
    }
}
