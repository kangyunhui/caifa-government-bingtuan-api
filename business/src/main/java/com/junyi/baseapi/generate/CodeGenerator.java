package com.junyi.baseapi.generate;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author erma66.feng
 * @email erma66@sina.cn
 * @date 2021-01-18
 * @description 代码生成器, 依赖mybatisplus-generator模块，dao使用mybaitsplus
 *     参考文档：https://baomidou.com/guide/generator.html#%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B 1，修改数据库连接
 *     2，数据库表应该以t_或者tbl_为前缀，如果不是请在本类中修改支持 3，数据库表和表字段都应该添加注释，这些注释会生成到实体中，也会用在swagger注解中
 *     4，生成使用的模板在resource/templates下，使用freemarker模板引擎
 *     5，默认生成增删改查的6个接口，其中查询分为分页查询和单记录查询，删除支持单条记录删除和批量删除 6，详细配置请参考官方文档
 */
public class CodeGenerator {
    private static final String URL = "jdbc:postgresql://localhost:5432/base";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";
    private static final String DRIVE_NAME = "org.postgresql.Driver";
    private static final String BASE_PACKAGE = "com.junyi.baseapi";

    /** 读取控制台内容 */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/business/src/main/java");
        gc.setAuthor("generator");
        gc.setOpen(false);
        gc.setServiceName("%sService");
        gc.setFileOverride(true);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(URL);
        // dsc.setSchemaName("public");
        dsc.setDriverName(DRIVE_NAME);
        dsc.setUsername(USERNAME);
        dsc.setPassword(PASSWORD);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //        String modelName = scanner("模块名(若没有直接回车)");
        String modelName = "";
        String dot = StringUtils.isBlank(modelName) ? "" : ".";
        String separator = StringUtils.isBlank(modelName) ? "" : "/";

        pc.setModuleName(modelName);
        pc.setParent(BASE_PACKAGE);
        pc.setController("controller" + dot + modelName);
        pc.setService("service" + dot + modelName);
        pc.setServiceImpl("service.impl" + dot + modelName);
        pc.setMapper("mapper" + dot + modelName);
        pc.setEntity("pojo.postgres" + dot + modelName);
        mpg.setPackageInfo(pc);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        // 公共父类
        //        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //        strategy.setSuperEntityColumns("id");
        String tables = scanner("表名，多个英文逗号分割");
        if (StringUtils.isBlank(tables)) {
            throw new MybatisPlusException("没有输入正确的表名");
        }
        strategy.setInclude(tables.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        // 表名前缀
        strategy.setTablePrefix("t_", "tbl_");
        mpg.setStrategy(strategy);

        // 自定义配置
        InjectionConfig cfg =
                new InjectionConfig() {
                    @Override
                    public void initMap() {
                        //                        Map<String, Object> param = new HashMap<>();
                        //                        param.put("abc", "123");
                        //                        this.setMap(param);
                    }
                };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(
                new FileOutConfig(templatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                        return projectPath
                                + "/business/src/main/resources/mapper/"
                                + modelName
                                + separator
                                + tableInfo.getEntityName()
                                + "Mapper"
                                + StringPool.DOT_XML;
                    }
                });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
