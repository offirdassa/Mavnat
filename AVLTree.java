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

    public AVLNode getMin() { return this.min;}

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
    public int insert(int k, boolean i) {
        int rebalancing = 1;
        AVLNode newNode = new AVLNode(k,i); //1
        if (!this.root.isRealNode()) {
            this.root = newNode;
            updateTreeFields(newNode,true);
            this.root.setHeight();
            return rebalancing;
        }
        AVLNode location = getNode(k); //virtual node to be replaced
        if (location.isRealNode())
            return -1;
        AVLNode parent = location.getParent(); //2
        setChild(location,parent,newNode); //boolean true for insert false for delete
        newNode.setPrefixXor(); //added 0405 !!!!
        AVLNode node = parent; //just for clarity
        updateTreeFields(newNode,true);
        while (node != null) { //3
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
        return rebalancing;    // to be replaced by student code
    }

    private void rotate(AVLNode node, int BF) {
        if (BF > 0) {
            if (balanceFactor(node.getLeft()) >= 0) {
                rightRotate(node);
            } else {
                leftRotate(node.getLeft());
                rightRotate(node);
            }
        } else {
            if (balanceFactor(node.getRight()) <= 0) {
                leftRotate(node);
            } else {
                rightRotate(node.getRight());
                leftRotate(node);
            }
        }
    }

    private void leftRotate(AVLNode A) {
        AVLNode B = A.getRight();
        AVLNode BLeft = B.getLeft();
        AVLNode AParent = A.getParent();
        Boolean leftSon = A.isLeftChild();
        A.setRight(BLeft);
        BLeft.setParent(A);
        B.setLeft(A);
        A.setParent(B);
        B.setParent(AParent);
        //
        A.setHeight();
        B.setHeight();
        A.setPrefixXor();
        if (leftSon==null)
            this.root = B;
        else {
            if (leftSon)
                AParent.setLeft(B);
            else
                AParent.setRight(B); }
        B.setPrefixXor();
    }

    private void rightRotate(AVLNode A) {
        AVLNode B = A.getLeft();
        AVLNode BRight = B.getRight();
        AVLNode AParent = A.getParent();
        Boolean leftSon = A.isLeftChild();
        A.setLeft(BRight);
        BRight.setParent(A);
        B.setRight(A);
        A.setParent(B);
        B.setParent(AParent);
        //
        A.setHeight();
        B.setHeight();
        A.setPrefixXor();
        if (leftSon==null)
            this.root = B;
        else {
            if (leftSon)
                AParent.setLeft(B);
            else
                AParent.setRight(B); }
        B.setPrefixXor();
    }

    private void setChild(AVLNode location, AVLNode parent, AVLNode newNode) {
        if (parent!=null){
            newNode.setParent(parent);
            if (parent.getRight() == location)
                parent.setRight(newNode);
            else parent.setLeft(newNode); }
        else{
            this.root=newNode;
            this.root.setParent(null);}
    }

    private int balanceFactor(AVLNode node) {
        return node.left.getHeight()-node.right.getHeight();
    }

    //true if insert, false if delete
    private void updateTreeFields(AVLNode node, boolean b) {
        int k = node.getKey();
        if (b) {
            if (k < this.min.getKey() | !this.min.isRealNode())
                this.min = node;
            if (k > this.max.getKey())
                this.max = node;
            this.size += 1;
        } else {
            if (this.size==1){
                this.min=new AVLNode(-1,null);
                this.max=this.min;
            }
            if (k == this.min.getKey())
                this.min = successor(node);
            if (k == this.max.getKey())
                this.max = predecessor(node);

            this.size -= 1; }
    }

    private AVLNode predecessor(AVLNode node) { //should be private
        if (node == this.min) return null;
        if (node.getLeft().getKey() != -1) {
            node = node.getLeft();
            while (node.getRight().getKey() != -1) {
                node = node.getRight();
            }
            return node;
        }
        AVLNode parent = node.getParent();
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
    public int delete(int k) {
        int rebalancing = 0;
        AVLNode node = getNode(k);
        if (!node.isRealNode()) { return -1;}
        AVLNode parent = node.getParent();
        updateTreeFields(node,false);
        if (node.isLeaf()) { //case 1
            setChild(node,parent,new AVLNode(-1,null));  }
        else if (node.hasOneChild()) { //case 2
            if (node.getRight().isRealNode()) {
                setChild(node,parent,node.getRight());
            } else {
                setChild(node,parent,node.getLeft());
            }
        } else { //case 3
            AVLNode newNode = successor(node);
            AVLNode newParent = newNode.getParent();
            setChild(newNode,newParent,newNode.getRight());
            replace(node,newNode);
            if (node == newParent) {
                parent = newNode;
            } else {
                parent = newParent;
            }
        }
        node = parent; //for clarity
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
        return rebalancing;    // to be replaced by student code
    }

    private void replace(AVLNode node, AVLNode newNode) {
        if (node==this.root) {
            this.root=newNode;
            newNode.setParent(null);
        }
        else {
            newNode.setParent(node.getParent());
            if (node.isLeftChild())
                node.getParent().setLeft(newNode);
            else
                node.getParent().setRight(newNode);
        }
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
        return this.max.getValue(); // to be replaced by student code
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    /** O(n) **/
    public int[] keysToArray() {
        if (this.size == 0) return new int[0];
        int[] arr = new int[this.size];
        AVLNode node = this.root;
        int[] index = new int[1];
        inOrderKeys_rec(node,arr,index);
        return arr;
    }

    private void inOrderKeys_rec(AVLNode node, int[] arr, int[] index) {
        /*if (node.isLeaf()) {
            arr[index[0]] = node.getKey();
            index[0] += 1;
        } else { */
            if (node.getLeft().isRealNode()) inOrderKeys_rec(node.getLeft(), arr, index);
            arr[index[0]] = node.getKey();
            index[0] += 1;
            if (node.getRight().isRealNode()) inOrderKeys_rec(node.getRight(), arr, index);
        //}
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
        if (this.size == 0) return new boolean[0];
        boolean[] arr = new boolean[this.size];
        AVLNode node = this.root;
        int[] index = new int[1];
        inOrderInfo_rec(node,arr,index);
        return arr;
    }

    private void inOrderInfo_rec(AVLNode node, boolean[] arr, int[] index) {
        /*if (node.isLeaf()) {
            arr[index[0]] = node.getValue();
            index[0] += 1;
        } else {*/
            if (node.getLeft().isRealNode()) inOrderInfo_rec(node.getLeft(), arr, index);
            arr[index[0]] = node.getValue();
            index[0] += 1;
            if (node.getRight().isRealNode()) inOrderInfo_rec(node.getRight(), arr, index);
        //}
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
            return this.key; // to be replaced by student code
        }

        //returns node's value [info] (for virtual node return null)
        /** O(1) **/
        public Boolean getValue() {
            return this.info; // to be replaced by student code
        }

        //sets left child
        /** O(1) **/
        public void setLeft(AVLNode node) {
            this.left = node;
        }

        //returns left child (if there is no left child return null)
        /** O(1) **/
        public AVLNode getLeft() {
            return this.left; // to be replaced by student code
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


