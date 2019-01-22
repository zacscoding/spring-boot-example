package crawler.web;

import crawler.util.DebugBuilder;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

/**
 * https://github.com/yasserg/crawler4j
 *
 * http://blog.naver.com/PostView.nhn?blogId=javaking75&logNo=221357838989&categoryNo=18&parentCategoryNo=0&viewDate=&currentPage=1&postListTopCurrentPage=1&from=postView
 */
@Slf4j
@Component
public class SimpleWebCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");

    private AntPathMatcher matcher;
    private String urlPattern;

    public SimpleWebCrawler() {
        this.matcher = new AntPathMatcher();
        // this.urlPattern = "https://www.ics.uci.edu/**";
        this.urlPattern = "https://www.wanted.co.kr/wdlist/518/872/**";
    }

    /**
     * This method receives two parameters. The first parameter is the page in which we have discovered this new url and
     * the second parameter is the new url. You should implement this function to specify whether the given url should
     * be crawled or not (based on your crawling logic). In this example, we are instructing the crawler to ignore urls
     * that have css, js, git, ... extensions and to only accept urls that start with "https://www.ics.uci.edu/". In
     * this case, we didn't need the referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        boolean shouldVisit = matcher.match(urlPattern, href);

        // logger.info("href :: {} >> {}", href, shouldVisit);
        return shouldVisit;
    }

    /**
     * This function is called when a page is fetched and ready to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        DebugBuilder builder = new DebugBuilder();
        builder.appendMessage("DocId : ", page.getWebURL().getDocid())
            .appendMessage("URL : ", page.getWebURL().getURL())
            .appendMessage("Domain : ", page.getWebURL().getDomain())
            .appendMessage("SubDomain : ", page.getWebURL().getSubDomain())
            .appendMessage("Path : ", page.getWebURL().getPath())
            .appendMessage("ParentUrl : ", page.getWebURL().getParentUrl())
            .appendMessage("Anchor text : ", page.getWebURL().getAnchor())
        ;
        logger.info("## Display WebURL \n{}", builder.toString());

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());
            System.out.println("-----------------------");
            System.out.println(html);
            System.out.println("-----------------------");
            Document document = Jsoup.parse(html);
            Elements elts = document.getElementsByClass("Card-RQot efjktk");
            System.out.println("check elts :: " + elts);
        } else {
            ParseData parseData = page.getParseData();
            logger.info("Can`t cast HtmlParseData.. :: " + parseData == null ? "null" : parseData.getClass().getName());
        }
    }
}
