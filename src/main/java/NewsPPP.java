import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONObject;


/**
 * 
 * @author 90783
 *
 */
public class NewsPPP {
	
	public void getNews(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true",
					"root", "163512");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//#\32 0190703A0AB6E_1 > div > h3 > a
		//#\32 0190703003775_2 > div > h3 > aa
		ArrayList<News> newsList=new ArrayList<>();
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
				Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
				/*Document doc=Jsoup.connect(url)
				        //模拟火狐浏览器
				        .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
				        .get();*/
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
				//获取时间 来源 类型
				String time="";
				String type="";
				String src="";
				Elements js=doc.select("script");
				for(Element jsE:js){
					String str=jsE.html();
					if(str.contains("window.DATA")){
						str=str.replace("window.DATA", "");
						str=str.replace("=", "");
						JSONObject jsObj=JSONObject.fromObject(str);
						src=jsObj.getString("media");
						time=jsObj.getString("pubtime");
						type=jsObj.getString("catalog1");
					}
				}
				//组装新闻对象
				n.setContent(content);
				n.setId(id);
				n.setIntroduction(introduction);
				n.setTitle(title);
				n.setTime(time);
				n.setType(type);
				//插入数据库
				try {
					PreparedStatement ps=conn.prepareStatement("insert into news values(?,?,?,?,?,?,?)");
					ps.setString(1, id);
					ps.setString(2, title);
					ps.setString(3, introduction);
					ps.setString(4, content);
					ps.setString(5, src);
					ps.setString(6, type);
					ps.setString(7, time);
					ps.execute();
				} catch (SQLException e1) {
					continue;
				}
			}
	             
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
