package part2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BTreeNodeTest {

    private BTree tree;

    @BeforeEach
    public void setUp() {
        tree = new BTree(2); // t=2 means max 4 keys per node
    }

    @Test
    @DisplayName("Test 1: Insert into empty tree")
    public void testInsertEmpty() {
        tree.insert(10);

        List<String> expected = List.of("Create_New_Root_10");
        assertEquals(expected, tree.getExecutionPath());
    }

    @Test
    @DisplayName("Test 2: Insert into leaf without split")
    public void testInsertLeafNoSplit() {
        tree.insert(10);
        tree.insert(20);

        List<String> expected = List.of(
                "Insert_Into_Existing",
                "InsertNonFull_Start_20",
                "Insert_Leaf_Find_Position",
                "Insert_Leaf_Complete",
                "Node_Exit"
        );
        assertEquals(expected, tree.getExecutionPath());
    }

    @Test
    @DisplayName("Test 3: Search existing key")
    public void testSearchExisting() {
        tree.insert(10);
        tree.insert(20);
        tree.insert(5);

        tree.search(20);

        List<String> path = tree.getExecutionPath();
        assertTrue(path.contains("Search_Found_20"));
    }

    @Test
    @DisplayName("Test 4: Search non-existing key")
    public void testSearchNonExisting() {
        tree.insert(10);
        tree.insert(20);

        tree.search(15);

        List<String> path = tree.getExecutionPath();
        assertTrue(path.contains("Search_Not_Found_Leaf"));
    }

    @Test
    @DisplayName("Test 5: Insert causing root split")
    public void testRootSplit() {
        // Insert keys to fill root
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);

        // This insert should cause split (max 4 keys)
        tree.insert(25);

        List<String> path = tree.getExecutionPath();
        assertTrue(path.contains("Split_Root_Start"));
        assertTrue(path.contains("Split_Root_Complete"));
    }

    @Test
    @DisplayName("Test 6: Insert causing internal node split")
    public void testInternalNodeSplit() {
        // Build a tree that will cause internal node split
        int[] values = {10, 20, 30, 40, 50, 60, 70, 25, 35, 45};
        for (int val : values) {
            tree.insert(val);
        }

        tree.insert(55); // This should cause some splits

        List<String> path = tree.getExecutionPath();
        assertTrue(path.stream().anyMatch(p -> p.contains("SplitChild")));
    }

    @Test
    @DisplayName("Test 7: Complex traversal pattern")
    public void testTraversal() {
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 85};
        for (int val : values) {
            tree.insert(val);
        }

        tree.traverse();
        List<String> path = tree.getExecutionPath();

        // Verify traversal entered internal nodes and leaves
        assertTrue(path.contains("Node_Enter_Internal"));
        assertTrue(path.contains("Node_Enter_Leaf"));
    }
}