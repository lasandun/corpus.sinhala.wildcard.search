/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corpus.sinhala.wildcard.search;

import java.util.LinkedList;

/**
 *
 * @author lahiru
 */
public class Test {
    public static void main(String[] args) {
        WildCardQuery x = new WildCardQuery();
        LinkedList<String> list = x.wildCardSearch("ම?");
        for(String s : list) {
            System.out.println(s);
        }
    }
}
