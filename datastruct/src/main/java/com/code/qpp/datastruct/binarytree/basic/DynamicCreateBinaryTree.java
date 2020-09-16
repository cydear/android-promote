package com.code.qpp.datastruct.binarytree.basic;

/**
 * @ClassName CreateBinaryTree
 * @Author: Lary.huang
 * @CreateDate: 2020/9/9 11:14 AM
 * @Version: 1.0
 * @Description: 动态创建二叉树
 */
public class DynamicCreateBinaryTree {
    public static void main(String[] args) {
        int[] arrays = {10, 20, 3, 5, 16, 28, 18, 11, 19, 8, 5, 17};
        //动态创建树
        TreeRoot root = new TreeRoot();
        for (int value : arrays) {
            createBinaryTree(root, value);
        }
        //先序遍历
        PreOrderTreeNode.preTraverseBinaryTree(root.getTreeRoot());
        System.out.println(" ");
        System.out.println("max height=>" + getHeight(root.getTreeRoot()));
        System.out.println("max num=>" + getMax(root.getTreeRoot()));
        System.exit(0);
    }

    /**
     * 创建二叉树
     *
     * @param treeRoot
     * @param value
     */
    public static void createBinaryTree(TreeRoot treeRoot, int value) {
        //树根节点为空，将第一个值作为根节点
        if (treeRoot.getTreeRoot() == null) {
            TreeNode treeNode = new TreeNode(value);
            treeRoot.setTreeRoot(treeNode);
        } else {
            //当前树根
            TreeNode tempRoot = treeRoot.getTreeRoot();
            while (tempRoot != null) {
                //当前值大于根植，往右边走
                if (value > tempRoot.getValue()) {
                    //右边没有树根，那就直接插入
                    if (tempRoot.getRightTreeNode() == null) {
                        tempRoot.setRightTreeNode(new TreeNode(value));
                        return;
                    } else {
                        //如果右边有树根，到右边的树根去
                        tempRoot = tempRoot.getRightTreeNode();
                    }
                } else {
                    //左没有树根，那就直接插入
                    if (tempRoot.getLeftTreeNode() == null) {
                        tempRoot.setLeftTreeNode(new TreeNode(value));
                        return;
                    } else {
                        //如果左有树根，到左边的树根去
                        tempRoot = tempRoot.getLeftTreeNode();
                    }
                }
            }
        }
    }

    /**
     * 查询二叉树深度
     *
     * @param treeNode
     * @return
     */
    public static int getHeight(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        } else {
            //左边深度
            int left = getHeight(treeNode.getLeftTreeNode());
            //右边的深度
            int right = getHeight(treeNode.getRightTreeNode());

            int max = right > left ? right : left;
            return max + 1;
        }
    }

    /**
     * 查找树中的最大值
     *
     * @param rootTreeNode
     * @return
     */
    public static int getMax(TreeNode rootTreeNode) {
        if (rootTreeNode == null) {
            return -1;
        } else {
            //查找左边最大值
            int left = getMax(rootTreeNode.getLeftTreeNode());
            //查找右边最大值
            int right = getMax(rootTreeNode.getRightTreeNode());
            //与当前根节点比较
            int currentRootValue = rootTreeNode.getValue();
            int max = right > left ? right : left;
            max = currentRootValue > max ? currentRootValue : max;
            return max;
        }
    }
}
