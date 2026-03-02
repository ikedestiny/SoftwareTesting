package part2;

import java.util.ArrayList;
import java.util.List;

class BHNode {
    private int value;
    private BHNode parent;
    private List<BHNode> children;
    private int degree;
    private boolean marked;

    public BHNode(int val){
        this.value = val;
        this.parent = null;
        this.children = new ArrayList<>();
        this.degree = 0;
        this.marked = false;
    }

    public int getDegree() {
        return degree;
        
    }

    public void setDegree(int degree) {
        this.degree = degree;
        
    }

    public BHNode getParent() {
        return parent;
        
    }

    public void setParent(BHNode parent) {
        this.parent = parent;
        
    }

    public List<BHNode> getChildren() {
        return children;
        
    }

    public void setChildren(List<BHNode> children) {
        this.children = children;
        
    }

    public int getValue() {
        return value;
        
    }

    public void setValue(int value) {
        this.value = value;
        
    }
}

