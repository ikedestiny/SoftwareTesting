package part2;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class BinomialHeap {
    private static final Logger logger = LogManager.getLogger(BinomialHeap.class);
    private List<BHNode> trees;
    private BHNode min_node;
    private int count;

    public List<BHNode> getTrees() { return trees; }
    public void setTrees(List<BHNode> trees) { this.trees = trees; }
    public BHNode getMin_node() { return min_node; }
    public void setMin_node(BHNode min_node) { this.min_node = min_node; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public BinomialHeap() {
        logger.trace("Вход в конструктор BinomialHeap");
        this.trees = new ArrayList<>();
        this.min_node = null;
        this.count = 0;
    }

    public boolean isEmpty() {
        if(min_node == null) {
            logger.trace("Куча пуста, минимальный узел: {}", min_node);
            return true;
        } else {
            logger.trace("Куча не пуста, минимальный узел: {}", min_node);
            return false;
        }
    }

    public BHNode insert(int value) {
        logger.trace("Вход в insert с value = {}", value);
        BHNode node = new BHNode(value);
        BinomialHeap heap = new BinomialHeap();
        heap.getTrees().add(node);
        heap.setCount(heap.getCount() + 1);
        merge(heap);
        logger.trace("Выход из insert, возвращён узел со значением {}", value);
        return node;
    }

    public void merge(BinomialHeap other_heap) {
        logger.trace("Вход в merge, размер текущей кучи = {}, размер другой кучи = {}", size(), other_heap.size());
        getTrees().addAll(other_heap.getTrees());
        setCount(getCount() + other_heap.getCount());
        other_heap.getTrees().clear();
        other_heap.setCount(0);
        other_heap.setMin_node(null);
        consolidate();
        findMin();
        logger.trace("Выход из merge, новый размер = {}", size());
    }

    public void findMin() {
        logger.trace("Вход в findMin");
        setMin_node(null);
        for (BHNode tree : getTrees()) {
            if (getMin_node() == null || tree.getValue() < getMin_node().getValue()) {
                setMin_node(tree);
                logger.trace("findMin: обновлён min_node до значения {}", tree.getValue());
            }
        }
        logger.trace("Выход из findMin, min_node = {}", (getMin_node() != null ? getMin_node().getValue() : "null"));
    }

    public int extractMin() {
        logger.trace("Вход в extractMin");
        BHNode minNode = getMin_node();
        if (minNode == null) {
            logger.trace("extractMin: куча пуста, выбрасываем исключение");
            throw new NoSuchElementException("Heap is empty");
        }

        getTrees().remove(minNode);
        logger.trace("extractMin: удалён минимальный узел со значением {}", minNode.getValue());

        for (BHNode child : minNode.getChildren()) {
            child.setParent(null);
        }
        getTrees().addAll(minNode.getChildren());
        logger.trace("extractMin: добавлены дети удалённого узла, количество детей = {}", minNode.getChildren().size());

        setCount(getCount() - 1);
        logger.trace("extractMin: новый счётчик = {}", getCount());

        consolidate();
        findMin();

        logger.trace("Выход из extractMin, возвращаем значение {}", minNode.getValue());
        return minNode.getValue();
    }

    public void decreaseKey(BHNode node, int new_value) {
        logger.trace("Вход в decreaseKey, узел со старым значением {}, новое значение {}", node.getValue(), new_value);
        if (new_value > node.getValue()) {
            logger.trace("decreaseKey: новое значение больше старого, выбрасываем исключение");
            throw new IllegalArgumentException("New value is greater than the current value");
        }
        node.setValue(new_value);
        bubbleUp(node);
        findMin();
        logger.trace("Выход из decreaseKey");
    }

    public void deleteNode(BHNode node) {
        logger.trace("Вход в deleteNode, удаляемый узел со значением {}", node.getValue());
        decreaseKey(node, Integer.MIN_VALUE);
        extractMin();
        logger.trace("Выход из deleteNode");
    }

    public void bubbleUp(BHNode node) {
        logger.trace("Вход в bubbleUp для узла со значением {}", node.getValue());
        BHNode parent = node.getParent();
        while (parent != null && node.getValue() < parent.getValue()) {
            logger.trace("bubbleUp: обмен значениями между узлом {} и родителем {}", node.getValue(), parent.getValue());
            int temp = node.getValue();
            node.setValue(parent.getValue());
            parent.setValue(temp);
            node = parent;
            parent = node.getParent();
        }
        logger.trace("Выход из bubbleUp");
    }

    public void link(BHNode tree1, BHNode tree2) {
        logger.trace("Вход в link, деревья со значениями {} и {}", tree1.getValue(), tree2.getValue());
        if (tree1.getValue() > tree2.getValue()) {
            BHNode temp = tree1;
            tree1 = tree2;
            tree2 = temp;
            logger.trace("link: перестановка, теперь tree1 = {}, tree2 = {}", tree1.getValue(), tree2.getValue());
        }
        tree2.setParent(tree1);
        tree1.getChildren().add(tree2);
        tree1.setDegree(tree1.getDegree() + 1);
        logger.trace("link: узел {} теперь родитель для {}, степень увеличена до {}", tree1.getValue(), tree2.getValue(), tree1.getDegree());
    }

    public void consolidate() {
        logger.trace("Вход в consolidate, текущий count = {}", getCount());
        if (getCount() == 0) {
            logger.trace("consolidate: куча пуста, сбрасываем trees и min_node");
            setTrees(new ArrayList<>());
            setMin_node(null);
            return;
        }

        int max_degree = (int) (Math.log(getCount()) / Math.log(2)) + 1;
        logger.trace("consolidate: максимальная степень = {}", max_degree);
        BHNode[] degree_to_tree = new BHNode[max_degree + 1];

        while (!getTrees().isEmpty()) {
            BHNode current = getTrees().remove(0);
            int degree = current.getDegree();
            logger.trace("consolidate: обрабатываем узел со значением {}, степень {}", current.getValue(), degree);

            while (degree_to_tree[degree] != null) {
                BHNode other = degree_to_tree[degree];
                degree_to_tree[degree] = null;
                logger.trace("consolidate: найдено дерево той же степени {} со значением {}, выполняем link", degree, other.getValue());

                if (current.getValue() < other.getValue()) {
                    link(current, other);
                } else {
                    link(other, current);
                    current = other;
                }
                degree++;
                logger.trace("consolidate: после link новая степень текущего узла = {}", degree);

                if (degree >= degree_to_tree.length) {
                    BHNode[] newArray = new BHNode[degree + 1];
                    System.arraycopy(degree_to_tree, 0, newArray, 0, degree_to_tree.length);
                    degree_to_tree = newArray;
                    logger.trace("consolidate: расширен массив degree_to_tree до длины {}", degree + 1);
                }
            }
            degree_to_tree[degree] = current;
            logger.trace("consolidate: установлен degree_to_tree[{}] = узел со значением {}", degree, current.getValue());
        }

        List<BHNode> newTrees = new ArrayList<>();
        setMin_node(null);
        for (BHNode tree : degree_to_tree) {
            if (tree != null) {
                newTrees.add(tree);
                if (getMin_node() == null || tree.getValue() < getMin_node().getValue()) {
                    setMin_node(tree);
                }
            }
        }
        setTrees(newTrees);
        logger.trace("Выход из consolidate, новое количество деревьев = {}", newTrees.size());
    }

    public int size() {
        return count;
    }

    public int getMin() {
        if(min_node == null) {
            logger.trace("min_node = {}, будет выброшен NPE", min_node);
        }
        return min_node.getValue();
    }

    public boolean isValid() {
        logger.trace("Вход в isValid");
        try {
            for (BHNode root : getTrees()) {
                if (root.getParent() != null) {
                    logger.trace("isValid: корень {} имеет родителя, недопустимо", root.getValue());
                    return false;
                }
            }

            int totalNodes = 0;
            for (BHNode root : getTrees()) {
                totalNodes += validateTree(root);
            }

            if (!areDegreesUnique()) {
                logger.trace("isValid: степени корней не уникальны");
                return false;
            }

            if (getMin_node() == null) {
                if (!getTrees().isEmpty()) {
                    logger.trace("isValid: min_node == null, но деревья не пусты");
                    return false;
                }
            } else {
                int actualMin = Integer.MAX_VALUE;
                for (BHNode root : getTrees()) {
                    if (root.getValue() < actualMin) actualMin = root.getValue();
                }
                if (getMin_node().getValue() != actualMin) {
                    logger.trace("isValid: min_node = {}, а реальный минимум = {}", getMin_node().getValue(), actualMin);
                    return false;
                }
            }

            boolean ok = totalNodes == getCount();
            logger.trace("isValid: общее число узлов {} совпадает с count? {}", totalNodes, ok);
            return ok;
        } catch (IllegalStateException e) {
            logger.trace("isValid: поймано исключение: {}", e.getMessage());
            return false;
        }
    }

    private int validateTree(BHNode node) {
        int expectedDegree = node.getDegree();
        if (node.getChildren().size() != expectedDegree) {
            logger.trace("validateTree: у узла {} ожидаемая степень {}, а детей {}", node.getValue(), expectedDegree, node.getChildren().size());
            throw new IllegalStateException("Degree mismatch");
        }

        int childCount = 0;
        for (BHNode child : node.getChildren()) {
            if (child.getParent() != node) {
                logger.trace("validateTree: у child {} parent не совпадает", child.getValue());
                throw new IllegalStateException("Parent link broken");
            }
            if (child.getValue() < node.getValue()) {
                logger.trace("validateTree: child {} меньше родителя {}", child.getValue(), node.getValue());
                throw new IllegalStateException("Heap property violated");
            }
            childCount += validateTree(child);
        }

        int expectedNodes = 1 << expectedDegree;
        if (childCount + 1 != expectedNodes) {
            logger.trace("validateTree: у узла {} количество узлов {} не совпадает с ожидаемым {}", node.getValue(), childCount + 1, expectedNodes);
            throw new IllegalStateException("Tree size mismatch");
        }

        return childCount + 1;
    }

    private boolean areDegreesUnique() {
        boolean[] seen = new boolean[getCount() + 1];
        for (BHNode root : getTrees()) {
            int deg = root.getDegree();
            if (seen[deg]) {
                logger.trace("areDegreesUnique: степень {} уже встречалась", deg);
                return false;
            }
            seen[deg] = true;
        }
        return true;
    }
}
