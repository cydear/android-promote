package com.code.qpp.datastruct.binarytree.basic;

/**
 * @ClassName TreeNode
 * @Author: Lary.huang
 * @CreateDate: 2020/9/9 10:28 AM
 * @Version: 1.0
 * @Description: 基础树形结构，树节点
 */
public class TreeNode {
    /**
     * 左节点
     */
    private TreeNode leftTreeNode;
    /**
     * 右节点
     */
    private TreeNode rightTreeNode;
    /**
     * 节点对应的值
     */
    private int value;

    public TreeNode(int value) {
        this.value = value;
    }

    public TreeNode getLeftTreeNode() {
        return leftTreeNode;
    }

    public void setLeftTreeNode(TreeNode leftTreeNode) {
        this.leftTreeNode = leftTreeNode;
    }

    public TreeNode getRightTreeNode() {
        return rightTreeNode;
    }

    public void setRightTreeNode(TreeNode rightTreeNode) {
        this.rightTreeNode = rightTreeNode;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setTreeNode(TreeNode leftNode, TreeNode rightNode) {
        this.leftTreeNode = leftNode;
        this.rightTreeNode = rightNode;
    }
}
