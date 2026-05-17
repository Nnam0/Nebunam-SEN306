package adt;

import java.util.ArrayList; 
import java.util.List; 

public class ArrayListQueue implements QueueADT { 
    private List<Integer> list = new ArrayList<>(); 

    @Override
    public void enqueue(int element) {
        list.add(element); 
    }

    @Override
    public int dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue empty"); 
        }
        return list.remove(0); 
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty(); 
    }

    @Override
    public int size() {
        return list.size(); 
    }
}