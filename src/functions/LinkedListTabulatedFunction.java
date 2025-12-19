package functions;

/**
 * Класс для работы с табулированными функциями одной переменной,
 * использующий двусвязный циклический связный список для хранения точек.
 *
 * @author haidyonish
 * @see FunctionPoint
 * @see TabulatedFunction
 */
public class LinkedListTabulatedFunction implements TabulatedFunction {

    /**
     * Узел двусвязного циклического списка.
     * Содержит точку функции и ссылки на соседние узлы.
     */
    private static class FunctionNode {
        private FunctionPoint data = null;
        private FunctionNode prev = this;
        private FunctionNode next = this;
    }

    private final FunctionNode head;
    private int size;
    private FunctionNode lastAccessedNode;
    private int lastAccessedNodeIndex;

    /**
     * Создаёт табулированную функцию с равномерной сеткой и нулевыми значениями.
     *
     * @param leftX левая граница области определения (меньше правой)
     * @param rightX правая граница области определения
     * @param pointsCount количество точек табулирования (не менее двух)
     * @throws IllegalArgumentException если левая граница области определения больше или равна правой, или если количество точек менее двух
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница области определения leftX должна быть строго меньше правой границы rightX");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Количество точек pointsCount должно быть не меньше двух");

        head = new FunctionNode();
        size = 0;
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < (pointsCount - 1); i++) {
            addNodeToTail().data = new FunctionPoint(leftX + i*step, 0);
        }
        addNodeToTail().data = new FunctionPoint(rightX, 0);
        lastAccessedNode = head.next;
        lastAccessedNodeIndex = 0;
    }

    /**
     * Создаёт табулированную функцию с равномерной сеткой и заданными значениями.
     *
     * @param leftX левая граница области определения (меньше правой)
     * @param rightX правая граница области определения
     * @param values значения функции в точках табулирования (не менее двух значений в массиве)
     * @throws IllegalArgumentException если левая граница области определения больше или равна правой, или если в массиве менее двух значений
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница области определения leftX должна быть строго меньше правой границы rightX");
        if (values.length < 2)
            throw new IllegalArgumentException("Количество значений функции в массиве values должно быть не меньше двух");
        int pointsCount = values.length;
        head = new FunctionNode();
        size = 0;
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < (pointsCount - 1); i++) {
            addNodeToTail().data = new FunctionPoint(leftX + i*step, values[i]);
        }
        addNodeToTail().data = new FunctionPoint(rightX, values[pointsCount-1]);
        lastAccessedNode = head.next;
        lastAccessedNodeIndex = 0;
    }

    @Override
    public double getLeftDomainBorder() {
        return head.next.data.getX();
    }

    @Override
    public double getRightDomainBorder() {
        return head.prev.data.getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            FunctionNode tempNode = head.next;
            for (int i = 1; i < size; i++) {
                if (x <= tempNode.next.data.getX() + 1e-10) {
                    if (Math.abs(x - tempNode.next.data.getX()) < 1e-10) {
                        return tempNode.next.data.getY();
                    }
                    double y1 = tempNode.data.getY();
                    double y2 = tempNode.next.data.getY();
                    double x1 = tempNode.data.getX();
                    double x2 = tempNode.next.data.getX();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
                tempNode = tempNode.next;
            }
        }
        return Double.NaN;
    }

    @Override
    public int getPointsCount() {
        return size;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        checkIndexBounds(index);
        return new FunctionPoint(getNodeByIndex(index).data);
    }

    @Override
    public double getPointX(int index) {
        checkIndexBounds(index);
        return getNodeByIndex(index).data.getX();
    }

    @Override
    public double getPointY(int index) {
        checkIndexBounds(index);
        return getNodeByIndex(index).data.getY();
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        checkIndexBounds(index);
        checkPointOrder(index, point.getX());
        getNodeByIndex(index).data = new FunctionPoint(point);
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        checkIndexBounds(index);
        checkPointOrder(index, x);
        getNodeByIndex(index).data.setX(x);
    }

    @Override
    public void setPointY(int index, double y) {
        checkIndexBounds(index);
        getNodeByIndex(index).data.setY(y);
    }

    @Override
    public void deletePoint(int index) {
        checkIndexBounds(index);
        if (size < 3) {
            throw new IllegalStateException("Нельзя удалить точку из функции, содержащей меньше трёх точек.");
        }
        deleteNodeByIndex(index);
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double pointX = point.getX();
        FunctionNode tempNode = head.next;
        for (int i = 0; i < size; i++) {
            if (Math.abs(tempNode.data.getX() - pointX) < 1e-10) {
                throw new InappropriateFunctionPointException(
                        String.format("Абсциссы точек не могут совпадать. (Точка с координатой X = %.2f уже есть в функции)", pointX)
                );
            }
            tempNode = tempNode.next;
        }
        tempNode = head.next;
        if (pointX > getRightDomainBorder()) {
            addNodeToTail().data = new FunctionPoint(point);
        } else {
            for (int i = 0; i < size; i++) {
                if (pointX < tempNode.data.getX()) {
                    addNodeByIndex(i).data = new FunctionPoint(point);
                    break;
                }
                tempNode = tempNode.next;
            }
        }

    }

    // ────────────────────────────
    // Вспомогательные методы
    // ────────────────────────────

    // Возвращает узел по указанному индексу.
    // Использует кэширование для оптимизации.
    private FunctionNode getNodeByIndex(int index) {
        checkIndexBounds(index);
        if (lastAccessedNode != null && lastAccessedNodeIndex == index) {
            return lastAccessedNode;
        }

        int distHead = Integer.min(index + 1, size - index);
        int distLast = (lastAccessedNode != null) ?
                Math.min((lastAccessedNodeIndex - index + size) % size, (index - lastAccessedNodeIndex + size) % size) :
                Integer.MAX_VALUE;

        FunctionNode resultNode;
        if (distLast < distHead) {
            resultNode = getNodeByIndexFromLast(index);
        } else {
            resultNode = getNodeByIndexFromHead(index);
        }

        lastAccessedNodeIndex = index;
        lastAccessedNode = resultNode;

        return resultNode;
    }

    // Находит узел по индексу, начиная от последнего использованного узла.
    // Выбирает направление обхода (вперед или назад) в зависимости от индекса.
    private FunctionNode getNodeByIndexFromLast(int index) {
        checkIndexBounds(index);
        FunctionNode resultNode = lastAccessedNode;
        int distPrev = (lastAccessedNodeIndex - index + size) % size;
        int distNext = (index - lastAccessedNodeIndex + size) % size;
        if (distPrev < distNext) {
            for (int i = 0; i < distPrev; i++) {
                resultNode = resultNode.prev;
            }
        } else {
            for (int i = 0; i < distNext; i++) {
                resultNode = resultNode.next;
            }
        }
        return resultNode;
    }

    // Находит узел по индексу, начиная от головы списка.
    // Выбирает направление обхода (вперед или назад) в зависимости от индекса.
    private FunctionNode getNodeByIndexFromHead(int index) {
        checkIndexBounds(index);
        FunctionNode resultNode = head;
        if (index <= size / 2) {
            for (int i = -1; i < index; i++) {
                resultNode = resultNode.next;
            }
        } else {
            for (int i = size; i > index; i--) {
                resultNode = resultNode.prev;
            }
        }
        return resultNode;
    }

    // Добавляет новый пустой узел в конец списка.

    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode();

        newNode.prev = head.prev;
        newNode.next = head;
        head.prev.next = newNode;
        head.prev = newNode;

        size++;

        if (lastAccessedNodeIndex == size - 2) {
            lastAccessedNode = newNode;
            lastAccessedNodeIndex = size - 1;
        }

        return newNode;
    }

    // Вставляет новый узел перед элементом с заданным индексом.
    // Возвращает ссылку на новый узел.
    private FunctionNode addNodeByIndex(int index) {
        checkIndexBounds(index);
        FunctionNode newNode = new FunctionNode();
        FunctionNode placeForNewNode = getNodeByIndex(index);

        placeForNewNode.prev.next = newNode;
        newNode.prev = placeForNewNode.prev;
        placeForNewNode.prev = newNode;
        newNode.next = placeForNewNode;

        size++;

        if (lastAccessedNodeIndex >= index) {
            lastAccessedNodeIndex++;
        }

        return newNode;
    }

    // Удаляет узел по индексу.
    // Корректирует кеш и размер списка.
    private FunctionNode deleteNodeByIndex(int index) {
        checkIndexBounds(index);
        FunctionNode deleteNode = getNodeByIndex(index);
        FunctionNode nextNode = deleteNode.next;

        deleteNode.prev.next = deleteNode.next;
        deleteNode.next.prev = deleteNode.prev;

        size--;

        if (lastAccessedNode == deleteNode) {
            if (index == size) { // удаляли последний
                lastAccessedNode = head.next;
                lastAccessedNodeIndex = 0;
            } else {
                lastAccessedNode = nextNode;
                lastAccessedNodeIndex = index;
            }
        } else if (lastAccessedNodeIndex > index) {
            lastAccessedNodeIndex--;
        }

        deleteNode.next = null;
        deleteNode.prev = null;

        return deleteNode;
    }

    // Метод для проверки корректности индекса.
    // (0 <= index < size)
    private void checkIndexBounds(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException(
                    String.format("Индекс точки должен быть не меньше нуля и меньше количества точек в функции. (На данный момент количество точек в функции - %d)", size)
            );
        }
    }

    // Метод для проверки корректности координаты X точки массива с индексом index при попытке её изменения.
    // (новая координата X не должна выходить за границы координат X соседних точек)
    private void checkPointOrder(int index, double pointX) throws InappropriateFunctionPointException {
        if (index == 0) {
            if (pointX >= getPointX(1) - 1e-10) {
                throw new InappropriateFunctionPointException(
                        String.format("Координата x задаваемой точки должна лежать в интервале, определяемом значениями соседних точек. (При данном индексе [%d], интервал - (-inf, %.2f))", index, getPointX(index+1))
                );
            }
        } else if (index == size-1) {
            if (pointX <= getPointX(index-1) + 1e-10) {
                throw new InappropriateFunctionPointException(
                        String.format("Координата x задаваемой точки должна лежать в интервале, определяемом значениями соседних точек. (При данном индексе [%d], интервал - (%.2f, +inf))", index, getPointX(index-1))
                );
            }
        } else if (pointX <= getPointX(index-1) + 1e-10 || pointX >= getPointX(index+1) - 1e-10) {
            throw new InappropriateFunctionPointException(
                    String.format("Координата x задаваемой точки должна лежать в интервале, определяемом значениями соседних точек. (При данном индексе [%d], интервал - (%.2f, %.2f))", index, getPointX(index-1), getPointX(index+1))
            );
        }
    }
}
