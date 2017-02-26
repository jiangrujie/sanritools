package temp.part3.pushmessage;
import com.gexin.rp.sdk.http.IGtPush;
public class CancelContentId {
    //采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
	private static String appId = "TxzlIyCcfS9KuENjjP4ux1";
    private static String appKey = "rAnoicfrNX7915IxPocAL2";
    private static String masterSecret = "KFDNBNKAVj9bgykwvqgeA5";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    //taskid就是ContentId
    static String taskid = "OSL-0406_YOKKckfqBeAjURXnzt7L04";

    public static void main(String[] args) {
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        boolean result = push.cancelContentId(taskid);
        System.out.println(result);
    }
}