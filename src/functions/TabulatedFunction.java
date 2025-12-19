package functions;

/**
 * Интерфейс табулированной функции.
 * Описывает набор общих операций для функций, представленных в виде набора точек (x, y).
 * Реализации могут использовать разные способы хранения данных (массив, связный список и т.д.).
 */
public interface TabulatedFunction {

    /**
     * Возвращает значение функции в заданной точке {@code x}.
     * Если {@code x} выходит за пределы определённого диапазона, выбрасывается исключение.
     *
     * @param x значение аргумента
     * @return значение функции в точке {@code x}
     * @throws IllegalArgumentException если {@code x} меньше минимального или больше максимального аргумента
     */
    double getFunctionValue(double x);

    /**
     * Возвращает количество точек в функции.
     *
     * @return количество точек
     */
    int getPointsCount();

    /**
     * Возвращает объект точки с указанным индексом.
     *
     * @param index индекс точки
     * @return объект {@link FunctionPoint}
     * @throws IndexOutOfBoundsException если индекс выходит за пределы диапазона точек
     */
    FunctionPoint getPoint(int index);

    /**
     * Устанавливает новое значение точки по индексу.
     *
     * @param index индекс точки
     * @param point новая точка
     * @throws InappropriateFunctionPointException если нарушается порядок X или уже существует точка с таким X
     * @throws IndexOutOfBoundsException если индекс выходит за пределы диапазона точек
     */
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;

    /**
     * Возвращает значение X точки с указанным индексом.
     *
     * @param index индекс точки
     * @return значение X
     * @throws IndexOutOfBoundsException если индекс выходит за пределы диапазона точек
     */
    double getPointX(int index);

    /**
     * Возвращает значение Y точки с указанным индексом.
     *
     * @param index индекс точки
     * @return значение Y
     * @throws IndexOutOfBoundsException если индекс выходит за пределы диапазона точек
     */
    double getPointY(int index);

    /**
     * Изменяет значение X точки с указанным индексом.
     *
     * @param index индекс точки
     * @param x новое значение X
     * @throws InappropriateFunctionPointException если нарушается порядок X или уже существует точка с таким X
     * @throws IndexOutOfBoundsException если индекс выходит за пределы диапазона точек
     */
    void setPointX(int index, double x) throws InappropriateFunctionPointException;

    /**
     * Изменяет значение Y точки с указанным индексом.
     *
     * @param index индекс точки
     * @param y новое значение Y
     * @throws IndexOutOfBoundsException если индекс выходит за пределы диапазона точек
     */
    void setPointY(int index, double y);

    /**
     * Добавляет новую точку в функцию.
     * Точка вставляется в соответствии с её значением X (в отсортированный порядок).
     *
     * @param point добавляемая точка
     * @throws InappropriateFunctionPointException если уже существует точка с таким X
     */
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    /**
     * Удаляет точку с указанным индексом.
     * Количество точек после удаления не может быть меньше двух.
     *
     * @param index индекс удаляемой точки
     * @throws IllegalStateException если после удаления останется меньше двух точек
     * @throws IndexOutOfBoundsException если индекс выходит за пределы диапазона точек
     */
    void deletePoint(int index);

    /**
     * Возвращает минимальное значение X среди всех точек функции.
     *
     * @return минимальный X
     */
    double getLeftDomainBorder();

    /**
     * Возвращает максимальное значение X среди всех точек функции.
     *
     * @return максимальный X
     */
    double getRightDomainBorder();
}
