from antlr4 import *
from LabeledExprVisitor import LabeledExprVisitor
from LabeledExprParser import LabeledExprParser
import math

class EvalVisitor(LabeledExprVisitor):
    def __init__(self):
        super().__init__()
        self.memory = {}
        self.degrees = False  # False = rad, True = deg

    # ID '=' expr NEWLINE
    def visitAssign(self, ctx:LabeledExprParser.AssignContext):
        varname = ctx.ID().getText()
        value = self.visit(ctx.expr())
        self.memory[varname] = value
        return value

    # expr NEWLINE
    def visitPrintExpr(self, ctx:LabeledExprParser.PrintExprContext):
        value = self.visit(ctx.expr())
        print(self._format_number(value))
        return 0.0

    # NEWLINE
    def visitBlank(self, ctx:LabeledExprParser.BlankContext):
        return 0.0

    # DEG NEWLINE
    def visitSetDeg(self, ctx:LabeledExprParser.SetDegContext):
        self.degrees = True
        return 0.0

    # RAD NEWLINE
    def visitSetRad(self, ctx:LabeledExprParser.SetRadContext):
        self.degrees = False
        return 0.0

    # NUMBER
    def visitNumber(self, ctx:LabeledExprParser.NumberContext):
        return float(ctx.NUMBER().getText())

    # ID
    def visitId(self, ctx:LabeledExprParser.IdContext):
        name = ctx.ID().getText()
        return self.memory.get(name, 0.0)

    # expr op=('*'|'/') expr
    def visitMulDiv(self, ctx:LabeledExprParser.MulDivContext):
        left = self.visit(ctx.expr(0))
        right = self.visit(ctx.expr(1))
        if ctx.op.type == LabeledExprParser.MUL:
            return left * right
        else:
            return left / right

    # expr op=('+'|'-') expr
    def visitAddSub(self, ctx:LabeledExprParser.AddSubContext):
        left = self.visit(ctx.expr(0))
        right = self.visit(ctx.expr(1))
        if ctx.op.type == LabeledExprParser.ADD:
            return left + right
        else:
            return left - right

    # SUB expr
    def visitNegate(self, ctx:LabeledExprParser.NegateContext):
        return -self.visit(ctx.expr())

    # expr FACT
    def visitFactorial(self, ctx:LabeledExprParser.FactorialContext):
        x = self.visit(ctx.expr())
        if (abs(x - round(x)) > 1e-12) or (x < 0):
            raise RuntimeError("Factorial sólo admite enteros no negativos.")
        return float(self._factorial(int(round(x))))

    # func '(' expr ')'
    def visitFunc(self, ctx:LabeledExprParser.FuncContext):
        x = self.visit(ctx.expr())
        f = ctx.func().getText().lower()
        if f == 'sin':
            return math.sin(self._to_radians_if_needed(x))
        elif f == 'cos':
            return math.cos(self._to_radians_if_needed(x))
        elif f == 'tan':
            return math.tan(self._to_radians_if_needed(x))
        elif f == 'sqrt':
            if x < 0:
                raise RuntimeError("Sqrt(x): x no puede ser negativo.")
            return math.sqrt(x)
        elif f == 'ln':
            if x <= 0:
                raise RuntimeError("Ln(x): x debe ser > 0.")
            return math.log(x)
        elif f == 'log':
            if x <= 0:
                raise RuntimeError("Log(x): x debe ser > 0.")
            return math.log10(x)
        else:
            raise RuntimeError(f"Función no soportada: {f}")

    # '(' expr ')'
    def visitParens(self, ctx:LabeledExprParser.ParensContext):
        return self.visit(ctx.expr())

    # ---------- utilidades ----------
    def _factorial(self, n:int) -> int:
        res = 1
        for i in range(2, n+1):
            res *= i
        return res

    def _to_radians_if_needed(self, x:float) -> float:
        return math.radians(x) if self.degrees else x

    def _format_number(self, v:float) -> str:
        if abs(v - round(v)) < 1e-12:
            return str(int(round(v)))
        return str(v)
