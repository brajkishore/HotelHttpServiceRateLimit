/**
 * 
 */
package com.hotel.models;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.hotel.utils.CommonUtil;

/**
 * @author braj.kishore
 *
 */

@Component("appHolder")
public class AppHolder implements InitializingBean {

	private ConcurrentMap<String,RateLimitter> apiKeysRateLimitters;
	private List<Hotel> hotels;

	/**
	 * @return the apiKeysRateLimitters
	 */
	public ConcurrentMap<String, RateLimitter> getApiKeysRateLimitters() {
		return apiKeysRateLimitters;
	}

	/**
	 * @param apiKeysRateLimitters the apiKeysRateLimitters to set
	 */
	public void setApiKeysRateLimitters(ConcurrentMap<String, RateLimitter> apiKeysRateLimitters) {
		this.apiKeysRateLimitters = apiKeysRateLimitters;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
		apiKeysRateLimitters=new ConcurrentHashMap<>();
		
		List<Hotel> hotels=CommonUtil.getHotelsFromCSV();
		setHotels(hotels);
	}

	/**
	 * @return the hotels
	 */
	public List<Hotel> getHotels() {
		return hotels;
	}

	/**
	 * @param hotels the hotels to set
	 */
	public void setHotels(List<Hotel> hotels) {
		this.hotels = hotels;
	}
	
}
