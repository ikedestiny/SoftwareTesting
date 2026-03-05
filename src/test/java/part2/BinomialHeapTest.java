package part2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

class BinomialHeapTest {

    private BinomialHeap heap;

    @BeforeEach
    public void setUp() {
        heap = new BinomialHeap(); // init new BinomHeap
    }

    @Test
    @DisplayName("Test 1: trying to get minimal element in empty heap")
    public void testGetMinInEmpty(){
        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());
        assertThrows(NullPointerException.class, heap::getMin);
    }

    @ParameterizedTest
    @DisplayName("Test 2: insert one element in empty heap")
    @MethodSource("singleInsertTestData")
    public void testInsertOne(List<Integer> nums, int expectedMin) {
        heap.insert(nums.get(0));
        assertFalse(heap.isEmpty());
        assertEquals(expectedMin, heap.getMin());
        assertTrue(heap.isValid());
    }

    static Stream<Arguments> singleInsertTestData() {
        return Stream.of(
            Arguments.of(List.of(5), 5),
            Arguments.of(List.of(3), 3),
            Arguments.of(List.of(10), 10),
            Arguments.of(List.of(-1), -1)
        );
    }

    @ParameterizedTest
    @DisplayName("Test 3: insert multiple elements in empty heap")
    @MethodSource("insertionTestData")
    public void testInsertMultiple(List<Integer> values, int expectedNum) {
        for (int value: values) {
            heap.insert(value);
        }
        assertEquals(expectedNum, heap.getMin());
        assertTrue(heap.isValid());
    }

    static Stream<Arguments> insertionTestData() {
        return Stream.of(
            Arguments.of(List.of(5), 5),
            Arguments.of(List.of(3, 3, 4), 3),
            Arguments.of(List.of(10, 20, 5, 15), 5),
            Arguments.of(List.of(-1, -5, 0), -5)
        );
    }

    @ParameterizedTest
    @DisplayName("Test 4: merge 2 heaps")
    @MethodSource("mergeTestData")
    public void testMerge(List<Integer> valuesForCurr, List<Integer> valuesForNew, int expectedMin, int expectedSize){
        for(int value : valuesForCurr){
            heap.insert(value);
        }
        BinomialHeap newHeap = new BinomialHeap();
        for(int value : valuesForNew){
            newHeap.insert(value);
        }
        heap.merge(newHeap);
        assertEquals(expectedMin, heap.getMin());
        assertEquals(expectedSize, heap.size());
        assertTrue(heap.isValid());
        assertTrue(newHeap.isEmpty());
    }

    static Stream<Arguments> mergeTestData() {
        return Stream.of(
            Arguments.of(List.of(5), List.of(3), 3, 2),
            Arguments.of(List.of(10, 20), List.of(5, 15), 5, 4),
            Arguments.of(List.of(1, 2, 3), List.of(), 1, 3),
            Arguments.of(List.of(), List.of(7, 4), 4, 2),
            Arguments.of(List.of(100, 200), List.of(50, 150, 30), 30, 5)
        );
    }

    @Test
    @DisplayName("Test 5: trying to extract min from empty heap")
    public void testExtractFromEmpty(){
        assertTrue(heap.isEmpty());
        assertThrows(NoSuchElementException.class, heap::extractMin);
    }
    

    @ParameterizedTest
    @DisplayName("Test 6: extract min from non-empty heap")
    @MethodSource("extractTestData")
    public void testExtract(List<Integer> data, int expectedMin){
        for(int value : data){
            heap.insert(value);
        }
        assertEquals(expectedMin, heap.extractMin());
        assertTrue(heap.isValid());
    }

    static Stream<Arguments> extractTestData() {
        return Stream.of(
            Arguments.of(List.of(5), 5),
            Arguments.of(List.of(10, 20), 10),
            Arguments.of(List.of(1, 2, 3), 1),
            Arguments.of(List.of(50, 150, 30), 30)
        );
    }

    @ParameterizedTest
    @DisplayName("Test 7: decrease key in heap")
    @MethodSource("decreaseTestData")
    public void testDecreaseKey(List<Integer> data, int targetIdx, int newValue, int expectedMin){
        BHNode[] nodes = new BHNode[data.size()];
        for(int i = 0; i < data.size(); i++){
            nodes[i] = heap.insert(data.get(i));
        }

        heap.decreaseKey(nodes[targetIdx], newValue);
        assertEquals(expectedMin, heap.getMin());
        assertTrue(heap.isValid());
    }

    static Stream<Arguments> decreaseTestData(){
        return Stream.of(
            Arguments.of(List.of(5), 0, 2, 2),
            Arguments.of(List.of(10, 5, 7), 0, 3, 3),
            Arguments.of(List.of(3, 8, 12), 0, 1, 1),
            Arguments.of(List.of(4, 2, 9), 2, 5, 2),
            Arguments.of(List.of(100, 200, 50), 1, Integer.MIN_VALUE, Integer.MIN_VALUE),
            Arguments.of(List.of(7, 3, 10), 2, 10, 3)
        );
    }

    @ParameterizedTest
    @DisplayName("Test 8: delete node out of heap")
    @MethodSource("deleteTestData")
    public void testDeleteNode(List<Integer> data, int targetIdx, Integer expectedMin, int expectedSize) {
        BHNode[] nodes = new BHNode[data.size()];
        for(int i = 0; i < data.size(); i++){
            nodes[i] = heap.insert(data.get(i));
        }

        heap.deleteNode(nodes[targetIdx]);
        assertEquals(expectedSize, heap.size());

        if(expectedMin != null) {
            assertEquals(expectedMin, heap.getMin());
        } else {
            assertTrue(heap.isEmpty());
        }
        assertTrue(heap.isValid());
    }

    static Stream<Arguments> deleteTestData() {
        return Stream.of(
            Arguments.of(List.of(5), 0, null, 0),
            Arguments.of(List.of(1, 5, 3), 0, 3, 2),
            Arguments.of(List.of(2, 4, 6), 1, 2, 2),
            Arguments.of(List.of(10, 7, 15), 0, 7, 2),
            Arguments.of(List.of(20, 10, 30, 5, 40), 2, 5, 4),
            Arguments.of(List.of(8, 3, 12, 1, 9), 4, 1, 4)
        ); 
    }

    @Test
    @DisplayName("Test 9: testing edge cases")
    public void testOfEdgeCases(){
        heap.insert(Integer.MIN_VALUE);
        heap.insert(Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE, heap.extractMin());
    }

} 