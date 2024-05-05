

package amongus.utils;


public class Pair<T,K> 
{
    private T first;
    private K second;
    
    public Pair(T first, K second)
    {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public K getSecond() {
        return second;
    }

    public void setSecond(K second) {
        this.second = second;
    }

    @Override
    public Pair<T,K> clone() 
    {
        return new Pair(this.first,this.second);    //Shallow copy | Esto significa que T,K deben ser inmutables
    }

    @Override
    public boolean equals(Object obj) 
    {
        if(!(obj instanceof Pair)) return false;
        
        var other = (Pair) obj;
        
        return 
        (
                other.getFirst().equals(this.first)
                && other.getSecond().equals(this.second)
        );
    }
    
    
    
    
    
    

}
