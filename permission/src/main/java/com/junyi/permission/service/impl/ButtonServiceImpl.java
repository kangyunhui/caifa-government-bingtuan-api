package com.junyi.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.junyi.permission.constant.CommonConstant;
import com.junyi.permission.entity.Button;
import com.junyi.permission.entity.ButtonInterface;
import com.junyi.permission.entity.RoleButton;
import com.junyi.permission.event.UserInterfaceChangeEvent;
import com.junyi.permission.exception.PermissionException;
import com.junyi.permission.mapper.ButtonInterfaceMapper;
import com.junyi.permission.mapper.ButtonMapper;
import com.junyi.permission.mapper.RoleButtonMapper;
import com.junyi.permission.model.Node;
import com.junyi.permission.model.UserView;
import com.junyi.permission.service.ButtonService;
import com.junyi.permission.service.UserService;
import com.junyi.permission.util.CommonUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ButtonServiceImpl extends ServiceImpl<ButtonMapper, Button> implements ButtonService {

    private final @NonNull ButtonMapper buttonMapper;
    private final @NonNull RoleButtonMapper roleButtonMapper;
    private final @NonNull ButtonInterfaceMapper buttonInterfaceMapper;
    private final @NonNull UserService userService;

    @Resource private HttpServletRequest request;
    @Resource private ApplicationContext context;

    @Override
    public IPage<Button> get(int page, int size, String sort, String order) {
        Page<Button> page1 = new Page<>(page, size, true);
        page1.addOrder(
                CommonConstant.ORDER.equals(order) ? OrderItem.desc(sort) : OrderItem.asc(sort));
        LambdaQueryWrapper<Button> wrapper = Wrappers.lambdaQuery();

        return buttonMapper.selectPage(page1, wrapper);
    }

    @Override
    public void update(Button button) {
        Button buttonOrg = buttonMapper.selectById(button.getGuid());
        if (buttonOrg == null) {
            throw new PermissionException("没有找到要更新的记录");
        }
        if (!buttonOrg.getName().equals(button.getName())) {
            existButton(button);
        }
        buttonMapper.updateById(button);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Button button = buttonMapper.selectById(id);
        if (button == null) {
            throw new PermissionException("没有找到要删除的记录");
        }
        int count =
                roleButtonMapper.selectCount(
                        Wrappers.<RoleButton>lambdaQuery().eq(RoleButton::getButtonId, id));
        if (count > 0) {
            throw new PermissionException("此按钮被其他角色引用，不能删除");
        }
        buttonInterfaceMapper.delete(
                Wrappers.<ButtonInterface>lambdaQuery().eq(ButtonInterface::getButtonId, id));
        buttonMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveButtonInterface(String btnId, String[] interfaceIds) {
        Button button = buttonMapper.selectById(btnId);
        if (button == null) {
            throw new PermissionException("没有找到要操作的按钮");
        }

        List<ButtonInterface> list = new ArrayList<>(interfaceIds.length);
        for (String interId : interfaceIds) {
            ButtonInterface buttonInterface = new ButtonInterface();
            buttonInterface.setGuid(CommonUtils.getUUID());
            buttonInterface.setButtonId(btnId);
            buttonInterface.setInterfaceId(interId);
            list.add(buttonInterface);
        }
        buttonInterfaceMapper.delete(
                Wrappers.<ButtonInterface>lambdaQuery().eq(ButtonInterface::getButtonId, btnId));
        if (!list.isEmpty()) {
            buttonInterfaceMapper.insertBatchSomeColumn(list);
        }
        context.publishEvent(new UserInterfaceChangeEvent(this));
    }

    @Override
    public List<Node> getGroupButton(String roleId) {
        List<Button> buttonList;

        String currentUser = UserService.getUserId(request);
        UserView userView = userService.getById(currentUser);
        // 不是管理员的话只能看到自己拥有权限的按钮
        if (!CommonConstant.ADMIN.equals(userView.getName())) {
            buttonList = buttonMapper.findAllByUser(currentUser);
        } else {
            buttonList =
                    buttonMapper.selectList(
                            Wrappers.<Button>lambdaQuery().orderByAsc(Button::getIndex));
        }

        List<RoleButton> roleButtonList =
                roleButtonMapper.selectList(
                        Wrappers.<RoleButton>lambdaQuery().eq(RoleButton::getRoleId, roleId));
        Set<String> checkedSet =
                roleButtonList.stream().map(RoleButton::getButtonId).collect(Collectors.toSet());
        buttonList.forEach(r -> r.setParentId(r.getParentId() == null ? "0" : r.getParentId()));
        Map<String, List<Button>> buttonGroup =
                buttonList.stream().collect(Collectors.groupingBy(Button::getParentId));

        List<Node> childNodeList = new ArrayList<>(buttonGroup.size());
        List<Button> childButtonList = buttonGroup.get("0");
        packageNode(childNodeList, childButtonList, buttonGroup, checkedSet);
        return childNodeList;
    }

    private void packageNode(
            List<Node> nodeList,
            List<Button> buttonList,
            Map<String, List<Button>> buttonGroup,
            Set<String> checkedSet) {
        if (CollectionUtils.isEmpty(buttonList)) {
            return;
        }
        for (Button button : buttonList) {
            Node node = new Node(button.getGuid(), button.getLabel());
            node.setChecked(checkedSet.contains(button.getGuid()));
            nodeList.add(node);
            List<Button> childButtonList = buttonGroup.get(button.getGuid());
            if (CollectionUtils.isEmpty(childButtonList)) {
                continue;
            }
            List<Node> childNodeList = new ArrayList<>(childButtonList.size());
            node.setChildren(childNodeList);
            packageNode(childNodeList, childButtonList, buttonGroup, checkedSet);
        }
    }

    @Override
    public List<Button> getUserButton() {
        List<Button> userButtonList;
        String userId = UserService.getUserId(request);
        UserView userView = userService.getById(userId);
        // 不是管理员的话只能看到自己拥有权限的按钮
        if (!CommonConstant.ADMIN.equals(userView.getName())) {
            userButtonList = buttonMapper.findAllByUser(userId);
        } else {
            userButtonList = buttonMapper.selectList(Wrappers.emptyWrapper());
        }
        if (CollectionUtils.isEmpty(userButtonList)) {
            return userButtonList;
        }

        // 补充父节点没有保存的情况
        Set<String> parentIdSet =
                userButtonList.stream().map(Button::getParentId).collect(Collectors.toSet());

        Set<String> idSet =
                userButtonList.stream().map(Button::getGuid).collect(Collectors.toSet());

        parentIdSet.removeIf(idSet::contains);

        List<Button> noMathcList = buttonMapper.selectBatchIds(parentIdSet);

        List<Button> result = new ArrayList<>(userButtonList.size() + noMathcList.size());
        result.addAll(noMathcList);
        result.addAll(userButtonList);

        result.sort(Comparator.comparing(Button::getIndex));

        return result;
    }

    @Override
    public void add(Button button) {
        existButton(button);
        buttonMapper.insert(button);
    }

    private void existButton(Button button) {
        Integer count =
                buttonMapper.selectCount(
                        Wrappers.<Button>lambdaQuery()
                                .eq(Button::getParentId, button.getParentId())
                                .eq(Button::getName, button.getName()));
        if (count > 0) {
            throw new PermissionException("同名按钮已存在，不可以重复添加");
        }
    }
}
