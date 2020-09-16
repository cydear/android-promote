package com.code.qpp.datastruct.binarytree.basic;

/**
 * @ClassName FollowOrderTreeNode
 * @Author: Lary.huang
 * @CreateDate: 2020/9/9 11:00 AM
 * @Version: 1.0
 * @Description: 后序遍历: 左 -> 右 -> 根
 */
public class FollowOrderTreeNode {
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
        //中序遍历
        followTraverseBinaryTree(rootTreeNode);
        System.out.println(" ");
        System.exit(0);
    }

    /**
     * 后序遍历
     *
     * @param rootTreeNode
     */
    private static void followTraverseBinaryTree(TreeNode rootTreeNode) {
        if (rootTreeNode == null) return;
        //左节点遍历
        followTraverseBinaryTree(rootTreeNode.getLeftTreeNode());
        //右节点遍历
        followTraverseBinaryTree(rootTreeNode.getRightTreeNode());
        //根节点
        System.out.print(rootTreeNode.getValue() + " ");
    }
}
