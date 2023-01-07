package cn.tqxd.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
//分类
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    /**
     * 类型 1 菜品分类 2 套餐分类
     */
    private Integer type;


    /**
     * 分类名称
     */
    private String name;


    /**
     * 顺序
     */
    private Integer sort;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    /**
     * 公共字段自动填充
     * 实现步骤：
     * 1.在实体类中的属性上加入@TableField注解，指定自动填充的策略
     * 2.按照框架要求编写元数据处理器，需实现MetaObjectHandler接口
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}

