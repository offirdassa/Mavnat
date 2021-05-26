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
    /** O(1) **/
    public AVLTree(){
        AVLNode virtual = new AVLNode(-1,null);
        this.root = virtual;
        this.min = virtual;
        this.max = virtual;
    }
    /** O(1) **/
    /**         ###################for tests should be deleted ###############**/
    public AVLNode getMin() { return this.min;}

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    /** O(1) **/
    public boolean empty() {return (this.root == null);}

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    /** O(logn)) **/
    public Boolean search(int k) {
        return getNode(k).getValue();
    }

    /**
     * public AVLNode getNode(int k)
     * <p>
     * returns the node with key k if it exists in the tree
     * otherwise, returns virtual node (was set in AVLNode)
     */
    /** O(logn)) **/
    public AVLNode getNode(int k) { //should be private
        AVLNode node = this.root;
        while (node.isRealNode()) {
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
    /** O(logn)) **/
    // the numbering in the code corresponds to the steps of the algorithm, as shown in class
    public int insert(int k, boolean i) {
        int rebalancing = 1;
        AVLNode newNode = new AVLNode(k,i);
        if (!this.root.isRealNode()) { //if the tree is empty
            this.root = newNode;
            updateTreeFields(newNode,true);
            this.root.setHeight();
            return rebalancing;
        }
        AVLNode location = getNode(k); //the location that the new node should be insert to
        if (location.isRealNode()) // if a node with key=k is already exists
            return -1;
        AVLNode parent = location.getParent();
        setChild(location,parent,newNode); // 1+2: sets the new node in its place
        newNode.setPrefixXor();
        AVLNode node = parent; //just for clarity
        updateTreeFields(newNode,true);
        //To make a legal insert for BST delete the while loop
        //loop's purpose: balance the tree and its fields
        while (node != null) {//3
            boolean[] changes = node.updateNodeFields(); //first arg - if height changed; second arg - if prefixXor changed
            AVLNode newParent = node.getParent();
            int BF = balanceFactor(node); //3.1
            if (Math.abs(BF) < 2 & !changes[0] & !changes[1]) { //3.2
                return rebalancing;
            }
            if (changes[0]) {//rebalancing was needed
                rebalancing++;
            }
            if (Math.abs(BF) < 2) {//3.3
                node = newParent;
            }
            else {//3.4
                if (!changes[0]) rebalancing++;
                rotate(node,BF);
                node = newParent;
            }
        }
        return rebalancing;
    }
    /**
     * private void rotate(AVLNode node,int BF)
     * <p>
     * performs the rotation of node due to its BF
     * the conditions are suitable for both insertion and deletion
     */
    /** O(1)) **/
    private void rotate(AVLNode node, int BF) {
        if (BF > 0) {
            if (balanceFactor(node.getLeft()) >= 0) { //right rotation
                rightRotate(node);
            } else { // left then right
                leftRotate(node.getLeft());
                rightRotate(node);
            }
        } else { // left rotation
            if (balanceFactor(node.getRight()) <= 0) {
                leftRotate(node);
            } else { // right then left
                rightRotate(node.getRight());
                leftRotate(node);
            }
        }
    }
    /**
     * private void leftRotate(AVLNode A)
     * <p>
     * performs a left rotation to the node A
     * update the fields of the moved nodes
     */
    /** O(1)) **/
    private void leftRotate(AVLNode A) {
        AVLNode B = A.getRight();
        AVLNode BLeft = B.getLeft();
        AVLNode AParent = A.getParent();
        Boolean leftSon = A.isLeftChild(); // which child A to AParent
        // B's left son becomes A's right
        A.setRight(BLeft);
        BLeft.setParent(A);
        // A becomes B's left son
        B.setLeft(A);
        A.setParent(B);
        // B's new parent is AParent
        B.setParent(AParent);
        //updating nodes fields of A and B
        A.setHeight();
        B.setHeight();
        A.setPrefixXor();
        B.setPrefixXor();
        // if the rotated node was the root
        if (leftSon==null)
            this.root = B;
        // setting B the child of AParent
        else {
            if (leftSon)
                AParent.setLeft(B);
            else
                AParent.setRight(B); }
    }
    /**
     * private void rightRotate(AVLNode A)
     * <p>
     * performs a right rotation to the node A
     * update the fields of the moved nodes
     */
    /** O(1)) **/
    private void rightRotate(AVLNode A) {
        AVLNode B = A.getLeft();
        AVLNode BRight = B.getRight();
        AVLNode AParent = A.getParent();
        Boolean leftSon = A.isLeftChild(); // which child A to AParent
        // B's right son becomes A's left
        A.setLeft(BRight);
        BRight.setParent(A);
        // A becomes B's right son
        B.setRight(A);
        A.setParent(B);
        // B's new parent is AParent
        B.setParent(AParent);
        //updating nodes fields of A and B
        A.setHeight();
        B.setHeight();
        A.setPrefixXor();
        B.setPrefixXor();
        // if the rotated node was the root
        if (leftSon==null)
            this.root = B;
        // setting B the child of AParent
        else {
            if (leftSon)
                AParent.setLeft(B);
            else
                AParent.setRight(B); }
    }
    /**
     * private void setChild(AVLNode location,AVLNode parent,AVLNode newNode)
     * <p>
     * put newNode as a child of parent instead of location
     */
    /** O(1)) **/
    private void setChild(AVLNode location, AVLNode parent, AVLNode newNode) {
        if (parent!=null){//if the location isn't the root
            newNode.setParent(parent);
            //check which son is location to parent
            if (parent.getRight() == location)
                parent.setRight(newNode);
            else parent.setLeft(newNode); }
        else{//location is the root
            this.root=newNode;
            this.root.setParent(null);}
    }
    /**
     * private int balanceFactor(AVLNode node)
     * <p>
     * compute and return the balance factor of node
     */
    /** O(1)) **/
    private int balanceFactor(AVLNode node) {
        return node.left.getHeight()-node.right.getHeight();
    }

    /**
     * private void updateTreeFields(AVLNode node,boolean b)
     * pre: boolean b is true for insert and false for delete
     * <p>
     * update the fields size,min,max in case of insertion/deletion
     */
    /** insertion: O(1), deletion: O(logn)) **/
    private void updateTreeFields(AVLNode node, boolean b) {
        int k = node.getKey();
        if (b) { // node was inserted to the tree
            if (k < this.min.getKey() | !this.min.isRealNode()) //the 2nd cond for insertion into empty tree
                this.min = node;
            if (k > this.max.getKey())
                this.max = node;
            this.size += 1;
        } else { // node was deleted from the tree
            if (this.size==1){ // node was the only node in the tree, setting empty tree fields
                this.min=new AVLNode(-1,null);
                this.max=this.min;
            }
            if (k == this.min.getKey())
                this.min = successor(node);
            if (k == this.max.getKey())
                this.max = predecessor(node);

            this.size -= 1; }
    }
    /**
     * private AVLNode predecessor(AVLNode node)
     * <p>
     * returns the node with maximal key k1 that maintains: k1<key(node)
    /** O(logn)) **/
    private AVLNode predecessor(AVLNode node) { //should be private
        if (node == this.min) return null;
        if (node.getLeft().getKey() != -1) { // the predecessor is in the left subtree of node
            node = node.getLeft();
            while (node.getRight().getKey() != -1) {// going right in the left subtree until gets to virtual node
                node = node.getRight();
            }
            return node;
        }
        AVLNode parent = node.getParent();
        // the predecessor is above node in the tree
        // going up until we were climb left in the tree
        while (parent != null && (node == this.root || node == parent.getLeft())) {
            node = parent;
            parent = node.getParent();
        }
        return parent;
    }


    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     */
    /** O(logn)) **/
    // the numbering in the code corresponds to the steps of the algorithm, as shown in class
    public int delete(int k) {
        int rebalancing = 0;
        AVLNode node = getNode(k);
        if (!node.isRealNode()) { return -1;} //if node with key k doesn't exist
        AVLNode parent = node.getParent();
        updateTreeFields(node,false); //update th tree fields
        if (node.isLeaf()) { //case 1: deleting a leaf
            setChild(node,parent,new AVLNode(-1,null));  }
        else if (node.hasOneChild()) { //case 2: deleting a node with 1 son
            //check which of node's son is real
            if (node.getRight().isRealNode()) {
                setChild(node,parent,node.getRight());
            } else {
                setChild(node,parent,node.getLeft());
            }
        } else { //case 3: deleting a node with 2 sons
            AVLNode newNode = successor(node);
            AVLNode newParent = newNode.getParent();
            setChild(newNode,newParent,newNode.getRight());
            replace(node,newNode); // insert newNode instead of node
            if (node == newParent) {
                parent = newNode;
            } else {
                parent = newParent;
            }
        }
        node = parent; //for clarity
        //To make a legal deletion for BST, delete the while loop
        //loop's purpose: balance the tree and its fields
        while (node != null) {
            boolean[] changes = node.updateNodeFields(); //first arg - if height changed; second arg - if prefixXor changed
            AVLNode newParent = node.getParent();
            int BF = balanceFactor(node); //3.1
            if (Math.abs(BF) < 2 & !changes[0] & !changes[1]) { //3.2
                return rebalancing;
            }
            if (changes[0]) {
                rebalancing++;
            }
            if (Math.abs(BF) < 2) {
                node = newParent;
            } else {
                if (!changes[0]) rebalancing++;
                rotate(node,BF);
                node = newParent;
            }
        }
        return rebalancing;
    }
    /**
     * private void replace(AVLNode node,AVLNode newNode)
     * <p>
     * this method place newNode instead of node
     */
    /** O(1)) **/
    private void replace(AVLNode node, AVLNode newNode) {
        if (node==this.root) {
            this.root=newNode;
            newNode.setParent(null);
        }
        else { //setting newNode's parent
            newNode.setParent(node.getParent());
            if (node.isLeftChild())
                node.getParent().setLeft(newNode);
            else
                node.getParent().setRight(newNode);
        }
        //setting newNode's children
        node.getRight().setParent(newNode);
        node.getLeft().setParent(newNode);
        newNode.setRight(node.getRight());
        newNode.setLeft(node.getLeft());
    }

    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    /** O(1) **/
    public Boolean min() {
        return this.min.getValue();
    }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    /** O(1) **/
    public Boolean max() {
        return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    /** O(n) **/
    public int[] keysToArray() {
        if (this.size == 0) return new int[0]; // empty tree
        int[] arr = new int[this.size];
        AVLNode node = this.root;
        int[] index = new int[1]; // the index in the array, starts with index=0
        inOrderKeys_rec(node,arr,index);
        return arr;
    }
    /**
     * private void inOrderkeys_rec(AVLNode node, int[] arr, int[] index)
     * <p>
     * update the integers array by the keys of the trees inorder.
     */
    /** O(n) **/
    private void inOrderKeys_rec(AVLNode node, int[] arr, int[] index) {
            if (node.getLeft().isRealNode()) inOrderKeys_rec(node.getLeft(), arr, index);
            arr[index[0]] = node.getKey();
            index[0] += 1;
            if (node.getRight().isRealNode()) inOrderKeys_rec(node.getRight(), arr, index);
    }


    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    /** O(n) **/
    public boolean[] infoToArray() {
        if (this.size == 0) return new boolean[0]; // empty tree
        boolean[] arr = new boolean[this.size];
        AVLNode node = this.root;
        int[] index = new int[1]; // index of the array, starts with index=0
        inOrderInfo_rec(node,arr,index);
        return arr;
    }
    /**
     * private void inOrderInfo_rec(AVLNode node, boolean[] arr, int[] index)
     * <p>
     * update the boolean array by the values of the nodes in order of their keys
     */
    /** O(n) **/
    private void inOrderInfo_rec(AVLNode node, boolean[] arr, int[] index) {
            if (node.getLeft().isRealNode()) inOrderInfo_rec(node.getLeft(), arr, index);
            arr[index[0]] = node.getValue();
            index[0] += 1;
            if (node.getRight().isRealNode()) inOrderInfo_rec(node.getRight(), arr, index);
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    /** O(1) **/
    public int size() {
        return this.size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    /** O(1) **/
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
    /** O(logn) **/
    public boolean prefixXor(int k){ //this is working
        AVLNode node = getNode(k);
        AVLNode parent = node.getParent();
        boolean res = xor(node.getLeft().getPrefixXor(),node.getValue());
        while (parent!=null) {
            if (parent.getRight()==node)
                res=xor(res,xor(parent.getValue(),parent.getLeft().getPrefixXor()));
            node=parent;
            parent=parent.getParent();
        }
        return res;
    }

    /** O(1) **/
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
    /** O(logn) **/
    public AVLNode successor(AVLNode node){
        if (node == this.max) return null;
        if (node.getRight().getKey() != -1) {
            node = node.getRight();
            while (node.getLeft().getKey() != -1) {
                node = node.getLeft();
            }
            return node;
        }
        AVLNode parent = node.getParent();
        while (parent != null && (node == this.root || node == parent.getRight())) {
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
    /** O(n) **/
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

        /** O(1) **/
        public AVLNode(int key, Boolean info) {
            if (key != -1) { //Real Node
                this.key = key;
                this.info = info;
                AVLNode leftVirtual = new AVLNode(-1, null);
                AVLNode rightVirtual = new AVLNode(-1, null);
                this.left = leftVirtual;
                leftVirtual.parent = this;
                this.right = rightVirtual;
                rightVirtual.parent = this;
               // this.prefixXor = info;
            } else { //Virtual Node;
                this.key = -1;
                this.info = null;
                this.height = -1;
            }

        }

        //returns node's key (for virtual node return -1)
        /** O(1) **/
        public int getKey() {
            return this.key;
        }

        //returns node's value [info] (for virtual node return null)
        /** O(1) **/
        public Boolean getValue() {
            return this.info;
        }

        //sets left child
        /** O(1) **/
        public void setLeft(AVLNode node) {
            this.left = node;
        }

        //returns left child (if there is no left child return null)
        /** O(1) **/
        public AVLNode getLeft() {
            return this.left;
        }

        //sets right child
        /** O(1) **/
        public void setRight(AVLNode node) {
            this.right = node;
        }

        //returns right child (if there is no right child return null)
        /** O(1) **/
        public AVLNode getRight() {
            return this.right;
        }

        //sets parent
        /** O(1) **/
        public void setParent(AVLNode node) {
            this.parent = node;
        }

        //returns the parent (if there is no parent return null)
        /** O(1) **/
        public AVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        /** O(1) **/
        public boolean isRealNode() {
            return !(this.key == -1);
        }

        // sets the height of the node
        /** O(1) **/
        public void setHeight() {
            this.height =  1 + Math.max(this.getLeft().getHeight(),this.getRight().getHeight());
        }

        // Returns the height of the node (-1 for virtual nodes)
        /** O(1) **/
        public int getHeight() {
            return this.height;
        }

        /** O(1) **/
        public void setPrefixXor() {
            this.prefixXor = xor(info,xor(this.right.prefixXor,this.left.prefixXor));
        }

        /** O(1) **/
        public boolean getPrefixXor() {
            return this.prefixXor;
        }

        /** O(1) **/
        public Boolean isLeftChild() {
            if (this.parent==null)
                return null;
            return this.parent.left == this;
        }

        /** O(1) **/
        public boolean isLeaf() {
            if (!this.left.isRealNode() && !this.right.isRealNode()) {
                return true;
            }
            return false;
        }

        /** O(1) **/
        public boolean[] updateNodeFields() {
            boolean prevPrefixXor = this.getPrefixXor();
            this.setPrefixXor();
            boolean newPrefixXor = this.getPrefixXor();
            int prevHeight = this.getHeight();
            this.setHeight();
            int newHeight = this.getHeight();
            return new boolean[] {prevHeight != newHeight,prevPrefixXor != newPrefixXor};
        }

        /** O(1) **/
        public boolean hasOneChild() {
            return xor(this.left.isRealNode(),this.right.isRealNode());
        }
    }

}


