package com.traffic.apn.news;
/*
 * 规则策略上下文类，个网站新闻获取规则生成调用此类
 */
public class RuleBuild{
	RuleStrategy ruleStrategy;

	public void setRuleStrategy(RuleStrategy ruleStrategy) {
		this.ruleStrategy = ruleStrategy;
	}
	
	public String getUrlFilter(String urlFilter){
		return ruleStrategy.urlFilterStrategy(urlFilter);
	}
	
}