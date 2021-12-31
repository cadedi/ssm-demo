package edu.gdut.test;


import com.github.pagehelper.PageInfo;
import edu.gdut.bean.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 使用spring测试模块的**测试请求**功能(需加上mvc配置文件)
 */


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration    //提供webApplicationContext的注入
@ContextConfiguration(locations={"classpath:applicationContext.xml","file:C:/Users/pcom/Desktop/Java/ssm-demo/src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
public class MVCTest {
    //传入SpringMVC的IOC
    @Autowired
    WebApplicationContext context;
    //虚拟mvc请求,获取处理结果
    MockMvc mockMvc;


    @Before
    public void initMockMVC(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testFile(){
        System.out.println("path4:" +System.getProperty("user.dir"));
    }

    @Test
    public void testPage() throws Exception {
        //模拟发出get请求并拿到返回值
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "1")).andReturn();

        //请求成功后,可取出请求域中控制器设置的pageInfo
        MockHttpServletRequest request = result.getRequest();
        PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
        System.out.println("当前页码: " + pageInfo.getPageNum());
        System.out.println("总页码: " + pageInfo.getPages());
        System.out.println("总记录数: " + pageInfo.getTotal());
        System.out.println("在当前页面需要连续显示的页码");
        int[] nums = pageInfo.getNavigatepageNums();
        for(int i : nums)
            System.out.println(" " + i);
        
        //获取员工数据
        List<Employee> list = pageInfo.getList();
        for(Employee employee : list){
            System.out.println("ID: " + employee.getEmpId() + "==>Name: " + employee.getEmpName());
        }
    }
}
