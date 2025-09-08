import java.util.HashMap;
import java.util.Map;

public class EvalVisitor extends LabeledExprBaseVisitor<Double> {
    /** Memoria para variables */
    Map<String, Double> memory = new HashMap<>();

    /** Modo ángulo: true = grados, false = radianes (por defecto radianes) */
    private boolean degrees = false;

    /** ID '=' expr NEWLINE */
    @Override
    public Double visitAssign(LabeledExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        double value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    /** expr NEWLINE */
    @Override
    public Double visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
        Double value = visit(ctx.expr());
        System.out.println(formatNumber(value));
        return 0.0;
    }

    /** NEWLINE (blank) */
    @Override
    public Double visitBlank(LabeledExprParser.BlankContext ctx) {
        return 0.0;
    }

    /** DEG NEWLINE */
    @Override
    public Double visitSetDeg(LabeledExprParser.SetDegContext ctx) {
        degrees = true;
        return 0.0;
    }

    /** RAD NEWLINE */
    @Override
    public Double visitSetRad(LabeledExprParser.SetRadContext ctx) {
        degrees = false;
        return 0.0;
    }

    /** NUMBER */
    @Override
    public Double visitNumber(LabeledExprParser.NumberContext ctx) {
        return Double.valueOf(ctx.NUMBER().getText());
    }

    /** ID */
    @Override
    public Double visitId(LabeledExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        return memory.getOrDefault(id, 0.0);
    }

    /** expr op=('*'|'/') expr */
    @Override
    public Double visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));
        if (ctx.op.getType() == LabeledExprParser.MUL) return left * right;
        return left / right; // DIV
    }

    /** expr op=('+'|'-') expr */
    @Override
    public Double visitAddSub(LabeledExprParser.AddSubContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));
        if (ctx.op.getType() == LabeledExprParser.ADD) return left + right;
        return left - right; // SUB
    }

    /** SUB expr (unario) */
    @Override
    public Double visitNegate(LabeledExprParser.NegateContext ctx) {
        return -visit(ctx.expr());
    }

    /** expr FACT (factorial) */
    @Override
    public Double visitFactorial(LabeledExprParser.FactorialContext ctx) {
        double x = visit(ctx.expr());
        if (!isWholeNumber(x) || x < 0) {
            throw new RuntimeException("Factorial sólo admite enteros no negativos.");
        }
        return (double) factorial((long)Math.round(x));
    }

    /** func '(' expr ')' */
    @Override
    public Double visitFunc(LabeledExprParser.FuncContext ctx) {
        double x = visit(ctx.expr());
        String f = ctx.func().getText().toLowerCase();

        switch (f) {
            case "sin": return Math.sin(toRadiansIfNeeded(x));
            case "cos": return Math.cos(toRadiansIfNeeded(x));
            case "tan": return Math.tan(toRadiansIfNeeded(x));
            case "sqrt":
                if (x < 0) throw new RuntimeException("Sqrt(x): x no puede ser negativo.");
                return Math.sqrt(x);
            case "ln":
                if (x <= 0) throw new RuntimeException("Ln(x): x debe ser > 0.");
                return Math.log(x);
            case "log":
                if (x <= 0) throw new RuntimeException("Log(x): x debe ser > 0.");
                return Math.log10(x);
            default:
                throw new RuntimeException("Función no soportada: " + f);
        }
    }

    /** '(' expr ')' */
    @Override
    public Double visitParens(LabeledExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    // utilidades 

    private boolean isWholeNumber(double v) {
        return Math.abs(v - Math.rint(v)) < 1e-12;
    }

    private long factorial(long n) {
        long res = 1;
        for (long i = 2; i <= n; i++) res *= i;
        return res;
    }

    private double toRadiansIfNeeded(double x) {
        return degrees ? Math.toRadians(x) : x;
    }

    private String formatNumber(double v) {
        // Evita "2.0" cuando es entero
        if (isWholeNumber(v)) return String.valueOf((long)Math.rint(v));
        return String.valueOf(v);
    }
}
