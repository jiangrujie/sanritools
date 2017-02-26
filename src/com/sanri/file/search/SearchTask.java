package com.sanri.file.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import sanri.utils.StringUtil;
import sanri.utils.Validate;

import com.sanri.thread.AbstractTask;
import com.sanri.thread.AbstractTaskThread;

public class SearchTask extends AbstractTask<TextFile,List<Find>> {
    private SearchInterest searchInterest;
    List<Pattern> patterns = null;
    @Override
    protected AbstractTaskThread<TextFile, List<Find>> newInstance() {
        SearchThread searchThread = new SearchThread();
        searchThread.setDown(1);
        searchThread.setUp(1);
        searchThread.setPatterns(patterns);
        return searchThread;
    }

    @Override
    public List<TextFile> getTaskDataList() {
        List<File> dirs = searchInterest.getDirs();
        IOFileFilter filter = searchInterest.getFileFilter();
        if (filter == null) {
            filter = new DefaultFileFilter();
        }
        List<File> files = new ArrayList<File>();
        try {
            for (File dir : dirs) {
//                files.addAll(F.listFiles(dir.getAbsolutePath(), filter));
            	files.addAll(FileUtils.listFiles(dir, filter, filter));
            }
            List<TextFile> textFiles = null;
            if (!Validate.isEmpty(files)) {
                textFiles = new ArrayList<TextFile>();
                // 转成 textFile
                for (File file : files) {
                    TextFile textFile = new TextFile(file.getAbsolutePath());
                    textFiles.add(textFile);
                }
            }
            return textFiles;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean beforeHandle() {
        List<String> interests = searchInterest.getInterests();
        patterns = new ArrayList<Pattern>();
        for (String interest : interests) {
            Pattern pattern = Pattern.compile(interest);
            patterns.add(pattern);
        }
        return super.beforeHandle();
    }
    
    /**
     * 
     * 作者:sanri</br>
     * 时间:2016-9-27下午4:32:42</br>
     * 功能:默认文件过滤器<br/>
     */
    class DefaultFileFilter implements IOFileFilter{
        String suffixAccept = ".TXT|.JAVA|.XML|.PHP|.JS|.CSS|.PROPERTIES|.SANRI|.HTML|.HTM";
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            if(suffixAccept.indexOf(StringUtil.suffix(path).toUpperCase()) != -1){
                return true;
            }
            return false;
        }
		@Override
		public boolean accept(File arg0, String arg1) {
			//这个 accept 啥意思
			return false;
		}
        
    }
    public SearchInterest getSearchInterest() {
        return searchInterest;
    }

    public void setSearchInterest(SearchInterest searchInterest) {
        this.searchInterest = searchInterest;
    }
    
    @Override
    public void afterHandle(List<Find> out) {
        if (!Validate.isEmpty(out)) {
            for (Find find : out) {
                System.out.println(find.getFile() + "_" + (find.getLine() + 1) +"_" +  find.getMark());
            }
        }
    }
}
