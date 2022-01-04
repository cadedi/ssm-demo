package edu.gdut.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdut.bean.Employee;
import edu.gdut.bean.Msg;
import edu.gdut.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*  URI
*   /emp/{id}   GET查询员工
*   /emp        POST保存员工
*   /emp/{id}   PUT修改员工
*   /emp/{id}   DELETE删除员工
* */

@Controller
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;


    //以json格式(利用Jackson包)返回数据(使用通用的包装类包装数据和消息)
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeService.getAll();

        PageInfo page = new PageInfo(emps,5);   //配置导航中连续显示的总页数
        return Msg.success().add("pageInfo", page);
    }
    //保存员工___JSR303实现校验逻辑
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        if(result.hasErrors()){
            //校验失败,返回失败
            Map<String,Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for(FieldError fieldError : errors){
                System.out.println("错误的字段名: " + fieldError.getField());
                System.out.println("错误信息: " + fieldError.getDefaultMessage());
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        }
        employeeService.saveEmp(employee);
        return Msg.success();
    }
    //用户名重复性检验(是否可用于添加用户)__控制层直接实现校验逻辑
    @RequestMapping("/checkuser")
    @ResponseBody
    public Msg checkuser(@RequestParam("empName") String empName){
        //先判断用户名是否合法表达式
        System.out.println(empName);
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
        if(!empName.matches(regx)){
            return Msg.fail().add("va_msg", "用户名必须是6-16位数字和字母组合或2-5位汉字");
        }
        //数据库用户名重复性校验
        boolean b = employeeService.checkUser(empName);
        if(b){
            return Msg.success();
        }else{
            return Msg.fail().add("va_msg", "用户名不可用");
        }


    }



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
