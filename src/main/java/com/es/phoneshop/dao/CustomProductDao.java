package com.es.phoneshop.dao;

import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomProductDao implements ProductDao {

    private static CustomProductDao instance;
    private final Lock readLock;
    private final Lock writeLock;
    private List<Product> productList;

    private CustomProductDao() {
        productList = createSampleProducts();
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public static CustomProductDao getInstance() {
        if (instance == null) {
            synchronized (CustomProductDao.class) {
                if (instance == null) {
                    instance = new CustomProductDao();
                }
            }
        }
        return instance;
    }

    private record ProductDescriptionMatch(Product product, int productDescriptionWordsCount,
                                           int matchingWordsCount) {
    }

    private List<Product> createSampleProducts() {
        List<Product> result = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        result.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        result.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        result.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        result.add(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        result.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        result.add(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        result.add(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        result.add(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        result.add(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        result.add(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        result.add(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        result.add(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        result.add(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
        return result;
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        if (id != null) {
            readLock.lock();
            try {
                return productList.stream()
                        .filter(product -> id.equals(product.getId()))
                        .findAny();
            } finally {
                readLock.unlock();
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findProducts() {
        readLock.lock();
        try {
            return productList.stream()
                    .filter(product -> product.getPrice() != null && product.getStock() > 0)
                    .toList();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Product> findProductsByDescription(String description) {
        if (description == null || description.isBlank()) {
            return findProducts();
        }
        readLock.lock();
        try {
            List<String> descriptionWords = Arrays.asList(description.split(" "));
            return findProducts().stream()
                    .map(product -> createProductDescriptionMatch(product, descriptionWords))
                    .filter(productDescriptionMatch -> productDescriptionMatch.matchingWordsCount > 0)
                    .sorted(Comparator.comparing(ProductDescriptionMatch::matchingWordsCount).reversed().thenComparing(ProductDescriptionMatch::productDescriptionWordsCount))
                    .map(productDescriptionMatch -> productDescriptionMatch.product)
                    .toList();
        } finally {
            readLock.unlock();
        }
    }

    private ProductDescriptionMatch createProductDescriptionMatch(Product product, List<String> descriptionWords) {
        List<String> productDescriptionWords = Arrays.asList(product.getDescription().split(" "));
        int matchingWordsCount = (int) descriptionWords.stream()
                .distinct()
                .filter(productDescriptionWords::contains)
                .count();
        return new ProductDescriptionMatch(product, productDescriptionWords.size(), matchingWordsCount);
    }

    @Override
    public List<Product> findProductsByDescriptionWithOrdering(String description, Comparator<Product> comparator) {
        return findProductsByDescription(description).stream()
                .sorted(comparator)
                .toList();
    }

    @Override
    public void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product was null");
        }
        writeLock.lock();
        try {
            Optional<Product> productWithSameId = getProduct(product.getId());
            if (productWithSameId.isPresent()) {
                int productWithSameIdIndex = productList.indexOf(productWithSameId.get());
                productList.set(productWithSameIdIndex, product);
            } else {
                productList.add(product);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id was null");
        }
        writeLock.lock();
        try {
            if (!productList.removeIf(product -> id.equals(product.getId()))) {
                throw new ProductNotFoundException(id);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
