import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Scanner sc = new Scanner(System.in) ;
        NFA nfa = new NFA(sc.next()) ;
        String s = nfa.convert_NFA_to_DFA() ;
        System.out.println(s);
        DFA dfa = new DFA(s) ;
        System.out.println(dfa.run(sc.next()));
    }
}