import java.util.* ;

public class NFA
{
    int N ;
    boolean [] accepted ;
    ArrayList<Integer> [][] adjMat ;

    NFA(String s)
    {
        StringTokenizer [] tokenizers = split(s);
        StringTokenizer zeros = tokenizers[0] , ones = tokenizers[1] ,epsilon = tokenizers[2], accpetedStates= tokenizers[3] ;
        ArrayList<Tuple> tuples = new ArrayList<>() ;
        int n = 0 ;
        while (zeros.hasMoreTokens())
        {
            StringTokenizer st = new StringTokenizer(zeros.nextToken() , ",");
            int i = Integer.parseInt(st.nextToken()) , j = Integer.parseInt(st.nextToken()) ;
            n = Math.max(n , Math.max(i , j));
            tuples.add(new Tuple(i , 0 , j))  ;
        }
        while (ones.hasMoreTokens())
        {
            StringTokenizer st = new StringTokenizer(ones.nextToken() , ",") ;
            int i = Integer.parseInt(st.nextToken()) , j = Integer.parseInt(st.nextToken()) ;
            n = Math.max(n , Math.max(i , j));
            tuples.add(new Tuple(i , 1 , j))  ;
        }
        while (epsilon.hasMoreTokens())
        {
            StringTokenizer st = new StringTokenizer(epsilon.nextToken() , ",") ;
            int i = Integer.parseInt(st.nextToken()) , j = Integer.parseInt(st.nextToken()) ;
            n = Math.max(n , Math.max(i , j));
            tuples.add(new Tuple(i , 2 , j))  ;
        }
        n ++;
        adjMat = new ArrayList[n][3] ;
        accepted = new boolean[n] ;
        while (accpetedStates.hasMoreTokens())
            accepted[Integer.parseInt(accpetedStates.nextToken())] = true ;
        for(int i = 0 ; i < n ; i++)
            for(int j = 0 ; j < 3 ; j ++)
                adjMat[i][j] = new ArrayList<>() ;
        for(Tuple tuple : tuples)
            adjMat[tuple.i][tuple.j].add(tuple.k);
        N = n ;

    }

    StringTokenizer [] split(String s)
    {
        StringTokenizer [] tokenizers = new StringTokenizer[4] ;
        for(int j = 0  , start = 0 , idx = -1 ; j < 4 ; j ++)
        {
            for (int i = idx + 1; i < s.length(); i++)
                if (s.charAt(i) == '#' || i == s.length() - 1) {
                    idx = i;
                    if(i == s.length() - 1)
                        idx ++ ;
                    break;
                }
            if(start <= idx)
                tokenizers[j] = new StringTokenizer(s.substring(start, idx), ";");
            start = idx + 1;
        }
        return tokenizers;
    }
    String convert_NFA_to_DFA()
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
            // transition Zero
            TreeSet<Integer> list = new TreeSet<>() ;
            for(int u : currentState.list) // u --> v (has transition zero then traverse to epsilon)
                for(int v : adjMat[u][0])
                    list.addAll(traverse_epsilon(v, new boolean[N]));
            State zeroState = new State(list , visited.getOrDefault(new State(list , -1) , -1));
            if(zeroState.id == -1)
            {
                zeroState.id = id++;
                queue.add(zeroState) ;
                visited.put(zeroState , zeroState.id) ;
            }

            list = new TreeSet<>() ;
            for(int u : currentState.list) // u --> v (has transition one then traverse to epsilon)
                for(int v : adjMat[u][1])
                    list.addAll(traverse_epsilon(v, new boolean[N]));
            State oneState = new State(list , visited.getOrDefault(new State(list , -1) , -1));
            if(oneState.id == -1)
            {
                oneState.id = id++;
                queue.add(oneState) ;
                visited.put(oneState , oneState.id) ;
            }
            tuples.add(new Tuple(currentState.id , zeroState.id , oneState.id)) ;
        }
        // Dead State
        if(visited.containsKey(new State(new ArrayList<>() , -1)))
        {
            int _ID = visited.get(new State(new ArrayList<>() , -1)) ;
            tuples.add(new Tuple(_ID , _ID , _ID));
        }
        StringBuilder dfa_input = new StringBuilder() ;
        int newN = 0 ;
        for(Tuple tuple : tuples)
        {
            if(dfa_input.length() > 0)
                dfa_input.append(";") ;
            newN = Math.max(newN , Math.max(Math.max(tuple.i , tuple.j) , tuple.k)) ;
            dfa_input.append(tuple.i).append(",").append(tuple.j).append(",").append(tuple.k);
        }
        newN ++ ;
        boolean [] newAcceptedStates = new boolean[newN] ;
        for(State state : visited.keySet())
        {
            System.out.println(state);
            for (int v : state.list)
                newAcceptedStates[state.id] |= accepted[v];
        }
        dfa_input.append("#") ;

        for(int i = 0 , cnt = 0 ; i < newN ; i++)
        {
            if(newAcceptedStates[i])
            {
                if(cnt > 0)
                    dfa_input.append(",");
                dfa_input.append(i) ;
                cnt++;
            }
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