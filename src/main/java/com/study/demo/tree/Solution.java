package com.study.demo.tree;

class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;
    // 这个属性有些操作需要，会是代码更简洁，空间换时间
    TreeNode parent;
 
    public TreeNode(int val){
        this.val = val;
    }
}
 
public class Solution {
 
    // 迭代版本比递归版本效率高
    public TreeNode search(TreeNode root, int val) {
        TreeNode itr = root;
        while (itr != null && itr.val != val) {
            if (itr.val < val) {
                itr =  itr.right;
            } else {
                itr = itr.left;
            }
        }
        return itr;
    }
 
    // 找bst的最右侧即可
    public TreeNode max(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode itr = root;
        while(itr.right != null) {
            itr = itr.right;
        }
        return itr;
    }
 
    // 找bst的最左侧即可
    public TreeNode min(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode itr = root;
        while(itr.left != null) {
            itr = itr.left;
        }
        return itr;
    }
 
    // 后继节点只有两种可能，一种是x的右子树的最小节点；另一种，如果没有右子树，就得找第一个右侧的父节点
    public TreeNode successor(TreeNode x) {
        if (x.right != null){
            return min(x.right);
        }
 
        while(x.parent.left != x){
            x = x.parent;
        }
 
        return x.parent;
    }
 
    // 插入算法与查找类似，一直遍历直到为空，就插在空的父节点下面
    public TreeNode insert(TreeNode root, TreeNode x) {
        TreeNode itr = root, p = null;
        while(itr != null){
            p = itr;
            if (itr.val > x.val) {
                itr = itr.left;
            } else {
                itr = itr.right;
            }
        }
        if (p == null) {
            return x;
        }
 
        if (p.val < x.val) {
            p.right = x;
        } else {
            p.left = x;
        }
        x.parent = p;
        return root;
    }
 
    // 删除操作比较复杂，共有四种情况。考虑一种简单的，即被删除的节点x至多有一个子树，那么很简单，只需要用x的子树代替x即可；
    // 事实上，所有四种情况都可以转化为这种简单情况来处理。
    // 一一来看：
    // 1. x.left空，用x.right替代x即可；
    // 2. x.right空，用x.left替代x即可；
    // 3. x.left和x.right都不空，先找x的后继，此时x的后继肯定位于右子树。如果x.right.left为空，那么x的后继其实就是x.right，此时用x.right替代x即可；
    // 4. x.left和x.right都不空，且x.right.left不为空，那么x的后继位于右子树的最小值。假设是y，y.left一定为空，那么可用y.right替换y，在用y替换x；
    public TreeNode delete(TreeNode root, TreeNode x) {
        if (x == null) {
            return null;
        }
 
        if (x.left == null) {
            return transfer(root, x, x.right);
        } else if (x.right == null){
            return transfer(root, x, x.left);
        } else {
            TreeNode successor = min(x.right); //找后继其实就是找右子树的最小值了
            if (successor.parent != x){
                transfer(root, successor, successor.right);
                successor.right = x.right;
                x.right.parent = successor;
            }
            successor.left = x.left;
            x.left.parent = successor;
            return transfer(root, x, successor);
        }
    }
 
    // 用y替代x，删除操作中的辅助函数，只调整父节点。
    public TreeNode transfer(TreeNode root, TreeNode x, TreeNode y) {
        if (x.parent == null) {
            return y;
        }
        if (x.parent.left == x) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        if (y != null){
            y.parent = x.parent;
        }
        return root;
    }
 
    public void print(TreeNode root){
        if (root == null) {
            return;
        }
        print(root.left);
        System.out.println(root.val);
        print(root.right);
    }
 
    public static void main(String args[]){
        Solution solution = new Solution();
        TreeNode root = new TreeNode(10);
        TreeNode n1 = new TreeNode(1);
        TreeNode n2 = new TreeNode(90);
        TreeNode n3 = new TreeNode(100);
        TreeNode n4 = new TreeNode(80);
        TreeNode n5 = new TreeNode(110);
        TreeNode n6 = new TreeNode(95);
 
        solution.insert(root, n1);
        solution.insert(root, n2);
        solution.insert(root, n3);
        solution.insert(root, n4);
        solution.insert(root, n5);
        solution.insert(root, n6);
        //System.out.println(new Solution().successor(n1).val);
        solution.print(root);
 
        solution.delete(root, n2);
        solution.print(root);
    }
}