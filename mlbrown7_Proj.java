/*************************
Project Name: Huffman Tree
File Name: mlbrown7_Proj.java
Student Name: Matthew Brown
Date: 4/3/2013
Task
1. Print Huffman Nodes
2. Encode "tea" and print encoded bits
3. Decode the bits and print the decode string
Computing requirements     
Language: Java
Platform: Linux

***************************/

import java.util.PriorityQueue;
import java.util.Stack;

public class mlbrown7_Proj {
    public static void main (String args[]) {
        HuffTree ht = new HuffTree();
        String message = "tea ate";
        String encodedMessage = "";
        String decodedMessage = "";

        ht.writeEncodingTable();
        encodedMessage = ht.getCode(message);
        decodedMessage = ht.getChar(encodedMessage);
        
        System.out.println("The Huffman Tree");
        ht.displayTree();
        System.out.println();

        System.out.println("The original message: " + message);
        System.out.println("The encoded message: " + encodedMessage);
        System.out.println("The decoded message: " + decodedMessage);
    }

}

//Characters class
//Stores frequency of each letter 
//and an  array of the original characters
class Characters {
    public static final int frequency[] = {10, 15, 12, 3, 4, 13, 1};
    public static final String code[] = {"a", "e", "i", "s", "t", " ", "nl"};
    public static final int DIFF_BITS = 7;
}

//HuffMan Tree class
class HuffTree {
    private HuffNode[] theNodes = new HuffNode[Characters.DIFF_BITS + 1];
    private HuffNode root;
    private String[] encodedLetters = new String[Characters.DIFF_BITS + 1 ];
    
    //Constructor - creates tree
    public HuffTree() {
        root = null;
        createTree();
    }
    
    //Creates Tree
    private void createTree() {
        PriorityQueue<HuffNode> pq = new PriorityQueue<HuffNode>();
        
        for( int i = 0; i < Characters.DIFF_BITS; i++) {
            if( Characters.frequency.length > 0) {
                HuffNode newNode = new HuffNode(Characters.code[i], Characters.frequency[i], null, null, null);
                theNodes[i] = newNode;
                pq.add(newNode);
            }
        }

        while( pq.size() > 1) {
            HuffNode n1 = pq.remove();
            HuffNode n2 = pq.remove();
            HuffNode result = new HuffNode(null, n1.weight + n2.weight, null, null, null);
            //if statements below builds tree like the book does
            if(n1.left != null && n1.right != null){
                result.left = n1;
                result.right = n2;
            }
            if(n2.left != null && n2.right != null) {
                result.left = n2;
                result.right = n1;
            }
            if(n1.left == null && n2.left == null) {
                result.left = n2;
                result.right = n1;
            }

            n1.parent = n2.parent = result;
            pq.add(result);
        }
        root = pq.element();
    }

    //gets the code for the  message
    public String getCode(String m) {
        HuffNode current = new HuffNode(null, 0, null, null, null);
        String v = "";
        String message = "";

        for(int i = 0; i < m.length(); i++) {
            for(int j = 0; j < Characters.DIFF_BITS; j++) {
                if(theNodes[j].value.equals(m.substring(i, i + 1)))
                    current = theNodes[j];
            }

            HuffNode par = current.parent;

            while(par != null) {
                if( par.left == current)
                    v = "0" + v;
                else
                    v = "1" + v;

                current = current.parent;
                par = current.parent;
            }

            message = message + v;
            v = "";
        } 
        
        return message;
    }

    //gets string from encoded messasge
    public String getChar(String code) {
        String message = "";
        HuffNode p = root;
            
        while(code.length() > 0) {    
            if(code.substring(0, 1).equals("0")) {
                p = p.left;
                code = code.substring(1);
            }
            if(code.substring(0, 1).equals("1")) {
                p = p.right;
                code = code.substring(1);
            }

            if(p.left == null && p.right == null) {
                message = message + p.value;
                p = root;
            }
        }
            
         return message;
    }

    //writes Encoding Table
    public void writeEncodingTable() {
        for(int i = 0; i < Characters.DIFF_BITS; i++) {
            encodedLetters[i] = getCode(Characters.code[i]);
        }
    }

    //Prints tree using Preorder Traversal
    private void preorder(HuffNode n, int level) {
        if(n == null)
            return;

        for(int i = 0; i < level; i++)
            System.out.print("-");
        
        if(n != root)
            System.out.print(" ");

        System.out.print(n.weight);

        if(n.value != null) {
            System.out.print(" : ");
            //test to see if vale is a space and is so prints "sp"
            if(n.value.equals(" "))
                System.out.print("sp");
            else
                System.out.print(n.value);
        }

        System.out.println();
        level++;
        preorder(n.left, level);
        preorder(n.right, level);
    }
    
    //Displays Huffman Tree
    public void displayTree() {
        preorder(root, 0);
    }
    
    //Inner class - HuffNode
    private class HuffNode implements Comparable<HuffNode> {
        public String value;
        public int weight;
        HuffNode left;
        HuffNode right;
        HuffNode parent;

        public int compareTo( HuffNode rhs) {
            return weight - rhs.weight;
        }

        public HuffNode( String v, int w, HuffNode l, HuffNode r, HuffNode p) {
            value = v;
            weight = w;
            left = l;
            right = r;
            parent = p;
        }
    }
}
