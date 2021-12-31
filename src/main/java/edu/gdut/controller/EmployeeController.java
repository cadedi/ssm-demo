package edu.gdut.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdut.bean.Employee;
import edu.gdut.bean.Msg;
import edu.gdut.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;


    //以json格式返回数据(使用通用的包装类包装数据和消息)
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeService.getAll();

        PageInfo page = new PageInfo(emps,5);   //配置导航中连续显示的总页数
        return Msg.success().add("pageInfo", page);
    }



    //以json格式返回数据
   /* @RequestMapping("/emps")
    @ResponseBody
    public PageInfo getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeService.getAll();

        PageInfo page = new PageInfo(emps,5);   //配置导航中连续显示的总页数
        return page;
    }
    */



    /**
     * 查询员工数据(分页查询)
     * 返回jsp
     * @return
     */
    //@RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model){

        //引入分页插件,查询前只需调用插件,传入页码及每页大小
        PageHelper.startPage(pn, 5);
        //PageHelper.startPage后紧跟的查询即是分页查询
        List<Employee> emps = employeeService.getAll();

        //使用PageHelper的PageInfo包装查询结果,包装中详细的分页信息
        //PageInfo page = new PageInfo(emps);
        PageInfo page = new PageInfo(emps,5);   //配置导航中连续显示的总页数
        model.addAttribute("pageInfo", page);

        return "list";
    }
}
