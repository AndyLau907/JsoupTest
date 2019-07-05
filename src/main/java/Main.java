import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Main {

	public static void main(String[] args) {

		// TODO Auto-generated method stub // 定义一个JobDetail
		JobDetail jobDetail = JobBuilder.newJob(NewsJob.class) // 定义name和group
				.withIdentity("newsJob", "group1").build(); // 定义一个Trigger
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1") // 加入
																							// scheduler之后立刻执行
				.startNow()
				.withSchedule(DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
						.startingDailyAt(TimeOfDay.hourAndMinuteOfDay(17,26)) // 每天6：00开始
						.endingDailyAt(TimeOfDay.hourAndMinuteOfDay(20, 0)) // 20：00
																			// //
																			// 结束
						.onDaysOfTheWeek(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY,
								Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY) // 周一到周天执行
						.withIntervalInMinutes(20))
				.build();// 每次执行间隔20分钟
		// 删除半个月前新闻
		JobDetail jobDetail2 = JobBuilder.newJob(DeleteJob.class) // 定义name和group
				.withIdentity("deleteJob", "group2").build(); // 定义一个Trigger
		Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger2", "group2") // 加scheduler之后立刻执行
				.startNow()
				.withSchedule(DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
						.startingDailyAt(TimeOfDay.hourAndMinuteOfDay(6, 0)) // 每天6：00执行
						.endingDailyAt(TimeOfDay.hourAndMinuteOfDay(6, 1)).onDaysOfTheWeek(Calendar.MONDAY,
								Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY,
								Calendar.SATURDAY, Calendar.SUNDAY))
				.build();
		try { // 创建scheduler
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(jobDetail, trigger);
			scheduler.scheduleJob(jobDetail2, trigger2);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		/*
		 * LogFactory.getFactory().setAttribute(
		 * "org.apache.commons.logging.Log",
		 * "org.apache.commons.logging.impl.NoOpLog");
		 * java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(
		 * Level.OFF);
		 * java.util.logging.Logger.getLogger("org.apache.http.client").setLevel
		 * (Level.OFF);
		 * 
		 * String url = "https://new.qq.com/rolls/?ext=sports";
		 * System.out.println(
		 * "Loading page now-----------------------------------------------: " +
		 * url);
		 * 
		 * // HtmlUnit 模拟浏览器 WebClient webClient = new
		 * WebClient(BrowserVersion.CHROME);
		 * webClient.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
		 * webClient.getOptions().setCssEnabled(false); // 禁用css支持
		 * webClient.getOptions().setThrowExceptionOnScriptError(false); //
		 * js运行错误时，是否抛出异常
		 * webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		 * webClient.getOptions().setTimeout(10 * 1000); // 设置连接超时时间 HtmlPage
		 * page=null; try { page = webClient.getPage(url); } catch
		 * (FailingHttpStatusCodeException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (MalformedURLException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } webClient.waitForBackgroundJavaScript(30 *
		 * 1000); // 等待js后台执行30秒
		 * 
		 * String pageAsXml = page.asXml(); Document doc=Jsoup.parse(pageAsXml);
		 */
	}

}
