package io.pelican.api.payment

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.ValidationError;
import grails.util.Environment

import io.pelican.api.payment.PaymentCharge

class BraintreePaymentsProcessor implements PaymentProcessor {
	
	def applicationService
	def currencyService
	
	BraintreePaymentsProcessor(applicationService, currencyService){
		this.applicationService = applicationService
		this.currencyService = currencyService
	}


	def charge(amount, nonce, account){
		try{
			println "btp -> " + amount
			def total = applicationService.formatTotal(amount)
			TransactionRequest request = new TransactionRequest().amount(total).paymentMethodNonce(nonce).options().submitForSettlement(true).done();
			
			def gateway = getGateway()
			
			if(gateway){
				
		  		Result<Transaction> result = gateway.transaction().sale(request);
		  		
				if (result.isSuccess()) {
		    		// See result.getTarget() for details
					print result.getTarget()
					
					result?.properties.each { 
						println "$it.key -> $it.value" 
					}

					result.target?.properties.each { 
						println "$it.key -> $it.value" 
					}
					
					def paymentCharge = new PaymentCharge()
					paymentCharge.gateway = PaymentCharge.BRAINTREE
					paymentCharge.id = result.getTarget().id
					
					return paymentCharge
					
		  		} else {

					result.properties.each{
						println "$it.key -> $it.value" 
					}
					result.getTarget().properties.each { 
						println "$it.key -> $it.value" 
					}
					
					throw new Exception("Something went wrong while processing checkout... please contact support")
		  		}
			
			}else{
				throw new Exception("Payment processor did not initialize correctly...")
			}
		  
	  	}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	
	
	def refund(transactionId){
		def gateway = getGateway()
		def braintreeRefundedCharge = gateway.transaction().refund(transactionId)

		braintreeRefundedCharge?.properties.each { 
			println "$it.key -> $it.value" 
		}
		
		if(!braintreeRefundedCharge.target){
			throw new Exception("Something went wrong while processing refund on Braintree. Make sure transaction is settled : " + transactionId)
		}
		
		def refundedCharge = new RefundedCharge()
		refundedCharge.id = braintreeRefundedCharge?.getTarget().id
		
		return refundedCharge
	}
	
	
	def getGateway(){
		def environment = "sandbox"
		if(Environment.current == Environment.PRODUCTION)environment = "production"
		
		def merchantId = applicationService.getBraintreeMerchantId()
		def publicKey = applicationService.getBraintreePublicKey()
		def privateKey = applicationService.getBraintreePrivateKey()
		
		println merchantId + " : " + publicKey + " : " + privateKey
		
		def gateway = new BraintreeGateway(environment, merchantId, publicKey, privateKey)
		
		
		
		return gateway
	}
	
}
