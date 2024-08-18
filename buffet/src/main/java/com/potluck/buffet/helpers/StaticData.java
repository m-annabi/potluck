package com.potluck.buffet.helpers;


import com.potluck.buffet.domain.model.Item;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "staticdata")
@PropertySource(value = "classpath:staticdata.yml", factory = YamlPropertySourceFactory.class)
public class StaticData {
    private List<Item> items;
    private String test;

    public StaticData() {

    }

    public StaticData(List<Item> items, String test) {
        this.items = items;
        this.test = test;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
