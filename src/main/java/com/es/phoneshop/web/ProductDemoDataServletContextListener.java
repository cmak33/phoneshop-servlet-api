package com.es.phoneshop.web;

import com.es.phoneshop.dao.CustomProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.PriceChange;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class ProductDemoDataServletContextListener implements ServletContextListener {

    private ProductDao productDao;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        boolean shouldInsertData = Boolean.parseBoolean(servletContextEvent.getServletContext().getInitParameter("insertDemoData"));
        if (shouldInsertData) {
            productDao = CustomProductDao.getInstance();
            insertDataToProductDao();
        }
    }

    private void insertDataToProductDao() {
        createSampleProducts().forEach(productDao::save);
    }

    private List<Product> createSampleProducts() {
        List<Product> result = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        result.add(new Product.ProductBuilder()
                .setCode("sgs")
                .setDescription("Samsung Galaxy S")
                .setPrice(new BigDecimal(100))
                .setCurrency(usd)
                .setStock(100)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2022, Calendar.JANUARY, 1), BigDecimal.valueOf(150)));
                    add(new PriceChange(createDate(2023, Calendar.MARCH, 23), BigDecimal.valueOf(100)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("sgs2")
                .setDescription("Samsung Galaxy S II")
                .setPrice(new BigDecimal(200))
                .setCurrency(usd)
                .setStock(0)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2019, Calendar.JANUARY, 22), BigDecimal.valueOf(150)));
                    add(new PriceChange(createDate(2023, Calendar.MARCH, 23), BigDecimal.valueOf(200)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("sgs3")
                .setDescription("Samsung Galaxy S III")
                .setPrice(new BigDecimal(300))
                .setCurrency(usd)
                .setStock(5)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2019, Calendar.JANUARY, 12), BigDecimal.valueOf(150)));
                    add(new PriceChange(createDate(2023, Calendar.MARCH, 21), BigDecimal.valueOf(300)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("iphone")
                .setDescription("Apple iPhone")
                .setPrice(new BigDecimal(200))
                .setCurrency(usd)
                .setStock(10)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2015, Calendar.JANUARY, 12), BigDecimal.valueOf(150)));
                    add(new PriceChange(createDate(2023, Calendar.MARCH, 20), BigDecimal.valueOf(250)));
                    add(new PriceChange(createDate(2023, Calendar.MARCH, 21), BigDecimal.valueOf(200)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("iphone6")
                .setDescription("Apple iPhone 6")
                .setPrice(new BigDecimal(1000))
                .setCurrency(usd)
                .setStock(10)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2011, Calendar.APRIL, 12), BigDecimal.valueOf(150)));
                    add(new PriceChange(createDate(2023, Calendar.APRIL, 21), BigDecimal.valueOf(1000)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("htces4g")
                .setDescription("HTC EVO Shift 4G")
                .setPrice(new BigDecimal(320))
                .setCurrency(usd)
                .setStock(123)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2018, Calendar.DECEMBER, 1), BigDecimal.valueOf(150)));
                    add(new PriceChange(createDate(2023, Calendar.APRIL, 21), BigDecimal.valueOf(320)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("sec901")
                .setDescription("Sony Ericsson C901")
                .setPrice(new BigDecimal(420))
                .setCurrency(usd)
                .setStock(300)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2018, Calendar.DECEMBER, 11), BigDecimal.valueOf(1500)));
                    add(new PriceChange(createDate(2023, Calendar.APRIL, 21), BigDecimal.valueOf(420)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("xperiaxz")
                .setDescription("Sony Xperia XZ")
                .setPrice(new BigDecimal(120))
                .setCurrency(usd)
                .setStock(100)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2020, Calendar.SEPTEMBER, 11), BigDecimal.valueOf(150)));
                    add(new PriceChange(createDate(2022, Calendar.SEPTEMBER, 21), BigDecimal.valueOf(120)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("nokia3310")
                .setDescription("Nokia 3310")
                .setPrice(new BigDecimal(70))
                .setCurrency(usd)
                .setStock(100)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2020, Calendar.SEPTEMBER, 11), BigDecimal.valueOf(250)));
                    add(new PriceChange(createDate(2022, Calendar.SEPTEMBER, 21), BigDecimal.valueOf(100)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("palmp")
                .setDescription("Palm Pixi")
                .setPrice(new BigDecimal(170))
                .setCurrency(usd)
                .setStock(30)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2020, Calendar.SEPTEMBER, 11), BigDecimal.valueOf(250)));
                    add(new PriceChange(createDate(2022, Calendar.SEPTEMBER, 21), BigDecimal.valueOf(170)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("simc56")
                .setDescription("Siemens C56")
                .setPrice(new BigDecimal(70))
                .setCurrency(usd)
                .setStock(20)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2020, Calendar.SEPTEMBER, 11), BigDecimal.valueOf(50)));
                    add(new PriceChange(createDate(2022, Calendar.SEPTEMBER, 21), BigDecimal.valueOf(70)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("simc61")
                .setDescription("Siemens C61")
                .setPrice(new BigDecimal(80))
                .setCurrency(usd)
                .setStock(30)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2020, Calendar.SEPTEMBER, 11), BigDecimal.valueOf(50)));
                    add(new PriceChange(createDate(2022, Calendar.SEPTEMBER, 11), BigDecimal.valueOf(80)));
                }})
                .build());
        result.add(new Product.ProductBuilder()
                .setCode("simsxg75")
                .setDescription("Siemens SXG75")
                .setPrice(new BigDecimal(150))
                .setCurrency(usd)
                .setStock(40)
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg")
                .setPriceHistory(new ArrayList<>() {{
                    add(new PriceChange(createDate(2020, Calendar.SEPTEMBER, 11), BigDecimal.valueOf(250)));
                    add(new PriceChange(createDate(2021, Calendar.OCTOBER, 11), BigDecimal.valueOf(150)));
                }})
                .build());
        return result;
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
}
