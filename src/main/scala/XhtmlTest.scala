import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import scala.io.Source
import scala.xml.parsing.XhtmlParser

/**
 * Created by IntelliJ IDEA.
 * User: tamaki-t
 * Date: 11/03/30
 * Time: 9:54
 * To change this template use File | Settings | File Templates.
 */

class XhtmlTest

object XhtmlTest {
  def main(args: Array[String]) = {
    val httpclient = new DefaultHttpClient();
//    val httpget = new HttpGet("http://www.apache.org/");
    val httpget = new HttpGet("http://www.ryutsuu.biz/");
    println("executing request " + httpget.getURI());
    val response = httpclient.execute(httpget);
    println("----------------------------------------");
    println(response.getStatusLine());
    println("----------------------------------------");
    // Get hold of the response entity
    val entity = response.getEntity();

    val str = HelloWorld.inputStreamToString(entity.getContent());
    val ptn2 = """(?s)\<ul\>.*?\<\/ul\>""".r;
//      println("ptn2:" + ptn2.findAllIn(str).toList.toString)
//        ptn2.findAllIn(str).foreach(x => println(x))
    val ul = ptn2.findAllIn(str).toList.tail.tail.head
    val xx = """\&""".r.replaceAllIn(ul,"&amp;");

    // If the response does not enclose an entity, there is no need
    // to bother about connection release
    if (entity != null) {
//      val source = Source.fromInputStream(entity.getContent())
      val source = Source.fromString(xx)
      var xhtml = XhtmlParser(source)
      println((xhtml \\ "li").toString)
    }
  }
}