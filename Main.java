import java.io.*;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out) ;
        NFA nfa = new NFA(br.readLine()) ;
        String s = nfa.convert_NFA_to_DFA() ;
        out.println(s);
        DFA dfa = new DFA(s) ;
        out.println(dfa.run(br.readLine()));
        out.flush();
    }
}