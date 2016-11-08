/**
 * 
 */
package com.hotel.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotel.models.Hotel;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * @author braj.kishore
 *
 */
public class CommonUtil {

	public static List<Hotel> getHotelsFromCSV() {
		List<Hotel> hotels = null;
		List<Hotel> hotelsWithCityId = new ArrayList<Hotel>();
		HeaderColumnNameTranslateMappingStrategy<Hotel> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<Hotel>();
		beanStrategy.setType(Hotel.class);
		
		Map<String, String> columnMapping = new HashMap<String, String>();
		columnMapping.put("CITY", "city");
		columnMapping.put("HOTELID", "hotelId");
		columnMapping.put("ROOM", "room");
		columnMapping.put("PRICE", "price");
		beanStrategy.setColumnMapping(columnMapping);
		CsvToBean<Hotel> csvToBean = new CsvToBean<Hotel>();
	
		
		File f = new File("hoteldb.csv");
		try (FileReader fileReader = new FileReader(f.getAbsolutePath())) {
			CSVReader reader = new CSVReader(fileReader, ',');
			hotels = csvToBean.parse(beanStrategy, reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		for (Hotel hotel : hotels) {
			hotel.setCityId(hotel.getCity().hashCode());//Assuming that same city (String) has same hashCode
			hotelsWithCityId.add(hotel);
		}
		
		return hotelsWithCityId;
	}

	public static List<Hotel> getHotelsByCityIdInOrder(List<Hotel> hotels, long cityId, String sortOrder) {
		// TODO Auto-generated method stub
		
		Comparator<Hotel> comparator;
		if("DESC".equalsIgnoreCase(sortOrder))
			comparator=new Comparator<Hotel>() {

				@Override
				public int compare(Hotel o1, Hotel o2) {
					// TODO Auto-generated method stub
					return (int)(o2.getPrice()-o1.getPrice());
				}
			};
		else	
			comparator=new Comparator<Hotel>() {

				@Override
				public int compare(Hotel o1, Hotel o2) {
					// TODO Auto-generated method stub
					return (int)(o1.getPrice()-o2.getPrice());
				}
		};
		
		List<Hotel> filteredList=new ArrayList<Hotel>();
		for(Hotel hotel:hotels)
			if(hotel.getCityId()==cityId)
				filteredList.add(hotel);
		
		
		Collections.sort(filteredList,comparator);
		return filteredList;
	}
}
