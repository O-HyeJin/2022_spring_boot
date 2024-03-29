package com.comento.spring_boot.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "com.comento.spring_boot.mapper")
public class MybatisConfig {
	
	@Bean
	public SqlSessionFactory sqlSessionFactory (DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setTypeAliasesPackage("com.comento.spring_boot.dto");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		
		sqlSessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis/mybatis-config.xml"));
		sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
		
		return sqlSessionFactory.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSession (SqlSessionFactory sqlSessionFactory) {
		sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
}
