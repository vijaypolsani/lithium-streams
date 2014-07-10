package com.lithium.streams.compliance.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ListOfSpringBeans {
	private static final Logger log = LoggerFactory.getLogger(ListOfSpringBeans.class);

	public ListOfSpringBeans() {
		// TODO Auto-generated constructor stub
	}

	public static List<Object> getInstantiatedSigletons(ApplicationContext ctx) {
		List<Object> singletons = new ArrayList<Object>();

		String[] all = ctx.getBeanDefinitionNames();

		ConfigurableListableBeanFactory clbf = ((AbstractApplicationContext) ctx).getBeanFactory();
		for (String name : all) {
			Object s = clbf.getSingleton(name);
			if (s != null) {
				singletons.add(s);
				log.info(">>> Singleton Spring Bean Loader: " + s);
			}
		}

		return singletons;

	}
}
