/**
 * Created by IntelliJ IDEA.
 * User: tamaki-t
 * Date: 11/03/28
 * Time: 9:05
 * To change this template use File | Settings | File Templates.
 */

import java.io.{InputStream, InputStreamReader, BufferedReader}
import org.apache.commons.lang.StringEscapeUtils
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils
import scala.xml._
;

class HelloWorld

object HelloWorld {
  /**
   *  InputStreamをStringに変換するユーティリティ
   */
  def inputStreamToString(in: InputStream): String = {
    var reader: BufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    var buf: StringBuffer = new StringBuffer();
//    reader.readLine
    var line: String = reader.readLine;
    while( line != null ){ // すべての行をファイルから読み込むまで繰り返し *3
      buf.append(line);
      buf.append("\n");
      line = reader.readLine   // 次の行を読み込む *5
    }
//    println(buf.toString())
    return buf.toString();
  }

  /**
   * HTMLエスケープ(& -> &amp;)するユーティリティ
   */
  def h(x: String) = StringEscapeUtils.escapeHtml(x)

  /**
   * RSSの個別のItemを生成
   * XMLリテラルを使っているけどあってるのか!?
   */
  def toRssDetail(link: String, title: String, description: String) = {
<item>
	<title>{h(title)}</title>
	<link>{h(link)}</link>
	<description>{h(description)}</description>
</item>
  }

  /**
   * RSS生成
   * Detailの生成が強引
   */
  def toRss(list: List[List[String]]) = {
<rss version="2.0">
<channel>
 <title>流通ニュース</title>
 <link><![CDATA[http://www.ryutsuu.biz]]></link>
   <description><![CDATA[]]></description>
  {for(x <- list)yield toRssDetail(x.head, x.tail.head, "")}
<language><![CDATA[ja]]></language>
</channel>
</rss>
  }

  /**
   * メイン
   */
  def main(args: Array[String]) = {
    // Hello!!
    println("Hello, world!!")

    /**
     * HTTPクライアントを生成して、HTMLを取得している
     * 疑問: HTTPクライアントは、org.apache.http.client.HttpClientでよいの？
     */
    val httpclient = new DefaultHttpClient();
//    val httpget = new HttpGet("http://www.apache.org/");
    val httpget = new HttpGet("http://www.ryutsuu.biz/");
    println("executing request " + httpget.getURI());
    val response = httpclient.execute(httpget);
    println("----------------------------------------");
    println(response.getStatusLine());
    println("----------------------------------------");

    /**
     * HTTPのContentsを取得して、ちゃんと結果が返ってきていたら、解析処理する
     */
    val entity = response.getEntity();
    // if (entity = response.getEntity() == null)と書けないのが不服
    if (entity != null) {
      val str = inputStreamToString(entity.getContent());
      val ptn2 = """(?s)\<ul\>.*?\<\/ul\>""".r;
//      println("ptn2:" + ptn2.findAllIn(str).toList.toString)
//        ptn2.findAllIn(str).foreach(x => println(x))
      val ul = ptn2.findAllIn(str).toList.tail.tail.head
      val xx = """\&""".r.replaceAllIn(ul,"&amp;");
//        println(xx)
//       val elem = XML.loadString("<ul></ul>")//StringEscapeUtils.escapeHtml(ul));
      val elem2 = XML.loadString(xx);
      val list = for {
        a <- elem2 \\ "a"
      } yield List(a.attribute("href") match{
        case Some(x) => x.toString
        case None => ""
      }, a.text)
      println("""<?xml version="1.0" encoding="UTF-8"?>""" + toRss(list.toList).toString)
//       println(list.toString)

//      val elem = XML.loadString(StringEscapeUtils.escapeHtml(inputStreamToString(entity.getContent())));
//      println("load XML");
//      val elem: Elem = XML.load(entity.getContent())

/*        val reader = new java.io.BufferedReader(new java.io.InputStreamReader(entity.getContent()));
        var line:String = reader.readLine

        while( line != null ){ // すべての行をファイルから読み込むまで繰り返し *3
          println( line )            // 引数にもらった関数オブジェクトを呼び出す *4
          line = reader.readLine   // 次の行を読み込む *5
        }
*/
/*          var buff = new Array[Byte](100);
//　　　　　do {
            b = instream.read(buff);
　　　　　　System.out.write(buff, 0, b);
//　　　　　}while(false)
          println(instream);
//            println(new String(EntityUtils.toString(entity).getBytes("UTF-8"), "UTF-8"));//print(instream.read());
*/
    }
    println("こんにちわ。世界!!");
  }
}