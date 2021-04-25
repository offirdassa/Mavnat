/**
 * public class AVLNode
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 * <p>
 * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
 * arguments. Changing these would break the automatic tester, and would result in worse grade.
 * <p>
 * However, you are allowed (and required) to implement the given functions, and can add functions of your own
 * according to your needs.
 */

public class AVLTree {

    private AVLNode root;
    private int size;
    private AVLNode min;
    private AVLNode max;

    /**
     * This constructor creates an empty AVLTree.
     */
    public AVLTree(){
        AVLNode virtual = new AVLNode(-1,null);
        this.root = virtual;
        this.min = virtual;
        this.max = virtual;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return (this.root == null);
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public Boolean search(int k) {
        return getNode(k).getValue();
    }

    private AVLNode getNode(int k) {
        AVLNode node = this.root;
        while (node.getKey() != -1) {
            if (node.getKey() == k)
                return node;
            if (k < node.getKey())
                node = node.getLeft();
            else node = node.getRight();
        }
        return node;

    }


    /**
     * public int insert(int k, boolean i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
	 * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
	 * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, boolean i) {
        AVLNode newNode = new AVLNode(k,i);
        if (this.root == null) {
            this.root = newNode;
            updateFields(newNode,true);
            return 0;
        }
        AVLNode location = getNode(k); //virtual node to be replaced
        if (location.isRealNode())
            return -1;
        //assigning
        AVLNode parent = location.getParent();
        newNode.setParent(parent);
        if (parent.getRight() == location)
            parent.setRight(newNode);
        else parent.setLeft(newNode);
        updateFields(newNode,true);
        return 42;    // to be replaced by student code
    }

    //true if insert, false if delete
    private void updateFields(AVLNode node, boolean b) {
        int k = node.getKey();
        if (b) {
            if (k < this.min.getKey() | !this.min.isRealNode())
                this.min = node;
            if (k > this.max.getKey())
                this.max = node;
            this.size += 1;

        }
    }


    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        return 42;    // to be replaced by student code
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public Boolean min() {
        return this.min.getValue();
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public Boolean max() {
        return this.max.getValue(); // to be replaced by student code
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size];
        AVLNode node = this.root;
        int[] index = new int[1];
        inOrderKeys_rec(node,arr,index);
        return arr;
    }

    private void inOrderKeys_rec(AVLNode node, int[] arr, int[] index) {
        if (node.getKey() == -1) {
            arr[index[0]] = node.getKey();
            index[0] += 1;
        } else {
            inOrderKeys_rec(node.getLeft(), arr, index);
            arr[index[0]] = node.getKey();
            index[0] += 1;
            inOrderKeys_rec(node.getRight(), arr, index);
        }
    }


    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public boolean[] infoToArray() {
        boolean[] arr = new boolean[this.size];
        AVLNode node = this.root;
        int[] index = new int[1];
        inOrderInfo_rec(node,arr,index);
        return arr;
    }

    private void inOrderInfo_rec(AVLNode node, boolean[] arr, int[] index) {
        if (node.getKey() == -1) {
            arr[index[0]] = node.getValue();
            index[0] += 1;
        } else {
            inOrderInfo_rec(node.getLeft(), arr, index);
            arr[index[0]] = node.getValue();
            index[0] += 1;
            inOrderInfo_rec(node.getRight(), arr, index);
        }
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public AVLNode getRoot() {
        return this.root;
    }

    /**
     * public boolean prefixXor(int k)
     *
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     *
     * precondition: this.search(k) != null
     *
     */
    public boolean prefixXor(int k){
        AVLNode node = getNode(k);
        AVLNode parent=node.getParent();
        boolean res=node.prefixXor;
        while (parent!=null) {
            if (parent.getRight()==node)
                res=xor(res,parent.prefixXor);
            node=parent;
            parent=parent.getParent();
        }
        return res;
    }

    private static boolean xor(boolean x, boolean y) {
        return (x != y);
    }

    /**
     * public AVLNode successor
     *
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     */
    public AVLNode successor(AVLNode node){
        if (node.getRight().getKey() != -1) {
            node = node.getRight();
            while (node.getLeft().getKey() != -1) {
                node = node.getLeft();
            }
            return node;
        }
        AVLNode parent = node.getParent();
        while (parent != null & node == parent.getRight()) {
            node = parent;
            parent = node.getParent();
        }
        return parent;
    }

    /**
     * public boolean succPrefixXor(int k)
     *
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     *
     * precondition: this.search(k) != null
     */
    public boolean succPrefixXor(int k){
        AVLNode node = this.min;
        boolean res = node.getValue();
        while(node.getKey() != k) {
            node = successor(node);
            res = xor(res,node.getValue());
        }
        return res;
    }


    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     * <p>
     * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
     * arguments. Changing these would break the automatic tester, and would result in worse grade.
     * <p>
     * However, you are allowed (and required) to implement the given functions, and can add functions of your own
     * according to your needs.
     */
    public class AVLNode {
        private int key;
        private Boolean info;
        private AVLNode right;
        private AVLNode left;
        private AVLNode parent;
        private int height;
        private boolean prefixXor;

        public AVLNode(int key, Boolean info) {
            if (key != -1) { //Real Node
                this.key = key;
                this.info = info;
                this.left = new AVLNode(-1, null);
                this.right = new AVLNode(-1,null);
                this.prefixXor = info;
            } else { //Virtual Node;
                this.key = -1;
                this.info = null;
                this.height = -1;
            }

        }

        //returns node's key (for virtual node return -1)
        public int getKey() {
            return this.key; // to be replaced by student code
        }

        public void setKey(int k) {
            this.key = k;
        }

        //returns node's value [info] (for virtual node return null)
        public Boolean getValue() {
            return this.info; // to be replaced by student code
        }

        public void setValue(boolean i) {
            this.info = i;
        }

        //sets left child
        public void setLeft(AVLNode node) {
            this.left = node;
        }

        //returns left child (if there is no left child return null)
        public AVLNode getLeft() {
            return this.left; // to be replaced by student code
        }

        //sets right child
        public void setRight(AVLNode node) {
            this.right = node;
        }

        //returns right child (if there is no right child return null)
        public AVLNode getRight() {
            return this.right;
        }

        //sets parent
        public void setParent(AVLNode node) {
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        public AVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            return !(this.key == -1);
        }

        // sets the height of the node
        public void setHeight(int height) {
            this.height = height;
        }

        // Returns the height of the node (-1 for virtual nodes)
        public int getHeight() {
            return this.height;
        }
    }

}


