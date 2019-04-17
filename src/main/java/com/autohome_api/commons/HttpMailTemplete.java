package com.autohome_api.commons;

import com.autohome_api.dto.BatchResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HttpMailTemplete {

    public String genHtmlContent(List<BatchResponseBody> list) {
        StringBuffer taskResult = new StringBuffer();
        taskResult
                .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")
                .append("<html>")
                .append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
                .append("<title>TASK运行结果</title>")
                .append("<div>")
                /*.append("<span class=\"test\">任务ID : task-" + "ewq"
                        + " (定时任务编号)<br/>任务名称 : " + "da"
                        + " (定时任务名称)<br/>任务标识 : " + "da" + " (唯一标识号)</span>")*/
                .append("</div>")
                .append("<style type=\"text/css\">")
                //.append("background:url(E:\javaproject\autohome_api\src\main\webapp\HTML\image\login_home_background.jpg)")
                .append("background-color:red")
                .append(".test{font-family:\"Microsoft Yahei\";font-size: 16px;color: #FFD700;font-weight: bold;}")
                .append(".bgth th{background: #9400D3;}.bgth th{color: #FFD700;}")
                .append(".titleblue{color: #1E90FF;}")
                .append(".tableMargin{margin-left:20px;width:100%}")
                .append("</style>")
                .append("</head>")
                .append("<body bgcolor=\"pink\">")
                .append("<div>")
                .append("<span class=\"test\">Hello All:</span>")
                .append("</div>")
                .append("<div><br/></div>")
                .append("<span style=\"font-weight:bold;\" class=\"titleblue\">CASE运行详情:</span>")
                .append("<div><br/></div>")
                .append("<div><br/></div>")
                .append("<div class=\"tableMargin\">")
                .append("<table border=\"1\"  width=\"1500\">")
                .append("<thead>")
                .append("<span style=\"font-weight:bold;\" class=\"titleblue\">用例汇总</span>")
                .append("<tr class=\"bgth\"><th>项目名称</th><th>用例编号</th><th>用例名称</th><th>预期状态码</th><th>消耗时间(ms)</th><th>是否通过</th></tr>")
                .append("</thead>").append("<tbody>");
        for (int i = 0; i < list.size(); i++) {
           if(StringUtils.equalsIgnoreCase( list.get(i).getIsPass(),"通过")){
               taskResult.append("<tr style=\"background-color:green\">");
           }else {
               taskResult.append("<tr style=\"background-color:red\">");
           }
            taskResult.append("<td><span>[" + list.get(i).getProjectName() + "]</span></td>");
            taskResult.append("<td><span>[" + list.get(i).getCaseId() + "]</span></td>");
            taskResult.append("<td>" + list.get(i).getCaseName() + "</td>");
            taskResult.append("<td>" + list.get(i).getExpected() + "</td>");
            taskResult.append("<td>" + list.get(i).getResponseTime() + "</td>");
            taskResult.append("<td>" + list.get(i).getIsPass() + "</td>");
            taskResult.append("</tr>");
        }
        taskResult.append("</table>");
        taskResult.append("<thead>")
                .append("<div><br/></div>")
                .append("<div><br/></div>")
                .append("<span style=\"font-weight:bold;\" class=\"titleblue\">用例入参(根据ID或者用例名称可找到对用的用例运行详情)</span>")
                //.append("<div class=\"tableMargin\">")
                .append("<table border=\"1\"  width=\"1500\">")
                .append("<thead>")
                .append("<tr class=\"bgth\"><th>用例编号</th><th>用例名称</th><th>请求参数</th></tr>")
                .append("</thead>")
                .append("<tbody>");
        for (int i = 0; i < list.size(); i++) {
            if(StringUtils.equalsIgnoreCase( list.get(i).getIsPass(),"通过")){
                taskResult.append("<tr style=\"background-color:green\">");
            }else {
                taskResult.append("<tr style=\"background-color:red\">");
            }
            taskResult.append("<td><span>[" + list.get(i).getCaseId() + "]</span></td>");
            taskResult.append("<td>" + list.get(i).getCaseName() + "</td>");
            taskResult.append("<td>" + list.get(i).getRequestParam() + "</td>");
            taskResult.append("</tr>");
        }
        taskResult.append("</table>");
        taskResult.append("<thead>")
                .append("<div><br/></div>")
                .append("<div><br/></div>")
                .append("<span style=\"font-weight:bold;\" class=\"titleblue\">返回结果(根据ID或者用例名称可找到对用的用例运行详情)</span>")
                //.append("<div class=\"tableMargin\">")
                .append("<table border=\"1\"  width=\"1500\">")
                .append("<thead>")
                .append("<tr class=\"bgth\"><th>用例编号</th><th>用例名称</th><th>返回结果</th></tr>")
                .append("</thead>")
                .append("<tbody>");
        for (int i = 0; i < list.size(); i++) {
            if(StringUtils.equalsIgnoreCase( list.get(i).getIsPass(),"通过")){
                taskResult.append("<tr style=\"background-color:green\">");
            }else {
                taskResult.append("<tr style=\"background-color:red\">");
            }
            taskResult.append("<td><span>[" + list.get(i).getCaseId() + "]</span></td>");
            taskResult.append("<td>" + list.get(i).getCaseName() + "</td>");
            taskResult.append("<td>" + list.get(i).getMessage() + "</td>");
            taskResult.append("</tr>");
        }
                taskResult.append("</tbody>")
                .append("</table>")
                .append("</div>");
        taskResult.append("</tbody>").append("</table>").append("</div>")
                .append("</body>").append("</html>");
        return taskResult.toString();
    }
}
