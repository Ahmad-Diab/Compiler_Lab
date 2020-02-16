import java.util.* ;

public class NFA
{
    public int N = 0 ;
    public boolean [] accepted ;
    public ArrayList<Integer> [][] adjMat ;

    public NFA(String s)
    {
        StringTokenizer [] tokenizers = split(s);
        ArrayList<Tuple> tuples = new ArrayList<>() ;
        for(int k = 0 ; k < 3 ; k ++)
        {
            while (tokenizers[k].hasMoreTokens())
            {
                StringTokenizer st = new StringTokenizer(tokenizers[k].nextToken(), ",");
                int i = Integer.parseInt(st.nextToken()), j = Integer.parseInt(st.nextToken());
                N = Math.max(N, Math.max(i + 1, j + 1));
                tuples.add(new Tuple(i, k, j));
            }
        }
        adjMat = new ArrayList[N][3] ;
        accepted = new boolean[N] ;
        while (tokenizers[3].hasMoreTokens())
            accepted[Integer.parseInt(tokenizers[3].nextToken())] = true ;
        for(int i = 0 ; i < N ; i++)
            for(int j = 0 ; j < 3 ; j ++)
                adjMat[i][j] = new ArrayList<>() ;
        for(Tuple tuple : tuples)
            adjMat[tuple.i][tuple.j].add(tuple.k);
    }

    StringTokenizer [] split(String s)
    {
        StringTokenizer [] tokenizers = new StringTokenizer[4] ;
        for(int j = 0  , start = 0 , idx = -1 ; j < 4 ; j ++ , start = idx + 1)
        {
            for (int i = idx + 1; i < s.length(); i++)
                if (s.charAt(i) == '#' || i == s.length() - 1)
                {
                    idx = i;
                    if(i == s.length() - 1) idx ++ ;
                    break;
                }
            if(start <= idx) tokenizers[j] = new StringTokenizer(s.substring(start, idx), ";");
        }
        return tokenizers;
    }
    public String convert_NFA_to_DFA()
    {
        int id = 0 ;
        Queue<State> queue = new LinkedList<>() ;
        TreeMap<State , Integer> visited = new TreeMap<>() ;
        ArrayList<Tuple> tuples = new ArrayList<>() ;
        State startState = new State(traverse_epsilon(0 , new boolean[N]) , id++);
        queue.add(startState);
        visited.put(startState , 0) ;
        while (!queue.isEmpty())
        {
            State currentState = queue.poll() ;
            if(currentState.list.isEmpty()) continue;
            int [] next = new int [2] ;

            for(int k = 0 ; k < 2 ; k++)
            {
                TreeSet<Integer> list = new TreeSet<>() ;
                for(int u : currentState.list) // u --> v (has transition k then traverse to epsilon)
                    for(int v : adjMat[u][k])
                        list.addAll(traverse_epsilon(v, new boolean[N]));
                State nextState = new State(list , visited.getOrDefault(new State(list , -1) , -1));
                if(nextState.id == -1)
                {
                    nextState.id = id++;
                    queue.add(nextState) ;
                    visited.put(nextState , nextState.id) ;
                }
                next[k] = nextState.id ;
            }
            tuples.add(new Tuple(currentState.id , next[0] , next[1])) ;
        }
        // Dead State
        if(visited.containsKey(new State(new ArrayList<>() , -1)))
        {
            int _ID = visited.get(new State(new ArrayList<>() , -1)) ;
            tuples.add(new Tuple(_ID , _ID , _ID));
        }
        StringBuilder dfa_input = new StringBuilder() ;
        int newSize = 0 ;
        for(Tuple tuple : tuples)
        {
            if(dfa_input.length() > 0) dfa_input.append(";") ;
            newSize = Math.max(newSize , Math.max(Math.max(tuple.i + 1 , tuple.j + 1) , tuple.k + 1)) ;
            dfa_input.append(tuple.i).append(",").append(tuple.j).append(",").append(tuple.k);
        }
        boolean [] newAcceptedStates = new boolean[newSize] ;
        for(State state : visited.keySet())
            for (int v : state.list)
                newAcceptedStates[state.id] |= accepted[v];
        dfa_input.append("#") ;

        for(int i = 0 , cnt = 0 ; i < newSize ; i++)
            if(newAcceptedStates[i])
            {
                if(cnt > 0) dfa_input.append(",");
                dfa_input.append(i) ;
                cnt++;
            }
        return dfa_input.toString() ;
    }
    TreeSet<Integer> traverse_epsilon(int u , boolean [] vis)
    {
        TreeSet<Integer> set = new TreeSet<>() ;
        set.add(u);
        vis[u] = true ;
        for(int v : adjMat[u][2])
            if(!vis[v])
                set.addAll(traverse_epsilon(v , vis));
        return set ;
    }
}