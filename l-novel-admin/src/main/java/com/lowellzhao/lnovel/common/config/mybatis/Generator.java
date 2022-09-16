package com.lowellzhao.lnovel.common.config.mybatis;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 代码生成器
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public class Generator {

    /**
     * 需要生成的表名
     */
    private static final String[] TABLE_NAME = {"t_operation_log", "t_menu", "t_user_info"};
    /**
     * 所属模块
     */
    private static final String MODULE_NAME = "novel";

    /**
     * 表前缀
     */
    private static final String TABLE_PREFIX = "t_";

    /**
     * 作者
     */
    private static final String AUTHOR = "lowellzhao";

    private static final String PACKAGE_NAME = "com.lowellzhao.lnovel";

    public static void main(String[] args) {
        generateByTables();
    }

    private static void generateByTables() {
        List<String> tableNameList = Arrays.stream(TABLE_NAME).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tableNameList)) {
            System.err.println("请输入表名");
            return;
        }
        if (StringUtils.isBlank(MODULE_NAME)) {
            System.err.println("请输入所属模块");
            return;
        }

        AutoGenerator autoGenerator = new AutoGenerator();
        // 数据源配置
        autoGenerator.setDataSource(getDataSourceConfig());
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + "/l-novel-admin/src/main/java/";
        globalConfig.setActiveRecord(true)
                .setAuthor(AUTHOR)
                .setOutputDir(projectPath)
                .setEnableCache(false)
                // 生成后是否打开资源管理器
                .setOpen(false)
                .setBaseResultMap(true)
                .setBaseColumnList(false)
                .setActiveRecord(true)
                .setServiceName("%sService")
                // 文件覆盖
                .setFileOverride(true);
        autoGenerator.setGlobalConfig(globalConfig);
        // 包配置
        PackageConfig packageConfig = new PackageConfig()
                .setParent(PACKAGE_NAME)
                .setEntity("model.entity");
        autoGenerator.setPackageInfo(packageConfig);
        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        String[] tableNameArray = new String[tableNameList.size()];
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setTablePrefix(TABLE_PREFIX)
                .setEntityTableFieldAnnotationEnable(true)
                // 表名生成策略
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNameList.toArray(tableNameArray));
        autoGenerator.setStrategy(strategyConfig);
        // 生成
        autoGenerator.execute();
        System.err.println("生成完成");
    }

    private static DataSourceConfig getDataSourceConfig() {
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/l_novel?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        return dsc;
    }

}
