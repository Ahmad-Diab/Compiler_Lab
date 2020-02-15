import java.util.*;

public class DFA
{
    boolean [] acceptedState ;
    int [][] next ;

    DFA(String s)
    {
        StringTokenizer st = new StringTokenizer(s , "#") ;
        String states_transitions = st.nextToken() ;
        String accepted_states = st.nextToken() ;
        st = new StringTokenizer(states_transitions , ";") ;

        ArrayList<Tuple> tuples = new ArrayList<>() ;

        while (st.hasMoreTokens())
        {
            String state = st.nextToken() ;
            StringTokenizer tokenizer = new StringTokenizer(state , ",") ;
            int i = Integer.parseInt(tokenizer.nextToken()) , j = Integer.parseInt(tokenizer.nextToken()) , k = Integer.parseInt(tokenizer.nextToken()) ;
            tuples.add(new Tuple(i , j , k)) ;
        }
        int n = tuples.size() ;
        next = new int [n][2] ;
        acceptedState = new boolean[n] ;
        for(int i = 0 ; i < n ; i ++)
        {
            Tuple curr = tuples.get(i) ;
            next[curr.i][0] = curr.j ;
            next[curr.i][1] = curr.k ;
        }
        st = new StringTokenizer(accepted_states , ",") ;
        
        while (st.hasMoreTokens())
        {
            int x = Integer.parseInt(st.nextToken()) ;
            acceptedState[x] = true ;
        }
    }
    boolean run(String s)
    {
        int state = 0 ;
        for(char c : s.toCharArray())
            state = next[state][c - '0'];
        return acceptedState[state] ;
    }
    public static void main(String [] args)
    {
        Scanner sc = new Scanner(System.in) ;
        DFA dfa = new DFA(sc.next()) ;
        System.out.println(dfa.run(sc.next()));
    }

    class Tuple
    {
        int i , j , k ;

        Tuple(int a , int b , int c)
        {
            i = a ; j  = b ; k = c ;
        }
    }
}
