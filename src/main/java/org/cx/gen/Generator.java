package org.cx.gen;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Generator {

	public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
		List<String> warnings = new ArrayList<String>();

		File configFile = new File(Generator.class.getClassLoader().getResource("generatorConfig.xml").getFile());

		Configuration config = new ConfigurationParser(warnings).parseConfiguration(configFile);

		DefaultShellCallback callback = new DefaultShellCallback(true);

		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		
		myBatisGenerator.generate(null);  
	}

}
