package part2;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class BinominalHeap {
    private List<BHNode> trees;
    private BHNode min_node;
    private int count;

    public List<BHNode> getTrees() { return trees; }
    public void setTrees(List<BHNode> trees) { this.trees = trees; }
    public BHNode getMin_node() { return min_node; }
    public void setMin_node(BHNode min_node) { this.min_node = min_node; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public BinominalHeap() {
        this.trees = new ArrayList<>();
        this.min_node = null;
        this.count = 0;
    }

    public boolean isEmpty() {
        return min_node == null;
    }

    public BHNode insert(int value) {
        BHNode node = new BHNode(value);
        BinominalHeap heap = new BinominalHeap();
        heap.getTrees().add(node);
        heap.setCount(heap.getCount() + 1);
        merge(heap);
        return node;
    }

    public void merge(BinominalHeap other_heap) {
        getTrees().addAll(other_heap.getTrees());
        setCount(getCount() + other_heap.getCount());
        other_heap.getTrees().clear();
        other_heap.setCount(0);
        other_heap.setMin_node(null);
        consolidate();
        findMin();
    }

    public void findMin() {
        setMin_node(null);
        for (BHNode tree : getTrees()) {
            if (getMin_node() == null || tree.getValue() < getMin_node().getValue()) {
                setMin_node(tree);
            }
        }
    }

    public int extractMin() {
        BHNode minNode = getMin_node();
        if (minNode == null) throw new NoSuchElementException("Heap is empty");

        getTrees().remove(minNode);

        for (BHNode child : minNode.getChildren()) {
            child.setParent(null);
        }
        getTrees().addAll(minNode.getChildren());

        setCount(getCount() - 1);

        consolidate();
        findMin();

        return minNode.getValue();
    }

    public void decreaseKey(BHNode node, int new_value) {
        if (new_value > node.getValue()) {
            throw new IllegalArgumentException("New value is greater than the current value");
        }
        node.setValue(new_value);
        bubbleUp(node);
        findMin();
    }

    public void deleteNode(BHNode node) {
        decreaseKey(node, Integer.MIN_VALUE);
        extractMin();
    }

    public void bubbleUp(BHNode node) {
        BHNode parent = node.getParent();
        while (parent != null && node.getValue() < parent.getValue()) {
            int temp = node.getValue();
            node.setValue(parent.getValue());
            parent.setValue(temp);
            node = parent;
            parent = node.getParent();
        }
    }

    public void link(BHNode tree1, BHNode tree2) {
        if (tree1.getValue() > tree2.getValue()) {
            BHNode temp = tree1;
            tree1 = tree2;
            tree2 = temp;
        }
        tree2.setParent(tree1);
        tree1.getChildren().add(tree2);
        tree1.setDegree(tree1.getDegree() + 1);
    }

    public void consolidate() {
        if (getCount() == 0) {
            setTrees(new ArrayList<>());
            setMin_node(null);
            return;
        }

        int max_degree = (int) (Math.log(getCount()) / Math.log(2)) + 1;
        BHNode[] degree_to_tree = new BHNode[max_degree + 1];

        while (!getTrees().isEmpty()) {
            BHNode current = getTrees().remove(0);
            int degree = current.getDegree();

            while (degree_to_tree[degree] != null) {
                BHNode other = degree_to_tree[degree];
                degree_to_tree[degree] = null;

                if (current.getValue() < other.getValue()) {
                    link(current, other);
                } else {
                    link(other, current);
                    current = other;
                }
                degree++;

                if (degree >= degree_to_tree.length) {
                    BHNode[] newArray = new BHNode[degree + 1];
                    System.arraycopy(degree_to_tree, 0, newArray, 0, degree_to_tree.length);
                    degree_to_tree = newArray;
                }
            }
            degree_to_tree[degree] = current;
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
    }

    public int size() {
        return count;
    }

    public int getMin() {
        return min_node.getValue();
    }


    public boolean isValid() {
        try {
            for (BHNode root : getTrees()) {
                if (root.getParent() != null) return false;
            }

            int totalNodes = 0;
            for (BHNode root : getTrees()) {
                totalNodes += validateTree(root);
            }

            if (!areDegreesUnique()) return false;

            if (getMin_node() == null) {
                if (!getTrees().isEmpty()) return false;
            } else {
                int actualMin = Integer.MAX_VALUE;
                for (BHNode root : getTrees()) {
                    if (root.getValue() < actualMin) actualMin = root.getValue();
                }
                if (getMin_node().getValue() != actualMin) return false;
            }

            return totalNodes == getCount();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private int validateTree(BHNode node) {
        int expectedDegree = node.getDegree();
        if (node.getChildren().size() != expectedDegree) {
            throw new IllegalStateException("Degree mismatch");
        }

        int childCount = 0;
        for (BHNode child : node.getChildren()) {
            if (child.getParent() != node) throw new IllegalStateException("Parent link broken");
            if (child.getValue() < node.getValue()) throw new IllegalStateException("Heap property violated");
            childCount += validateTree(child);
        }

        int expectedNodes = 1 << expectedDegree;
        if (childCount + 1 != expectedNodes) {
            throw new IllegalStateException("Tree size mismatch");
        }

        return childCount + 1;
    }

    private boolean areDegreesUnique() {
        boolean[] seen = new boolean[getCount() + 1];
        for (BHNode root : getTrees()) {
            int deg = root.getDegree();
            if (seen[deg]) return false;
            seen[deg] = true;
        }
        return true;
    }
}
