package cn.edu.bupt.utils;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class AnalysisYaml {

	// yaml配置文件路径默认为根目录的kafka.yaml文件
	private String yamlPath = System.getProperty("user.dir") +  System.getProperty("file.separator") + ("kafka.yaml");

	public AnalysisYaml() {
	}

	/**
	 * 生成配置文件解析对象
	 * 
	 * @param
	 */
	public AnalysisYaml(String yamlPath) {
		this.yamlPath = yamlPath;
	}

	public String getYamlPath() {
		return yamlPath;
	}

	public void setYamlPath(String yamlPath) {
		this.yamlPath = yamlPath;
	}

	/**
	 * 通过生产者名，读取配置文件中的指定生产者配置，生成生产者配置文件实体类
	 * 
	 * @param ，所用生产者的名字
	 * @return 配置文件实体类
	 */
	public ProducerProperties getProducerProperties(String producerName) {

		Yaml yaml = new Yaml();
		ProducerProperties producerProperties;
		try {
			Map<String, Map<String, Object>> yamlMap = yaml.loadAs(new FileInputStream(yamlPath), Map.class);
			producerProperties = (ProducerProperties) yamlMap.get("producer").get(producerName);
		} catch (Exception e) {
			return null;
		}

		return producerProperties;

	}

	// 测试
	public static void main(String args[]) {
		AnalysisYaml test = new AnalysisYaml();
		System.out.println(test.getProducerProperties("producerTest").getBootstrapServers());
		System.out.println(test.getProducerProperties("producerTest").getTopic());
	}

}
