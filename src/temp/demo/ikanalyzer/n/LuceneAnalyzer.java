package temp.demo.ikanalyzer.n;

import org.apache.lucene.analysis.Tokenizer;


/**
 * 
 * 创建时间:2017-1-4下午8:29:53<br/>
 * 创建者:sanri<br/>
 * 功能:lucene 分词相关<br/>
 * Analyzer
 * 	SimpleAnalyzer
 * 	WhitespaceAnalyzer
 * 	StopAnalyzer
 * 	StandardAnalyzer
 * 
 * TokenStream{
 * 	PositionIncrementAttribute:位置增量的属性,存储语汇单元之间的距离,
 * 	OffsetAttribute:每个语汇单元的偏移量,
 * 	CharTermAttribute:每个单独的词,
 * 	TypeAttribute:使用的分词器的类型信息}
 * 	Tokenizer
 * 	TokenFilter
 * 
 * 都有一个公共的方法 tokenStream
 * 
 * Reader ->Tokenizer ->TokenFilter->TokenStream
 */
public class LuceneAnalyzer {
}
