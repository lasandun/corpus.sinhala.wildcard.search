/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package corpus.sinhala.wildcard.search;

public class SinhalaLetter {
    
    private int sinhalaLetter;
    private int sinhalaVowelSign;
    private String searchLetter;
    private String nonSinhalaChar;
    
    // for search letters *, ?
    public SinhalaLetter(String searchLetter) {
        this.searchLetter = searchLetter;
        nonSinhalaChar = null;
    }

    // for sinhala letters
    public SinhalaLetter(int sinhalaLetter) {
        sinhalaVowelSign = 0;
        this.sinhalaLetter = sinhalaLetter + 1;
        searchLetter = null;
        nonSinhalaChar = null;
    }
    
    // for non-sinhala chars
    public SinhalaLetter() {
        sinhalaLetter = -1;
        sinhalaVowelSign = -1;
        searchLetter = null;
        nonSinhalaChar = "";
    }
    
    public boolean isANonSinhalaChar() {
        return (nonSinhalaChar != null);
    }
    
    public String getNonSinhalaChar() {
        return nonSinhalaChar;
    }
    
    public void setNonSinhalaChar(String nonSinhalaChar) {
        this.nonSinhalaChar = nonSinhalaChar;
    }
    
    public boolean isSearchLetter() {
        return (searchLetter != null);
    }
    
    public String getSearchLetter() {
        return searchLetter;
    }
    
    public void setVowel(int sinhalaVowelSignIndex) {
        this.sinhalaVowelSign = sinhalaVowelSignIndex + 1;
    }
    
    public String getValue() {
        return String.format("%02d", sinhalaLetter) + String.format("%02d", sinhalaVowelSign);
    }
    
}
