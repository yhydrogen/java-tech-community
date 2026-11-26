package likou;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    private final int capacity;
    private int size;
    private final Map<Integer, CacheNode> map;
    private final CacheNode head;
    private final CacheNode tail;

    private static class CacheNode {
        int key;
        int value;
        CacheNode prev;
        CacheNode next;
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.map = new HashMap<>();
        this.head = new CacheNode();
        this.tail = new CacheNode();
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        CacheNode node = map.get(key);
        if(node == null){
            return -1;
        }
        removeNode(node);
        addFirst(node);
        return node.value;
    }

    public void put(int key, int value) {
        CacheNode node = map.get(key);
        if(node != null){
            removeNode(node);
            node.value = value;
            addFirst(node);
        }else {
            CacheNode newnode = new CacheNode();
            newnode.value = value;
            newnode.key = key;
            map.put(key, newnode);
            addFirst(newnode);
            size++;
            if(size > capacity){
                CacheNode removenode= tail.prev;
                map.remove(removenode.key);
                removeNode(tail.prev);
                --size;
            }
        }
    }
    public void removeNode(CacheNode node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    public void addFirst(CacheNode node){
        node.next = head.next;
        head.next.prev = node;
        node.prev = head;
        head.next = node;
    }
}
