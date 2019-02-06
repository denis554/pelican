package io.pelican

import org.apache.shiro.crypto.hash.Sha256Hash
import io.pelican.common.ShoppingCartStatus
import io.pelican.common.OrderStatus
import io.pelican.common.RoleName
import io.pelican.ShoppingCart
import io.pelican.ShoppingCartItem
import io.pelican.ShoppingCartItemOption
import io.pelican.Transaction
import io.pelican.Account
import io.pelican.Product
import io.pelican.Catalog
import io.pelican.Page
import io.pelican.Role

import io.pelican.log.PageViewLog
import io.pelican.log.ProductViewLog
import io.pelican.log.CatalogViewLog
import io.pelican.log.SearchLog

public class DevelopmentDataSimple {

	def CUSTOMERS_COUNT = 10	
	
	def catalogs = [ 
		[ 
			"name" : "Poker Chips",
			"subcatalogs" : [
				[ 
                    "name" : "Ceramic Poker Chips",
                    "products" : 10
                ],
				[ 
                    "name" : "Clay Poker Chips",
                    "products" : 20
                ],
				[ 
                    "name" : "Composite Poker Chips",
                    "products" : 10
                ]
			]
		]
	]
    
	def layout
	def springSecurityService

	DevelopmentDataSimple(springSecurityService){
		this.springSecurityService = springSecurityService
	}
	
	def init(){
		println "***********************************************"
		println "***       Generating Development Data       ***"
		println "***********************************************"
		
		getSetLayout()
		createAccounts()
		createCatalogs()
		createProducts()
		
		println "***********************************************"
	}


	def getSetLayout(){
		layout = Layout.findByDefaultLayout(true)
	}



	def createAccounts(){

		def customerRole = Role.findByAuthority(RoleName.ROLE_CUSTOMER.description())
		//def password = new Sha256Hash('customer').toHex()
		def password = springSecurityService.encodePassword("password")

		(1..CUSTOMERS_COUNT).each {
			def customer = new Account()
			customer.username = "customer${it}"
			customer.password = password
			customer.name = "John Smith ${it}"
			customer.email = "customer${it}@email.com"

			customer.address1 = "${it} Main Street"
			customer.address2 = "Apt. #${it}"
			customer.city = "Anchorage"
			customer.state = State.findByName("Alaska")
			customer.zip = "99501"
			customer.save(flush:true)

			customer.createAccountRoles(false)
			customer.createAccountPermission()

		}

		println "Accounts : ${Account.count()}"
	}




	def createCatalogs(){
		catalogs.each{ c ->
			def catalog = new Catalog()
			catalog.name = c.name
			catalog.toplevel = true
			catalog.layout = layout
			catalog.save(flush:true)
			if(c.subcatalogs){
				createSubcatalogs(c, catalog)
			}
		}
		
		println "Catalogs : ${Catalog.count()}"
	}
	
	
	
	def createSubcatalogs(catalogData, parentCatalog){
		catalogData.subcatalogs.each { c ->
			def subcatalog = new Catalog()
			subcatalog.name = c.name
			subcatalog.toplevel = false
			subcatalog.layout = layout
			subcatalog.parentCatalog = parentCatalog
			subcatalog.save(flush:true)
			
			parentCatalog.addToSubcatalogs(subcatalog)
			parentCatalog.save(flush:true)
			
			if(c.subcatalogs){
				createSubcatalogs(c, subcatalog)
			}
		}
	}
	
	
	
	def createProducts(){
		catalogs.each { c ->
			def catalog = Catalog.findByName(c.name)
			if(!c.subcatalogs){
				def catalogIdsArray = []
				catalogIdsArray.add(catalog.id)
				createCatalogProducts(catalogIdsArray, c.products)
			}else{
				createSubcatalogProducts(c, catalog)
			}
		}
		println "Products : ${Product.count()}"
	}
	
	
	
	
	def createSubcatalogProducts(catalogData, parentCatalog){
		catalogData.subcatalogs.each { c ->
			def catalog = Catalog.findByName(c.name)
			if(c.subcatalogs){
				createSubcatalogProducts(c, catalog)
			}else{
				def ids = getCatalogIdsArray(catalog)
				def catalogIdsArray = ids.split(',').collect{it as int}
				createCatalogProducts(catalogIdsArray, c.products)
			}
		}
	}
	
	
	def createCatalogProducts(catalogIdsArray, numberProducts){
        if(numberProducts > 0){
            (1..numberProducts).each{ i ->
    			def product = new Product()
    			product.price = i * 10
				if(i % 3 == 0){
					product.salesPrice = i * 10 - 7
				}
    			product.quantity = i * 10
    			product.weight = 16
				product.layout = layout
    			catalogIdsArray.each {
    				def cc = Catalog.get(it)
    				product.addToCatalogs(cc)
    			}
    			def lastCatalogId = catalogIdsArray[catalogIdsArray.size() - 1 ]
    			def lastCatalog = Catalog.get(lastCatalogId)
    			product.name = "${lastCatalog.name} ${i}"
			
    			product.save(flush:true)
    		}
        }
	}


    def createProductOptions(){
		
		productOptions.each() { po ->

			def catalog = Catalog.findByName(po.catalog)
			if(catalog){
				def products = Product.createCriteria().list(){
					catalogs {
				    	idEq(catalog.id)
				 	}
				}
				products.each(){ p ->
					def productOption = new ProductOption()
					productOption.name = po.name
					productOption.product = p
					productOption.save(flush:true)
					
					po.variants.each() { v -> 
						def variant = new Variant()
						variant.name = v.name
						variant.price = v.price
						variant.productOption = productOption
						variant.save(flush:true)
						
						productOption.addToVariants(variant)
						productOption.save(flush:true)
					}
					
					p.addToProductOptions(productOption)
					p.save(flush:true)
				}
			}
		}
		
		println "Product Options : ${ProductOption.count()}"
		println "Option Variants : ${Variant.count()}"
	}
	
	
	
    def createSpecifications(){
        specifications.eachWithIndex() { specificationObj, specificationPosition ->
            def specification = new Specification()
            specification.name = specificationObj.name
            specification.filterName = specificationObj.name.replaceAll(" ", "_").toLowerCase()
            specification.position = specificationPosition
            specification.save(flush:true)
        
            specificationObj.catalogs.each { catalogName ->
                def catalog = Catalog.findByName(catalogName)
                specification.addToCatalogs(catalog)
                specification.save(flush:true)
            }
            
            specificationObj.options.eachWithIndex(){ optionName, optionPosition ->
                def option = new SpecificationOption()
                option.name = optionName
                option.specification = specification
                option.position = optionPosition
                option.save(flush:true)
                
                specification.addToSpecificationOptions(option)
                specification.save(flush:true)                
            }
        }
		println "Specifications : ${Specification.count()}"
		println "SpecificationOptions : ${SpecificationOption.count()}"
    }
	

    
    
    def createProductSpecifications(){
		Random rand = new Random()
        specifications.each{ specificationObj ->
            def specification = Specification.findByName(specificationObj.name)
            def specificationCatalogs = specificationObj.catalogs

            def ids = []
            specificationCatalogs.each { catalogName ->
                def catalog = Catalog.findByName(catalogName)
                if(catalog){
                    ids.add(catalog.id)
                }
            }
            
            def products = Product.createCriteria().list{
                catalogs{
                    'in'('id', ids)
                }
            }
            
            products.unique { it.id }
            
            if(products){
                products.each { product ->
    			    int index = rand.nextInt(specificationObj.options.size())
                    
                    def optionName = specificationObj.options.get(index)
                    def option = SpecificationOption.findByName(optionName)
                    
                    def existing = ProductSpecification.findAllByProductAndSpecificationAndSpecificationOption(product, specification, option)
                    if(!existing){
                        def productSpecification = new ProductSpecification()
                        productSpecification.specificationOption = option
                        productSpecification.product = product
                        productSpecification.specification = specification
                        productSpecification.save(flush:true)
                    }
                }
            }
        }  
            
		println "ProductSpecifications : ${ProductSpecification.count()}"
    }
    
	
	def getCatalogIdsArray(catalog){
		def ids = new StringBuffer()
		ids.append(catalog.id)
		if(catalog.parentCatalog){
			ids.insert(0, getCatalogIdsArray(catalog.parentCatalog) + ",")
		}
		return ids.toString()
	}
	
	
}