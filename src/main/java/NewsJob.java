
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NewsJob implements Job {
	String mainUrl = "https://new.qq.com/ch/";
	// 娱乐 军事 国际 科技 财经 汽车 时尚 图片 游戏 房产 文化 动漫 情感 数码 健康 生活 旅游 美食 历史 宠物
	String type[] = { "ent/", "milite/", "world/", "tech/", "finance/", "auto/", "fashion/", "photo/", "games/",
			"house/", "cul/", "comic/", "emotion/", "digi/", "health/", "life/", "visit/", "food/", "history/",
			"pet/" };
	// 体育
	String sportsUrl = "https://new.qq.com/rolls/?ext=sports";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("newsJob:::"+new Date());
		new newsThread(sportsUrl, "sports").start();
		for (String t : type) {
			new newsThread(mainUrl + t, t.replace("/", "")).start();
		}
		
	}

	class newsThread extends Thread {
		private String url;
		private String type;

		public newsThread(String url, String type) {
			this.url = url;
			this.type = type;
		}

		@Override
		public void run() {
			NewsPPP.getNews(url, type);
		}
	}
}
