import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Проверка ArrayTabulatedFunction ===");
        testFunction(new ArrayTabulatedFunction(0, 10, 5));

        System.out.println("\n=== Проверка LinkedListTabulatedFunction ===");
        testFunction(new LinkedListTabulatedFunction(0, 10, 5));

        System.out.println("\n=== Проверка выбрасывания исключений ===");
        testExceptions();
    }

    private static void testFunction(TabulatedFunction func) {
        System.out.println("Количество точек: " + func.getPointsCount());
        System.out.printf("Границы области: [%.2f, %.2f]%n", func.getLeftDomainBorder(), func.getRightDomainBorder());

        System.out.println("\nЗначения функции:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.printf("x=%.2f, y=%.2f%n", func.getPointX(i), func.getPointY(i));
        }

        System.out.println("\nИзменение точек:");
        func.setPointY(2, 10);
        System.out.printf("После изменения: y(%.2f) = %.2f%n", func.getPointX(2), func.getPointY(2));

        double x = 4.5;
        double y = func.getFunctionValue(x);
        System.out.printf("Интерполяция при x=%.2f -> y=%.2f%n", x, y);

        System.out.println("\nДобавление новой точки:");
        try {
            func.addPoint(new FunctionPoint(11, 5));
            System.out.println("Точка (11, 5) добавлена успешно.");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при добавлении точки: " + e.getMessage());
        }

        System.out.println("\nУдаление точки:");
        try {
            func.deletePoint(1);
            System.out.println("Точка с индексом 1 удалена успешно.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении точки: " + e.getMessage());
        }

        System.out.println("\nИтоговые точки:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.printf("[%d] x=%.2f, y=%.2f%n", i, func.getPointX(i), func.getPointY(i));
        }
    }

    private static void testExceptions() {
        try {
            System.out.println("Создание функции с одной точкой:");
            new ArrayTabulatedFunction(0, 1, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
        }

        try {
            System.out.println("\nСоздание функции с одинаковыми границами:");
            new LinkedListTabulatedFunction(5, 5, 3);
        } catch (IllegalArgumentException e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
        }

        try {
            System.out.println("\nПопытка установить X, нарушающий порядок:");
            TabulatedFunction f = new ArrayTabulatedFunction(0, 4, 3);
            f.setPointX(1, 5); // выходит за пределы
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
        }

        try {
            System.out.println("\nПопытка добавить точку с существующим X:");
            TabulatedFunction f = new LinkedListTabulatedFunction(0, 4, 3);
            f.addPoint(new FunctionPoint(2, 10)); // совпадает с существующей
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
        }

        try {
            System.out.println("\nПопытка удалить точку при размере 2:");
            TabulatedFunction f = new ArrayTabulatedFunction(0, 1, 2);
            f.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
        }

        try {
            System.out.println("\nПопытка обратиться к несуществующему индексу:");
            TabulatedFunction f = new LinkedListTabulatedFunction(0, 10, 3);
            f.getPoint(5);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
        }
    }
}
