package functions;

/**
 * Класс, представляющий точку на плоскости с координатами (x, y).
 * Используется для описания точек табулированной функции.
 *
 * @author haidyonish
 * @version 1.0
 * @see TabulatedFunction
 */
public class FunctionPoint {
    private double x, y;

    /**
     * Создает точку с заданными координатами.
     *
     * @param x координата по оси абсцисс
     * @param y координата по оси ординат
     */
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Создает копию существующей точки.
     * Конструктор копирования обеспечивает создание независимой копии объекта.
     *
     * @param point точка для копирования (не должна быть null)
     */
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    /**
     * Создает точку в начале координат (0, 0).
     * Конструктор по умолчанию для удобства создания точек с нулевыми координатами.
     */
    public FunctionPoint() {
        x = 0;
        y = 0;
    }

    /**
     * Возвращает координату X точки.
     *
     * @return координата по оси абсцисс
     */
    public double getX() {
        return x;
    }

    /**
     * Возвращает координату Y точки.
     *
     * @return координата по оси ординат
     */
    public double getY() {
        return y;
    }

    /**
     * Устанавливает новое значение координаты X.
     *
     * @param x новое значение координаты по оси абсцисс
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Устанавливает новое значение координаты Y.
     *
     * @param y новое значение координаты по оси ординат
     */
    public void setY(double y) {
        this.y = y;
    }
}