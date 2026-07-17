package com.finnetlimited.reportservice.core.server.utils.tree;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 06-Apr-2010
 * Time: 20:47:34
 */
public class Tree<T> {
    private Node<T> rootElement;

    /**
     * Default ctor.
     */
    public Tree() {
        super();
    }

    /**
     * Return the root Node of the tree.
     *
     * @return the root element.
     */
    public Node<T> getRootElement() {
        return this.rootElement;
    }

    /**
     * Set the root Element for the tree.
     *
     * @param rootElement the root element to set.
     */
    public void setRootElement(Node<T> rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * Returns the Tree<T> as a List of Node<T> objects. The elements of the
     * List are generated from a pre-order traversal of the tree.
     *
     * @return a List<Node<T>>.
     */
    public ArrayList<Node<T>> toList() {
        ArrayList<Node<T>> list = new ArrayList<>();
        walk(rootElement, list);
        return list;
    }

    /**
     * Returns a String representation of the Tree. The elements are generated
     * from a pre-order traversal of the Tree.
     *
     * @return the String representation of the Tree.
     */
    public String toString() {
        return toList().toString();
    }

    /**
     * Walks the Tree in pre-order style. This is a recursive method, and is
     * called from the toList() method with the root element as the first
     * argument. It appends to the second argument, which is passed by reference     * as it recurses down the tree.
     *
     * @param element the starting element.
     * @param list    the output of the walk.
     */
    private void walk(Node<T> element, ArrayList<Node<T>> list) {
        list.add(element);
        for (Node<T> data : element.getChildren()) {
            walk(data, list);
        }
    }

}
