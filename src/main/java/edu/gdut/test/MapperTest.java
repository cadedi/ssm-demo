package edu.gdut.test;

import edu.gdut.bean.Department;
import edu.gdut.bean.Employee;
import edu.gdut.dao.DepartmentMapper;
import edu.gdut.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;


/**
 *  测试dao层的工作
 *  使用spring的单元测试,可自动注入所需组件
 *  1. 导入spring test包
 *  2.@ContextConfiguration指定spring配置文件位置以及@RunWith指定spring测试模块
 *  3.直接Autowired要使用的组件
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class MapperTest {


    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;

    //用于批量执行语句的性能优化batch模式的sqlSession(在spring配置文件中和mybatis一块注册)
    @Autowired
    SqlSession sqlSession;

    //测试DepartmentMapper
    @Test
    public void testCRUD(){
        /*
            不使用spring测试模块时
            //1.创建spring IOC容器
            ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
            //2.从容器中获取mapper
            DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);
        */
        //使用spring的junit
        System.out.println(departmentMapper);

        //插入部门数据
        //departmentMapper.insertSelective(new Department(null,"开发部"));
        //departmentMapper.insertSelective(new Department(null,"测试部"));


        //插入员工数据
        //employeeMapper.insertSelective(new Employee(null,"Jerry","M","Jerry@163.com",1));

        //BATCH模式的sqlSession批量插入多个员工
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for (int i = 0; i < 100; i++){
            String uid = UUID.randomUUID().toString().substring(0,5) + i;
            mapper.insertSelective(new Employee(null, uid, "M", uid + "@163.com", 1));
        }
        //for(int i = 2; i < 1002; i++){
        //    mapper.deleteByPrimaryKey(i);
        //}
        System.out.println("批量完成");


    }
}
