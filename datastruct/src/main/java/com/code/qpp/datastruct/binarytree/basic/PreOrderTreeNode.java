package com.code.qpp.datastruct.binarytree.basic;

/**
 * @ClassName PreOrderTreeNode
 * @Author: Lary.huang
 * @CreateDate: 2020/9/9 10:36 AM
 * @Version: 1.0
 * @Description: 先序遍历，根 -> 左 -> 右
 */
public class PreOrderTreeNode {
    public static void main(String[] args) {
        //构建二叉树
        TreeNode rootTreeNode = new TreeNode(10);
        TreeNode treeNode9 = new TreeNode(9);
        TreeNode treeNode20 = new TreeNode(20);
        TreeNode treeNode8 = new TreeNode(8);
        TreeNode treeNode11 = new TreeNode(11);
        TreeNode treeNode15 = new TreeNode(15);
        TreeNode treeNode35 = new TreeNode(35);
        TreeNode treeNode6 = new TreeNode(6);
        TreeNode treeNode12 = new TreeNode(12);
        TreeNode treeNode14 = new TreeNode(14);
        TreeNode treeNode16 = new TreeNode(16);
        //关联二叉树
        rootTreeNode.setTreeNode(treeNode9, treeNode20);
        treeNode9.setTreeNode(treeNode8, treeNode11);
        treeNode8.setTreeNode(treeNode6, treeNode12);
        treeNode20.setTreeNode(treeNode15, treeNode35);
        treeNode15.setTreeNode(treeNode14, treeNode16);
        //先序二叉树
        preTraverseBinaryTree(rootTreeNode);
        System.out.println(" ");
        System.exit(0);
    }

    /**
     * 先序遍历二叉树
     *
     * @param rootTreeNode
     */
    public static void preTraverseBinaryTree(TreeNode rootTreeNode) {
        if (rootTreeNode == null) return;
        //访问根节点
        System.out.print(rootTreeNode.getValue() + " ");
        //访问左节点
        preTraverseBinaryTree(rootTreeNode.getLeftTreeNode());
        //访问右节点
        preTraverseBinaryTree(rootTreeNode.getRightTreeNode());
    }
}
