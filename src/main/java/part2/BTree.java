package part2;

import java.util.ArrayList;
import java.util.List;

class BTree {
    BTreeNode root;
    int t;
    List<String> executionPath = new ArrayList<>();

    public BTree(int t) {
        this.t = t;
        root = null;
    }

    public void traverse() {
        executionPath.clear();
        if (root != null) {
            root.visitedPoints.clear();
            root.traverse();
            executionPath.addAll(root.visitedPoints);
        }
    }

    public BTreeNode search(int key) {
        executionPath.clear();
        if (root == null) {
            executionPath.add("Tree_Empty");
            return null;
        }
        root.visitedPoints.clear();
        BTreeNode result = root.search(key);
        executionPath.addAll(root.visitedPoints);
        return result;
    }

    public void insert(int key) {
        executionPath.clear();

        if (root == null) {
            executionPath.add("Create_New_Root_" + key);
            root = new BTreeNode(t, true);
            root.keys[0] = key;
            root.n = 1;
            return;
        }

        root.visitedPoints.clear();

        if (root.n == 2*t - 1) {
            executionPath.add("Split_Root_Start");
            BTreeNode s = new BTreeNode(t, false);
            s.children[0] = root;
            s.splitChild(0, root);

            int i = 0;
            if (s.keys[0] < key) {
                i++;
            }
            s.children[i].insertNonFull(key);
            root = s;
            executionPath.add("Split_Root_Complete");
        } else {
            executionPath.add("Insert_Into_Existing");
            root.insertNonFull(key);
        }

        executionPath.addAll(root.visitedPoints);
    }

    public List<String> getExecutionPath() {
        return executionPath;
    }
}
