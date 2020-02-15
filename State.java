import java.util.ArrayList;
import java.util.Collection;

public class State implements Comparable<State>
{
    public ArrayList<Integer> list;
    public int id ;
    public State(Collection<Integer> a , int b)
    {
        list = new ArrayList<>(a) ;
        id = b ;
    }
    @Override
    public int compareTo(State o)
    {
        if(list.size() != o.list.size())
            return list.size() - o.list.size() ;
        for(int i = 0 ; i < list.size() ; i++)
            if(!list.get(i).equals(o.list.get(i)))
                return list.get(i) - o.list.get(i) ;
        return 0;
    }

    @Override
    public String toString() {
        return "State{" +
                "list=" + list +
                ", id=" + id +
                '}';
    }
}
