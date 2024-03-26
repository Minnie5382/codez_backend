package nob.codez.service;

public class Solution {
    public int solution(int[] args) {
        int answer = 0;

        return answer;
    }

    // "2 3"
    public int[] parseInput(String[] args) {
        int[] result = new int[args.length];
        for (int i=0 ; i<args.length ; i++)
            result[i] = Integer.parseInt(args[i]);
        return result;
    }
}

