/**
 * 
 */
package com.hotel.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.models.AppHolder;
import com.hotel.models.Hotel;
import com.hotel.models.RateLimitter;
import com.hotel.models.ResponseJson;
import com.hotel.utils.CommonUtil;


/**
 * @author braj.kishore
 *
 */
@RestController
@EnableConfigurationProperties
public class HotelController {

	@Resource(name="appHolder")
	private AppHolder appHolder;
	
	@Value("${global.perRequestIntervalInMillis}")
	Long perRequestIntervalInMillis;

	@Value("${global.suspensionIntervalInMillis}")
	Long suspensionIntervalInMillis;

	@Value("${global.maxLimit}")
	int maxLimit;
	
	
	@RequestMapping(value="/",
			method=RequestMethod.GET)	
	public ResponseEntity<?> welcome(){
			return ResponseEntity.ok().body("Welcome to Agoda !!");
	}
	
	
	@RequestMapping(value="/hotels",
			method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<ResponseJson> getAllHotels(){
		
		List<Hotel> hotels=appHolder.getHotels();
		ResponseJson json=new ResponseJson();
		if(hotels==null || hotels.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json.setStatusCode(HttpStatus.NOT_FOUND.value()).setMessage("No hotels"));
		
			return ResponseEntity.status(HttpStatus.OK).body(json.setStatusCode(HttpStatus.OK.value()).setData(hotels).setMessage("Success"));
	}
	
	@RequestMapping(value="/apiKeys/{apiKey}",
			method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<ResponseJson> deleteApiKey(@PathVariable("apiKey") String apiKey){
		
		ResponseJson json=new ResponseJson();
		if(appHolder.getApiKeysRateLimitters().containsKey(apiKey))
		{
			appHolder.getApiKeysRateLimitters().remove(apiKey);
			return ResponseEntity.status(HttpStatus.OK).body(json.setStatusCode(HttpStatus.OK.value()).setMessage("Success"));
		}
	
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json.setStatusCode(HttpStatus.NOT_FOUND.value()).setMessage("No such api key"));
	}
	
	
	@RequestMapping(value="/hotels/{apiKey}/{cityId}",
			method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<ResponseJson> getHotels(@PathVariable("apiKey") String apiKey,
			@PathVariable("cityId") long cityId,
			@RequestParam(value="sort",required=false,defaultValue="ASC") String sortOrder){
				
		
		ResponseJson json=new ResponseJson();
		RateLimitter limitter=appHolder.getApiKeysRateLimitters().get(apiKey);
		
		
		if(limitter!=null){
			
			/*
			 * Check if request is first time since instance is up
			 */
			
			if(!limitter.getIsUsed().get()){
				/*
				 * First time request, send a valid response
				 */	
				limitter.getIsUsed().set(true);				
				return getHotelList(cityId, sortOrder,limitter);
			}
			/*
			 * Get difference between last request and current
			 */
			
			Long diffOfLastTime = System.currentTimeMillis() - limitter.getLastUpdatedOn().get();
			/*
			 * Check if requests are within max limit defined	
			 */			
			if (limitter.getCurrentCount().get()+1 <= limitter.getMaxLimit()){ 					
					
				if (diffOfLastTime <= limitter.getPerRequestIntervalInMillis())					
					return exceedMessageLimitPerInterval(limitter);
				else 
					return getHotelList(cityId, sortOrder,limitter);								
			}
			else{
				if (diffOfLastTime <= limitter.getSuspensionIntervalInMillis()) 					
					return suspensionMessage(limitter);					
				 else {								
					 limitter.getCurrentCount().set(0);
					 return getHotelList(cityId, sortOrder,limitter);
				 }
				
			}
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(json.setStatusCode(HttpStatus.FORBIDDEN.value()).setMessage("Invalid api key"));
	}
		
	
	private ResponseEntity<ResponseJson> suspensionMessage(RateLimitter limitter) {
		// TODO Auto-generated method stub
		ResponseJson json=new ResponseJson();
		return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body(json.setStatusCode(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value()).setMessage("Api key is suspended for next "+
				(limitter.getSuspensionIntervalInMillis()/1000)+" seconds"));
		
	}


	private ResponseEntity<ResponseJson> exceedMessageLimitPerInterval(RateLimitter limitter) {
		// TODO Auto-generated method stub
		ResponseJson json=new ResponseJson();
		return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).
				body(json.setStatusCode(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value()).
						setMessage("Api key is blocked due to max requests in interval of "+(limitter.getPerRequestIntervalInMillis()/1000)+" seconds"));
		
	}


	private ResponseEntity<ResponseJson> getHotelList(long cityId, String sortOrder, RateLimitter limitter) {
		// TODO Auto-generated method stub
		List<Hotel> hotels=CommonUtil.getHotelsByCityIdInOrder(appHolder.getHotels(),cityId,sortOrder);
		
		ResponseJson json=new ResponseJson();
		
		if(hotels==null || hotels.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json.setStatusCode(HttpStatus.NOT_FOUND.value()).setMessage("No hotels"));
		
		limitter.getIsUsed().set(true);
		limitter.getCurrentCount().incrementAndGet();
		limitter.getLastUpdatedOn().set(System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.OK).body(json.setStatusCode(HttpStatus.OK.value()).setData(hotels).setMessage("Success"));
	}
		
	
	@RequestMapping(value="/apiKeys",method=RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseJson> generateAPIKey(@RequestBody RateLimitter rateLimitter) {
		
			ResponseJson json=new ResponseJson();
			if(rateLimitter.getApiKey()==null||rateLimitter.getApiKey().isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json.setStatusCode(HttpStatus.BAD_REQUEST.value()).setMessage("Invalid request"));
				
			
			if(appHolder.getApiKeysRateLimitters().containsKey(rateLimitter.getApiKey()))
				return ResponseEntity.status(HttpStatus.OK).body(json.setStatusCode(HttpStatus.OK.value()).setData(appHolder.getApiKeysRateLimitters().get(rateLimitter.getApiKey()))
						.setMessage("Already exists"));
			
		
			if(rateLimitter.getMaxLimit()==0L)
				rateLimitter.setMaxLimit(maxLimit);
			if(rateLimitter.getPerRequestIntervalInMillis()==0L)
				rateLimitter.setPerRequestIntervalInMillis(perRequestIntervalInMillis);
			if(rateLimitter.getSuspensionIntervalInMillis()==0L)
				rateLimitter.setSuspensionIntervalInMillis(suspensionIntervalInMillis);
			
			rateLimitter.setIsUsed(new AtomicBoolean());				
			rateLimitter.getIsUsed().set(false);
			
			rateLimitter.setLastUpdatedOn(new AtomicLong());
			rateLimitter.getLastUpdatedOn().set(0L);
			
			rateLimitter.setCurrentCount(new AtomicInteger());
			rateLimitter.getCurrentCount().set(0);
			

			appHolder.getApiKeysRateLimitters().put(rateLimitter.getApiKey(), rateLimitter);			
						
			return ResponseEntity.status(HttpStatus.OK).body(json.setStatusCode(HttpStatus.OK.value()).setData(rateLimitter)
					.setMessage("Success"));
		
			
	}
	
}

	
