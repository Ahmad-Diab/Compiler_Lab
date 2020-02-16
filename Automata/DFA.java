import java.util.*;

public class DFA
{
    boolean [] acceptedState ;
    int [][] next ;
    public DFA(String s)
    {
        StringTokenizer st = new StringTokenizer(s , "#") ;
        String states_transitions = st.nextToken() , accepted_states = st.nextToken() ;
        st = new StringTokenizer(states_transitions , ";") ;
        ArrayList<Tuple> tuples = new ArrayList<>() ;
        while (st.hasMoreTokens())
        {
            StringTokenizer tokenizer = new StringTokenizer(st.nextToken() , ",") ;
            tuples.add(new Tuple(Integer.parseInt(tokenizer.nextToken()) , Integer.parseInt(tokenizer.nextToken()) , Integer.parseInt(tokenizer.nextToken()))) ;
        }
        int n = tuples.size() ;
        next = new int [n][2] ;
        acceptedState = new boolean[n] ;
        for(Tuple curr : tuples)
        {
            next[curr.i][0] = curr.j ;
            next[curr.i][1] = curr.k ;
        }
        st = new StringTokenizer(accepted_states , ",") ;
        while (st.hasMoreTokens()) acceptedState[Integer.parseInt(st.nextToken())] = true ;
    }
    public boolean run(String s)
    {
        int state = 0 ;
        for(char c : s.toCharArray()) state = next[state][c - '0'];
        return acceptedState[state] ;
    }
}