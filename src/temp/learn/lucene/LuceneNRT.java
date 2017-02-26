package temp.learn.lucene;

import org.apache.lucene.search.NRTManager;

/**
 * 
 * @author sanri
 * 功能说明:近实时搜索<br/>
 * 修改了信息之后,先保存在内存中;之后再一个统一的时间提交
 * 改了不 commit
 * 创建时间:2017-1-6下午2:53:54
 */
public class LuceneNRT {
	NRTManager nrtManager;
}
