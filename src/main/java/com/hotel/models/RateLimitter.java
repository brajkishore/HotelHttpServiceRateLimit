/**
 * 
 */
package com.hotel.models;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author braj.kishore
 *
 */
public class RateLimitter {

	private String apiKey;
	private long maxLimit;
	private long perRequestIntervalInMillis;
	private long suspensionIntervalInMillis;
	private AtomicInteger currentCount;
	private AtomicLong lastUpdatedOn;
	private AtomicBoolean isUsed;
	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}
	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	/**
	 * @return the lastUpdatedOn
	 */
	public AtomicLong getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	/**
	 * @param lastUpdatedOn the lastUpdatedOn to set
	 */
	public void setLastUpdatedOn(AtomicLong lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RateLimitter)) {
			return false;
		}
		RateLimitter other = (RateLimitter) obj;
		if (apiKey == null) {
			if (other.apiKey != null) {
				return false;
			}
		} else if (!apiKey.equals(other.apiKey)) {
			return false;
		}
		return true;
	}
	/**
	 * @return the maxLimit
	 */
	public long getMaxLimit() {
		return maxLimit;
	}
	/**
	 * @param maxLimit the maxLimit to set
	 */
	public void setMaxLimit(long maxLimit) {
		this.maxLimit = maxLimit;
	}
	/**
	 * @return the currentCount
	 */
	public AtomicInteger getCurrentCount() {
		return currentCount;
	}
	/**
	 * @param currentCount the currentCount to set
	 */
	public void setCurrentCount(AtomicInteger currentCount) {
		this.currentCount = currentCount;
	}
	
	
	/**
	 * @return the isUsed
	 */
	public AtomicBoolean getIsUsed() {
		return isUsed;
	}
	/**
	 * @param isUsed the isUsed to set
	 */
	public void setIsUsed(AtomicBoolean isUsed) {
		this.isUsed = isUsed;
	}
	/**
	 * @return the perRequestIntervalInMillis
	 */
	public long getPerRequestIntervalInMillis() {
		return perRequestIntervalInMillis;
	}
	/**
	 * @param perRequestIntervalInMillis the perRequestIntervalInMillis to set
	 */
	public void setPerRequestIntervalInMillis(long perRequestIntervalInMillis) {
		this.perRequestIntervalInMillis = perRequestIntervalInMillis;
	}
	/**
	 * @return the suspensionIntervalInMillis
	 */
	public long getSuspensionIntervalInMillis() {
		return suspensionIntervalInMillis;
	}
	/**
	 * @param suspensionIntervalInMillis the suspensionIntervalInMillis to set
	 */
	public void setSuspensionIntervalInMillis(long suspensionIntervalInMillis) {
		this.suspensionIntervalInMillis = suspensionIntervalInMillis;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RateLimitter [apiKey=" + apiKey + ", maxLimit=" + maxLimit + ", perRequestIntervalInMillis="
				+ perRequestIntervalInMillis + ", suspensionIntervalInMillis=" + suspensionIntervalInMillis
				+ ", currentCount=" + currentCount + ", lastUpdatedOn=" + lastUpdatedOn + ", isUsed=" + isUsed + "]";
	}
	
	
	
	
}
