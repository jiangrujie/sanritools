package com.sanri.file.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SearchTask searchTask = new SearchTask();
        List<File> files = new ArrayList<File>();
//      files.add(new File("E:\\project\\kjdp-3.5.0-SNAPSHOT.src"));
        files.add(new File("E:\\workspace\\sanritools"));
        List<String> interests = new ArrayList<String>();
        interests.add("image");
        SearchInterest searInterest = new SearchInterest(files, interests);
        searInterest.setUp(1l);
        searInterest.setDown(1l);
        searchTask.setSearchInterest(searInterest);
        searchTask.start();
    }
}
