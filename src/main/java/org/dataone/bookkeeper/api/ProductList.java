/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A list of products used as a representation response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductList extends BaseList {

    private List<Product> products;

    /**
     * Construct an empty product list
     */
    public ProductList(List<Product> products) {
        this.products = products;
    }

    /**
     * Get the products list
     * @return products  the products list
     */
    @JsonProperty
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Set the products list
     * @param products  the products list
     */
    @JsonProperty
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
