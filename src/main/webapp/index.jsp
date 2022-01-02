<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--@elvariable id="emp" type="edu.gdut.bean.Employee"--%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>员工列表</title>
    <%
        pageContext.setAttribute("APP_PATH", request.getContextPath());
    %>
    <script type="text/javascript" src="${APP_PATH}/static/js/jquery.min.js"></script>
    <link href="${APP_PATH}/static/bootstrap-3.4.1-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${APP_PATH}/static/bootstrap-3.4.1-dist/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <!-- 标题 -->
    <div class="row">
        <div class="col-md-12">
            <h1>SSM-CRUD</h1>
        </div>
    </div>
    <!-- 按钮 -->
    <div class="row">
        <div class="col-md-4 col-md-offset-8">
            <button class="btn btn-primary btn-sm">新增</button>
            <button class="btn btn-danger btn-sm">删除</button>
        </div>
    </div>
    <!-- 显示表格数据 -->
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="emps_table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>empName</th>
                        <th>gender</th>
                        <th>email</th>
                        <th>departName</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <!-- 显示分页信息 -->
    <div class="row">
        <!--分页文字信息-->
        <div class="col-md-6" id="page_info_area"></div>
        <!--分页条信息-->
        <div class="col-md-6" id="page_nav_area"></div>
    </div>
</div>
<script>
    // 页面加载完毕发送AJAX请求获取第一页数据
    $(function () {
      to_page(1)
    })
    function to_page(pn){
        $.ajax({
            url: "${APP_PATH}/emps",
            data: "pn="+pn,
            type: "GET",
            success: function (result) {
                // 1.解析并显示员工数据
                build_emps_table(result)
                // 2.解析并显示分页信息      
                build_page_info(result)
                // 3.解析并显示分页条数据
                build_page_nav(result)
            }
        })
    }
    // 解析并显示员工数据表格
    function build_emps_table(result) {
        // 清空当前表格
        $("#emps_table tbody").empty()
        let emps = result.extend.pageInfo.list
        // 构建每行数据
        $.each(emps,function (index,item) {
            let checkBoxTd = $("<td><input type='checkbox' class='check_item'/></td>")
            let empIdTd = $("<td></td>").append(item.empId)
            let empNameTd = $("<td></td>").append(item.empName)
            let genderTd = $("<td></td>").append( item.gender=='M'?'男':'女')
            let emailTd = $("<td></td>").append(item.email)
            let deptNameTd = $("<td></td>").append(item.department.deptName)
            let editBtn = $("<button></button>").addClass("btn btn-primary btn-sm")
                .append($("<span></span>").addClass("glyphicon glyphicon-pencil")).append("编辑")
            editBtn.attr("edit-id",item.empId)
            let delBtn = $("<button></button>").addClass("btn btn-danger btn-sm")
                .append($("<span></span>").addClass("glyphicon glyphicon-trash")).append("删除")
            delBtn.attr("del-id",item.empId)
            let btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn)
            $("<tr></tr>").append(checkBoxTd)
                .append(empIdTd)
                .append(empNameTd)
                .append(genderTd)
                .append(emailTd)
                .append(deptNameTd)
                .append(btnTd)
                .appendTo("#emps_table tbody")
            
        })
    }
    // 解析并显示分页信息 
    function build_page_info(result) {
      // 清空
      $("#page_info_area").empty()
      // 添加到页面
      $("#page_info_area")
        .append("当前"+result.extend.pageInfo.pageNum+"页,总"+result.extend.pageInfo.pages+"页,总"+result.extend.pageInfo.total+"条记录")
      totalRecord = result.extend.pageInfo.total
      currentPage = result.extend.pageInfo.pageNum
    }
    // 解析显示分页条
    function build_page_nav(result){
      // 清空
      $("#page_nav_area").empty()
      // bootstrap文档分页条
      let ul = $('<ul></ul>').addClass("pagination")
      // 首页,前一页
      let firstPageLi = $("<li></li>").append($("<a></a>").append("首页").attr("href","#"))
      let prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"))
      if(!result.extend.pageInfo.hasPreviousPage){
        firstPageLi.addClass("disable")
        prePageLi.addClass("disable")
      }else{
        // 为首页和上一页绑定点击翻页事件
        firstPageLi.click(function(){
          to_page(1)
        })
        prePageLi.click(function(){
          to_page(result.extend.pageInfo.pageNum - 1)
        })
      }
      // 下一页,末页
      let nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"))
      let lastPageLi = $("<li></li>").append($("<a></a>").append("末页").attr("href","#"))
      if(result.extend.pageInfo.hasNextPage == false){
        nextPageLi.addClass("disable")
        lastPageLi.addClass("disable")
      }else{
        // 为下一页和末页绑定点击翻页事件
        nextPageLi.click(function(){
          to_page(result.extend.pageInfo.pageNum +1)
        })
        lastPageLi.click(function(){
          to_page(result.extend.pageInfo.pages)
        })
      }
      // 前一页,首页添加到分页条
      ul.append(firstPageLi).append(prePageLi)
      // 显示当前连续页码并添加点击翻页事件
      $.each(result.extend.pageInfo.navigatepageNums,function(index,item){
        let numLi = $("<li></li>").append($("<a></a>").append(item))
        // 当前页高亮
        if(result.extend.pageInfo.pageNum == item){
          numLi.addClass("active")
        }
        // 添加点击翻页事件
        numLi.click(function(){
          to_page(item)
        })
        ul.append(numLi)
      })
      // 下一页,末页添加到分页条
      ul.append(nextPageLi).append(lastPageLi)
      // 添加到页面
      let navEle = $("<nav></nav>").append(ul)
      navEle.appendTo("#page_nav_area")
    }
</script>
</body>
</html>