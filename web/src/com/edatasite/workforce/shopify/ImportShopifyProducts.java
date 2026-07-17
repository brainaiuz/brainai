package com.edatasite.workforce.shopify;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductListItemTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImportShopifyProducts {

    static RestTemplate restTemplate = new RestTemplate();
    static String SHOPIFY_PRODUCTS_URL = "https://wealcan-estore.myshopify.com/admin/api/2019-07/products.json?limit=250&since_id=%s";//API_KEY
    static String SHOPIFY_INVENTORIES_URL = "https://wealcan-estore.myshopify.com/admin/api/2019-07/inventory_items.json?limit=250&ids=%s";//API_KEY
    static String SHOPIFY_USERNAME = "f8d20adc06eb2bfabeadc0f6dda7ca6a";//API_KEY
    static String SHOPIFY_PASSWORD = "c3745ba0806d7771fb967b846be4aa4e";//PASSWORD
    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        /*String json = "{admin_graphql_api_id: gid://shopify/ProductVariant/29652828061740\nbarcode: 4042809015553\ncompare_at_price: None\ncreated_at: 2019-08-21T11:28:57-05:00\nfulfillment_service: wealcan-lvks\ngrams: 272\nid: 29652828061740\nimage_id: None\ninventory_item_id: 30912149979180\ninventory_management: shopify\ninventory_policy: continue\ninventory_quantity: 6\nold_inventory_quantity: 6\noption1: S/M 20\"\noption2: None\noption3: None\nposition: 1\nprice: 0.07\nproduct_id: 3986070601772\nrequires_shipping: True\nsku: 7281908\ntaxable: True\ntitle: S/M 20\"\nupdated_at: 2019-08-21T11:28:57-05:00\nweight: 0.6\nweight_unit: lb\n\nadmin_graphql_api_id: gid://shopify/ProductVariant/29652828094508\nbarcode: 4042809015584\ncompare_at_price: None\ncreated_at: 2019-08-21T11:28:58-05:00\nfulfillment_service: wealcan-lvks\ngrams: 272\nid: 29652828094508\nimage_id: None\ninventory_item_id: 30912150011948\ninventory_management: shopify\ninventory_policy: continue\ninventory_quantity: 18\nold_inventory_quantity: 18\noption1: M/L 20\"\noption2: None\noption3: None\nposition: 2\nprice: 0.10\nproduct_id: 3986070601772\nrequires_shipping: True\nsku: 7281909\ntaxable: True\ntitle: M/L 20\"\nupdated_at: 2019-08-21T11:28:58-05:00\nweight: 0.6\nweight_unit: lb\n\nadmin_graphql_api_id: gid://shopify/ProductVariant/29652828127276\nbarcode: 4042809015614\ncompare_at_price: None\ncreated_at: 2019-08-21T11:28:58-05:00\nfulfillment_service: wealcan-lvks\ngrams: 272\nid: 29652828127276\nimage_id: None\ninventory_item_id: 30912150044716\ninventory_management: shopify\ninventory_policy: continue\ninventory_quantity: 24\nold_inventory_quantity: 24\noption1: S/M 24\"\noption2: None\noption3: None\nposition: 3\nprice: 0.11\nproduct_id: 3986070601772\nrequires_shipping: True\nsku: 7281910\ntaxable: True\ntitle: S/M 24\"\nupdated_at: 2019-08-21T11:28:58-05:00\nweight: 0.6\nweight_unit: lb\n\nadmin_graphql_api_id: gid://shopify/ProductVariant/29652828160044\nbarcode: 4042809015645\ncompare_at_price: None\ncreated_at: 2019-08-21T11:28:58-05:00\nfulfillment_service: wealcan-lvks\ngrams: 272\nid: 29652828160044\nimage_id: None\ninventory_item_id: 30912150110252\ninventory_management: shopify\ninventory_policy: continue\ninventory_quantity: 39\nold_inventory_quantity: 39\noption1: M/L 24\"\noption2: None\noption3: None\nposition: 4\nprice: 0.12\nproduct_id: 3986070601772\nrequires_shipping: True\nsku: 7281911\ntaxable: True\ntitle: M/L 24\"\nupdated_at: 2019-08-21T11:28:58-05:00\nweight: 0.6\nweight_unit: lb\n\nadmin_graphql_api_id: gid://shopify/ProductVariant/29652828192812\nbarcode: 4042809015676\ncompare_at_price: None\ncreated_at: 2019-08-21T11:28:58-05:00\nfulfillment_service: wealcan-lvks\ngrams: 272\nid: 29652828192812\nimage_id: None\ninventory_item_id: 30912150143020\ninventory_management: shopify\ninventory_policy: continue\ninventory_quantity: 49\nold_inventory_quantity: 49\noption1: S/M 28\"\noption2: None\noption3: None\nposition: 5\nprice: 0.08\nproduct_id: 3986070601772\nrequires_shipping: True\nsku: 7281912\ntaxable: True\ntitle: S/M 28\"\nupdated_at: 2019-08-21T11:28:58-05:00\nweight: 0.6\nweight_unit: lb\n\nadmin_graphql_api_id: gid://shopify/ProductVariant/29652828225580\nbarcode: 4042809015706\ncompare_at_price: None\ncreated_at: 2019-08-21T11:28:58-05:00\nfulfillment_service: wealcan-lvks\ngrams: 272\nid: 29652828225580\nimage_id: None\ninventory_item_id: 30912150208556\ninventory_management: shopify\ninventory_policy: continue\ninventory_quantity: 59\nold_inventory_quantity: 59\noption1: M/L 28\"\noption2: None\noption3: None\nposition: 6\nprice: 0.09\nproduct_id: 3986070601772\nrequires_shipping: True\nsku: 7281913\ntaxable: True\ntitle: M/L 28\"\nupdated_at: 2019-08-21T11:28:58-05:00\nweight: 0.6\nweight_unit: lb}";
        try {
            ShopifyProductVariant shopifyProduct = objectMapper.readValue(json, ShopifyProductVariant.class);
            System.out.println(shopifyProduct);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(1);*/

        //Retrieve all products from shopify
        List<ShopifyProduct> allProducts = getAllShopifyProducts();


        fillInventoryCostPrice(allProducts);
        /*String updateSQL = "UPDATE \"73042\".item SET unitPrice=%s WHERE zapiervariantid=%s;";
        for (ShopifyProduct product : allProducts) {
            if (product.getVariants() != null) {
                if (product.getVariants().size() > 1) {
                    for (ShopifyProductVariant variant : product.getVariants()) {
                        if (variant.getCost() != null) {
                            System.out.println(String.format(updateSQL, variant.getCost(), variant.getId()));
                        }
                    }
                } else if (product.getVariants().size() == 1) {
                    //@TODO we need to handle single variant items
                }
            }
        }*/
        //Import Single Product
//        createProductOnKPI(allProducts.get(0));
        //Import All Products
        importShopifyProducts(allProducts);

    }

    static void importShopifyProducts(List<ShopifyProduct> allProducts) {
        long start = System.currentTimeMillis();
        int variantscount = 0;
        int defaultvariantscount = 0;

        if (allProducts != null) {
            int i = 1;
            for (ShopifyProduct product : allProducts) {
                System.out.println(i + " - " + product.getTitle() + " Variants Count = " + product.getVariants().size());
//                if(product.getVariants().size()==1) {
                //@TODO commented below to check logs
                createProductOnKPI(product);

                if (product.getVariants().size() > 1) {
                    variantscount = variantscount + product.getVariants().size();
                } else {
                    defaultvariantscount++;
                }
//                }
                i++;
            }
        }
        System.out.println("All product variants took: " + variantscount + " time=" + (System.currentTimeMillis() - start) + "ms \n\n\n\n");
        System.out.println("All Default product variants took: " + defaultvariantscount + " time=" + (System.currentTimeMillis() - start) + "ms \n\n\n\n");
    }

    static List<ShopifyProduct> getAllShopifyProducts() {
        long start = System.currentTimeMillis();
        //Get All Products from Shopify
        List<ShopifyProduct> allProducts = getProducts(0L);

        System.out.println("Loading all products took: " + (System.currentTimeMillis() - start) + "ms \n\n\n\n");
        return allProducts;
    }

    static List<ShopifyProduct> getProducts(Long since) {

        ResponseEntity<ShopifyProductList> productList = restTemplate.exchange(String.format(SHOPIFY_PRODUCTS_URL, since),
                HttpMethod.GET,
                new HttpEntity<>(
                        createHeaders(SHOPIFY_USERNAME, SHOPIFY_PASSWORD)
                ),
                ShopifyProductList.class);

        if (productList.getStatusCode() == HttpStatus.OK) {

            ArrayList<ShopifyProduct> result = new ArrayList<>(productList.getBody().getProducts());

            if (productList.getBody().getProducts().size() >= 250) {
                //Recursivly retrieve others
                result.addAll(getProducts(productList.getBody().getProducts().get(productList.getBody().getProducts().size() - 1).getId()));
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    static void fillInventoryCostPrice(List<ShopifyProduct> products) {

        for (ShopifyProduct shopifyProduct : products) {
            if (shopifyProduct.getVariants() != null) {

                String ids = shopifyProduct.getVariants().stream()
                        .filter(shopifyProductVariant -> shopifyProductVariant.getInventory_item_id() != null && shopifyProductVariant.getInventory_item_id() > 0
                        ).map(v -> v.getInventory_item_id().toString()).collect(Collectors.joining(","));

                //If there are inventory type variants
                if (StringUtils.isNotBlank(ids)) {
                    ResponseEntity<ShopifyInventoryList> inventoryList = restTemplate.exchange(String.format(SHOPIFY_INVENTORIES_URL, ids),
                            HttpMethod.GET,
                            new HttpEntity<>(
                                    createHeaders(SHOPIFY_USERNAME, SHOPIFY_PASSWORD)
                            ),
                            ShopifyInventoryList.class);

                    if (inventoryList.getStatusCode() == HttpStatus.OK && inventoryList.getBody().inventory_items != null) {

                        Map<Long, BigDecimal> inventoryCosts = inventoryList.getBody().inventory_items.stream().filter(i -> i.getCost() != null).collect(Collectors.toMap(ShopifyInventory::getId, ShopifyInventory::getCost));

                        shopifyProduct.getVariants().forEach(v -> {
                            if (inventoryCosts.get(v.getInventory_item_id()) != null) {
                                v.setCost(inventoryCosts.get(v.getInventory_item_id()).toString());
                            }
                        });
                    }
                }
            }

        }

    }

    static HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    static ProductListItemTO convertProduct(ShopifyProduct shopifyProduct) {
        ProductListItemTO productListItemTO = new ProductListItemTO();
        productListItemTO.setName(shopifyProduct.getTitle());
        productListItemTO.setDescription(shopifyProduct.getBody_html());
        productListItemTO.setVendor(shopifyProduct.getVendor());
        productListItemTO.setCategories(shopifyProduct.getProduct_type());

        if (shopifyProduct.getVariants() != null) {
            if (shopifyProduct.getVariants().size() > 1) {
                try {
                    StringBuilder variants = new StringBuilder();
                    for (ShopifyProductVariant variant : shopifyProduct.getVariants()) {
                        variants.append("\n\n");
                        variants.append("id:").append(variant.getId()).append("\n");
                        if (StringUtils.isNotBlank(variant.getPrice())) {
                            variants.append("price:").append(variant.getPrice()).append("\n");
                        }
                        if (StringUtils.isNotBlank(variant.getCost())) {
                            variants.append("cost:").append(variant.getCost()).append("\n");
                        }
                        if (StringUtils.isNotBlank(variant.getSku())) {
                            variants.append("sku:").append(variant.getSku()).append("\n");
                        }
                        variants.append("title:").append(variant.getTitle()).append("\n");
                        if (StringUtils.isNotBlank(variant.getBarcode())) {
                            variants.append("barcode:").append(variant.getBarcode()).append("\n");
                        }
                        if (variant.getInventory_quantity() != null) {
                            variants.append("inventory_quantity:").append(variant.getInventory_quantity());
                        }
                    }

                    productListItemTO.setVariants(variants.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (shopifyProduct.getVariants().size() == 1
                    && StringUtils.isNotBlank(shopifyProduct.getVariants().get(0).getTitle()) && shopifyProduct.getVariants().get(0).getTitle().startsWith("Default")) {

                ShopifyProductVariant variant = shopifyProduct.getVariants().get(0);

                productListItemTO.setSku_number(variant.getSku());
                productListItemTO.setBarcode(variant.getBarcode());

                try {
                    productListItemTO.setUnit_price(new BigDecimal(variant.getPrice()));
                } catch (Exception e) {
                    e.printStackTrace();
                    productListItemTO.setUnit_price(new BigDecimal("0"));
                }

                try {
                    productListItemTO.setQuantity(new BigDecimal(variant.getInventory_quantity()));
                } catch (Exception e) {
                    e.printStackTrace();
                    productListItemTO.setQuantity(new BigDecimal("0"));
                }
            }
        }

        return productListItemTO;
    }

    static String createProductOnKPI(ShopifyProduct shopifyProduct) {
        //Convert Product
        ProductListItemTO kpiProduct = convertProduct(shopifyProduct);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        //DEV
        httpHeaders.set("x-auth", "FREE$73042$1534DFB802D74639");
        httpHeaders.set("accessToken", "3940593409603496503490593409");
        //LOCAL
//        httpHeaders.set("x-auth", "FREE$73042$D6D4FF2A0C2F306A");
//        httpHeaders.set("accessToken", "3940593409603496503490593409");

        HttpEntity<ProductListItemTO> httpRequest = new HttpEntity<ProductListItemTO>(kpiProduct, httpHeaders);


        String resp = restTemplate.postForObject("https://dev.kpi.com/services/api/v2/2/product/create_zapier", httpRequest, String.class);
//        String resp = restTemplate.postForObject("http://localhost:8080/services/api/v2/2/product/create_zapier", httpRequest, String.class);
        try {
            System.out.println(objectMapper.writeValueAsString(resp));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resp;
    }
}
