package adt;


public class Main { 
    public static void main(String[] args) { 
        
        QueueADT queue = new LinkedQueue(); 
        
        queue.enqueue(10); 
        queue.enqueue(20); 
        
        System.out.println(queue.dequeue()); 
    }
}