package part2;

import java.util.ArrayList;
import java.util.List;

class BTreeNode {
    int[] keys;
    int t;  // minimum degree (t=2 for max 4 keys)
    BTreeNode[] children;
    int n;  // current number of keys
    boolean leaf;
    List<String> visitedPoints = new ArrayList<>();

    public BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        this.keys = new int[2*t - 1];
        this.children = new BTreeNode[2*t];
        this.n = 0;
    }

    public void traverse() {
        visitedPoints.add("Node_Enter_" + (leaf ? "Leaf" : "Internal"));

        for (int i = 0; i < n; i++) {
            if (!leaf) {
                visitedPoints.add("Child_Before_" + i);
                children[i].traverse();
            }
            visitedPoints.add("Key_" + keys[i]);
        }

        if (!leaf) {
            visitedPoints.add("Child_After_Last");
            children[n].traverse();
        }

        visitedPoints.add("Node_Exit");
    }

    public BTreeNode search(int key) {
        visitedPoints.add("Search_Start_" + key);

        int i = 0;
        while (i < n && key > keys[i]) {
            i++;
        }

        visitedPoints.add("Search_Compare_" + i);

        if (i < n && keys[i] == key) {
            visitedPoints.add("Search_Found_" + key);
            return this;
        }

        if (leaf) {
            visitedPoints.add("Search_Not_Found_Leaf");
            return null;
        }

        visitedPoints.add("Search_Recurse_Child_" + i);
        return children[i].search(key);
    }

    public void insertNonFull(int key) {
        visitedPoints.add("InsertNonFull_Start_" + key);

        int i = n - 1;

        if (leaf) {
            visitedPoints.add("Insert_Leaf_Find_Position");
            while (i >= 0 && keys[i] > key) {
                keys[i + 1] = keys[i];
                i--;
            }

            keys[i + 1] = key;
            n++;
            visitedPoints.add("Insert_Leaf_Complete");
        } else {
            visitedPoints.add("Insert_Internal_Find_Child");
            while (i >= 0 && keys[i] > key) {
                i--;
            }
            i++;

            if (children[i].n == 2*t - 1) {
                visitedPoints.add("Insert_Split_Child_Start_" + i);
                splitChild(i, children[i]);
                visitedPoints.add("Insert_Split_Child_Complete");

                if (keys[i] < key) {
                    i++;
                }
            }
            children[i].insertNonFull(key);
        }
    }

    public void splitChild(int i, BTreeNode y) {
        visitedPoints.add("SplitChild_Start_" + i);

        BTreeNode z = new BTreeNode(y.t, y.leaf);
        z.n = t - 1;

        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
        }

        if (!y.leaf) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
            }
        }

        y.n = t - 1;

        for (int j = n; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        children[i + 1] = z;

        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        keys[i] = y.keys[t - 1];

        n++;
        visitedPoints.add("SplitChild_Complete");
    }
}

