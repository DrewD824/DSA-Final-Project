public class LinkedList {

    private class Node {
        Object data;
        Node next;

        /**
         * constructs a single node for a linked list object
         * @param data the object that the node is storing
         */
        Node(Object data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    /**
     * constructs a linked list object
     */
    public LinkedList() {
        head = null;
        size= 0;
    }

    /**
     * returns the size of the given linked list
     * @return size
     */
    public int size() {
        return size;
    }

    /**
     * determines if the given linkedlist is empty
     * @return true/false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * adds a given object to the end of the linked list
     * @param o the object that is being added to the linked list
     */
    public void add(Object o) {
        Node newNode = new Node(o);

        if(head == null) {
            head = newNode;
        }
        else {
            Node current = head;

            while (current.next != null) {
                current = current.next;
            }

            current.next = newNode;
        }
        size++;
    }

    /**
     * converts a given linked list into a normal java array
     * @return the array created from the linked list
     */
    public Object[] toArray() {

        Object[] arr = new Object[size];

        Node current = head;
        int index = 0;

        while (current != null) {
            arr[index] = current.data;
            current = current.next;
            index++;

        }
        return arr;
    }

    /**
     * inserts a given object to the linked list at a specific given index
     * @param index the index the object is being inserted at
     * @param o the object that is being inserted
     */
    public void add(int index, Object o) {

        if(index < 1 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node newNode = new Node(o);

        if(index == 1) {
            newNode.next = head;
            head = newNode;
        }
        else {
            Node current = head;

            for(int i = 1; i < index -1; i++) {
                current = current.next;
            }

            newNode.next = current.next;
            current.next = newNode;
        }

        size++;
    }

    /**
     * retrieves and returns a specific object from the linked list based on the given index
     * @param index the index the object is being retrieved from
     * @return the object at the given index
     */
    public Object get(int index) {

        if (index < 1 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node current = head;

        for (int i = 1; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    /**
     * replaces an object in the linked list with another given object at a given index, different than add because it does not insert,
     * it replaces entirely keeping the length of the list the same
     * @param index the index being replaced
     * @param o the new object replacing the old one
     */
    public void set(int index, Object o) {

        if (index < 1 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node current = head;

        for (int i = 1; i < index; i++) {
            current = current.next;
        }

        current.data = o;
    }

    /**
     * checks to see if the linked list contains a given object
     * @param o the object that the method is checking for
     * @return true if the object is in the list, false if it is not
     */
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * searches for a given object in the list and returns the index of that object
     * @param o the object the method is checking for
     * @return the index of the object if it is in the list, -1 if it is not
     */
    public int indexOf(Object o) {

        Node current = head;
        int position = 1;

        while (current != null) {

            if (o == null) {
                if (current.data == null) {
                    return position;
                }
            } else {
                if (o.equals(current.data)) {
                    return position;
                }
            }

            current = current.next;
            position++;
        }

        return -1;
    }

    /**
     * removes an object from the list at the given index
     * @param index the index of the object being removed
     */
    public void remove(int index) {

        if (index < 1 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        // Remove head
        if (index == 1) {
            head = head.next;
        }
        else {
            Node current = head;

            // Move to node BEFORE the one we want to remove
            for (int i = 1; i < index - 1; i++) {
                current = current.next;
            }

            current.next = current.next.next;
        }

        size--;
    }

    /**
     * removes a specific given object from the list
     * @param o the object being removed
     */
    public void remove(Object o) {

        if (head == null) {
            return;
        }

        // Case 1: Removing head
        if ((o == null && head.data == null) ||
                (o != null && o.equals(head.data))) {

            head = head.next;
            size--;
            return;
        }

        // Case 2: Removing from middle/end
        Node current = head;

        while (current.next != null) {

            if ((o == null && current.next.data == null) ||
                    (o != null && o.equals(current.next.data))) {

                current.next = current.next.next;
                size--;
                return;
            }

            current = current.next;
        }
    }

    /**
     * clears the list
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * swaps two objects in the list, the objects that are swapped are based in the two given indexes
     * @param position1 swap index one
     * @param position2 swap index two
     */
    public void swap(int position1, int position2) {

        if (position1 < 1 || position1 > size ||
                position2 < 1 || position2 > size) {
            throw new IndexOutOfBoundsException();
        }

        if (position1 == position2) {
            return; // nothing to swap
        }

        Node node1 = head;
        Node node2 = head;

        for (int i = 1; i < position1; i++) {
            node1 = node1.next;
        }

        for (int i = 1; i < position2; i++) {
            node2 = node2.next;
        }

        // Swap data
        Object temp = node1.data;
        node1.data = node2.data;
        node2.data = temp;
    }

    /**
     * creates a new sub list based on two given indexes, leaves the original list unmodified after creating the new sublist
     * @param fromIndex the first bound for the new list
     * @param toIndex the second bound for the new list
     * @return the new sub list
     */
    public LinkedList subList(int fromIndex, int toIndex) {

        if (fromIndex < 1 || toIndex > size + 1 || fromIndex >= toIndex) {
            throw new IllegalArgumentException();
        }

        LinkedList newList = new LinkedList();

        Node current = head;

        // Move to starting position
        for (int i = 1; i < fromIndex; i++) {
            current = current.next;
        }

        // Add elements up to (but not including) toIndex
        for (int i = fromIndex; i < toIndex; i++) {
            newList.add(current.data);
            current = current.next;
        }

        return newList;
    }

    /**
     * shifts the array a given amount of positions negative for left and positive for right, shift is cyclic so an object that is pushed
     * out of the array is reset back to the beginning
     * @param positions amount of positions the objects in the array are shifted
     */
    public void shift(int positions) {

        if (size == 0 || size == 1) {
            return;
        }

        int k = positions % size;

        if (k < 0) {
            k += size;
        }

        if (k == 0) {
            return;
        }

        // Find old tail
        Node oldTail = head;
        while (oldTail.next != null) {
            oldTail = oldTail.next;
        }

        // Make cyclic
        oldTail.next = head;

        // Find new tail (at position size - k)
        Node newTail = head;
        for (int i = 1; i < size - k; i++) {
            newTail = newTail.next;
        }

        // New head is after newTail
        head = newTail.next;

        // Break circle
        newTail.next = null;
    }








}
