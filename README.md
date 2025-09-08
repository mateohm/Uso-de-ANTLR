# Taller Uso de ANTLR

## Cómo ejecutar y probar la calculadora 

 - Generar el lexer, parser y visitor:

```
antlr4 -no-listener -visitor LabeledExpr.g4
```
Esto creará los archivos LabeledExprLexer.java, LabeledExprParser.java, LabeledExprBaseVisitor.java, etc.

- Compilar todos los .java

```
javac *.java
```

- Crear un archivo de prueba, por ejemplo test.expr:

```
Sin(30)
Cos(60)+2
5!
Ln(1)
Log(1000)
rad
Tan(3.1415926535/4)
x = 2.5
x * Sqrt(4)
```

- Ejecutar la calculadora:

```
java Calc test.expr
```

- Ejemplo de salida:

```
0.5
2.5
120
0
3
1
5
```

### Versión Python

- Generar el lexer, parser y visitor para Python:
  
```
antlr4 -Dlanguage=Python3 -no-listener -visitor LabeledExpr.g4

```

- Ejecutar con el mismo archivo de prueba:

```
python Calc.py test.expr
```

## Desarrollo 

### Introducción

Este taller se basa en el capítulo 4 del libro “The Definitive ANTLR 4 Reference”, donde se explica cómo construir una calculadora con ANTLR utilizando la gramática LabeledExpr.g4 y el patrón de diseño Visitor.

El objetivo principal es extender la calculadora para que no solo realice operaciones aritméticas básicas, sino que también pueda trabajar con funciones matemáticas más avanzadas, incluyendo trigonometría, logaritmos, raíces y factoriales. Además, la calculadora debe implementarse en dos lenguajes: Java y Python, demostrando la capacidad de ANTLR para generar analizadores en múltiples lenguajes destino.

### Desarrollo del taller

1. Construcción de la gramática (LabeledExpr.g4)

- Se partió de la gramática base del libro, que soporta operaciones +, -, *, /, variables y asignaciones.

- Se extendió la gramática para incluir:

    - Funciones matemáticas: Sin(x), Cos(x), Tan(x), Sqrt(x), Ln(x), Log(x).

    - El operador factorial !.

    - El uso de grados o radianes mediante los comandos deg y rad.

2. Implementación del EvalVisitor en Java

    - Se desarrolló una clase EvalVisitor.java que hereda de LabeledExprBaseVisitor<Double>.

    -  Se añadieron métodos para manejar cada nueva regla de la gramática.

    - Se implementó soporte para memoria de variables y para elegir entre radianes       o grados.

3. Implementación del EvalVisitor en Python

    - Se replicó la lógica de Java en un EvalVisitor.py.

    - Se utilizaron librerías estándar de Python (math) para los cálculos.

4. Driver de ejecución

    - En Java: Calc.java lee expresiones desde archivo o stdin y las evalúa.

    - En Python: Calc.py cumple la misma función.

5. Pruebas

    - Se preparó un archivo test.expr con ejemplos de todas las funciones.

    - Los resultados fueron validados en ambos lenguajes, obteniendo los mismos valores.
