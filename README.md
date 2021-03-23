# bas-api
API基础框架


### 技术栈说明
1. 工程使用mybatis-plus框架作为数据访问层框架，mybatis-plus是国内团队开发的mybatis的增强版，完全兼容mybatis，但可以自动实现常用的增删改查功能，极大的提高代码开发效率，文档地址：https://baomidou.com/guide/
2. 工程中数据源使用动态数据源组件，相关配置已经在配置文件中写好，只需要根据实际情况修改数据库连接即可，数据源切换非常方便，参考文档：https://baomidou.com/guide/dynamic-datasource.html
3. 工程使用swagger自动生成接口文档，开发过程中添加的接口都需要使用详细的swagger注解标注，保证接口文档的完整
4. 工程中使用mybatis-plus-generator作为代码生成器，其入口在generate包下的CodeGenerator类，使用说明请查看该类
5. 单元测试使用springboot中集成的Mockito框架，基础写法可以参考目前工程中的两个测试用例