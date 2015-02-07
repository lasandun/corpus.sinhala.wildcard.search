/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corpus.sinhala.wildcard.search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author lahiru
 */
public class Test {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/home/lahiru/Desktop/export.csv"));
        String line;
        int k = 0;
        SinhalaVowelLetterFixer f = new SinhalaVowelLetterFixer();
        while((line = br.readLine()) != null) {
            line = line.replaceAll(" ", "");
            line = line.replaceAll("\"", "");
            String parts[] = line.split(",");
            if(parts.length < 2) continue;
            String word = parts[1];
            String fixed = f.fixText(word, false);
            if(!word.equals(fixed)) {
                System.out.println(word);
            }
        }
        
        
    }
}
