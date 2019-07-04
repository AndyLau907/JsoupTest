import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 
 * @author 90783
 *
 */
public class NewsPPP {
	
	public void getNews(){
		//#\32 0190703A0AB6E_1 > div > h3 > a
		//#\32 0190703003775_2 > div > h3 > aa
		ArrayList<News> newsList=new ArrayList<>();
		//获取编辑推荐页
        try {
			Document document=Jsoup.connect("https://new.qq.com/ch/tech/")
			        //模拟火狐浏览器
			        .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
			        .get();
			//System.out.println(document.html());
			Elements blocks=document.getElementsByClass("list");
			Elements elements=blocks.get(0).select("li");
			for(Element e:elements){
				//新建新闻对象
				News n=new News();
				//获取id
				String id=e.attr("id");
			
				String url=e.select("div").select("h3").select("a").attr("href");
				if(!url.contains(".html")||url.contains("id")){
					continue;
				}
				Document doc=Jsoup.connect(url)
				        //模拟火狐浏览器
				        .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
				        .get();
				Elements temp=doc.getElementsByClass("videoPlayerWrap");
				if(doc.title()==null||doc.title().isEmpty()){
					continue;
				}
				if(temp.size()>0){
					continue;
				}
				temp=doc.getElementsByClass("LEFT");
				Element news=temp.get(0);
				String title=news.select("h1").get(0).ownText();
				String introduction="";
				Elements ts=news.getElementsByClass("content clearfix").get(0).getElementsByClass("content-article").get(0).getElementsByClass("introduction");
				if(ts.size()>0){
					introduction=ts.get(0).ownText();
				}
				
				//内容
				String content="";
				StringBuilder builder=new StringBuilder(content);
				
				Elements contents=news.getElementsByClass("content clearfix").get(0).getElementsByClass("content-article").get(0).getElementsByClass("one-p");
				for(Element element:contents){
					Elements imgs=element.select("img");
					if(imgs.size()>0){
						builder.append(imgs.get(0).attr("src"));
					}
					builder.append(element.ownText());
				}
				content=builder.toString();
				//组装新闻对象
				n.setContent(content);
				n.setId(id);
				n.setIntroduction(introduction);
				n.setTitle(title);
				
				System.out.println(id+"-"+title+"-"+introduction+"-"+content);
			}
	             
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
