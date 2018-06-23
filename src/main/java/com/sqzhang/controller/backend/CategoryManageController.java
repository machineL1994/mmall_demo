package com.sqzhang.controller.backend;

import com.sqzhang.common.Const;
import com.sqzhang.common.ResponseCode;
import com.sqzhang.common.ServerResponse;
import com.sqzhang.pojo.User;
import com.sqzhang.service.ICategoryService;
import com.sqzhang.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //增加进行处理分类的逻辑
            return iCategoryService.addCategory(categoryName, parentId);
        }else{
            return ServerResponse.createByErrorMessage("不是管理员权限，需要获得管理员权限");
        }
    }
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //更新分类的逻辑
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }else{
            return ServerResponse.createByErrorMessage("不是管理员权限，需要获得管理员权限");
        }
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //查询子节点的Category信息，并且不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("不是管理员权限，需要获得管理员权限");
        }
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //查询子节点的Category信息，并且递归查询其更深子节点的信息
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("不是管理员权限，需要获得管理员权限");
        }
    }
}
