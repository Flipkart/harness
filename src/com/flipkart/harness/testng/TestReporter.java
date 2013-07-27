package com.flipkart.harness.testng;

/**
 * Created with IntelliJ IDEA.
 * User: sudhanshu.gupta
 * Date: 09/07/13
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */

import org.testng.*;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class TestReporter implements IReporter {
    private static final Logger logger = Logger.getLogger(TestReporter.class);
    private PrintWriter out;
    private int row, methodIndex, rowTotal;
    private boolean haveScreenShots  = false;

    public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir)
    {
        out = createWriter(outdir);
        startHTML(out);
        generateSuiteSummaryReport(suites);
        generateMethodSummaryReport(suites);
        generateMethodDetailReport(suites);
        stopHTML(out);
        out.flush();
        out.close();
    }

    protected PrintWriter createWriter(String outdir)
    {
        PrintWriter writer = null;
        try
        {
            new File(outdir).mkdir();
            writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "report.html"))));
            FileInputStream inStream = new FileInputStream(new File("resources/style.css"));
            FileOutputStream outStream = new FileOutputStream(new File(outdir+"/style.css"));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0)
                outStream.write(buffer, 0, length);
            inStream = new FileInputStream(new File("resources/report.js"));
            outStream = new FileOutputStream(new File(outdir+"/report.js"));
            buffer = new byte[1024];
            while ((length = inStream.read(buffer)) > 0)
                outStream.write(buffer, 0, length);
            inStream.close();
            outStream.close();
            File folder = new File(outdir+"/screenshots");
            if(folder.exists())
                haveScreenShots = true;
        }
        catch (IOException e)
        {
            logger.error("Output File", e);
        }
        return writer;
    }

    protected void startHTML(PrintWriter out)
    {
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("\t<head>");
        out.println("\t\t<title>TestNG Customized Report</title>");
        out.println("\t\t<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" />");
        out.println("\t\t<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js?ver=1.3.2\"></script>");
        out.println("\t\t<script type=\"text/javascript\" src=\"http://cdn.jquerytools.org/1.2.7/full/jquery.tools.min.js\"></script>");
        out.println("\t\t<script type=\"text/javascript\" src=\"report.js\"></script>");
        out.println("\t</head>");
        out.println("\t<body onload=\"$('#block').hide();\">");
        out.println("\t\t<a href='index.html' class='o-report'> View Default TestNG Report </a>");
        out.println("\t\t<div class='heading' id='summary'>TestNG Report</div>");
        out.println("\t\t<div id='block' class='box'></div>");
    }

    protected void stopHTML(PrintWriter out)
    {
        out.println("\t</body>");
        out.println("</html>");
    }

    public void generateSuiteSummaryReport(List<ISuite> suites)
    {
        int qty_tests=0, qty_pass_m=0, qty_pass_s=0, qty_fail=0, qty_skipped=0;
        long startTime = Long.MAX_VALUE, endTime = Long.MIN_VALUE;
        startTable("summary_center", 1);
        out.println("\t<tr>");
        tableColumnStart("Test");
        tableColumnStart("# Passed");
        //tableColumnStart("Scenarios Passed");
        tableColumnStart("# Failed");
        tableColumnStart("# Skipped");
        tableColumnStart("Total Time");
        tableColumnStart("Included Groups");
        tableColumnStart("Excluded Groups");
        out.println("\t</tr>");
        NumberFormat format = new DecimalFormat("#,##0.0");
        for(ISuite suite: suites)
        {
            if(suites.size()>1)
            {
                titleRow(suite.getName(), 7);
            }
            Map<String, ISuiteResult> tests = suite.getResults();
            for(ISuiteResult r: tests.values())
            {
                qty_tests++;
                ITestContext overview = r.getTestContext();
                startSummaryRow(overview.getName());
                int q = getMethodSet(overview.getPassedTests()).size();
                qty_pass_m += q;
                summaryCell(q, Integer.MAX_VALUE,"passed");
                q = overview.getPassedTests().size();
                //qty_pass_s += q;
                //summaryCell(q, Integer.MAX_VALUE, "passed");
                q = overview.getFailedTests().size();
                qty_fail += q;
                summaryCell(q, 0, "failed");
                q = overview.getSkippedTests().size();
                qty_skipped += q;
                summaryCell(q, 0, "skipped");
                startTime = Math.min(overview.getStartDate().getTime(), startTime);
                endTime = Math.min(overview.getEndDate().getTime(), endTime);
                summaryCell(format.format((overview.getEndDate().getTime() - overview.getStartDate().getTime()) / 1000.)+" seconds", true, "");
                summaryCell(overview.getIncludedGroups(),"");
                summaryCell(overview.getExcludedGroups(),"");
                out.println("\t</tr>");
            }
        }
        if(qty_tests > 1)
        {
            out.println("<tr><td class='cell'>Total</td>");
            summaryCell(qty_pass_m, Integer.MAX_VALUE, "passed");
            summaryCell(qty_pass_s, Integer.MAX_VALUE, "passed");
            summaryCell(qty_fail, 0, "failed");
            summaryCell(qty_skipped, 0, "skipped");
            summaryCell(format.format((endTime-startTime)/1000.) + " sec", true, "");
            out.println("<td colspan='2'>&nbsp;</td></tr>");
        }
        out.println("</table>");
    }

    private void startTable(String cssClass, int space)
    {
        out.println("<table cellspacing='"+space+"' "+ (cssClass!=null?"class='"+cssClass+"'":"border='1' align='center'")+">");
        row = 0;
    }

    private void tableColumnStart(String label)
    {
        out.println("\t\t<th class='cell'>"+label+"</th>");
    }

    private void titleRow(String label, int cq)
    {
        out.println("<tr><th class='cell_background' colspan=\"" + cq + "\">" + label + "</th></tr>");
        row = 0;
    }

    private void startSummaryRow(String label)
    {
        row++;
        out.println("\t\t<td class='cell'>"+label+"</td>");
    }

    private void summaryCell(String v, boolean isGood, String style)
    {
        out.print("<td class='"+(isGood?"cell":style)+"'>" + v + "</td>");
    }

    private void summaryCell(int v, int maxexpected, String style)
    {
        summaryCell(String.valueOf(v),v<=maxexpected, style);
        rowTotal += v;
    }

    private void summaryCell(String [] val, String style)
    {
        StringBuffer buffer = new StringBuffer();
        for(String str : val)
        {
            buffer.append(str+" ");
        }
        summaryCell(buffer.toString(), true, style);
    }

    private Collection<ITestNGMethod> getMethodSet(IResultMap tests)
    {
        List<ITestNGMethod> r = new ArrayList<ITestNGMethod>(tests.getAllMethods());
        Arrays.sort(r.toArray(new ITestNGMethod[r.size()]), new TestSorter());
        return r;
    }

    private class TestSorter implements Comparator<ITestNGMethod> {
        public int compare(ITestNGMethod o1, ITestNGMethod o2) {
            return (int) (o1.getDate() - o2.getDate());
        }
    }

    private void generateMethodSummaryReport(List<ISuite> suites)
    {
        methodIndex = 0;
        startResultSummaryTable("center");
        for(ISuite suite: suites)
        {
            if(suites.size()>1) {
                titleRow(suite.getName(), 4);
            }
            Map<String, ISuiteResult> r = suite.getResults();
            for(ISuiteResult r2: r.values())
            {
                ITestContext testContext = r2.getTestContext();
                String testName = testContext.getName();
                resultSummary(testContext.getFailedConfigurations(), testName, "failed", " (configuration methods)");
                resultSummary(testContext.getFailedTests(), testName, "failed", "");
                resultSummary(testContext.getSkippedConfigurations(), testName, "skipped", " (configuration methods)");
                resultSummary(testContext.getSkippedTests(), testName, "skipped", "");
                resultSummary(testContext.getPassedTests(), testName, "passed", "");
            }
        }
        out.println("</table>");
    }

    private void resultSummary(IResultMap tests, String testname, String style, String details) {
        if (tests.getAllResults().size() > 0)
        {
            StringBuffer buff = new StringBuffer();
            String lastClassName = "";
            int mq = 0;
            int cq = 0;
            for (ITestNGMethod method : getMethodSet(tests))
            {
                row += 1;
                methodIndex += 1;
                ITestClass testClass = method.getTestClass();
                String className = testClass.getName();
                if (mq == 0)
                    titleRow(testname + " &#8212; " + style + details, 5);
                if (!className.equalsIgnoreCase(lastClassName))
                {
                    if (mq > 0)
                    {
                        cq += 1;
                        out.println("<tr><td class=\"" + style + "\" rowspan='"
                                + mq + "'>" + lastClassName.substring(lastClassName.indexOf(".src.")+5) + buff);
                    }
                    mq = 0;
                    buff.setLength(0);
                    lastClassName = className;
                }
                Set<ITestResult> resultSet = tests.getResults(method);
                long end = Long.MIN_VALUE;
                long start = Long.MAX_VALUE;
                for (ITestResult testResult : tests.getResults(method))
                {
                    if (testResult.getEndMillis() > end)
                        end = testResult.getEndMillis();
                    if (testResult.getStartMillis() < start)
                        start = testResult.getStartMillis();

                }
                mq += 1;
                if (mq > 1)
                    buff.append("<tr>");
                String description = method.getDescription();
                String testInstanceName = resultSet.toArray(new ITestResult[]{})[0].getTestName();
                String name = method.getMethodName();
                buff.append("<td class=\""+style+"\"><a href=\"#m" + methodIndex + "\" onclick='openDetailReport(this);'>"
                        + name
                        + "</a>"
                        + "</td>" + "<td class=\""+style+"\">"
                        + resultSet.size() + "</td><td class='"+style+"'>" + (end - start)
                        + "</td>" + "<td class='"+style+"'>" + ((style.equalsIgnoreCase("failed") && haveScreenShots)?"<div align='center'><a href='screenshots/" + name.toLowerCase() + ".jpg' style='text-decoration:none'><img src=\"screenshots/"+name.toLowerCase()+".jpg\" alt=\"screenshot\" width=\"50\" height=\"30\" class='image' onmouseover='showBigImage(this);' onmouseout='hideBigImage();' /></a></div>":"")+"</td></tr>");
            }
            if (mq > 0)
            {
                cq += 1;
                out.println("<tr>" + "<td  class=\"" + style + "\" rowspan=\"" + mq + "\">" + lastClassName.substring(lastClassName.indexOf(".src.")+5) + buff);
            }
        }
    }

    private String qualifiedName(ITestNGMethod method)
    {
        StringBuilder addon = new StringBuilder();
        String[] groups = method.getGroups();
        int length = groups.length;
        if (length > 0 && !"basic".equalsIgnoreCase(groups[0]))
        {
            addon.append("(");
            for (int i = 0; i < length; i++)
            {
                if (i > 0)
                {
                    addon.append(", ");
                }
                addon.append(groups[i]);
            }
            addon.append(")");
        }
        return method.getMethodName() + addon;
    }

    private void startResultSummaryTable(String style)
    {
        startTable(style, 0);
        out.println("\t<tr>");
        out.println("\t\t<th class='cell'>Class</th>");
        out.println("\t\t<th class='cell'>Method</th>");
        out.println("\t\t<th class='cell'># of Scenario</th>");
        out.println("\t\t<th class='cell'>Time (msec)</th>");
        out.println("\t\t<th class='cell'>ScreenShot</th>");
        out.println("\t</tr>");
        row = 0;
    }

    private void generateMethodDetailReport(List<ISuite> suites)
    {
        methodIndex = 0;
        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> r = suite.getResults();
            for (ISuiteResult r2 : r.values())
            {
                ITestContext testContext = r2.getTestContext();
                if (r.values().size() > 0)
                    out.println("<h1 align=\"center\">" + testContext.getName() + "</h1>");
                resultDetail(testContext.getFailedConfigurations(), "failed");
                resultDetail(testContext.getFailedTests(), "failed");
                resultDetail(testContext.getSkippedConfigurations(), "skipped");
                resultDetail(testContext.getSkippedTests(), "skipped");
                resultDetail(testContext.getPassedTests(), "passed");
            }
        }
    }

    private void resultDetail(IResultMap tests, final String style)
    {
        for (ITestResult result : tests.getAllResults())
        {
            int m_row = 0;
            ITestNGMethod method = result.getMethod();
            m_row += 1;
            methodIndex += 1;
            String cname = method.getTestClass().getName();
            out.println("<div class='collapsiable' id='m" + methodIndex + "' onclick='showCollapsiable(this);' style='padding-right:1%'>" + cname + " : " + method.getMethodName() + "<div class='sign'> + </div> </div>");
            Set<ITestResult> resultSet = tests.getResults(method);
            out.println("<div class='center_collapsiable' id='c" + methodIndex + "' >");
            generateForResult(result, method, resultSet.size());
            out.println("<a href='#summary' style='float:right;padding:5px;'>back to summary</a></div>");
        }
    }

    private void generateForResult(ITestResult ans, ITestNGMethod method, int resultSetSize)
    {
        int rq = 0;
        rq += 1;
        Object[] parameters = ans.getParameters();
        boolean hasParameters = parameters != null && parameters.length > 0;
        if (hasParameters)
        {
            if (rq == 1)
            {
                startTable(null, 0);
                out.print("<tr>");
                for (int x = 1; x <= parameters.length; x++)
                {
                    out.print("<th>Parameter #"
                            + x + "</th>");
                }
                out.println("</tr>");
            }
            out.print("<tr>");
            for (Object p : parameters)
            {
                out.println("<td style='text-align:center'>"
                        + (p != null ? Utils.escapeHtml(p.toString()) : "null") + "</td>");
            }
            out.println("</tr>");
        }
        List<String> msgs = org.testng.Reporter.getOutput(ans);
        boolean hasReporterOutput = msgs.size() > 0;
        Throwable exception=ans.getThrowable();
        boolean hasThrowable = exception!=null;
        if (hasReporterOutput||hasThrowable)
        {
            String indent = " style=\"padding-left:3em\"";
            if (hasParameters)
                out.println("<tr><td colspan='"+ parameters.length+"'>");
            else
                out.println("<div>");
            if (hasReporterOutput)
            {
                if(hasThrowable)
                    out.println("<h3>Test Messages</h3>");
                for (String line : msgs)
                    out.println(line + "<br/>");
            }
            if(hasThrowable)
            {
                boolean wantsMinimalOutput = ans.getStatus()==ITestResult.SUCCESS;
                if(hasReporterOutput)
                    out.println("<h3>" + (wantsMinimalOutput?"Expected Exception":"Failure") + "</h3>");
                generateExceptionReport(exception,method);
            }
            if (hasParameters)
                out.println("</td></tr>");
            else
                out.println("</div>");
        }
        if (hasParameters)
        {
            //if (rq == resultSetSize)
            out.println("</table>");
        }
    }

    protected void generateExceptionReport(Throwable exception,ITestNGMethod method)
    {
        generateExceptionReport(exception, method, exception.getLocalizedMessage());
    }

    private void generateExceptionReport(Throwable exception,ITestNGMethod method,String title)
    {
        out.println("<p>" + (title != null && title.startsWith("Failed")?"java.lang.AssertionError : ":"") + Utils.escapeHtml(title) + "</p>");
        StackTraceElement[] s1= exception.getStackTrace();
        Throwable t2= exception.getCause();
        if(t2 == exception)
            t2= null;
        for(int i=0;i<s1.length;i++)
            out.println((i>0 ? "<br/>at " : "") + Utils.escapeHtml(s1[i].toString()));
        if(t2 != null)
            generateExceptionReport(t2, method, "Caused by " + t2.getLocalizedMessage());
    }
}
